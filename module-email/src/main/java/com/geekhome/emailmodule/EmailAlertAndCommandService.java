package com.geekhome.emailmodule;

import com.geekhome.common.alerts.AlertServiceBase;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.coremodule.commands.*;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.configuration.Alert;
import com.geekhome.common.automation.IDeviceAutomationUnit;
import com.geekhome.common.automation.MasterAutomation;
import com.geekhome.common.settings.AutomationSettings;
import com.geekhome.http.ILocalizationProvider;

import javax.mail.Address;
import java.util.Collection;
import java.util.Date;

class EmailAlertAndCommandService extends AlertServiceBase {
    private ILogger _logger = LoggingService.getLogger();
    private final EmailService _emailService;
    private final ILocalizationProvider _localizationProvider;
    private final MasterAutomation _masterAutomation;
    private AutomationSettings _automationSettings;

    EmailAlertAndCommandService(EmailService emailService, ILocalizationProvider localizationProvider,
                                MasterAutomation masterAutomation, AutomationSettings automationSettings,
                                final CommandsProcessor commandsProcessor) {
        super(new DescriptiveName(localizationProvider.getValue("E:EmailMessage"), "email"), false);
        _emailService = emailService;
        _localizationProvider = localizationProvider;
        _masterAutomation = masterAutomation;
        _automationSettings = automationSettings;
        _emailService.setEmailReceivedListener(new EmailService.IEmailReceivedListener() {
            @Override
            public void emailReceived(Address from, Date receivedDate, String subject) throws Exception {
                if (isTrustedRecipient(from)) {
                    CommandResult response = commandsProcessor.processCommand(receivedDate, extractEmail(from.toString()), subject, true);
                    AwaitingMessage email = new AwaitingMessage(extractEmail(from.toString()), composeResponseSubject(subject), response.getOutput());
                    _emailService.addToSendingQueue(email);
                }
            }
        });
    }

    private String composeResponseSubject(String subject) {
        return String.format("%s: %s", _localizationProvider.getValue("E:Re"), subject);
    }

    private boolean isTrustedRecipient(Address from) {
        Collection<String> trustedRecipients = _automationSettings.getTableKeys(EmailModule.TABLE_TRUSTED_RECIPIENTS);
        for (String trustedRecipient : trustedRecipients) {
            if (from.toString().equals(trustedRecipient) || from.toString().toLowerCase().contains("<" + trustedRecipient.toLowerCase() + ">")) {
                return true;
            }
        }

        _logger.info(String.format("Received email from non-trusted recipient: %s. Ignoring.", from));
        return false;
    }

    private String extractEmail(String source) {
        if (source.indexOf("<") > 0 && source.indexOf(">") > 0) {
            return source.substring(source.indexOf("<") + 1, source.indexOf(">"));
        }

        return source;
    }

    @Override
    public void raiseAlert(Alert alert) throws Exception {
        sendToAll(alert.getName().getName(), composeAlertDescription(alert));
    }

    private String composeAlertDescription(Alert alert) {
        StringBuilder sb = new StringBuilder();
        if (alert.getName().getDescription() != null && !alert.getName().getDescription().isEmpty()) {
            sb.append(alert.getName().getDescription());
            sb.append("\r\n\r\n");
        }
        if (alert.getDevicesIds() != null && !alert.getDevicesIds().isEmpty()) {
            for (String deviceId : alert.getDevicesIds()) {
                try {
                    IDeviceAutomationUnit deviceUnit = _masterAutomation.findDeviceUnit(deviceId);
                    sb.append(String.format("%s - %s\r\n", deviceUnit.getName(), deviceUnit.buildEvaluationResult().getInterfaceValue()));
                } catch (Exception ignored) {
                }
            }
        }

        return sb.toString();
    }

    @Override
    public void clearAlert(Alert alert) throws Exception {
    }

    private void sendToAll(String subject, String description) throws Exception {
        Collection<String> trustedRecipients = _automationSettings.getTableKeys(EmailModule.TABLE_TRUSTED_RECIPIENTS);

        for (String trustedRecipient : trustedRecipients) {
            AwaitingMessage message = new AwaitingMessage(trustedRecipient, subject, description);
            _emailService.addToSendingQueue(message);
        }
    }
}
