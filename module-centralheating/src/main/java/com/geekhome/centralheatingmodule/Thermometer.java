package com.geekhome.centralheatingmodule;

import com.geekhome.common.ControlType;
import com.geekhome.common.configuration.*;
import com.geekhome.common.DeviceCategory;
import com.geekhome.common.localization.ILocalizationProvider;

public class Thermometer extends Device implements IRoomDevice, IPortDrivenDevice, IValueDevice {
    private String _portId;
    private String _roomId;

    @Persistable(name = "PortId")
    public String getPortId() {
        return _portId;
    }

    public void setPortId(String value) {
        _portId = value;
    }

    @Persistable(name = "RoomId")
    public String getRoomId() {
        return _roomId;
    }

    public void setRoomId(String value) {
        _roomId = value;
    }

    public Thermometer(DescriptiveName name, String portId, String roomId, DeviceCategory deviceCategory) {
        super(name, "thermometer", deviceCategory);
        setPortId(portId);
        setRoomId(roomId);
    }

    @Override
    public String getGroupName(ILocalizationProvider localizationProvider) {
        return localizationProvider.getValue("CH:Thermometers");
    }

    @Override
    protected ControlType calculateControlType() {
        return ControlType.ReadValue;
    }

    @Override
    @Persistable(name = "ValueType")
    public ValueType getValueType() {
        return ValueType.Temperature;
    }
}
