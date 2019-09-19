package com.geekhome.coremodule.automation;

import com.geekhome.common.EqualityOperator;
import com.geekhome.coremodule.ValueCondition;

import java.util.Calendar;

class ValueConditionAutomationUnit extends EvaluableAutomationUnit {
    private boolean _previousEvaluation;
    private ValueCondition _condition;

    private DeviceAutomationUnit _deviceUnit;

    ValueConditionAutomationUnit(ValueCondition condition, MasterAutomation masterAutomation) throws Exception {
        super(condition.getName());
        _condition = condition;
        _deviceUnit = (DeviceAutomationUnit) masterAutomation.findDeviceUnit(condition.getDeviceId());
        _previousEvaluation = false;
    }

    @Override
    protected boolean doEvaluate(Calendar now) {
        if (_deviceUnit.getValue() == null) {
            return false;
        }

        double correction = _previousEvaluation ? _condition.getHysteresis() : 0;
        double deviceValue = (double)_deviceUnit.getValue();

        if (_condition.getOperator() == EqualityOperator.GreaterOrEqual) {
            _previousEvaluation = deviceValue >= _condition.getValue() - correction;
        } else {
            _previousEvaluation = deviceValue < _condition.getValue() + correction;
        }

        return _previousEvaluation;
    }
}
