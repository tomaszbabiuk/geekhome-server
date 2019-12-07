package com.geekhome.shellymodule;

import com.google.gson.Gson;

public class ShellyPowerOutputPort extends ShellyOutputPort<Integer> {

    private final Gson _gson;

    public ShellyPowerOutputPort(String id, Integer initialValue, String readTopic, String writeTopic) {
        super(id, initialValue, readTopic, writeTopic);
        _gson = new Gson();
    }

    public int convertMqttPayloadToValue(String payload) {
        ShellyLightResponse response = _gson.fromJson(payload, ShellyLightResponse.class);
        return response.isOn() ? response.getBrightness() * 256/100 : 0;
    }

    public String convertValueToMqttPayload(int value) {
        ShellyLightResponse response = new ShellyLightResponse();
        if (value == 0) {
            response.setOn(false);
        } else {
            response.setOn(true);
            response.setBrightness(value * 100/256);
        }

        return _gson.toJson(response);
    }
}
