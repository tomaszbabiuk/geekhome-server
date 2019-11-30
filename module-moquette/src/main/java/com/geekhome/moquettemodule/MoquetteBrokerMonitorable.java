package com.geekhome.moquettemodule;

import com.geekhome.common.DescriptiveName;
import com.geekhome.common.MonitorableBase;
import io.moquette.BrokerConstants;
import io.moquette.broker.Server;
import io.moquette.broker.config.*;
import io.moquette.interception.AbstractInterceptHandler;
import io.moquette.interception.InterceptHandler;
import io.moquette.interception.messages.*;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.MqttMessageBuilders;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;

import java.net.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

public class MoquetteBrokerMonitorable extends MonitorableBase {


    public MoquetteBrokerMonitorable() {
        super(new DescriptiveName("Moquette Broker", "MQTB"), false, "N/A");
    }

    static class PublisherListener extends AbstractInterceptHandler {

        @Override
        public String getID() {
            return "EmbeddedLauncherPublishListener";
        }

        @Override
        public void onPublish(InterceptPublishMessage msg) {
            System.out.println("Received on topic: " + msg.getTopicName());

            while (msg.getPayload().isReadable()) {
                System.out.println(msg.getPayload().readByte());
            }
        }
    }

    @Override
    public void start() {
        try {
            Server mqttBroker = new Server();
            List<? extends InterceptHandler> userHandlers = Collections.singletonList(new PublisherListener());

            MemoryConfig memoryConfig = new MemoryConfig(new Properties());
            memoryConfig.setProperty(BrokerConstants.ALLOW_ANONYMOUS_PROPERTY_NAME, "true");

            mqttBroker.startServer(memoryConfig, userHandlers);

//            Thread.sleep(20000);
//            System.out.println("Before self publish");
//            MqttPublishMessage message = MqttMessageBuilders.publish()
//                    .topicName("/exit")
//                    .retained(true)
//                    .qos(MqttQoS.EXACTLY_ONCE)
//                    .payload(Unpooled.copiedBuffer("Hello World!!".getBytes()))
//                    .build();
//
//            mqttBroker.internalPublish(message, "INTRLPUB");
//            System.out.println("After self publish");

            updateStatus(true, "OK");
        } catch (Exception ex) {
            updateStatus(false, ex.getMessage());
        }
    }
}
