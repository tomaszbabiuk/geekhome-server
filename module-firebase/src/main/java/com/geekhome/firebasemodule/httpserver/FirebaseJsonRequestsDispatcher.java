package com.geekhome.firebasemodule.httpserver;

import com.geekhome.coremodule.settings.AutomationSettings;
import com.geekhome.http.IHttpListenerRequest;
import com.geekhome.http.IResponse;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.http.jetty.JsonRequestsDispatcherBase;

public class FirebaseJsonRequestsDispatcher extends JsonRequestsDispatcherBase {

    public FirebaseJsonRequestsDispatcher(AutomationSettings automationSettings, ILocalizationProvider localizationProvider) {
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) throws Exception {
        String originalStringUppercased = request.getUrl().getUrlNoQuery().toUpperCase();

        return null;
    }
}
