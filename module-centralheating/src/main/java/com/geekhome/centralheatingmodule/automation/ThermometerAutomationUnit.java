package com.geekhome.centralheatingmodule.automation;

import com.geekhome.centralheatingmodule.Thermometer;
import com.geekhome.common.automation.EvaluationResult;
import com.geekhome.common.hardwaremanager.IInputPort;
import com.geekhome.common.hardwaremanager.IPort;

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
        return new EvaluationResult(getValue(), interfaceValue, false, isConnected());
    }

    @Override
    public IPort[] getUsedPorts() {
        return new IPort[] { _port };
    }
}