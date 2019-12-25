package com.geekhome.coremodule;

import com.geekhome.common.configuration.IDevice;

public interface MatchDeviceDelegate {
    boolean execute(IDevice device);
}
