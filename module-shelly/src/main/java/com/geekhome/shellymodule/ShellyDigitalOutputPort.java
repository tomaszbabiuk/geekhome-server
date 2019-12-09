package com.geekhome.shellymodule;

public class ShellyDigitalOutputPort extends ShellyOutputPort<Boolean> {

    private ShellyDigitalOutputPort(String shellyId, int channel, Boolean initialValue) {
        super(shellyId + "-PWM-" + channel, initialValue,
                "shellies/" + shellyId + "/relay/" + channel,
                "shellies/" + shellyId + "/relay/" + channel+ "/command");
    }

    ShellyDigitalOutputPort(ShellySettingsResponse settingsResponse, int channel) {
        this(settingsResponse.getDevice().getHostname(), channel, settingsResponse.getRelays().get(channel).isOn());
    }

    public String convertValueToMqttPayload() {
        return getValue() ? "on" : "off";
    }

    @Override
    public void setValueFromMqttPayload(String payload) {
        boolean value = payload != null && payload.equals("on");
        setValue(value);
    }
}
