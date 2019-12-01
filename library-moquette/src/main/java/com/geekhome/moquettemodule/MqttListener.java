package com.geekhome.moquettemodule;

public interface MqttListener {
    void onPublish(String topicName, String msgAsString);
}
