package com.geekhome.shellymodule;

public class ShellyDigitalOutputPort extends ShellyOutputPort<Boolean> {

    public ShellyDigitalOutputPort(String shellyId, int channel, Boolean initialValue) {
        super(shellyId + "-PWM-" + channel, initialValue,
                "shellies/" + shellyId + "/relay/" + channel,
                "shellies/" + shellyId + "/relay/" + channel+ "/command");
    }

    public ShellyDigitalOutputPort(ShellySettingsResponse settingsResponse, int channel) {
        this(settingsResponse.getDevice().getHostname(), channel, settingsResponse.getRelays().get(channel).isOn());
    }

    public boolean convertMqttPayloadToValue(String payload) {
        return payload != null && payload.equals("on");
    }

    public String convertValueToMqttPayload() {
        return getValue() ? "on" : "off";
    }


}
