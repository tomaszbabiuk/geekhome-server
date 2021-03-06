package com.geekhome.pi4jmodule;

import com.geekhome.common.hardwaremanager.IHardwareManagerAdapterFactory;
import com.geekhome.common.localization.ILocalizationProvider;
import com.geekhome.common.localization.Resource;
import com.geekhome.coremodule.modules.Module;

import java.util.ArrayList;

public class Pi4JModule extends Module {
    private ILocalizationProvider _localizationProvider;

    public Pi4JModule(ILocalizationProvider localizationProvider) {
        _localizationProvider = localizationProvider;
    }

    @Override
    public Resource[] getResources() {
        return new Resource[0];
    }

    @Override
    public String getTextResourcesPrefix() {
        return "P4J";
    }

    @Override
    public void addSerialAdaptersFactory(ArrayList<IHardwareManagerAdapterFactory> factories) {
        factories.add(new Pi4jAdapterFactory(_localizationProvider));
    }
}
