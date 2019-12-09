package com.geekhome.shellymodule;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.InetAddress;

public class ShellyPowerOutputPort extends ShellyOutputPort<Integer> {

    private final Gson _gson;

    ShellyPowerOutputPort(ShellySettingsResponse settingsResponse, ShellyLightResponse lightResponse, int i, InetAddress shellyIP) throws IOException {
        this(settingsResponse.getDevice().getHostname(), i, calculateBrightness(lightResponse));
    }

    private static int calculateBrightness(ShellyLightResponse lightResponse) {
        boolean isOn = lightResponse.isOn();
        return isOn ? lightResponse.getBrightness() * 256/100 : 0;
    }

    private ShellyPowerOutputPort(String shellyId, int channel, Integer initialValue) {
        super(shellyId + "-PWM-" + channel, initialValue,
                "shellies/" + shellyId + "/light/" + channel + "/status",
                "shellies/" + shellyId + "/light/" + channel + "/set");

        _gson = new Gson();
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

    @Override
    public void setValueFromMqttPayload(String payload) {
        ShellyLightResponse response = _gson.fromJson(payload, ShellyLightResponse.class);
        int value = calculateBrightness(response);
        setValue(value);
    }
}