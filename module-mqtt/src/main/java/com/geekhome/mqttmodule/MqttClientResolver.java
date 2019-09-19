package com.geekhome.mqttmodule;

import org.eclipse.paho.client.mqttv3.MqttClient;

public class MqttClientResolver {
    private MqttClient _client;

    public MqttClient getClient() {
        return _client;
    }

    public void setClient(MqttClient client) {
        _client = client;
    }
}
