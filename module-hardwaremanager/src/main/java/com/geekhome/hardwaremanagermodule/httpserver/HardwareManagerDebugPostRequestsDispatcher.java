package com.geekhome.hardwaremanagermodule.httpserver;

import com.geekhome.common.SynchronizedInputPort;
import com.geekhome.common.hardwaremanager.IHardwareManager;
import com.geekhome.http.IHttpListenerRequest;
import com.geekhome.http.IResponse;
import com.geekhome.http.QueryString;
import com.geekhome.httpserver.JsonResponse;
import com.geekhome.httpserver.PostRequestsDispatcherBase;

public class HardwareManagerDebugPostRequestsDispatcher extends PostRequestsDispatcherBase {

    private IHardwareManager _hardwareManager;

    public HardwareManagerDebugPostRequestsDispatcher(IHardwareManager hardwareManager) {
        _hardwareManager = hardwareManager;
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) throws Exception {
        String originalStringUppercased =
                request.getUrl().getUrlNoQuery().substring(1, request.getUrl().getUrlNoQuery().length() - 5).toUpperCase();
        QueryString queryString = new QueryString("?" + new String(request.getPostData(), "UTF-8"));

        if (originalStringUppercased.equals("CHANGEINPUTPORT")) {
            String id = queryString.getValues().getValue("id");
            String value = queryString.getValues().getValue("value");
            SynchronizedInputPort<Boolean> inputPort = (SynchronizedInputPort<Boolean>) _hardwareManager.findDigitalInputPort(id);
            boolean booleanValue = value.toLowerCase().equals("true");
            inputPort.setValue(booleanValue);
            return new JsonResponse(JsonResponse.createJSONResult(true), false);
        }

        if (originalStringUppercased.equals("CHANGETEMPERATUREPORT")) {
            String id = queryString.getValues().getValue("id");
            String value = queryString.getValues().getValue("value");
            SynchronizedInputPort<Double> temperaturePort = (SynchronizedInputPort<Double>) _hardwareManager.findTemperaturePort(id);
            double doubleValue = Double.parseDouble(value);
            temperaturePort.setValue(doubleValue);
            return new JsonResponse(JsonResponse.createJSONResult(true), false);
        }

        if (originalStringUppercased.equals("CHANGEHUMIDITYPORT")) {
            String id = queryString.getValues().getValue("id");
            String value = queryString.getValues().getValue("value");
            SynchronizedInputPort<Double> humidityPort = (SynchronizedInputPort<Double>) _hardwareManager.findHumidityPort(id);
            double doubleValue = Double.parseDouble(value);
            humidityPort.setValue(doubleValue);
            return new JsonResponse(JsonResponse.createJSONResult(true), false);
        }

        if (originalStringUppercased.equals("CHANGELUMINOSITYPORT")) {
            String id = queryString.getValues().getValue("id");
            String value = queryString.getValues().getValue("value");
            SynchronizedInputPort<Double> luminosityPort = (SynchronizedInputPort<Double>) _hardwareManager.findLuminosityPort(id);
            double doubleValue = Double.parseDouble(value);
            luminosityPort.setValue(doubleValue);
            return new JsonResponse(JsonResponse.createJSONResult(true), false);
        }

        return null;
    }
}
