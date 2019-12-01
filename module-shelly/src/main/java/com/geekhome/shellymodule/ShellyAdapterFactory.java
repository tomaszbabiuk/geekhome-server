package com.geekhome.shellymodule;

import com.geekhome.common.HardwareManager;
import com.geekhome.common.IHardwareManagerAdapter;
import com.geekhome.common.IHardwareManagerAdapterFactory;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.moquettemodule.MqttBroker;

import java.util.ArrayList;

class ShellyAdapterFactory implements IHardwareManagerAdapterFactory {

    private HardwareManager _hardwareManager;
    private ILocalizationProvider _localizationProvider;
    private MqttBroker _mqttBroker;

    ShellyAdapterFactory(final HardwareManager hardwareManager,
                         final ILocalizationProvider localizationProvider,
                         MqttBroker mqttBroker) {
        _hardwareManager = hardwareManager;
        _localizationProvider = localizationProvider;
        _mqttBroker = mqttBroker;
    }

    @Override
    public ArrayList<? extends IHardwareManagerAdapter> createAdapters() {
        ArrayList<IHardwareManagerAdapter> result = new ArrayList<>();
        ShellyAdapter adapter = new ShellyAdapter(_hardwareManager, _localizationProvider, _mqttBroker);
        result.add(adapter);

        return result;
    }
}