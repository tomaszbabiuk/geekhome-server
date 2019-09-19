package com.geekhome.coremodule.automation;

import com.geekhome.coremodule.KeySwitch;
import com.geekhome.http.ILocalizationProvider;

public class VirtualKeySwitchAutomationUnit extends MultistateDeviceAutomationUnit<KeySwitch> {
    @Override
    protected boolean isSignaled() {
        return getStateId().equals("on");
    }

    public VirtualKeySwitchAutomationUnit(KeySwitch keySwitch, ILocalizationProvider localizationProvider) throws Exception {
        super(keySwitch, localizationProvider);
        changeStateInternal("off", ControlMode.Manual);
    }
}