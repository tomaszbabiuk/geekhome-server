package com.geekhome.emailmodule;

import com.geekhome.common.*;
import com.geekhome.coremodule.commands.AwaitingMessage;
import com.geekhome.coremodule.commands.FailedMessage;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.utils.Sleeper;
import com.geekhome.common.settings.AutomationSettings;
import com.geekhome.emailmodule.httpserver.EmailSettings;
import com.geekhome.http.ILocalizationProvider;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EmailService extends MonitorableBase {
    private static int SMALL_SLEPP = 30000;
    private static int BIG_SLEPP = 60000;

    public interface IEmailReceivedListener {
        void emailReceived(Address from, Date receivedDate, String subject) throws Exception;
    }

    private final Session _session;
    private final EmailSettings _settings;
    private final Store _store;
    private ILogger _logger = LoggingService.getLogger();
    private Folder _inbox;
    private ConcurrentLinkedQueue<FailedMessage> _failedQueue;
    private ConcurrentLinkedQueue<AwaitingMessage> _sendingQueue;
    private ILocalizationProvider _localizationProvider;
    private IEmailReceivedListener _emailReceivedListener;

    protected void onEmailReceived(Address from, Date receivedDate, String subject) throws Exception {
        if (_emailReceivedListener != null) {
            _emailReceivedListener.emailReceived(from, receivedDate, subject);
        }
    }

    public EmailService(AutomationSettings automationSettings,
                        ILocalizationProvider localizationProvider) throws NoSuchProviderException {
        super(new DescriptiveName(localizationProvider.getValue("E:EmailControl"), "email"), false, localizationProvider.getValue("C:Initialization"));
        _localizationProvider = localizationProvider;
        _failedQueue = new ConcurrentLinkedQueue<>();
        _sendingQueue = new ConcurrentLinkedQueue<>();
        _settings = new EmailSettings(automationSettings);
        _session = startSession();
        _store = _session.getStore("imaps");
    }

    public void setEmailReceivedListener(IEmailReceivedListener emailReceivedListener) {
        _emailReceivedListener = emailReceivedListener;
    }

    private Thread createEmailSendingThread() {
        Runnable sendingWork = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    sendScheduledEmails();
                    Sleeper.trySleep(1000);
                }
            }
        };

        return new Thread(sendingWork);
    }

    private Thread createIdleThread() {
        Runnable idleWork = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        sendFailedEmails();

                        if (_inbox == null) {
                            _inbox = _store.getFolder("Inbox");
                        }

                        if (!_inbox.isOpen()) {
                            _inbox.open(Folder.READ_WRITE);
                        }

                        for (Message message : _inbox.getMessages()) {
                            processEmail(message);
                        }

                        updateStatus(true, _localizationProvider.getValue("C:Operational"));
                    } catch (MessagingException me) {
                        updateStatus(false, _localizationProvider.getValue("C:ConnectionError"));
                        Exception next = me.getNextException();
                        _logger.info("Email service, Messaging exception.");
                        if (next != null) {
                            if (next instanceof SocketTimeoutException) {
                                try {
                                    _logger.info("Email service, trying to reconnect to the store.");
                                    if (!_store.isConnected()) {
                                        _store.connect(_settings.getImapHost(), _settings.getUser(), _settings.getPassword());
                                    }
                                } catch (Exception ex) {
                                    _logger.info("Reconnecting store exception: ");
                                }
                            }

                            if (next instanceof UnknownHostException) {
                                _logger.warning("Email service, unknown host exception or DNS error! ", next);
                            }
                        }

                    } catch (IllegalStateException iste) {
                        updateStatus(false, _localizationProvider.getValue("C:ConnectionError"));
                        if (!_store.isConnected()) {
                            try {
                                _store.connect(_settings.getImapHost(), _settings.getUser(), _settings.getPassword());
                            } catch (Exception ex) {
                                _logger.debug("Cannot connect to the store, sleeping for 30s");
                            }
                            Sleeper.trySleep(BIG_SLEPP);
                        }

                    } catch (Exception ex) {
                        updateStatus(false, _localizationProvider.getValue("C:ConnectionError"));
                        _logger.debug("Monitoring Email Service exception, sleeping for 60s");
                    } finally {
                        Sleeper.trySleep(SMALL_SLEPP);
                    }
                }
            }
        };

        Thread t = new Thread(idleWork);
        t.setContextClassLoader(this.getClass().getClassLoader());
        return t;
    }

    private void sendScheduledEmails() {
        while (_sendingQueue.size() > 0) {
            AwaitingMessage email = _sendingQueue.poll();
            try {
                sendEmailInternal(email.getTargetUser(), email.getSubject(), email.getContent());
            } catch (MessagingException e) {
                updateStatus(false, _localizationProvider.getValue("C:ConnectionError"));
                _logger.debug(String.format("Cannot send email to '%s'. Subject: '%s', content: '%s'.", email.getTargetUser(), email.getSubject(), email.getContent()));
                _failedQueue.offer(new FailedMessage(email, _localizationProvider));
            }
        }
    }

    private void sendFailedEmails() throws MessagingException {
        while (_failedQueue.size() > 0) {
            FailedMessage email = _failedQueue.poll();
            try {
                sendEmailInternal(email.getTargetUser(), email.getSubject(), email.getContent());
            } catch (MessagingException e) {
                updateStatus(false, _localizationProvider.getValue("C:ConnectionError"));
                _failedQueue.offer(email);
                throw new MessagingException();
            }
        }
    }

    private Session startSession() {
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        props.put("mail.smtp.host", _settings.getSmtpHost());
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.imap.connectiontimeout", "30000");
        props.put("mail.imap.timeout", "30000");

        try {
            return Session.getDefaultInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(_settings.getUser(), _settings.getPassword());
                }
            });
        } catch (Exception ex) {
            updateStatus(false, _localizationProvider.getValue("C:ConnectionError"));
            _logger.debug("Cannot create SMTP/IMAP session!");
        }

        return null;
    }

    public void addToSendingQueue(AwaitingMessage message) {
        _sendingQueue.offer(message);
    }

    private void sendEmailInternal(String targetUser, String subject, String content) throws MessagingException {
        Message message = composeEmailMessage(_session, targetUser, subject, content);
        Transport.send(message);
    }

    private Message composeEmailMessage(Session session, String to, String subject, String content) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(content);
        return message;
    }

    private void processEmail(Message message) {
        try {
            if (_inbox.isOpen() && !message.isExpunged()) {
                Address from = message.getFrom()[0];
                String subject = message.getSubject();
                Date receivedDate = message.getReceivedDate();
                _logger.info(String.format("Processing email from '%s', command '%s'", from, subject));
                onEmailReceived(from, receivedDate, subject);
                message.setFlag(Flags.Flag.DELETED, true);
            }
        } catch (Exception ex) {
            updateStatus(false, _localizationProvider.getValue("C:ProcessingError"));
            _logger.warning("Processing email message exception.", ex);
        }
    }

    @Override
    public void start() {
        if (!_settings.getImapHost().equals("") && !_settings.getUser().equals("") && !_settings.getPassword().equals("")) {
            Thread idleThread = createIdleThread();
            idleThread.start();
            Thread sendingThread = createEmailSendingThread();
            sendingThread.start();
        } else {
            _logger.info("Email command service not configured - SKIPPING");
            updateStatus(false, _localizationProvider.getValue("C:ConfigurationError"));
        }
    }
}