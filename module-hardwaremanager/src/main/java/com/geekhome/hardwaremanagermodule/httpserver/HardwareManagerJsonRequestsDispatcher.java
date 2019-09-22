package com.geekhome.hardwaremanagermodule.httpserver;

import com.geekhome.hardwaremanager.*;
import com.geekhome.http.IHttpListenerRequest;
import com.geekhome.http.IResponse;
import com.geekhome.http.jetty.JsonRequestsDispatcherBase;
import com.geekhome.httpserver.JsonResponse;
import org.json.simple.JSONObject;

public class HardwareManagerJsonRequestsDispatcher extends JsonRequestsDispatcherBase {
    private IHardwareManager _hardwareManager;

    public HardwareManagerJsonRequestsDispatcher(IHardwareManager hardwareManager) {
        _hardwareManager = hardwareManager;
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) throws Exception{
        String originalStringUppercased = request.getUrl().getUrlNoQuery().toUpperCase();

        if (originalStringUppercased.equals("/READDIGITALINPUTPORT.JSON")) {
            String portId = request.getUrl().getQueryString().getValues().getValue("id");
            IInputPort<Boolean> port = _hardwareManager.findDigitalInputPort(portId);
            boolean value = port.read();
            JSONObject json = JsonResponse.createJSONResult(value);
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/READDIGITALOUTPUTPORT.JSON")) {
            String portId = request.getUrl().getQueryString().getValues().getValue("id");
            IInputPort<Boolean> port = _hardwareManager.findDigitalOutputPort(portId);
            Boolean value = port.read();
            JSONObject json = JsonResponse.createJSONResult(value);
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/READPOWERINPUTPORT.JSON")) {
            String portId = request.getUrl().getQueryString().getValues().getValue("id");
            IInputPort<Integer> port = _hardwareManager.findPowerInputPort(portId);
            int value = port.read();
            JSONObject json = JsonResponse.createJSONResult(value);
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/READPOWEROUTPUTPORT.JSON")) {
            String portId = request.getUrl().getQueryString().getValues().getValue("id");
            IInputPort<Integer> port = _hardwareManager.findPowerOutputPort(portId);
            int value = port.read();
            JSONObject json = JsonResponse.createJSONResult(value);
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/WRITEPOWEROUTPUTPORT.JSON")) {
            String portId = request.getUrl().getQueryString().getValues().getValue("id");
            int value = Integer.parseInt(request.getUrl().getQueryString().getValues().getValue("value"));
            IOutputPort<Integer> powerOutputPort = _hardwareManager.findPowerOutputPort(portId);
            powerOutputPort.write(value);
            JSONObject json = JsonResponse.createJSONResult(true);
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/TOGGLEDIGITALOUTPUTPORT.JSON")) {
            String portId = request.getUrl().getQueryString().getValues().getValue("id");
            IOutputPort<Boolean> port = _hardwareManager.findDigitalOutputPort(portId);
            boolean value = port.read();
            port.write(!value);
            JSONObject json = JsonResponse.createJSONResult(!value);
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/TOGGLEPORTON.JSON")) {
            String portId = request.getUrl().getQueryString().getValues().getValue("id");
            ITogglePort port = _hardwareManager.findTogglePort(portId);
            port.toggleOn();
            JSONObject json = JsonResponse.createJSONResult(true);
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/TOGGLEPORTOFF.JSON")) {
            String portId = request.getUrl().getQueryString().getValues().getValue("id");
            ITogglePort port = _hardwareManager.findTogglePort(portId);
            port.toggleOff();
            JSONObject json = JsonResponse.createJSONResult(true);
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/READTEMPERATUREPORT.JSON")) {
            String portId = request.getUrl().getQueryString().getValues().getValue("id");
            IInputPort<Double> port = _hardwareManager.findTemperaturePort(portId);
            double value = port.read();
            JSONObject json = JsonResponse.createJSONResult(value);
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/READHUMIDITYPORT.JSON")) {
            String portId = request.getUrl().getQueryString().getValues().getValue("id");
            IInputPort<Double> port = _hardwareManager.findHumidityPort(portId);
            double value = port.read();
            JSONObject json = JsonResponse.createJSONResult(value);
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/READLUMINOSITYPORT.JSON")) {
            String portId = request.getUrl().getQueryString().getValues().getValue("id");
            IInputPort<Double> port = _hardwareManager.findLuminosityPort(portId);
            double value = port.read();
            JSONObject json = JsonResponse.createJSONResult(value);
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/DIGITALINPUTPORTS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_hardwareManager.getDigitalInputPorts());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/DIGITALOUTPUTPORTS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_hardwareManager.getDigitalOutputPorts());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/POWERINPUTPORTS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_hardwareManager.getPowerInputPorts());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/POWEROUTPUTPORTS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_hardwareManager.getPowerOutputPorts());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/TEMPERATUREPORTS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_hardwareManager.getTemperaturePorts());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/HUMIDITYPORTS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_hardwareManager.getHumidityPorts());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/LUMINOSITYPORTS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_hardwareManager.getLuminosityPorts());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/TOGGLEPORTS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_hardwareManager.getTogglePorts());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/HMREFRESH.JSON")) {
            _hardwareManager.refreshAndWait();
            JSONObject json = JsonResponse.createJSONResult(true);
            return new JsonResponse(json, true);
        }

        return null;
    }
}
