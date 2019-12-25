package com.geekhome.coremodule;

import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.NamedObject;
import com.geekhome.common.configuration.IDevice;
import com.geekhome.common.configuration.Persistable;
import com.geekhome.httpserver.modules.CollectorCollection;

public class DevicesGroup extends NamedObject {
    private CollectorCollection<IDevice> _devices;

    public DevicesGroup(DescriptiveName name) {
        super(name);
        _devices = new CollectorCollection<>();
    }

    @Persistable(name="Devices")
    public CollectorCollection<IDevice> getDevices() {
        return _devices;
    }

}
