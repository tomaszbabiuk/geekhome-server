package com.geekhome.emailmodule.httpserver;

import com.geekhome.common.settings.AutomationSettings;
import com.geekhome.http.IHttpListenerRequest;
import com.geekhome.http.IResponse;
import com.geekhome.http.QueryString;
import com.geekhome.httpserver.PostRequestsDispatcherBase;
import com.geekhome.http.jetty.RedirectionResponse;
import org.eclipse.jetty.util.security.Password;

public class EmailPostRequestsDispatcher extends PostRequestsDispatcherBase {
    private AutomationSettings _automationSettings;

    public EmailPostRequestsDispatcher(AutomationSettings automationSettings) {
        _automationSettings = automationSettings;
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) throws Exception {
        String originalStringUppercased =
                request.getUrl().getUrlNoQuery().substring(1, request.getUrl().getUrlNoQuery().length() - 5).toUpperCase();
        QueryString queryString = new QueryString("?" + new String(request.getPostData(), "UTF-8"));

        if (originalStringUppercased.equals("CONFIG/MODIFYEMAILSETTINGS")) {
            String smtpHost = queryString.getValues().getValue("smtphost");
            String imapHost = queryString.getValues().getValue("imaphost");
            String user = queryString.getValues().getValue("user");
            String password = queryString.getValues().getValue("password");

            _automationSettings.changeSetting("Email.SMTPHost", smtpHost);
            _automationSettings.changeSetting("Email.IMAPHost", imapHost);
            _automationSettings.changeSetting("Email.User", user);
            _automationSettings.changeSetting("Email.Password", Password.obfuscate(password));

            return new RedirectionResponse("/config/email.htm");
        }

        return null;
    }
}
