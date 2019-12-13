package com.geekhome.coremodule.automation;

import com.geekhome.common.json.JSONArrayList;
import com.geekhome.coremodule.OnOffDeviceBase;
import com.geekhome.hardwaremanager.IPort;
import com.geekhome.hardwaremanager.ITogglePort;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.httpserver.modules.ObjectNotFoundException;

import java.util.Calendar;

public class ToggledOnOffDeviceAutomationUnit<D extends OnOffDeviceBase> extends MultistateDeviceAutomationUnit<D> {
    private String _previousStateId;
    private ITogglePort _togglePort;

    public ITogglePort getPort() {
        return _togglePort;
    }

    @Override
    public EvaluationResult buildEvaluationResult() {
        return new EvaluationResult(getValue(), getState().getName().getName(), isSignaled(), isConnected(), new JSONArrayList<>(), getControlMode(), true);
    }

    @Override
    public IPort[] getUsedPorts() {
        return new IPort[] { _togglePort };
    }

    public ToggledOnOffDeviceAutomationUnit(ITogglePort togglePort, D device, ILocalizationProvider localizationProvider) throws ObjectNotFoundException {
        super(device, localizationProvider);
        _togglePort = togglePort;
//        if (device.getControlType() == ControlType.Multistate) {
//            device.setControlType(ControlType.Multitoggle);
//        }
        setState("off");
        _previousStateId = getStateId();
    }

    @Override
    public void changeStateInternal(String state, ControlMode controlMode) throws Exception {
        super.changeStateInternal(state, controlMode);
    }

    @Override
    public void calculateInternal(Calendar now) throws Exception {
        if (getControlMode() == ControlMode.Auto) {
            if (checkIfAnyBlockPasses("on")) {
                changeStateInternal("on", ControlMode.Auto);
            } else {
                changeStateInternal("off", ControlMode.Auto);
            }
        }

        if (getStateId().equals("on") && !_previousStateId.equals("on")) {
            _togglePort.toggleOn();
        } else if (getStateId().equals("off") && !_previousStateId.equals("off")) {
            _togglePort.toggleOff();
        }
        _previousStateId = getStateId();
    }

    @Override
    public boolean isSignaled() {
        return getStateId().equals("on");
    }
}