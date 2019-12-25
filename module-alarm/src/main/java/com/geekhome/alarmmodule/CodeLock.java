package com.geekhome.alarmmodule;

import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.configuration.Persistable;
import com.geekhome.coremodule.IPortsDrivenDevice;

public class CodeLock extends Lock implements IPortsDrivenDevice {
    private String _armingPortId;
    private String _statusPortId;

    @Persistable(name="ArmingPortId")
    public String getArmingPortId() {
        return _armingPortId;
    }

    public void setArmingPortId(String value) {
        _armingPortId = value;
    }

    @Persistable(name="StatusPortId")
    public String getStatusPortId() {
        return _statusPortId;
    }

    public void setStatusPortId(String value) {
        _statusPortId = value;
    }

    public CodeLock(DescriptiveName name, String roomId, String armingPortId, String statusPortId) {
        super(name, roomId);
        setArmingPortId(armingPortId);
        setStatusPortId(statusPortId);
    }

    @Override
    public String getPortsIds() {
        return getArmingPortId() + "," + getStatusPortId();
    }
}