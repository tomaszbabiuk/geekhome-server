package com.geekhome.alarmmodule;

import com.geekhome.common.configuration.JSONArrayList;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.common.configuration.IConfigurationValidator;

public class AlarmConfigurationValidator implements IConfigurationValidator {
    private ILocalizationProvider _localizationProvider;
    private AlarmConfiguration _alarmConfiguration;

    public AlarmConfigurationValidator(ILocalizationProvider localizationProvider, AlarmConfiguration alarmConfiguration) {
        _localizationProvider = localizationProvider;
        _alarmConfiguration = alarmConfiguration;
    }

    @Override
    public void addValidations(JSONArrayList<String> validations) {
        for (AlarmZone alarmZone : _alarmConfiguration.getAlarmZones().values()) {
            boolean signalizatorFound = false;
            for (Signalizator signalizator : _alarmConfiguration.getSignalizators().values()) {

                if (signalizator.supportsAlarmType(alarmZone.getAlarmType())) {
                    signalizatorFound = true;
                    break;
                }
            }

            if (!signalizatorFound) {
                String validationMessage = String.format(_localizationProvider.getValue("ALARM:NoSignalizatorForAlarmType"),
                        alarmZone.getAlarmType().translate(_localizationProvider));
                if (!validations.contains(validationMessage)) {
                    validations.add(validationMessage);
                }
            }
        }
    }
}
