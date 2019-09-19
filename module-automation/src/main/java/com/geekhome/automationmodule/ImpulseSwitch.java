package com.geekhome.automationmodule;

import com.geekhome.common.ControlType;
import com.geekhome.common.DescriptiveName;
import com.geekhome.common.DeviceCategory;
import com.geekhome.common.Persistable;
import com.geekhome.coremodule.KeySwitch;

public class ImpulseSwitch extends KeySwitch {
    private String _impulseTime;

    @Persistable(name = "ImpulseTime")
    public String getImpulseTime() {
        return _impulseTime;
    }

    public void setImpulseTime(String value) {
        _impulseTime = value;
    }

    public ImpulseSwitch(DescriptiveName name, String port, String roomId, String impulseTime, DeviceCategory deviceCategory) {
        super(name, port, roomId, deviceCategory);
        setImpulseTime(impulseTime);
        setIconName("pulse");
        setRoomId(roomId);
    }

    @Override
    protected ControlType calculateControlType() {
        return (getPortId() != null && getPortId().equals("-")) ? ControlType.Code : ControlType.ReadValue;
    }
}
