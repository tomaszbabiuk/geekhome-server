package com.geekhome.ventilationmodule.httpserver;

import com.geekhome.coremodule.httpserver.IHttpListenerRequest;
import com.geekhome.coremodule.httpserver.IResponse;
import com.geekhome.coremodule.jetty.JsonRequestsDispatcherBase;
import com.geekhome.coremodule.httpserver.JsonResponse;
import com.geekhome.ventilationmodule.VentilationConfiguration;
import org.json.simple.JSONObject;

public class VentilationJsonRequestsDispatcher extends JsonRequestsDispatcherBase {
    private VentilationConfiguration _ventilationConfiguration;

    public VentilationJsonRequestsDispatcher(VentilationConfiguration ventilationConfiguration) {
        _ventilationConfiguration = ventilationConfiguration;
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) {
        String originalStringUppercased = request.getUrl().getUrlNoQuery().toUpperCase();
        if (originalStringUppercased.equals("/CONFIG/RECUPERATORS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_ventilationConfiguration.getRecuperators());
            return new JsonResponse(json, true);
        }

        return null;
    }
}
