package com.geekhome.mqttmodule;

import com.geekhome.mqttmodule.tasmotaapi.Power;

public class TasmotaDigitalOutputPort extends TasmotaOutputPort<Boolean> {

    public TasmotaDigitalOutputPort(MqttClientResolver clientResolver, String id, Boolean initialValue, long connectionLostInterval) {
        super(clientResolver, id, initialValue, connectionLostInterval);
    }

    @Override
    protected String buildMqttContentFromValue(Boolean value) {
        return value ? Power.On.getCode() : Power.Off.getCode();
    }

    @Override
    protected String getTopicEnding() {
        return "Power";
    }
}
