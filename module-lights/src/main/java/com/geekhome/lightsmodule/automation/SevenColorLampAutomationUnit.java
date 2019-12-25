package com.geekhome.lightsmodule.automation;

import com.geekhome.coremodule.automation.*;
import com.geekhome.common.hardwaremanager.IOutputPort;
import com.geekhome.common.hardwaremanager.IPort;
import com.geekhome.common.hardwaremanager.TogglePortWrapper;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.lightsmodule.SevenColorLamp;

import java.util.Calendar;

public class SevenColorLampAutomationUnit extends MultistateDeviceAutomationUnit<SevenColorLamp> implements IDeviceAutomationUnit<String> {
    private final boolean _hasTogglePorts;
    private IOutputPort<Boolean> _redPort;
    private IOutputPort<Boolean> _greenPort;
    private IOutputPort<Boolean> _bluePort;
    private String _lastState;

    public SevenColorLampAutomationUnit(SevenColorLamp device, IOutputPort<Boolean> redPort, IOutputPort<Boolean> greenPort,
                                        IOutputPort<Boolean> bluePort, ILocalizationProvider localizationProvider) throws Exception {
        super(device, localizationProvider);
        changeStateInternal("0off", ControlMode.Auto);
        _redPort = redPort;
        _greenPort = greenPort;
        _bluePort = bluePort;
        _lastState = "0off";
        _hasTogglePorts = redPort instanceof TogglePortWrapper || greenPort instanceof TogglePortWrapper || bluePort instanceof TogglePortWrapper;
    }

    @Override
    public EvaluationResult buildEvaluationResult() {
        return new EvaluationResult(getValue(), getState().getName().getName(), isSignaled(), isConnected(), null, getControlMode(), _hasTogglePorts);
    }

    @Override
    public IPort[] getUsedPorts() {
        return new IPort[] { _redPort, _greenPort, _bluePort };
    }

    @Override
    public boolean isSignaled() {
        return !getStateId().equals("0off") && !getStateId().equals("unknown");
    }

    @Override
    public void calculateInternal(Calendar now) throws Exception {
        boolean invalidate = !getStateId().equals(_lastState);

        if (getControlMode() == ControlMode.Auto) {
            if (checkIfAnyBlockPasses("white")) {
                changeStateInternal("1white", ControlMode.Auto);
            } else if (checkIfAnyBlockPasses("red")) {
                changeStateInternal("2red", ControlMode.Auto);
            } else if (checkIfAnyBlockPasses("green")) {
                changeStateInternal("3green", ControlMode.Auto);
            } else if (checkIfAnyBlockPasses("blue")) {
                changeStateInternal("4blue", ControlMode.Auto);
            } else if (checkIfAnyBlockPasses("cyan")) {
                changeStateInternal("5cyan", ControlMode.Auto);
            } else if (checkIfAnyBlockPasses("magenta")) {
                changeStateInternal("6magenta", ControlMode.Auto);
            } else if (checkIfAnyBlockPasses("yellow")) {
                changeStateInternal("7yellow", ControlMode.Auto);
            } else {
                changeStateInternal("0off", ControlMode.Auto);
            }
        }

        switch (getStateId()) {
            case "1white":
                changeOutputPortStateIfNeeded(_redPort, true, invalidate);
                changeOutputPortStateIfNeeded(_greenPort, true, invalidate);
                changeOutputPortStateIfNeeded(_bluePort, true, invalidate);
                break;
            case "2red":
                changeOutputPortStateIfNeeded(_redPort, true, invalidate);
                changeOutputPortStateIfNeeded(_greenPort, false, invalidate);
                changeOutputPortStateIfNeeded(_bluePort, false, invalidate);
                break;
            case "3green":
                changeOutputPortStateIfNeeded(_redPort, false, invalidate);
                changeOutputPortStateIfNeeded(_greenPort, true, invalidate);
                changeOutputPortStateIfNeeded(_bluePort, false, invalidate);
                break;
            case "4blue":
                changeOutputPortStateIfNeeded(_redPort, false, invalidate);
                changeOutputPortStateIfNeeded(_greenPort, false, invalidate);
                changeOutputPortStateIfNeeded(_bluePort, true, invalidate);
                break;
            case "5cyan":
                changeOutputPortStateIfNeeded(_redPort, false, invalidate);
                changeOutputPortStateIfNeeded(_greenPort, true, invalidate);
                changeOutputPortStateIfNeeded(_bluePort, true, invalidate);
                break;
            case "6magenta":
                changeOutputPortStateIfNeeded(_redPort, true, invalidate);
                changeOutputPortStateIfNeeded(_greenPort, false, invalidate);
                changeOutputPortStateIfNeeded(_bluePort, true, invalidate);
                break;
            case "7yellow":
                changeOutputPortStateIfNeeded(_redPort, true, invalidate);
                changeOutputPortStateIfNeeded(_greenPort, true, invalidate);
                changeOutputPortStateIfNeeded(_bluePort, false, invalidate);
                break;
            case "0off":
                changeOutputPortStateIfNeeded(_redPort, false, invalidate);
                changeOutputPortStateIfNeeded(_greenPort, false, invalidate);
                changeOutputPortStateIfNeeded(_bluePort, false, invalidate);
                break;
        }

        _lastState = getStateId();
    }
}