package com.geekhome.shellymodule;

import com.geekhome.common.IConnectible;

public interface IShellyPort extends IConnectible {
        String getReadTopic();
        void setValueFromMqttPayload(String payload);
}
