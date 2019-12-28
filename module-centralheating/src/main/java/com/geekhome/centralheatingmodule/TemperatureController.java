package com.geekhome.centralheatingmodule;

import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.DeviceCategory;
import com.geekhome.common.configuration.Persistable;
import com.geekhome.common.configuration.IRoomDevice;
import com.geekhome.common.configuration.Multicontroller;
import com.geekhome.common.localization.ILocalizationProvider;

public class TemperatureController extends Multicontroller implements IRoomDevice {
    private String _roomId;

    @Persistable(name="RoomId")
    public String getRoomId() {
        return _roomId;
    }

    public void setRoomId(String value) {
        _roomId = value;
    }

    public TemperatureController(DescriptiveName name, String roomId, double minValue, double maxValue, double initialValue,
                                 String modesIds, DeviceCategory deviceCategory) {
        super(name, minValue, maxValue, initialValue, modesIds, "thermostat", deviceCategory);
        setRoomId(roomId);
    }

    @Override
    public String getGroupName(ILocalizationProvider localizationProvider) {
        return localizationProvider.getValue("CH:TemperatureControllers");
    }
}