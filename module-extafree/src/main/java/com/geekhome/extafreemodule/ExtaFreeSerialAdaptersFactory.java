package com.geekhome.extafreemodule;

import com.geekhome.common.hardwaremanager.IHardwareManagerAdapter;
import com.geekhome.common.SerialAdapterFactoryBase;
import com.geekhome.common.localization.ILocalizationProvider;

class ExtaFreeSerialAdaptersFactory extends SerialAdapterFactoryBase {
    private final ILocalizationProvider _localizationProvider;

    @Override
    protected String getFactoryType() {
        return "extafree";
    }

    @Override
    protected IHardwareManagerAdapter createAdapter(String adapterPort, String adapterAlias) {
        return new ExtaFreeSerialAdapter(adapterPort, _localizationProvider);
    }

    ExtaFreeSerialAdaptersFactory(ILocalizationProvider localizationProvider) {
        _localizationProvider = localizationProvider;
    }
}
