package com.geekhome.alarmmodule;

import com.geekhome.common.ControlType;
import com.geekhome.common.DescriptiveName;
import com.geekhome.common.DeviceCategory;
import com.geekhome.common.Persistable;
import com.geekhome.coremodule.Device;
import com.geekhome.coremodule.IRoomDevice;
import com.geekhome.http.ILocalizationProvider;

public class Lock extends Device implements IRoomDevice {
    private String _roomId;

    @Persistable(name="RoomId")
    public String getRoomId() {
        return _roomId;
    }

    public void setRoomId(String value) {
        _roomId = value;
    }

    protected Lock(DescriptiveName name, String roomId) {
        super(name, "keyboard", DeviceCategory.Locks);
        setRoomId(roomId);
    }

    @Override
    public String getGroupName(ILocalizationProvider localizationProvider) {
        return localizationProvider.getValue("ALARM:Locks");
    }

    @Override
    protected ControlType calculateControlType() {
        return ControlType.ReadValue;
    }
}
