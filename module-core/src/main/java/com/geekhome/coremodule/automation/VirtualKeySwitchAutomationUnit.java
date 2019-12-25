package com.geekhome.coremodule.automation;

import com.geekhome.coremodule.KeySwitch;
import com.geekhome.common.hardwaremanager.IPort;
import com.geekhome.http.ILocalizationProvider;

import java.util.Calendar;

public class VirtualKeySwitchAutomationUnit extends MultistateDeviceAutomationUnit<KeySwitch> {
    @Override
    protected boolean isSignaled() {
        return getStateId().equals("on");
    }

    public VirtualKeySwitchAutomationUnit(KeySwitch keySwitch, ILocalizationProvider localizationProvider) throws Exception {
        super(keySwitch, localizationProvider);
        changeStateInternal("off", ControlMode.Manual);
    }

    @Override
    public IPort[] getUsedPorts() {
        return new IPort[0];
    }

    @Override
    protected void calculateInternal(Calendar now) throws Exception {
    }
}