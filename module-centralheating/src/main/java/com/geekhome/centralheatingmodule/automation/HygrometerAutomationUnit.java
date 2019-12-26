package com.geekhome.centralheatingmodule.automation;
import com.geekhome.centralheatingmodule.Hygrometer;
import com.geekhome.common.automation.DeviceAutomationUnit;
import com.geekhome.common.automation.EvaluationResult;
import com.geekhome.common.hardwaremanager.IInputPort;
import com.geekhome.common.hardwaremanager.IPort;

import java.util.Calendar;

class HygrometerAutomationUnit extends DeviceAutomationUnit<Double, Hygrometer> {
    private IInputPort<Double> _port;

    HygrometerAutomationUnit(Hygrometer hygrometer, IInputPort<Double> port) {
        super(hygrometer);
        _port = port;
    }

    @Override
    public Double getValue() {
        return _port.read();
    }

    @Override
    public EvaluationResult buildEvaluationResult() {
        String interfaceValue = String.format("%s%%", getValue());
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