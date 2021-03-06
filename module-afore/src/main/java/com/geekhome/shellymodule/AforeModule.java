package com.geekhome.shellymodule;

import com.geekhome.common.hardwaremanager.HardwareManager;
import com.geekhome.common.hardwaremanager.IHardwareManagerAdapterFactory;
import com.geekhome.common.localization.ILocalizationProvider;
import com.geekhome.common.localization.Resource;
import com.geekhome.coremodule.modules.Module;

import java.util.ArrayList;

public class AforeModule extends Module {

    private HardwareManager _hardwareManager;
    private ILocalizationProvider _localizationProvider;

    public AforeModule(final HardwareManager hardwareManager,
                      final ILocalizationProvider localizationProvider) {
        _hardwareManager = hardwareManager;
        _localizationProvider = localizationProvider;
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
        factories.add(new AforeAdapterFactory(_hardwareManager, _localizationProvider));
    }
}
