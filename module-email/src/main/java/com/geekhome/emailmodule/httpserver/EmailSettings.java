package com.geekhome.emailmodule.httpserver;

import com.geekhome.coremodule.settings.AutomationSettings;
import org.eclipse.jetty.util.security.Password;

public class EmailSettings {
    private final String _imapHost;
    private final String _smtpHost;
    private final String _user;
    private final String _password;

    public String getImapHost() {
        return _imapHost;
    }

    public String getSmtpHost() {
        return _smtpHost;
    }

    public String getUser() {
        return _user;
    }

    public String getPassword() {
        return _password;
    }

    public EmailSettings(AutomationSettings settings) {
        _imapHost = settings.readSetting("Email.IMAPHost");
        _smtpHost = settings.readSetting("Email.SMTPHost");
        _user = settings.readSetting("Email.User");
        final String passwordObfuscated = settings.readSetting("Email.Password");
        _password = Password.deobfuscate(passwordObfuscated);
    }
}
