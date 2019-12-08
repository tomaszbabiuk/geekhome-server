package com.geekhome.shellymodule;

public interface IShellyOutputPort {
    void resetLatch();
    boolean didChangeValue();

    String getWriteTopic();
    String convertValueToMqttPayload();
}
