package com.geekhome.centralheatingmodule;

import com.geekhome.common.ControlType;
import com.geekhome.common.DescriptiveName;
import com.geekhome.common.DeviceCategory;
import com.geekhome.common.Persistable;
import com.geekhome.coremodule.*;
import com.geekhome.http.ILocalizationProvider;

public class Comfortmeter extends Device implements IRoomDevice, IPortsDrivenDevice, IValueDevice {
    private String _temperaturePortId;
    private String _humidityPortId;
    private String _roomId;

    @Persistable(name = "TemperaturePortId")
    public String getTemperaturePortId() {
        return _temperaturePortId;
    }

    public void setTemperaturePortId(String value) {
        _temperaturePortId = value;
    }

    @Persistable(name = "HumidityPortId")
    public String getHumidityPortId() {
        return _humidityPortId;
    }

    public void setHumidityPortId(String value) {
        _humidityPortId = value;
    }

    @Persistable(name = "RoomId")
    public String getRoomId() {
        return _roomId;
    }

    public void setRoomId(String value) {
        _roomId = value;
    }

    public Comfortmeter(DescriptiveName name, String temperaturePortId, String humidityPortId, String roomId, DeviceCategory deviceCategory) {
        super(name, "comfortmeter", deviceCategory);
        setTemperaturePortId(temperaturePortId);
        setHumidityPortId(humidityPortId);
        setRoomId(roomId);
    }

    @Override
    public String getGroupName(ILocalizationProvider localizationProvider) {
        return localizationProvider.getValue("CH:Comfortmeters");
    }

    @Override
    protected ControlType calculateControlType() {
        return ControlType.ReadValue;
    }

    @Override
    public String getPortsIds() {
        return getTemperaturePortId() + "," + getHumidityPortId();
    }

    @Override
    @Persistable(name = "ValueType")
    public ValueType getValueType() {
        return ValueType.Temperature;
    }
}
