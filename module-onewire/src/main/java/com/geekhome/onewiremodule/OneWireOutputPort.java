package com.geekhome.onewiremodule;

import com.geekhome.common.PortBase;
import com.geekhome.common.hardwaremanager.IOutputPort;

public class OneWireOutputPort extends PortBase implements IOutputPort<Boolean> {
    private SwitchContainerWrapper _switchContainer;
    private int _channel;

    public OneWireOutputPort(String id, SwitchContainerWrapper switchContainer, int channel) {
        super(id);
        _switchContainer = switchContainer;
        _channel = channel;
    }

    @Override
    public Boolean read() {
        return _switchContainer.read(_channel);
    }

    @Override
    public void write(Boolean value) throws Exception {
        _switchContainer.write(_channel, value);
    }
}
