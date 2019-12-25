package com.geekhome.centralheatingmodule;

import com.geekhome.common.*;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.configuration.Persistable;
import com.geekhome.common.configuration.JSONArrayList;
import com.geekhome.common.configuration.IBlocksTarget;
import com.geekhome.common.configuration.IPortsDrivenDevice;
import com.geekhome.common.configuration.IRoomDevice;
import com.geekhome.common.configuration.MultistateDevice;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.common.configuration.CollectorCollection;

public class AirConditioner extends MultistateDevice implements IRoomDevice, IPortsDrivenDevice, IBlocksTarget {

    private String _temperatureControlPortId;
    private String _roomId;
    private String _temperatureControllerId;

    @Persistable(name="TemperatureControlPortId")
    public String getTemperatureControlPortId() {
        return _temperatureControlPortId;
    }

    public void setTemperatureControlPortId(String value) {
        _temperatureControlPortId = value;
    }

    @Persistable(name="RoomId")
    public String getRoomId() {
        return _roomId;
    }

    public void setRoomId(String value) {
        _roomId = value;
    }

    @Persistable(name="TemperatureControllerId")
    public String getTemperatureControllerId() {
        return _temperatureControllerId;
    }

    public void setTemperatureControllerId(String value) {
        _temperatureControllerId = value;
    }

    public AirConditioner(DescriptiveName name, String temperatureControlPortId, String roomId,
                          String temperatureControllerId) {
        super(name, "snow", DeviceCategory.Heating);
        setRoomId(roomId);
        setTemperatureControlPortId(temperatureControlPortId);
        setTemperatureControllerId(temperatureControllerId);
    }

    @Override
    public CollectorCollection<State> buildStates(ILocalizationProvider localizationProvider) {
        CollectorCollection<State> states = new CollectorCollection<>();
        states.add(new State(new DescriptiveName(localizationProvider.getValue("CH:NoDemand"),"nodemand"), StateType.ReadOnly, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("CH:Cooling"),"cooling"), StateType.Control, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("CH:Heating"),"heating"), StateType.Control, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("CH:Manual"),"manual"), StateType.Control, false));

        return states;
    }

    @Override
    @Persistable(name="HasNonReadonlyStates")
    public boolean hasNonReadonlyStates() {
        return false;
    }

    @Override
    public String getPortsIds() {
        return getTemperatureControlPortId();
    }

    @Override
    public JSONArrayList<DescriptiveName> buildBlockCategories(ILocalizationProvider localizationProvider) {
        JSONArrayList<DescriptiveName> categories = new JSONArrayList<>();
        categories.add(new DescriptiveName(localizationProvider.getValue("CH:BlocksEnablingCooling"), "cooling"));
        categories.add(new DescriptiveName(localizationProvider.getValue("CH:BlocksEnablingHeating"), "heating"));
        categories.add(new DescriptiveName(localizationProvider.getValue("CH:BlocksEnablingManualMode"), "manual"));
        return categories;
    }

    @Override
    public String getGroupName(ILocalizationProvider localizationProvider) {
        return localizationProvider.getValue("CH:AirConditioners");
    }
}