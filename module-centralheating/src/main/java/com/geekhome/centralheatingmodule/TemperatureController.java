package com.geekhome.centralheatingmodule;

import com.geekhome.common.DescriptiveName;
import com.geekhome.common.DeviceCategory;
import com.geekhome.common.Persistable;
import com.geekhome.coremodule.IRoomDevice;
import com.geekhome.coremodule.Multicontroller;
import com.geekhome.http.ILocalizationProvider;

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