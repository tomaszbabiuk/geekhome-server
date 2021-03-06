package com.geekhome.coremodule.httpserver;

import com.geekhome.common.configuration.MasterConfiguration;
import com.geekhome.coremodule.jetty.JsonRequestsDispatcherBase;
import org.json.simple.JSONObject;

public class BackupJsonRequestsDispatcher extends JsonRequestsDispatcherBase {
    private MasterConfiguration _masterConfiguration;

    public BackupJsonRequestsDispatcher(MasterConfiguration masterConfiguration) {
        _masterConfiguration = masterConfiguration;
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) throws Exception {
        String originalStringUppercased = request.getUrl().getUrlNoQuery().toUpperCase();
        if (originalStringUppercased.equals("/CONFIG/BACKUPS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_masterConfiguration.getConfigurationProvider().getBackups());
            return new JsonResponse(json, false);
        }

        return null;
    }
}
