package com.geekhome.onewiremodule;

import com.geekhome.common.hardwaremanager.IHardwareManagerAdapterFactory;
import com.geekhome.common.settings.AutomationSettings;
import com.geekhome.common.hardwaremanager.IHardwareManager;
import com.geekhome.common.localization.ILocalizationProvider;
import com.geekhome.common.localization.Resource;
import com.geekhome.common.configuration.IConfigurationValidator;
import com.geekhome.coremodule.modules.Module;

import java.util.ArrayList;

public class OneWireModule extends Module {
    private ILocalizationProvider _localizationProvider;
    private IHardwareManager _hardwareManager;
    private AutomationSettings _automationSettings;

    public OneWireModule(ILocalizationProvider localizationProvider, IHardwareManager hardwareManager, AutomationSettings automationSettings) {
        _localizationProvider = localizationProvider;
        _hardwareManager = hardwareManager;
        _automationSettings = automationSettings;
    }

    @Override
    public void addSerialAdaptersFactory(ArrayList<IHardwareManagerAdapterFactory> factories) {
        IHardwareManagerAdapterFactory factory = new OneWireSerialAdaptersFactory(_hardwareManager, _automationSettings, _localizationProvider);
        factories.add(factory);
    }

    @Override
    public IConfigurationValidator createConfigurationValidator() {
        return new OneWireConfigurationValidator(_localizationProvider, _hardwareManager);
    }

    @Override
    public Resource[] getResources() {
        return new Resource[] {
                new Resource("OW:SwitchConflictMessage", "Conflict detected: switch DS2408 '%s' is used as relay board and alarm sensing board at once!",
                        "Wykryto konflikt: karta DS2408 '%s' jest używana jednocześnie jako karta przekaźników oraz karta wejść alarmowych!"),
        };
    }

    @Override
    public String getTextResourcesPrefix() {
        return "OW";
    }
}
