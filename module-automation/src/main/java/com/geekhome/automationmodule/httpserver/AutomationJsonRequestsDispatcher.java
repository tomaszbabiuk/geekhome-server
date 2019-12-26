package com.geekhome.automationmodule.httpserver;

import com.geekhome.automationmodule.AutomationConfiguration;
import com.geekhome.http.IHttpListenerRequest;
import com.geekhome.http.IResponse;
import com.geekhome.http.jetty.JsonRequestsDispatcherBase;
import com.geekhome.coremodule.httpserver.JsonResponse;
import org.json.simple.JSONObject;

public class AutomationJsonRequestsDispatcher extends JsonRequestsDispatcherBase {
    private AutomationConfiguration _automationConfiguration;

    public AutomationJsonRequestsDispatcher(AutomationConfiguration automationConfiguration) {
        _automationConfiguration = automationConfiguration;
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) {
        String originalStringUppercased = request.getUrl().getUrlNoQuery().toUpperCase();
        if (originalStringUppercased.equals("/CONFIG/ONOFFDEVICES.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_automationConfiguration.getOnOffDevices());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/IMPULSESWITCHES.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_automationConfiguration.getImpulseSwitches());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/NFCCONDITIONS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_automationConfiguration.getNfcConditions());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/INTENSITYDEVICES.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_automationConfiguration.getIntensityDevices());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/POWERMETERS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_automationConfiguration.getPowerMeters());
            return new JsonResponse(json, true);
        }

        return null;
    }
}