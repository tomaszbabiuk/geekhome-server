package com.geekhome.centralheatingmodule;

import com.geekhome.common.ControlType;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.DeviceCategory;
import com.geekhome.common.configuration.Persistable;
import com.geekhome.common.configuration.Device;
import com.geekhome.common.configuration.IRoomDevice;
import com.geekhome.common.configuration.IValueDevice;
import com.geekhome.common.configuration.ValueType;
import com.geekhome.http.ILocalizationProvider;

public class AveragingThermometer extends Device implements IRoomDevice, IValueDevice {
    private String _thermometersIds;
    private String _roomId;

    @Persistable(name = "ThermometersIds")
    public String getThermometersIds() {
        return _thermometersIds;
    }

    public void setThermometersIds(String value) {
        _thermometersIds = value;
    }

    @Persistable(name = "RoomId")
    public String getRoomId() {
        return _roomId;
    }

    public void setRoomId(String value) {
        _roomId = value;
    }

    public AveragingThermometer(DescriptiveName name, String roomId, String thermometersIds, DeviceCategory deviceCategory) {
        super(name, "averagingthermometer", deviceCategory);
        setRoomId(roomId);
        setThermometersIds(thermometersIds);
    }

    @Override
    public String getGroupName(ILocalizationProvider localizationProvider) {
        return localizationProvider.getValue("CH:AveragingThermometers");
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
