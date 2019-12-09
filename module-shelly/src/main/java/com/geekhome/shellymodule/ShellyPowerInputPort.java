package com.geekhome.shellymodule;

public class ShellyPowerInputPort extends ShellyInputPort<Double> implements IShellyPort{
    private ShellyPowerInputPort(String shellyId, int channel, Double initialValue) {
        super(shellyId + "-PWR-" + channel, initialValue,
                "shellies/" + shellyId + "/relay/" + channel + "/power");
    }

    ShellyPowerInputPort(ShellySettingsResponse settingsResponse, int channel) {
        this(settingsResponse.getDevice().getHostname(), channel,
                settingsResponse.getMeters().get(channel).getPower());
    }

    @Override
    public void setValueFromMqttPayload(String payload) {
        double value = Double.parseDouble(payload);
        setValue(value);
    }
}
