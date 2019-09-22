package com.geekhome.centralheatingmodule.httpserver;

import com.geekhome.centralheatingmodule.CentralHeatingConfiguration;
import com.geekhome.centralheatingmodule.CentralHeatingConfigurationValidation;
import com.geekhome.coremodule.MasterConfiguration;
import com.geekhome.http.IHttpListenerRequest;
import com.geekhome.http.IResponse;
import com.geekhome.http.jetty.JsonRequestsDispatcherBase;
import com.geekhome.httpserver.JsonResponse;
import org.json.simple.JSONObject;

public class CentralHeatingJsonRequestsDispatcher extends JsonRequestsDispatcherBase {
    private CentralHeatingConfiguration _centralHeatingConfiguration;
    private MasterConfiguration _masterConfiguration;

    public CentralHeatingJsonRequestsDispatcher(MasterConfiguration masterConfiguration, CentralHeatingConfiguration centralHeatingConfiguration) {
        _centralHeatingConfiguration = centralHeatingConfiguration;
        _masterConfiguration = masterConfiguration;
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) throws Exception {
        String originalStringUppercased = request.getUrl().getUrlNoQuery().toUpperCase();
        if (originalStringUppercased.equals("/CONFIG/THERMOMETERS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_centralHeatingConfiguration.getThermometers());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/HYGROMETERS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_centralHeatingConfiguration.getHygrometers());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/AVERAGINGTHERMOMETERS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_centralHeatingConfiguration.getAveragingThermometers());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/COMFORTMETERS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_centralHeatingConfiguration.getComfortmeters());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/TEMPERATURECONTROLLERS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_centralHeatingConfiguration.getTemperatureControllers());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/THERMOSTATCONDITIONS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_centralHeatingConfiguration.getThermostatConditions());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/HEATINGMANIFOLDS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_centralHeatingConfiguration.getHeatingManifolds());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/AIRCONDITIONERS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_centralHeatingConfiguration.getAirConditioners());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/CIRCULATIONPUMPS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_centralHeatingConfiguration.getCirculationPumps());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/RADIATORS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_centralHeatingConfiguration.getRadiators());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/UNDERFLOORCIRCUITS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_centralHeatingConfiguration.getUnderfloorCircuits());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/RTLCIRCUITS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_centralHeatingConfiguration.getRTLCircuits());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/ALLCIRCUITS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_centralHeatingConfiguration.buildAllCircuits());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/ALLTHERMOMETERS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_centralHeatingConfiguration.buildAllThermometers());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/CANADDAVERAGINGTHERMOMETERS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(CentralHeatingConfigurationValidation.canAddAveragingThermometers(_masterConfiguration, _centralHeatingConfiguration));
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/CONFIG/CANADDHEATINGMANIFOLDS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(CentralHeatingConfigurationValidation.canAddHeatingManifolds(_centralHeatingConfiguration));
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/CONFIG/CANADDAIRCONDITIONERS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(CentralHeatingConfigurationValidation.canAddAirConditioners(_masterConfiguration));
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/CONFIG/CANADDTHERMOSTATCONDITIONS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(CentralHeatingConfigurationValidation.canAddThermostatConditions(_centralHeatingConfiguration));
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/CONFIG/CANADDCIRCULATIONPUMPS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(CentralHeatingConfigurationValidation.canAddCirculationPumps(_centralHeatingConfiguration));
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/CONFIG/CANADDCIRCUITS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(CentralHeatingConfigurationValidation.canAddCircuits(_centralHeatingConfiguration));
            return new JsonResponse(json, false);
        }

        return null;
    }
}

