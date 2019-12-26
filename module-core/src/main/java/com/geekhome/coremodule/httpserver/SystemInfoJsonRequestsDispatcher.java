package com.geekhome.coremodule.httpserver;

import com.geekhome.coremodule.SystemStatus;
import com.geekhome.common.settings.AutomationSettings;
import com.geekhome.http.IHttpListenerRequest;
import com.geekhome.http.IResponse;
import com.geekhome.http.jetty.JsonRequestsDispatcherBase;
import com.geekhome.common.automation.SystemInfo;
import com.geekhome.common.configuration.ObjectNotFoundException;
import org.json.simple.JSONObject;

import java.util.Objects;

public class SystemInfoJsonRequestsDispatcher extends JsonRequestsDispatcherBase {
    private SystemInfo _systemInfo;
    private AutomationSettings _automationSettings;

    public SystemInfoJsonRequestsDispatcher(SystemInfo systemInfo, AutomationSettings automationSettings) {
        _systemInfo = systemInfo;
        _automationSettings = automationSettings;
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) throws ObjectNotFoundException {
        String originalStringUppercased = request.getUrl().getUrlNoQuery().toUpperCase();

        if (originalStringUppercased.equals("/SYSTEMINFO/STATUS.JSON")) {
            boolean isLicenceAccepted = Objects.equals(_automationSettings.readSetting("System.LicenseAccepted", "0"), "1");
            SystemStatus status = new SystemStatus(request.getUserName(), _systemInfo.getOperationMode().toInt(),
                    isLicenceAccepted);
            JSONObject json = JsonResponse.createJSONResult(status);
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/SYSTEMINFO/ISDATERELIABLE.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(SystemInfo.isDateReliable());
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/SYSTEMINFO/MONITORABLES.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_systemInfo.getMonitorables());
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/SYSTEMINFO/ALERTSERVICES.JSON")) {
            return new JsonResponse(_systemInfo.getAlertServices(), true);
        }

        return null;
    }
}