package com.geekhome.onewiremodule;

import com.geekhome.common.hardwaremanager.IHardwareManagerAdapter;
import com.geekhome.common.SerialAdapterFactoryBase;
import com.geekhome.coremodule.settings.AutomationSettings;
import com.geekhome.common.hardwaremanager.IHardwareManager;
import com.geekhome.http.ILocalizationProvider;

class OneWireSerialAdaptersFactory extends SerialAdapterFactoryBase {
    private IHardwareManager _hardwareManager;
    private AutomationSettings _automationSettings;
    private final ILocalizationProvider _localizationProvider;

    OneWireSerialAdaptersFactory(IHardwareManager hardwareManager, AutomationSettings automationSettings, ILocalizationProvider localizationProvider) {
        _hardwareManager = hardwareManager;
        _automationSettings = automationSettings;
        _localizationProvider = localizationProvider;
    }

    @Override
    protected String getFactoryType() {
        return "1wire";
    }

    @Override
    protected IHardwareManagerAdapter createAdapter(String adapterPort, String adapterAlias) throws Exception {
        return new OneWireAdapter(_hardwareManager, adapterPort, _automationSettings, _localizationProvider);
    }
}
