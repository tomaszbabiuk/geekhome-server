package com.geekhome.mqttmodule;

import com.geekhome.common.SynchronizedInputPort;
import com.geekhome.common.SynchronizedOutputPort;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.hardwaremanager.InputPortsCollection;
import com.geekhome.hardwaremanager.OutputPortsCollection;
import com.geekhome.hardwaremanager.TogglePortsCollection;
import com.geekhome.mqttmodule.tasmotaapi.Power;
import com.geekhome.mqttmodule.tasmotaapi.PowerStatus;
import com.geekhome.mqttmodule.tasmotaapi.SensorStatus;

import javax.jmdns.ServiceEvent;
import java.io.IOException;
import java.net.Inet4Address;
import java.util.Map;

public class SonoffDiscoveryHelper {
    private static ILogger _logger = LoggingService.getLogger();
    private final String _mqttHost;
    private final MqttClientResolver _mqttClientResolver;
    private final SonoffHijacker _sonoffHijacker;

    public SonoffDiscoveryHelper(final String mqttHost,
                                 final MqttClientResolver mqttClientResolver,
                                 final SonoffHijacker sonoffHijacker) {
        _mqttHost = mqttHost;
        _mqttClientResolver = mqttClientResolver;
        _sonoffHijacker = sonoffHijacker;
    }

    public void processSonoffDiscovery(final ServiceEvent serviceEvent,
                                       final InputPortsCollection<Boolean> digitalInputPorts,
                                       final OutputPortsCollection<Boolean> digitalOutputPorts,
                                       final InputPortsCollection<Double> powerInputPorts,
                                       final OutputPortsCollection<Integer> powerOutputPorts,
                                       final InputPortsCollection<Double> temperaturePorts,
                                       final TogglePortsCollection togglePortsCollection,
                                       final InputPortsCollection<Double> humidityPorts,
                                       final InputPortsCollection<Double> luminosityPorts) {
        Inet4Address address = serviceEvent.getInfo().getInet4Addresses()[0];
        _logger.info("Getting status of: " + serviceEvent.getName());

        try {
            PowerStatus powerStatus = _sonoffHijacker.readPowerStatus(address);
            if (powerStatus.getPower() != null) {
                addDigitalOutputPort(serviceEvent, -1, powerStatus.getPower(), digitalOutputPorts);
            }

            if (powerStatus.getPower1() != null) {
                addDigitalOutputPort(serviceEvent, 0, powerStatus.getPower1(), digitalOutputPorts);
            }

            if (powerStatus.getPower2() != null) {
                addDigitalOutputPort(serviceEvent, 1, powerStatus.getPower2(), digitalOutputPorts);
            }

            if (powerStatus.getPower3() != null) {
                addDigitalOutputPort(serviceEvent, 2, powerStatus.getPower3(), digitalOutputPorts);
            }

            if (powerStatus.getPower4() != null) {
                addDigitalOutputPort(serviceEvent, 3, powerStatus.getPower4(), digitalOutputPorts);
            }

            Map<String, Integer> pwm = powerStatus.getPwm();
            for (String tasmotaPWMChannel : pwm.keySet()) {
                Integer initialPwmValue = pwm.get(tasmotaPWMChannel);
                int channel = Integer.valueOf(tasmotaPWMChannel.replace("PWM", "")) - 1;
                addPowerOutputPort(serviceEvent, channel, powerOutputPorts, initialPwmValue);
            }

            SensorStatus sensorStatus = _sonoffHijacker.readSensorStatus(address);
            try {
                double initialTemperature = sensorStatus.extractTemperature();
                addTemperaturePort(serviceEvent, temperaturePorts, initialTemperature);
            } catch (SensorStatus.StatusExtractionException ignored) {
            }
            try {
                double initialHumidity = sensorStatus.extractHumidity();
                addHumidityPort(serviceEvent, humidityPorts, initialHumidity);
            } catch (SensorStatus.StatusExtractionException ignored) {
            }

        } catch (IOException e) {
            _logger.error("Problem hijacking sonoff device", e);
        }
    }

    private void addPowerOutputPort(final ServiceEvent serviceEvent,
                                     final int channel,
                                     final OutputPortsCollection<Integer> powerOutputPorts,
                                     final Integer initialValue) {
        String portId = serviceEvent.getName() + ":" + channel;
        SynchronizedOutputPort<Integer> powerOutputPort = new TasmotaPowerOutputPort(_mqttClientResolver, portId, initialValue, 0);
        powerOutputPorts.add(powerOutputPort);
    }

    private void addTemperaturePort(final ServiceEvent serviceEvent,
                                    final InputPortsCollection<Double> temperaturePorts,
                                    final double initialTemperature) {
        SynchronizedInputPort<Double> temperaturePort = new SynchronizedInputPort<>(serviceEvent.getName(), initialTemperature, 0);
        temperaturePorts.add(temperaturePort);
    }

    private void addHumidityPort(final ServiceEvent serviceEvent,
                                 final InputPortsCollection<Double> humidityPorts,
                                 final double initialHumidity) {
        SynchronizedInputPort<Double> humidityPort = new SynchronizedInputPort<>(serviceEvent.getName(), initialHumidity, 0);
        humidityPorts.add(humidityPort);
    }

    private void addDigitalOutputPort(final ServiceEvent serviceEvent,
                                      final int channel,
                                      final Power power,
                                      final OutputPortsCollection<Boolean> digitalOutputPorts) {
        String portId = serviceEvent.getName();
        if (channel >=0) {
            portId += ":" + String.valueOf(channel);
        }

        SynchronizedOutputPort<Boolean> outputPort = new TasmotaDigitalOutputPort(_mqttClientResolver,
                portId, power.asBoolean(), 0);
        digitalOutputPorts.add(outputPort);
    }
}
