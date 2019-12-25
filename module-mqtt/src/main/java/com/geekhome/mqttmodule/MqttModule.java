package com.geekhome.mqttmodule;

import com.geekhome.common.IHardwareManagerAdapterFactory;
import com.geekhome.common.hardwaremanager.IHardwareManager;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.http.Resource;
import com.geekhome.httpserver.modules.Module;

import java.util.ArrayList;

public class MqttModule extends Module {

    private static final String ARG_MQTT_HOST = "mqtthost=";
    private static final String DAFAULT_MQTT_HOST = "127.0.0.1";
    private IHardwareManager _hardwareManager;
    private final String _mqttHost;

    private ILocalizationProvider _localizationProvider;

    public MqttModule(final ILocalizationProvider localizationProvider,
                      final IHardwareManager hardwareManager,
                      final String[] args) {
        _localizationProvider = localizationProvider;
        _hardwareManager = hardwareManager;
        _mqttHost = extractMqttHost(args);
    }

    @Override
    public Resource[] getResources() {
        return new Resource[0];
    }

    @Override
    public String getTextResourcesPrefix() {
        return "MQTT";
    }

    @Override
    public void addSerialAdaptersFactory(ArrayList<IHardwareManagerAdapterFactory> factories) {
        factories.add(new MqttAdapterFactory(_localizationProvider, _mqttHost, _hardwareManager));
    }

    private String extractMqttHost(String[] args) {
        for (String arg : args) {
            if (arg.toLowerCase().startsWith(ARG_MQTT_HOST)) {
                return arg.substring(ARG_MQTT_HOST.length());
            }
        }

        return DAFAULT_MQTT_HOST;
    }
}
