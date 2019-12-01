package com.geekhome.shellymodule;

public class ShellyDigitalOutputPort extends ShellyOutputPort<Boolean> {

    public ShellyDigitalOutputPort(String id, Boolean initialValue, String readTopic, String writeTopic) {
        super(id, initialValue, readTopic, writeTopic);
    }

    public boolean convertMqttPayloadToValue(String payload) {
        return payload != null && payload.equals("on");
    }

    public String convertValueToMqttPayload(Boolean value) {
        return value ? "on" : "off";
    }


}
