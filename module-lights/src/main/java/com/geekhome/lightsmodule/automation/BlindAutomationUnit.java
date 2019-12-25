package com.geekhome.lightsmodule.automation;

import com.geekhome.common.KeyValue;
import com.geekhome.common.configuration.JSONArrayList;
import com.geekhome.coremodule.Duration;
import com.geekhome.coremodule.automation.*;
import com.geekhome.common.hardwaremanager.IOutputPort;
import com.geekhome.common.hardwaremanager.IPort;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.lightsmodule.Blind;

import java.util.Calendar;

public class BlindAutomationUnit extends MultistateDeviceAutomationUnit<Blind> implements IDeviceAutomationUnit<String> {
    private final long _closingTime;
    private final long _comfortSetTime;
    private boolean _positionReset;
    private IOutputPort<Boolean> _automaticControlPort;
    private IOutputPort<Boolean> _motorPort;
    private long _stopAt;
    private String _previousStateId;
    private boolean _comfortPositionDone;

    public BlindAutomationUnit(Blind device, IOutputPort<Boolean> automaticControlPort, IOutputPort<Boolean> motorPort,
                                 ILocalizationProvider localizationProvider) throws Exception {
        super(device, localizationProvider);
        changeStateInternal("3manual", ControlMode.Auto);
        _automaticControlPort = automaticControlPort;
        _motorPort = motorPort;
        _stopAt = Long.MAX_VALUE;
        _closingTime = Duration.parse(device.getClosingTime());
        _comfortSetTime = Duration.parse(device.getComfortSetTime());
        _positionReset = false;
        _comfortPositionDone = false;
    }

    @Override
    public boolean isSignaled() {
        return !getStateId().equals("3manual");
    }

    @Override
    public EvaluationResult buildEvaluationResult() {
        boolean isAlternating = getStateId().equals("1comfortposition") && _comfortPositionDone;
        JSONArrayList<KeyValue> descriptions = new JSONArrayList<>();

        if (getStateId().equals("1comfortposition") && !_comfortPositionDone) {
            String warningKey = getLocalizationProvider().getValue("C:PleaseWait");
            String warningValue = getLocalizationProvider().getValue(_positionReset ? "LIGHTS:PositionReset" : "C:Opening");
            descriptions.add(new KeyValue(warningKey, warningValue));
        }

        return new EvaluationResult(getValue(), getState().getName().getName(), isSignaled(), isConnected(), descriptions, getControlMode(), isAlternating);
    }

    @Override
    public IPort[] getUsedPorts() {
        return new IPort[] { _motorPort, _automaticControlPort };
    }

    @Override
    public void calculateInternal(Calendar now) throws Exception {
        if (getControlMode() == ControlMode.Auto) {
            if (checkIfAnyBlockPasses("close")) {
                if (!getStateId().equals("2closing")) {
                    changeStateInternal("2closing", ControlMode.Auto);
                }
            } else if (checkIfAnyBlockPasses("open")) {
                if (!getStateId().equals("0opening")) {
                    changeStateInternal("0opening", ControlMode.Auto);
                }
            } else if (checkIfAnyBlockPasses("comfort")) {
                if (!getStateId().equals("1comfortposition")) {
                    changeStateInternal("1comfortposition", ControlMode.Auto);
                }
            } else {
                changeStateInternal("3manual", ControlMode.Auto);
            }
        }

        if (getStateId().equals("1comfortposition")) {
            if (now.getTimeInMillis() > _stopAt) {
                if (_positionReset) {
                    _positionReset = false;
                    _comfortPositionDone = false;
                    _stopAt = now.getTimeInMillis() + _comfortSetTime;
                    changeOutputPortStateIfNeeded(_motorPort, false);
                } else {
                    _comfortPositionDone = true;
                    changeOutputPortStateIfNeeded(_automaticControlPort, false);
                    changeOutputPortStateIfNeeded(_motorPort, false);
                }
            }
        }

        if (!getStateId().equals(_previousStateId)) {
            if (getStateId().equals("0opening")) {
                changeOutputPortStateIfNeeded(_automaticControlPort, true);
                changeOutputPortStateIfNeeded(_motorPort, false);
            } else if (getStateId().equals("1comfortposition")) {
                _stopAt = now.getTimeInMillis() + _closingTime;
                _positionReset = true;
                _comfortPositionDone = false;
                changeOutputPortStateIfNeeded(_automaticControlPort, true);
                changeOutputPortStateIfNeeded(_motorPort, true);
            } else if (getStateId().equals("2closing")) {
                changeOutputPortStateIfNeeded(_automaticControlPort, true);
                changeOutputPortStateIfNeeded(_motorPort, true);
            } else if (getStateId().equals("3manual")) {
                changeOutputPortStateIfNeeded(_automaticControlPort, false);
                changeOutputPortStateIfNeeded(_motorPort, false);
            }
        }

        _previousStateId = getStateId();
    }
}