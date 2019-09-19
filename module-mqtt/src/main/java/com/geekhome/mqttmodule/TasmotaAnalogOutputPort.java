package com.geekhome.mqttmodule;

public class TasmotaAnalogOutputPort extends TasmotaOutputPort<Integer> {

    public TasmotaAnalogOutputPort(MqttClientResolver clientResolver, String id, Integer initialValue) {
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
