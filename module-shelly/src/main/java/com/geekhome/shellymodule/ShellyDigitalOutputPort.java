package com.geekhome.shellymodule;

public class ShellyDigitalOutputPort extends ShellyOutputPort<Boolean> {

    public ShellyDigitalOutputPort(String shellyId, int channel, Boolean initialValue) {
        super(shellyId + "-PWM-" + channel, initialValue,
                "shellies/" + shellyId + "/relay/" + channel,
                "shellies/" + shellyId + "/relay/" + channel+ "/command");
    }

    public boolean convertMqttPayloadToValue(String payload) {
        return payload != null && payload.equals("on");
    }

    public String convertValueToMqttPayload() {
        return getValue() ? "on" : "off";
    }


}
