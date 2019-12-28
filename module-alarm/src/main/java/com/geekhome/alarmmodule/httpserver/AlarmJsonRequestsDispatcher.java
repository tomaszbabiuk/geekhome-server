package com.geekhome.alarmmodule.httpserver;

import com.geekhome.alarmmodule.AlarmConfiguration;
import com.geekhome.alarmmodule.AlarmConfigurationValidation;
import com.geekhome.common.configuration.IDevice;
import com.geekhome.coremodule.httpserver.IHttpListenerRequest;
import com.geekhome.coremodule.httpserver.IResponse;
import com.geekhome.coremodule.jetty.JsonRequestsDispatcherBase;
import com.geekhome.coremodule.httpserver.JsonResponse;
import com.geekhome.common.configuration.CollectorCollection;
import org.json.simple.JSONObject;

public class AlarmJsonRequestsDispatcher extends JsonRequestsDispatcherBase {
    private AlarmConfiguration _alarmConfiguration;

    public AlarmJsonRequestsDispatcher(AlarmConfiguration alarmConfiguration) {
        _alarmConfiguration = alarmConfiguration;
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) throws Exception {
        String originalStringUppercased = request.getUrl().getUrlNoQuery().toUpperCase();
        if (originalStringUppercased.equals("/CONFIG/ALARMSENSORS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_alarmConfiguration.getAlarmSensors());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/PRESENCECONDITIONS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_alarmConfiguration.getPresenceConditions());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/MOVEMENTDETECTORS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_alarmConfiguration.getMovementDetectors());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/MAGNETICDETECTORS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_alarmConfiguration.getMagneticDetectors());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/GATES.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_alarmConfiguration.getGates());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/SIGNALIZATORS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_alarmConfiguration.getSignalizators());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/CODELOCKS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_alarmConfiguration.getCodeLocks());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/ALARMZONES.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_alarmConfiguration.getAlarmZones());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/ALLLOCKS.JSON")) {
            CollectorCollection<IDevice> devices = new CollectorCollection<>();
            devices.putAll(_alarmConfiguration.getCodeLocks());
            JSONObject json = JsonResponse.createJSONResult(devices);
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/ALLALARMSENSORS.JSON")) {
            CollectorCollection<IDevice> devices = new CollectorCollection<>();
            devices.putAll(_alarmConfiguration.getAlarmSensors());
            devices.putAll(_alarmConfiguration.getMagneticDetectors());
            devices.putAll(_alarmConfiguration.getMovementDetectors());
            devices.putAll(_alarmConfiguration.getGates());
            JSONObject json = JsonResponse.createJSONResult(devices);
            return new JsonResponse(json, true);
        }

        //Validation
        if (originalStringUppercased.equals("/CONFIG/CANADDPRESENCECONDITIONS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(AlarmConfigurationValidation.canAddPresenceConditions(_alarmConfiguration));
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/CONFIG/CANADDALARMZONES.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(AlarmConfigurationValidation.canAddAlarmZones(_alarmConfiguration));
            return new JsonResponse(json, false);
        }

        return null;
    }
}
