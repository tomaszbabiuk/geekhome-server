package com.geekhome.shellymodule;

import com.google.gson.Gson;

public class ShellyPowerOutputPort extends ShellyOutputPort<Integer> {

    private final Gson _gson;

    public ShellyPowerOutputPort(String shellyId, int channel, Integer initialValue) {
        super(shellyId + "-PWM-" + channel, initialValue,
                "shellies/" + shellyId + "/light/" + channel + "/status",
                "shellies/" + shellyId + "/light/" + channel + "/set");

        _gson = new Gson();
    }

    public int convertMqttPayloadToValue(String payload) {
        ShellyLightResponse response = _gson.fromJson(payload, ShellyLightResponse.class);
        return response.isOn() ? response.getBrightness() * 256 / 100 : 0;
    }

    public String convertValueToMqttPayload() {
        ShellyLightSet response = new ShellyLightSet();
        if (getValue() == 0) {
            response.setTurn("off");
        } else {
            response.setTurn("on");
            response.setBrightness(getValue() * 100 / 256);
        }

        return _gson.toJson(response);
    }
}