package com.geekhome.automationmodule.automation;

import com.geekhome.automationmodule.PowerMeter;
import com.geekhome.coremodule.automation.DeviceAutomationUnit;
import com.geekhome.coremodule.automation.EvaluationResult;
import com.geekhome.hardwaremanager.IInputPort;

class PowerMeterAutomationUnit extends DeviceAutomationUnit<Integer, PowerMeter> {
    private IInputPort<Integer> _port;

    PowerMeterAutomationUnit(PowerMeter powerMeter, IInputPort<Integer> port) {
        super(powerMeter);
        _port = port;
    }

    @Override
    public Integer getValue() {
        return _port.read();
    }

    @Override
    public EvaluationResult buildEvaluationResult() {
        String interfaceValue = String.format("%d W", getValue());
        return new EvaluationResult(getValue(), interfaceValue, false);
    }
}