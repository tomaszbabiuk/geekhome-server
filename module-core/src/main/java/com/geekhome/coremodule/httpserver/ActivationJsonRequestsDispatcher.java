package com.geekhome.coremodule.httpserver;

import com.geekhome.common.ILicenseManager;
import com.geekhome.coremodule.ActivationStatus;
import com.geekhome.coremodule.SystemStatus;
import com.geekhome.coremodule.settings.AutomationSettings;
import com.geekhome.http.IHttpListenerRequest;
import com.geekhome.http.IResponse;
import com.geekhome.http.jetty.JsonRequestsDispatcherBase;
import com.geekhome.httpserver.JsonResponse;
import com.geekhome.httpserver.SystemInfo;
import com.geekhome.httpserver.modules.CollectorCollection;
import com.geekhome.httpserver.modules.ObjectNotFoundException;
import org.json.simple.JSONObject;

import java.util.Objects;

public class ActivationJsonRequestsDispatcher extends JsonRequestsDispatcherBase {
    private ILicenseManager _licenseManager;

    public ActivationJsonRequestsDispatcher( ILicenseManager licenseManager) {
        _licenseManager = licenseManager;
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) throws ObjectNotFoundException {
        String originalStringUppercased = request.getUrl().getUrlNoQuery().toUpperCase();

        if (originalStringUppercased.equals("/ACTIVATION/ADAPTERACTIVATIONS.JSON")) {
            CollectorCollection<ActivationStatus> activations = new CollectorCollection<>();
            for (ActivationStatus activation : _licenseManager.getActivationErrors().values()) {
                activations.add(activation);
            }
            JSONObject json = JsonResponse.createJSONResult(activations);
            return new JsonResponse(json, false);
        }

        return null;
    }
}