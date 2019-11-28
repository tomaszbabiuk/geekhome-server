package com.geekhome.shellymodule;

import com.geekhome.common.HardwareManager;
import com.geekhome.common.IHardwareManagerAdapter;
import com.geekhome.common.IHardwareManagerAdapterFactory;
import com.geekhome.http.ILocalizationProvider;

import java.util.ArrayList;

class ShellyAdapterFactory implements IHardwareManagerAdapterFactory {

    private HardwareManager _hardwareManager;
    private ILocalizationProvider _localizationProvider;

    ShellyAdapterFactory(final HardwareManager hardwareManager,
                         final ILocalizationProvider localizationProvider) {
        _hardwareManager = hardwareManager;
        _localizationProvider = localizationProvider;
    }

    @Override
    public ArrayList<? extends IHardwareManagerAdapter> createAdapters() {
        ArrayList<IHardwareManagerAdapter> result = new ArrayList<>();
        ShellyAdapter mqttAdapter = new ShellyAdapter(_hardwareManager, _localizationProvider);
        result.add(mqttAdapter);

        return result;
    }
}