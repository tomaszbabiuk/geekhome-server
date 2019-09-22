package com.geekhome.mqttmodule;

public class TasmotaPowerOutputPort extends TasmotaOutputPort<Integer> {

    public TasmotaPowerOutputPort(MqttClientResolver clientResolver, String id, Integer initialValue) {
        super(clientResolver, id, initialValue);
    }

    @Override
    protected String buildMqttContentFromValue(Integer value) {
        return String.valueOf(value);
    }

    @Override
    protected String getTopicEnding() {
        return "PWM";
    }
}
