package com.geekhome.centralheatingmodule.automation;

import com.geekhome.centralheatingmodule.Thermometer;
import com.geekhome.coremodule.automation.EvaluationResult;
import com.geekhome.hardwaremanager.IInputPort;

class ThermometerAutomationUnit extends ThermometerAutomationUnitBase<Thermometer> implements IThermometerAutomationUnit {
    private IInputPort<Double> _port;

    ThermometerAutomationUnit(Thermometer thermometer, IInputPort<Double> port) {
        super(thermometer);
        _port = port;
    }

    @Override
    public Double getValue() {
        return _port.read();
    }

    @Override
    public EvaluationResult buildEvaluationResult() {
        String interfaceValue = String.format("%.2f°C", getValue());
        return new EvaluationResult(getValue(), interfaceValue, false);
    }
}