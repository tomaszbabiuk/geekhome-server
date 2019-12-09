package com.geekhome.shellymodule;

public interface IShellyPort {
        String getReadTopic();
        void setValueFromMqttPayload(String payload);
}
