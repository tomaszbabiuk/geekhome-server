package com.geekhome.coremodule;

import com.geekhome.common.DescriptiveName;
import com.geekhome.common.Persistable;

public class ReadValueCommand extends CommandBase {
    private String _devicesIds;

    @Persistable(name="DevicesIds")
    public String getDevicesIds() {
        return _devicesIds;
    }

    public void setDevicesIds(String value) {
        _devicesIds = value;
    }

    public ReadValueCommand(DescriptiveName name, String devicesIds) {
        super(name);
        setDevicesIds(devicesIds);
    }
}
