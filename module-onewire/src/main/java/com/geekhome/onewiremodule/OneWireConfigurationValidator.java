package com.geekhome.onewiremodule;

import com.geekhome.common.IHardwareManagerAdapter;
import com.geekhome.common.json.JSONArrayList;
import com.geekhome.hardwaremanager.IHardwareManager;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.httpserver.modules.IConfigurationValidator;

public class OneWireConfigurationValidator implements IConfigurationValidator {
    private ILocalizationProvider _localizationProvider;
    private final IHardwareManager _hardwareManager;

    public OneWireConfigurationValidator(ILocalizationProvider localizationProvider, IHardwareManager hardwareManager) {
        _localizationProvider = localizationProvider;
        _hardwareManager = hardwareManager;
    }

    @Override
    public void addValidations(JSONArrayList<String> validations) {
        for (IHardwareManagerAdapter adapter : _hardwareManager.getAdapters()) {
            if (adapter instanceof OneWireAdapter) {
                OneWireAdapter oneWireAdapter = (OneWireAdapter)adapter;
                for (SwitchDiscoveryInfo di : oneWireAdapter.getAllSwitches()) {
                    SwitchRole role = di.calculateSwitchRole(_hardwareManager);

                    if (role == SwitchRole.BothConfliting) {
                        String message = String.format(_localizationProvider.getValue("OW:SwitchConflictMessage"), di.getAddress());
                        validations.add(message);
                    }
                }
            }
        }
    }
}
