package com.geekhome.alarmmodule.automation;

import com.geekhome.alarmmodule.MagneticDetector;
import com.geekhome.common.KeyValue;
import com.geekhome.coremodule.automation.EvaluationResult;
import com.geekhome.coremodule.automation.MasterAutomation;
import com.geekhome.common.hardwaremanager.IInputPort;
import com.geekhome.http.ILocalizationProvider;

public class DisarmingMagneticDetectorAutomationUnit extends MagneticDetectorAutomationUnit {
    private boolean _breachedFromTheInside;

    private AlarmSensorAutomationUnit _movementDetectorAutomationUnit;

    @Override
    public EvaluationResult buildEvaluationResult() {
        EvaluationResult result = super.buildEvaluationResult();
        if (getStateId().equals("watching") && isLineBreached()) {
            String breachedFrom = _breachedFromTheInside
                    ? getLocalizationProvider().getValue("ALARM:Inside")
                    : getLocalizationProvider().getValue("ALARM:Outside");
            result.getDescriptions().clear();
            result.getDescriptions().add(new KeyValue(getLocalizationProvider().getValue("ALARM:BreachedFrom"), breachedFrom));
        }
        return result;
    }

    public DisarmingMagneticDetectorAutomationUnit(IInputPort<Boolean> inputPort, MagneticDetector sensor, ILocalizationProvider localizationProvider, MasterAutomation masterAutomation) throws Exception {
        super(inputPort, sensor, localizationProvider);
        _movementDetectorAutomationUnit = (AlarmSensorAutomationUnit) masterAutomation.findDeviceUnit(sensor.getDisarmingMovementDetectorId());
    }

    @Override
    protected boolean causeForAlarm() {
        _breachedFromTheInside = getLastBreachedTime().getTimeInMillis() - _movementDetectorAutomationUnit.getLastBreachedTime().getTimeInMillis() < 1000 * 20;
        return isLineBreached() && !_breachedFromTheInside;
    }
}

