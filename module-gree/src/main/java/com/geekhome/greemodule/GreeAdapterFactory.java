package com.geekhome.greemodule;

import com.geekhome.common.IHardwareManagerAdapter;
import com.geekhome.common.IHardwareManagerAdapterFactory;
import com.geekhome.http.ILocalizationProvider;

import java.util.ArrayList;

class GreeAdapterFactory implements IHardwareManagerAdapterFactory {

    private ILocalizationProvider _localizationProvider;

    GreeAdapterFactory(final ILocalizationProvider localizationProvider) {
        _localizationProvider = localizationProvider;
    }

    @Override
    public ArrayList<? extends IHardwareManagerAdapter> createAdapters() {
        ArrayList<IHardwareManagerAdapter> result = new ArrayList<>();
        GreeAdapter mqttAdapter = new GreeAdapter(_localizationProvider);
        result.add(mqttAdapter);

        return result;
    }
}