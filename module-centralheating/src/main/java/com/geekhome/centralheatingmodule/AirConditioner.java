package com.geekhome.centralheatingmodule;

import com.geekhome.common.*;
import com.geekhome.common.json.JSONArrayList;
import com.geekhome.coremodule.IBlocksTarget;
import com.geekhome.coremodule.IPortsDrivenDevice;
import com.geekhome.coremodule.IRoomDevice;
import com.geekhome.coremodule.MultistateDevice;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.httpserver.modules.CollectorCollection;

public class AirConditioner extends MultistateDevice implements IRoomDevice, IPortsDrivenDevice, IBlocksTarget {

    private String _powerOnPortId;
    private String _heatingCoolingTogglePortId;
    private String _forceManualPortId;
    private String _temperatureControlPortId;
    private String _roomId;
    private String _temperatureControllerId;

    @Persistable(name="PowerOnPortId")
    public String getPowerOnPortId() {
        return _powerOnPortId;
    }

    public void setPowerOnPortId(String value) {
        _powerOnPortId = value;
    }

    @Persistable(name="HeatingCoolingTogglePortId")
    public String getHeatingCoolingTogglePortId() {
        return _heatingCoolingTogglePortId;
    }

    public void setHeatingCoolingTogglePortId(String value) {
        _heatingCoolingTogglePortId = value;
    }

    @Persistable(name="ForceManualPortId")
    public String getForceManualPortId() {
        return _forceManualPortId;
    }

    public void setForceManualPortId(String value) {
        _forceManualPortId = value;
    }

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

    public AirConditioner(DescriptiveName name, String powerOnPortId, String heatingCoolingTogglePortId,
                          String forceManualPortId, String temperatureControlPortId, String roomId,
                          String temperatureControllerId) {
        super(name, "snow", DeviceCategory.Heating);
        setRoomId(roomId);
        setPowerOnPortId(powerOnPortId);
        setHeatingCoolingTogglePortId(heatingCoolingTogglePortId);
        setForceManualPortId(forceManualPortId);
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
        return getPowerOnPortId() + "," + getHeatingCoolingTogglePortId() + "," + getForceManualPortId();
    }

    @Override
    public JSONArrayList<DescriptiveName> buildBlockCategories(ILocalizationProvider localizationProvider) {
        JSONArrayList<DescriptiveName> categories = new JSONArrayList<>();
        categories.add(new DescriptiveName(localizationProvider.getValue("CH:BlocksEnablingCooling"), "cooling"));
        categories.add(new DescriptiveName(localizationProvider.getValue("CH:BlocksEnablingHeating"), "heating"));
        return categories;
    }

    @Override
    public String getGroupName(ILocalizationProvider localizationProvider) {
        return localizationProvider.getValue("CH:AirConditioners");
    }
}