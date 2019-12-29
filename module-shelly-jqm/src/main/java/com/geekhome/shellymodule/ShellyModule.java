package com.geekhome.shellymodule;

import com.geekhome.common.hardwaremanager.IHardwareManagerAdapterFactory;
import com.geekhome.common.localization.Resource;
import com.geekhome.coremodule.modules.Module;
import com.geekhome.moquettemodule.MqttBroker;

import java.util.ArrayList;

public class ShellyModule extends Module {

    private MqttBroker _mqttBroker;

    public ShellyModule(final MqttBroker mqttBroker) {
        _mqttBroker = mqttBroker;
    }

    @Override
    public Resource[] getResources() {
        return new Resource[0];
    }

    @Override
    public String getTextResourcesPrefix() {
        return "SLY";
    }

    @Override
    public void addSerialAdaptersFactory(ArrayList<IHardwareManagerAdapterFactory> factories) {
        factories.add(new ShellyAdapterFactory(_mqttBroker));
    }
}