package com.geekhome.shellymodule;

import com.geekhome.common.hardwaremanager.HardwareManager;
import com.geekhome.common.hardwaremanager.IHardwareManagerAdapter;
import com.geekhome.common.hardwaremanager.IHardwareManagerAdapterFactory;
import com.geekhome.common.localization.ILocalizationProvider;

import java.util.ArrayList;

class AforeAdapterFactory implements IHardwareManagerAdapterFactory {

    private HardwareManager _hardwareManager;
    private ILocalizationProvider _localizationProvider;

    AforeAdapterFactory(final HardwareManager hardwareManager,
                        final ILocalizationProvider localizationProvider) {
        _hardwareManager = hardwareManager;
        _localizationProvider = localizationProvider;
    }

    @Override
    public ArrayList<? extends IHardwareManagerAdapter> createAdapters() {
        ArrayList<IHardwareManagerAdapter> result = new ArrayList<>();
        AforeAdapter mqttAdapter = new AforeAdapter(_hardwareManager, _localizationProvider);
        result.add(mqttAdapter);

        return result;
    }
}