package com.geekhome.extafreemodule.httpserver;

import com.geekhome.extafreemodule.ExtaFreeConfiguration;
import com.geekhome.coremodule.httpserver.IHttpListenerRequest;
import com.geekhome.coremodule.httpserver.IResponse;
import com.geekhome.coremodule.jetty.JsonRequestsDispatcherBase;
import com.geekhome.coremodule.httpserver.JsonResponse;
import org.json.simple.JSONObject;

public class ExtaFreeJsonRequestsDispatcher extends JsonRequestsDispatcherBase {
    private ExtaFreeConfiguration _extaFreeConfiguration;

    public ExtaFreeJsonRequestsDispatcher(ExtaFreeConfiguration automationConfiguration) {
        _extaFreeConfiguration = automationConfiguration;
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) {
        String originalStringUppercased = request.getUrl().getUrlNoQuery().toUpperCase();

        if (originalStringUppercased.equals("/CONFIG/EXTAFREEBLINDS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_extaFreeConfiguration.getExtaFreeBlinds());
            return new JsonResponse(json, true);
        }

        return null;
    }
}