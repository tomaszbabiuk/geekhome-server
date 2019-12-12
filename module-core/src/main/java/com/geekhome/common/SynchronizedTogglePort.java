package com.geekhome.common;

import com.geekhome.hardwaremanager.ITogglePort;

public class SynchronizedTogglePort extends ConnectiblePortBase implements ITogglePort {
    public SynchronizedTogglePort(String id) {
        super(id);
    }

    @Override
    public void toggleOn() throws Exception {
    }

    @Override
    public void toggleOff() throws Exception {
    }
}
