package com.geekhome.automationmodule.automation;

import com.geekhome.automationmodule.PowerMeter;
import com.geekhome.coremodule.automation.DeviceAutomationUnit;
import com.geekhome.coremodule.automation.EvaluationResult;
import com.geekhome.hardwaremanager.IInputPort;

class PowerMeterAutomationUnit extends DeviceAutomationUnit<Double, PowerMeter> {
    private IInputPort<Double> _port;

    PowerMeterAutomationUnit(PowerMeter powerMeter, IInputPort<Double> port) {
        super(powerMeter);
        _port = port;
    }

    @Override
    public Double getValue() {
        return _port.read();
    }

    @Override
    public EvaluationResult buildEvaluationResult() {
        String interfaceValue = String.format("%.2f W", getValue());
        return new EvaluationResult(getValue(), interfaceValue, false);
    }
}