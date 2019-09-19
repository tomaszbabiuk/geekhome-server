package com.geekhome.lightsmodule;

import com.geekhome.common.json.JSONArrayList;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.httpserver.modules.IConfigurationValidator;

public class LightsConfigurationValidator implements IConfigurationValidator {
    private ILocalizationProvider _localizationProvider;
    private LightsConfiguration _lightsConfiguration;

    public LightsConfigurationValidator(ILocalizationProvider localizationProvider, LightsConfiguration lightsConfiguration) {
        _localizationProvider = localizationProvider;
        _lightsConfiguration = lightsConfiguration;
    }

    @Override
    public void addValidations(JSONArrayList<String> validations) {
        for (RgbLamp rgbLamp : _lightsConfiguration.getRgbLamps().values()) {
            if (rgbLamp.getRedPortId().equals(rgbLamp.getGreenPortId()) ||
                rgbLamp.getRedPortId().equals(rgbLamp.getBluePortId()) ||
                rgbLamp.getGreenPortId().equals(rgbLamp.getBluePortId())) {
                String validationMessage = String.format(_localizationProvider.getValue("LIGHTS:RgbLampPortsAreSame"), rgbLamp.toString());
                validations.add(validationMessage);
            }
        }

        for (SevenColorLamp sevenColorLamp : _lightsConfiguration.get7ColorLamps().values()) {
            if (sevenColorLamp.getRedPortId().equals(sevenColorLamp.getGreenPortId()) ||
                    sevenColorLamp.getRedPortId().equals(sevenColorLamp.getBluePortId()) ||
                    sevenColorLamp.getGreenPortId().equals(sevenColorLamp.getBluePortId())) {
                String validationMessage = String.format(_localizationProvider.getValue("LIGHTS:7ColorLampPortsAreSame"), sevenColorLamp.toString());
                validations.add(validationMessage);
            }
        }

        for (Blind blind : _lightsConfiguration.getBlinds().values()) {
            if (blind.getAutomaticControlPortId().equals(blind.getMotorPortId())) {
                String validationMessage = String.format(_localizationProvider.getValue("LIGHTS:BlindPortsAreSame"), blind.toString());
                validations.add(validationMessage);
            }
        }
    }
}
