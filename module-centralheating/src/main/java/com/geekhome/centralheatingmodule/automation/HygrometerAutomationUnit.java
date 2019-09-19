package com.geekhome.centralheatingmodule.automation;
import com.geekhome.centralheatingmodule.Hygrometer;
import com.geekhome.coremodule.automation.DeviceAutomationUnit;
import com.geekhome.coremodule.automation.EvaluationResult;
import com.geekhome.hardwaremanager.IInputPort;

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
        return new EvaluationResult(getValue(), interfaceValue, false);
    }
}