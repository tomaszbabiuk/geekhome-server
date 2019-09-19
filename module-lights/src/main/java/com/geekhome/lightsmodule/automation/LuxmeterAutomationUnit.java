package com.geekhome.lightsmodule.automation;

import com.geekhome.coremodule.automation.DeviceAutomationUnit;
import com.geekhome.coremodule.automation.EvaluationResult;
import com.geekhome.hardwaremanager.IInputPort;
import com.geekhome.lightsmodule.Luxmeter;

class LuxmeterAutomationUnit extends DeviceAutomationUnit<Double, Luxmeter> {
    private IInputPort<Double> _port;

    LuxmeterAutomationUnit(Luxmeter luxmeter, IInputPort<Double> port) {
        super(luxmeter);
        _port = port;
    }

    @Override
    public Double getValue() {
        return _port.read();
    }

    @Override
    public EvaluationResult buildEvaluationResult() {
        String interfaceValue = String.format("%s lux", getValue());
        return new EvaluationResult(getValue(), interfaceValue, false);
    }
}