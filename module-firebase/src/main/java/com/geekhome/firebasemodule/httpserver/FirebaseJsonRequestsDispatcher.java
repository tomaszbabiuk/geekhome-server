package com.geekhome.firebasemodule.httpserver;

import com.geekhome.common.settings.AutomationSettings;
import com.geekhome.coremodule.httpserver.IHttpListenerRequest;
import com.geekhome.coremodule.httpserver.IResponse;
import com.geekhome.common.localization.ILocalizationProvider;
import com.geekhome.coremodule.jetty.JsonRequestsDispatcherBase;

public class FirebaseJsonRequestsDispatcher extends JsonRequestsDispatcherBase {

    public FirebaseJsonRequestsDispatcher(AutomationSettings automationSettings, ILocalizationProvider localizationProvider) {
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) throws Exception {
        String originalStringUppercased = request.getUrl().getUrlNoQuery().toUpperCase();

        return null;
    }
}
