package com.geekhome.ventilationmodule;

import com.geekhome.common.configuration.JSONArrayList;
import com.geekhome.common.localization.ILocalizationProvider;
import com.geekhome.common.configuration.IConfigurationValidator;

public class VentilationConfigurationValidator implements IConfigurationValidator {
    private ILocalizationProvider _localizationProvider;
    private VentilationConfiguration _ventilationConfiguration;

    public VentilationConfigurationValidator(ILocalizationProvider localizationProvider, VentilationConfiguration ventilationConfiguration) {
        _localizationProvider = localizationProvider;
        _ventilationConfiguration = ventilationConfiguration;
    }

    @Override
    public void addValidations(JSONArrayList<String> validations) {
        for (Recuperator recuperator : _ventilationConfiguration.getRecuperators().values()) {
            if (recuperator.getAutomaticControlPortId().equals(recuperator.getSecondGearPortId()) ||
                recuperator.getAutomaticControlPortId().equals(recuperator.getThirdGearPortId()) ||
                recuperator.getSecondGearPortId().equals(recuperator.getThirdGearPortId())) {
                String validationMessage = String.format(_localizationProvider.getValue("VENT:RecuperatorPortsAreSame"), recuperator.toString());
                validations.add(validationMessage);
            }
        }
    }
}
