package com.geekhome.coremodule;

import com.geekhome.common.*;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.configuration.IDevice;
import com.geekhome.common.configuration.Persistable;

public abstract class Device extends NamedObject implements IDevice {
    private String _iconName;
    private DeviceCategory _deviceCategory;

    @Persistable(name="IconName")
    public String getIconName() {
        return _iconName;
    }

    public void setIconName(String value) {
        _iconName = value;
    }

    @Persistable(name="ControlType")
    public ControlType getControlType() {
        return calculateControlType();
    }

    protected abstract ControlType calculateControlType();

    @Persistable(name="DeviceCategory")
    public DeviceCategory getCategory() { return _deviceCategory; }

    public void setDeviceCategory(DeviceCategory deviceCategory) {
        _deviceCategory = deviceCategory;
    }

    protected Device(DescriptiveName name, String iconName, DeviceCategory deviceCategory) {
        super(name);
        setName(name);
        setIconName(iconName);
        setDeviceCategory(deviceCategory);
    }
}