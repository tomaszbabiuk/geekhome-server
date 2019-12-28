package com.geekhome.mqttmodule;

import com.geekhome.common.hardwaremanager.IHardwareManagerAdapter;
import com.geekhome.common.IHardwareManagerAdapterFactory;
import com.geekhome.common.hardwaremanager.IHardwareManager;
import com.geekhome.common.localization.ILocalizationProvider;

import java.util.ArrayList;

class MqttAdapterFactory implements IHardwareManagerAdapterFactory {

    private ILocalizationProvider _localizationProvider;
    private String _mqttHost;
    private IHardwareManager _hardwareManager;

    MqttAdapterFactory(final ILocalizationProvider localizationProvider,
                       final String mqttHost,
                       final IHardwareManager hardwareManager) {
        _localizationProvider = localizationProvider;
        _mqttHost = mqttHost;
        _hardwareManager = hardwareManager;
    }

    @Override
    public ArrayList<? extends IHardwareManagerAdapter> createAdapters() {
        ArrayList<IHardwareManagerAdapter> result = new ArrayList<>();
        MqttAdapter mqttAdapter = new MqttAdapter(_localizationProvider, _mqttHost, _hardwareManager);
        result.add(mqttAdapter);

        return result;
    }
}