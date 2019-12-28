package com.geekhome.firebasemodule.httpserver;

import com.geekhome.common.settings.AutomationSettings;
import com.geekhome.coremodule.httpserver.IHttpListenerRequest;
import com.geekhome.coremodule.httpserver.IResponse;
import com.geekhome.coremodule.httpserver.QueryString;
import com.geekhome.coremodule.httpserver.PostRequestsDispatcherBase;
import com.geekhome.coremodule.jetty.RedirectionResponse;

public class FirebasePostRequestsDispatcher extends PostRequestsDispatcherBase {
    private AutomationSettings _automationSettings;

    public FirebasePostRequestsDispatcher(AutomationSettings automationSettings) {
        _automationSettings = automationSettings;
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) throws Exception {
        String originalStringUppercased =
                request.getUrl().getUrlNoQuery().substring(1, request.getUrl().getUrlNoQuery().length() - 5).toUpperCase();
        QueryString queryString = new QueryString("?" + new String(request.getPostData(), "UTF-8"));

        if (originalStringUppercased.equals("CONFIG/MODIFYFIREBASESETTINGS")) {
            String host = queryString.getValues().getValue("host");
            String path = queryString.getValues().getValue("path");

            _automationSettings.changeSetting("Firebase.Host", host);
            _automationSettings.changeSetting("Firebase.Path", path);
            return new RedirectionResponse("/config/firebase.htm");
        }

        return null;
    }
}
