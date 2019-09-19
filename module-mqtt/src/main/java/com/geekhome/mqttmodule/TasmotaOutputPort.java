package com.geekhome.mqttmodule;

import com.geekhome.common.SynchronizedOutputPort;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public abstract class  TasmotaOutputPort<T> extends SynchronizedOutputPort<T> {

    private String _topic;
    private MqttClientResolver _clientResolver;

    public TasmotaOutputPort(MqttClientResolver client, String id, T initialValue) {
        super(id, initialValue);
        _clientResolver = client;
        String deviceId = getId();
        int channel = -1;
        if (getId().contains(":")) {
            String[] idSplit = getId().split(":");
            deviceId = idSplit[0];
            channel = Integer.valueOf(idSplit[1]);
        }

        _topic = "geekhome/" + deviceId + "/cmnd/" + getTopicEnding();
        if (channel >= 0) {
            _topic += String.valueOf(channel + 1);
        }
    }

    @Override
    public void write(T value) throws Exception {
        T currentValue = read();
        if (value != currentValue) {
            super.write(value);

            String content = buildMqttContentFromValue(value);
            publishMessage(content);
        }
    }

    private void publishMessage(String content) throws MqttException {
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(0);
        MqttClient client = _clientResolver.getClient();
        if (!client.isConnected()) {
            client.connect();
        }
        client.publish(_topic, message);
    }

    protected abstract String buildMqttContentFromValue(T value);

    protected abstract String getTopicEnding();
}
