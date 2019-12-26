package com.geekhome.extafreemodule.httpserver;

import com.geekhome.common.utils.Sleeper;
import com.geekhome.extafreemodule.PairingType;
import com.geekhome.common.hardwaremanager.IHardwareManager;
import com.geekhome.common.hardwaremanager.ITogglePort;
import com.geekhome.http.IHttpListenerRequest;
import com.geekhome.http.IResponse;
import com.geekhome.http.jetty.JsonRequestsDispatcherBase;
import com.geekhome.coremodule.httpserver.JsonResponse;

public class ExtaFreeJsonHardwareRequestsDispatcher extends JsonRequestsDispatcherBase {
    private IHardwareManager _hardwareManager;

    public ExtaFreeJsonHardwareRequestsDispatcher(IHardwareManager hardwareManager) {
        _hardwareManager = hardwareManager;
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) throws Exception {
        String originalStringUppercased = request.getUrl().getUrlNoQuery().toUpperCase();

        if (originalStringUppercased.equals("/DOEXTAFREEPAIRING.JSON")) {
            String id = request.getUrl().getQueryString().getValues().getValue("id");
            String type = request.getUrl().getQueryString().getValues().getValue("type");
            ITogglePort testedPort = _hardwareManager.findTogglePort(id);
            if (PairingType.ROP.getType().equals(type.toLowerCase())) {
                testedPort.toggleOff();
                Sleeper.trySleep(2000);
                testedPort.toggleOn();
                Sleeper.trySleep(2000);
                testedPort.toggleOff();
            } else {
                testedPort.toggleOn();
                Sleeper.trySleep(500);
                testedPort.toggleOff();
            }

            return new JsonResponse(JsonResponse.createJSONResult(true), false);
        }

        return null;
    }
}
