package com.geekhome.centralheatingmodule.automation;

import com.geekhome.centralheatingmodule.ThermostatCondition;
import com.geekhome.common.EqualityOperator;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;
import com.geekhome.coremodule.automation.EvaluableAutomationUnit;
import com.geekhome.coremodule.automation.MasterAutomation;

import java.util.Calendar;

public class ThermostatConditionAutomationUnit extends EvaluableAutomationUnit {
    private ThermostatCondition _condition;
    private TemperatureMulticontrollerAutomationUnit _temperatureControllerUnit;
    private IThermometerAutomationUnit _thermometerUnit;
    private static ILogger Logger = LoggingService.getLogger();
    private boolean _lastEvaluation;

    public ThermostatConditionAutomationUnit(ThermostatCondition condition, MasterAutomation masterAutomation) throws Exception {
        super(condition.getName());
        _condition = condition;
        _temperatureControllerUnit = (TemperatureMulticontrollerAutomationUnit) masterAutomation.findDeviceUnit(condition.getTemperatureControllerId());
        _thermometerUnit = (IThermometerAutomationUnit) masterAutomation.findDeviceUnit(condition.getThermometerId());
    }

    @Override
    protected boolean doEvaluate(Calendar now) {
        if (_thermometerUnit.getValue() == null) {
            Logger.debug(_thermometerUnit.getName() + "not ready");
            return false;
        }

        if (_temperatureControllerUnit.getValue() == null) {
            _temperatureControllerUnit.calculateInternal(null);
        }

        double hysteresis = _lastEvaluation ? 0.5 : 0;
        boolean result = (_condition.getOperator() == EqualityOperator.GreaterOrEqual)
            ? _thermometerUnit.getValue() >= _temperatureControllerUnit.getValue() - hysteresis
            : _thermometerUnit.getValue() <  _temperatureControllerUnit.getValue() + hysteresis;

        _lastEvaluation = result;

        return result;
    }
}
