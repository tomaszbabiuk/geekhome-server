package com.geekhome.shellymodule;

import com.geekhome.common.IHardwareManagerAdapterFactory;
import com.geekhome.common.hardwaremanager.IHardwareManagerAdapter;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.common.hardwaremanager.HardwareManager;

import java.util.ArrayList;

class GreeAdapterFactory implements IHardwareManagerAdapterFactory {

    private HardwareManager _hardwareManager;
    private ILocalizationProvider _localizationProvider;

    GreeAdapterFactory(final HardwareManager hardwareManager,
                       final ILocalizationProvider localizationProvider) {
        _hardwareManager = hardwareManager;
        _localizationProvider = localizationProvider;
    }

    @Override
    public ArrayList<? extends IHardwareManagerAdapter> createAdapters() {
        ArrayList<IHardwareManagerAdapter> result = new ArrayList<>();
        GreeAdapter mqttAdapter = new GreeAdapter(_hardwareManager, _localizationProvider);
        result.add(mqttAdapter);

        return result;
    }
}