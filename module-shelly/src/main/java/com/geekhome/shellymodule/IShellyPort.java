package com.geekhome.shellymodule;

import com.geekhome.common.IConnectible;
import com.geekhome.hardwaremanager.IPort;

public interface IShellyPort extends IConnectible, IPort {
        String getReadTopic();
        void setValueFromMqttPayload(String payload);
}
