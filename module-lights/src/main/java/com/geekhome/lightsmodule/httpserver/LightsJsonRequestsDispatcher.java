package com.geekhome.lightsmodule.httpserver;

import com.geekhome.coremodule.httpserver.IHttpListenerRequest;
import com.geekhome.coremodule.httpserver.IResponse;
import com.geekhome.coremodule.jetty.JsonRequestsDispatcherBase;
import com.geekhome.coremodule.httpserver.JsonResponse;
import com.geekhome.lightsmodule.LightsConfiguration;
import org.json.simple.JSONObject;

public class LightsJsonRequestsDispatcher extends JsonRequestsDispatcherBase {
    private LightsConfiguration _lightsConfiguration;

    public LightsJsonRequestsDispatcher(LightsConfiguration lightsConfiguration) {
        _lightsConfiguration = lightsConfiguration;
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) {
        String originalStringUppercased = request.getUrl().getUrlNoQuery().toUpperCase();

        if (originalStringUppercased.equals("/CONFIG/BLINDS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_lightsConfiguration.getBlinds());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/RGBLAMPS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_lightsConfiguration.getRgbLamps());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/7COLORLAMPS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_lightsConfiguration.get7ColorLamps());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/LUXMETERS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_lightsConfiguration.getLuxmeters());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/TWILIGHTCONDITIONS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_lightsConfiguration.getTwilightConditions());
            return new JsonResponse(json, true);
        }

        return null;
    }
}