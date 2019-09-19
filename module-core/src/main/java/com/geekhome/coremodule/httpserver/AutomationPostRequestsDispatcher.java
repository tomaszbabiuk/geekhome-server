package com.geekhome.coremodule.httpserver;

import com.geekhome.common.CodeInvalidException;
import com.geekhome.coremodule.automation.*;
import com.geekhome.http.IHttpListenerRequest;
import com.geekhome.http.IResponse;
import com.geekhome.http.QueryString;
import com.geekhome.httpserver.JsonResponse;
import com.geekhome.httpserver.PostRequestsDispatcherBase;

public class AutomationPostRequestsDispatcher extends PostRequestsDispatcherBase {
    private MasterAutomation _masterAutomation;

    public AutomationPostRequestsDispatcher(MasterAutomation masterAutomation) {
        _masterAutomation = masterAutomation;
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) throws Exception {
        String originalStringUppercased =
                request.getUrl().getUrlNoQuery().substring(1, request.getUrl().getUrlNoQuery().length() - 5).toUpperCase();
        QueryString queryString = new QueryString("?" + new String(request.getPostData(), "UTF-8"));

        if (originalStringUppercased.equals("SWITCHTOAUTOMATIC")) {
            String id = queryString.getValues().getValue("id");
            IDeviceAutomationUnit deviceUnit = _masterAutomation.findDeviceUnit(id);

            if (deviceUnit instanceof IMultistateDeviceAutomationUnit) {
                IMultistateDeviceAutomationUnit stateableUnit = (IMultistateDeviceAutomationUnit) deviceUnit;
                stateableUnit.setControlMode(ControlMode.Auto);
                return new JsonResponse(JsonResponse.createJSONResult(true), false);
            }
        }

        if (originalStringUppercased.equals("CHANGEDEVICESTATE")) {
            String id = queryString.getValues().getValue("id");
            String value = queryString.getValues().getValue("value");
            String code = queryString.getValues().getValue("code");

            IDeviceAutomationUnit deviceUnit = _masterAutomation.findDeviceUnit(id);
            if (deviceUnit instanceof IMultistateDeviceAutomationUnit) {
                IMultistateDeviceAutomationUnit stateableUnit = (IMultistateDeviceAutomationUnit) deviceUnit;
                try {
                    stateableUnit.changeState(value, ControlMode.Manual, code, request.getUserName());
                    return new JsonResponse(JsonResponse.createJSONResult(true), false);
                }
                catch (CodeInvalidException ex) {
                    return new JsonResponse(JsonResponse.createJSONResult(false), false);
                }
            }

            throw new Exception("Cannot change device state of: " + id);
        }

        return null;
    }
}
