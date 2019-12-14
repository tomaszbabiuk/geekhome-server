package com.geekhome.shellymodule;

import com.geekhome.hardwaremanager.IUserChangeable;

public interface IShellyOutputPort extends IShellyPort, IUserChangeable {
    void resetLatch();
    boolean didChangeValue();

    String getWriteTopic();
    String convertValueToMqttPayload();
}
