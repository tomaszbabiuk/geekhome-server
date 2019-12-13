package com.geekhome.shellymodule;

import com.geekhome.common.IHardwareManagerAdapter;
import com.geekhome.common.IHardwareManagerAdapterFactory;
import com.geekhome.moquettemodule.MqttBroker;

import java.util.ArrayList;

class ShellyAdapterFactory implements IHardwareManagerAdapterFactory {

    private MqttBroker _mqttBroker;

    ShellyAdapterFactory(MqttBroker mqttBroker) {
        _mqttBroker = mqttBroker;
    }

    @Override
    public ArrayList<? extends IHardwareManagerAdapter> createAdapters() {
        ArrayList<IHardwareManagerAdapter> result = new ArrayList<>();
        ShellyAdapter adapter = new ShellyAdapter(_mqttBroker);
        result.add(adapter);

        return result;
    }
}