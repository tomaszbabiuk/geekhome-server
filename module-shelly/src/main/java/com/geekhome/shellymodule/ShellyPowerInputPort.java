package com.geekhome.shellymodule;

public class ShellyPowerInputPort extends ShellyInputPort<Double> {
    public ShellyPowerInputPort(String id, Double initialValue, String readTopic) {
        super(id, initialValue, readTopic);
    }

    public double convertMqttPayloadToValue(String payload) {
        return Double.parseDouble(payload);
    }
}
