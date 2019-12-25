package com.geekhome.extafreemodule;

import com.geekhome.common.configuration.JSONArrayList;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.httpserver.modules.IConfigurationValidator;

public class ExtaFreeConfigurationValidator implements IConfigurationValidator {
    private ILocalizationProvider _localizationProvider;
    private ExtaFreeConfiguration _extaFreeConfiguration;

    public ExtaFreeConfigurationValidator(ILocalizationProvider localizationProvider, ExtaFreeConfiguration extaFreeConfiguration) {
        _localizationProvider = localizationProvider;
        _extaFreeConfiguration = extaFreeConfiguration;
    }

    @Override
    public void addValidations(JSONArrayList<String> validations) {
        for (ExtaFreeBlind blind : _extaFreeConfiguration.getExtaFreeBlinds().values()) {
            if (blind.getUpPortId().equals(blind.getDownPortId())) {
                String validationMessage = String.format(_localizationProvider.getValue("EXF:BlindPortsAreSame"), blind.toString());
                validations.add(validationMessage);
            }
        }
    }
}
