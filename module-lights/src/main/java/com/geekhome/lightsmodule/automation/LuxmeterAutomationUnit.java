package com.geekhome.lightsmodule.automation;

import com.geekhome.common.automation.DeviceAutomationUnit;
import com.geekhome.common.automation.EvaluationResult;
import com.geekhome.common.hardwaremanager.IInputPort;
import com.geekhome.common.hardwaremanager.IPort;
import com.geekhome.lightsmodule.Luxmeter;

import java.util.Calendar;

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
        return new EvaluationResult(getValue(), interfaceValue, false, isConnected());
    }

    @Override
    public IPort[] getUsedPorts() {
        return new IPort[] { _port };
    }

    @Override
    protected void calculateInternal(Calendar now) throws Exception {
    }
}