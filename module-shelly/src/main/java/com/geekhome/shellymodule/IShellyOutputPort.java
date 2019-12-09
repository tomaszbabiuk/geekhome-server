package com.geekhome.shellymodule;

public interface IShellyOutputPort extends IShellyPort {
    void resetLatch();
    boolean didChangeValue();

    String getWriteTopic();
    String convertValueToMqttPayload();
}
