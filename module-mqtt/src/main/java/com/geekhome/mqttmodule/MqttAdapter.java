package com.geekhome.mqttmodule;

import com.geekhome.common.*;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.hardwaremanager.*;
import com.geekhome.common.localization.ILocalizationProvider;
import com.geekhome.common.OperationMode;
import com.geekhome.mqttmodule.tasmotaapi.GsonProvider;
import com.geekhome.mqttmodule.tasmotaapi.SensorStatus;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;

class MqttAdapter extends NamedObject implements IHardwareManagerAdapter, MqttCallback {

    private final SonoffHijacker _sonoffHijacker;
    private final SonoffDiscoveryHelper _sonoffDiscoveryHelper;
    private final GsonProvider _gsonProvider;
    private ILocalizationProvider _localizationProvider;
    private String _mqttHost;
    private IHardwareManager _hardwareManager;
    private MqttClientResolver _mqttClientResolver;
    private static ILogger _logger = LoggingService.getLogger();
    private MqttClient _mqttClient;
    private boolean _isOperational;


    MqttAdapter(final ILocalizationProvider localizationProvider,
                final String mqttHost,
                final IHardwareManager hardwareManager) {
        super(new DescriptiveName("MQTT Adapter", "MQTT"));
        _localizationProvider = localizationProvider;
        _mqttHost = mqttHost;
        _hardwareManager = hardwareManager;
        _gsonProvider = new GsonProvider();
        _sonoffHijacker = new SonoffHijacker(_gsonProvider);
        _mqttClientResolver = new MqttClientResolver();
        _sonoffDiscoveryHelper = new SonoffDiscoveryHelper(mqttHost, _mqttClientResolver, _sonoffHijacker);
    }

    @Override
    public void discover(final InputPortsCollection<Boolean> digitalInputPorts,
                           final OutputPortsCollection<Boolean> digitalOutputPorts,
                           final InputPortsCollection<Double> powerInputPorts,
                           final OutputPortsCollection<Integer> powerOutputPorts,
                           final InputPortsCollection<Double> temperaturePorts,
                           final TogglePortsCollection togglePorts,
                           final InputPortsCollection<Double> humidityPorts,
                           final InputPortsCollection<Double> luminosityPorts) throws DiscoveryException {
        _logger.info("Starting Sonoff discovery");
        _logger.info("Using mDNS and hijacking MQTT settings");
        try {
            JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());
            final ArrayList<ServiceEvent> sonoffsFound = new ArrayList<>();
            // Add a service listener
            jmdns.addServiceListener("_http._tcp.local.", new ServiceListener() {
                @Override
                public void serviceAdded(ServiceEvent serviceEvent) {
                }

                @Override
                public void serviceRemoved(ServiceEvent serviceEvent) {

                }

                @Override
                public void serviceResolved(ServiceEvent serviceEvent) {
                    if (serviceEvent.getName().toLowerCase().startsWith("sonoff")) {
                        sonoffsFound.add(serviceEvent);
                        Inet4Address address = serviceEvent.getInfo().getInet4Addresses()[0];
                        try {
                            _sonoffHijacker.hijackMqtt(address, _mqttHost, "geekhome/" + serviceEvent.getName());
                        } catch (IOException e) {
                            try {
                                _sonoffHijacker.hijackMqtt(address, _mqttHost, "geekhome/" + serviceEvent.getName());
                            } catch (IOException e2) {
                                _logger.error("Cannot hijack sonoff device: " + serviceEvent.getName(), e2);
                            }
                        }
                    }
                }
            });

            Thread.sleep(10000);
            _logger.info("mDNS discovery ~ 10 secs left");
            Thread.sleep(10000);

            for (ServiceEvent sonoffService : sonoffsFound) {
                _sonoffDiscoveryHelper.processSonoffDiscovery(sonoffService, digitalInputPorts, digitalOutputPorts,
                    powerInputPorts, powerOutputPorts, temperaturePorts, togglePorts, humidityPorts, luminosityPorts);
            }
        } catch (ConnectException cex) {
            _logger.info("Cannot connect to MQTT server... discovery skipped.");
        } catch (Exception e) {
            throw new DiscoveryException("Unhandled error during MQTT discovery", e);
        }
    }

    @Override
    public boolean canBeRediscovered() {
        return false;
    }


    @Override
    public boolean isOperational() {
        return _isOperational;
    }

    @Override
    public void start() {
        String topic = "geekhome/#";
        String broker = "tcp://" + _mqttHost + ":1883";
        String clientId = "GeekHOME v1";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            _mqttClient = new MqttClient(broker, clientId, persistence);

            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(10);
            connOpts.setConnectionTimeout(100);
            _mqttClient.connect(connOpts);

            _mqttClient.subscribe(topic);
            _mqttClient.setCallback(this);

            _mqttClientResolver.setClient(_mqttClient);
            _isOperational = true;
        } catch (MqttException me) {
            _logger.error("Problem connecting to MQTT broker!", me);
        }
    }

    @Override
    public String getStatus() {
        return _localizationProvider.getValue("C:NA");
    }

    @Override
    public void reconfigure(OperationMode operationMode) {
    }

    @Override
    public void stop() {
        try {
            _mqttClient.disconnect();
        } catch (MqttException e) {
            _logger.warning("Problem disconnecting from mqtt broker", e);
        }
    }

    @Override
    public void refresh(Calendar now) throws Exception {
    }

    @Override
    public RefreshState getRefreshState() {
        return RefreshState.NA;
    }

    @Override
    public void resetLatches() {

    }

    @Override
    public void connectionLost(Throwable throwable) {
        _logger.error("MQTT connection lost", throwable);
        _isOperational = false;
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) {
        _logger.info("MQTT message arrived " + mqttMessage.toString());

        if (topic.startsWith("geekhome")) {
            processPowerTopic(topic, mqttMessage.toString(), -1);
            processPowerTopic(topic, mqttMessage.toString(), 0);
            processPowerTopic(topic, mqttMessage.toString(), 1);
            processPowerTopic(topic, mqttMessage.toString(), 2);
            processPowerTopic(topic, mqttMessage.toString(), 3);
            processSensorTopic(topic, mqttMessage.toString());
            processPWMTopic(topic, mqttMessage.toString(), 0);
            processPWMTopic(topic, mqttMessage.toString(), 1);
            processPWMTopic(topic, mqttMessage.toString(), 2);
            processPWMTopic(topic, mqttMessage.toString(), 3);
            processPWMTopic(topic, mqttMessage.toString(), 4);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        _logger.info("MQTT message delivered");
    }

    private String extractDeviceIdFromTopic(String topic) {
        return topic.split("/")[1];
    }

    private void processPowerTopic(String topic, String mqttMessage, int channel) {
        String deviceId = extractDeviceIdFromTopic(topic);
        String expectedEnding = "POWER";
        if (channel >=0) {
            expectedEnding += String.valueOf(channel + 1);
            deviceId += ":" + channel;
        }
        if (topic.endsWith(expectedEnding)) {
            SynchronizedOutputPort<Boolean> outputPort = (SynchronizedOutputPort<Boolean>) _hardwareManager.tryFindDigitalOutputPort(deviceId);
            if (outputPort != null) {
                boolean state = mqttMessage.equals("ON");
                outputPort.setValue(state);
            }
        }
    }

    private void processPWMTopic(String topic, String mqttMessage, int channel) {
        String deviceId = extractDeviceIdFromTopic(topic);
        String expectedEnding = "PWM";
        if (channel >=0) {
            expectedEnding += String.valueOf(channel + 1);
            deviceId += ":" + channel;
        }
        if (topic.endsWith(expectedEnding)) {
            SynchronizedOutputPort<Integer> outputPort = (SynchronizedOutputPort<Integer>) _hardwareManager.tryFindPowerOutputPort(deviceId);
            if (outputPort != null) {
                Integer value = Integer.valueOf(mqttMessage);
                outputPort.setValue(value);
            }
        }
    }

    private void processSensorTopic(String topic, String mqttMessage) {
        if (topic.endsWith("SENSOR")) {
            SensorStatus status = _gsonProvider.provide().fromJson(mqttMessage, SensorStatus.class);
            String deviceId = extractDeviceIdFromTopic(topic);
            try {
                double temperature = status.extractTemperature();
                SynchronizedInputPort<Double> temperaturePort =
                        (SynchronizedInputPort<Double>) _hardwareManager.tryFindTemperaturePort(deviceId);
                if (temperaturePort != null) {
                    temperaturePort.setValue(temperature);
                }
            } catch (SensorStatus.StatusExtractionException ignored) {
            }

            try {
                double humidity = status.extractHumidity();
                SynchronizedInputPort<Double> humidityPort =
                        (SynchronizedInputPort<Double>) _hardwareManager.tryFindHumidityPort(deviceId);
                if (humidityPort != null) {
                    humidityPort.setValue(humidity);
                }
            } catch (SensorStatus.StatusExtractionException ignored) {
            }
        }
    }
}
