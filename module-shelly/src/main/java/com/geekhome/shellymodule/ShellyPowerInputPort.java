package com.geekhome.shellymodule;

public class ShellyPowerInputPort extends ShellyInputPort<Double> {
    public ShellyPowerInputPort(String shellyId, int channel, Double initialValue) {
        super(shellyId + "-PWR-" + channel, initialValue,
                "shellies/" + shellyId + "/relay/" + channel + "/power");

    }

    public double convertMqttPayloadToValue(String payload) {
        return Double.parseDouble(payload);
    }
}
