package com.geekhome.coremodule.automation;

import com.geekhome.coremodule.OnOffDeviceBase;
import com.geekhome.hardwaremanager.IOutputPort;
import com.geekhome.hardwaremanager.IPort;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.httpserver.modules.ObjectNotFoundException;

import java.util.Calendar;

public class OnOffDeviceAutomationUnit<D extends OnOffDeviceBase> extends MultistateDeviceAutomationUnit<D> {
    private IOutputPort<Boolean> _outputPort;
    private Calendar _lastSwitchingOnTime = null;

    protected Calendar getLastSwitchingOnTime() {
        return _lastSwitchingOnTime;
    }

    protected void setLastSwitchingOnTime(Calendar value) {
        _lastSwitchingOnTime = value;
    }

    public IOutputPort<Boolean> getPort() {
        return _outputPort;
    }

    public OnOffDeviceAutomationUnit(IOutputPort<Boolean> outputPort, D device, ILocalizationProvider localizationProvider) throws ObjectNotFoundException {
        super(device, localizationProvider);
        _outputPort = outputPort;
        setLastSwitchingOnTime(Calendar.getInstance());
        setState("off");
    }

    public boolean isOn() {
        return _outputPort.read();
    }

    @Override
    public IPort[] getUsedPorts() {
        return new IPort[] { _outputPort };
    }

    @Override
    public void calculateInternal(Calendar now) throws Exception {
        if (getControlMode() == ControlMode.Auto) {
            if (checkIfAnyBlockPasses("on")) {
                if (!getPort().read()) {
                    setLastSwitchingOnTime(Calendar.getInstance());
                }
                changeStateInternal("on", ControlMode.Auto);
            } else {
                changeStateInternal("off", ControlMode.Auto);
            }
        }

        if (getStateId().equals("on")) {
            changeOutputPortStateIfNeeded(getPort(), true);
        } else if (getStateId().equals("off")) {
            changeOutputPortStateIfNeeded(getPort(), false);
        }
    }

    @Override
    public boolean isSignaled() {
        return getStateId().equals("on");
    }
}