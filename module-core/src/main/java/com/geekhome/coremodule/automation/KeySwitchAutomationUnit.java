package com.geekhome.coremodule.automation;

import com.geekhome.coremodule.KeySwitch;
import com.geekhome.hardwaremanager.IInputPort;
import com.geekhome.hardwaremanager.IPort;
import com.geekhome.http.ILocalizationProvider;

import java.util.Calendar;

public class KeySwitchAutomationUnit extends VirtualKeySwitchAutomationUnit {
    private IInputPort<Boolean> _inputPort;

    public KeySwitchAutomationUnit(IInputPort<Boolean> inputPort, KeySwitch device, ILocalizationProvider localizationProvider) throws Exception {
          super(device, localizationProvider);
        _inputPort = inputPort;
        setControlMode(ControlMode.Auto);
    }

    @Override
    protected boolean isSignaled() {
        return _inputPort.read();
    }

    @Override
    public void calculateInternal(Calendar now) throws Exception {
        if (_inputPort.read()) {
            changeStateInternal("on", ControlMode.Auto);
        } else {
            changeStateInternal("off", ControlMode.Auto);
        }
    }

    @Override
    public IPort[] getUsedPorts() {
        return new IPort[] { _inputPort };
    }
}