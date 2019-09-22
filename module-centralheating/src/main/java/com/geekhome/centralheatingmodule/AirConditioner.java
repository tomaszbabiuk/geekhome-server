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

    private String _heatingModePortId;
    private String _coolingModePortId;
    private String _forceManualPortId;
    private String _powerOutputPortId;
    private String _roomId;
    private String _temperatureControllerId;

    @Persistable(name="HeatingModePortId")
    public String getHeatingModePortId() {
        return _heatingModePortId;
    }

    public void setHeatingModePortId(String value) {
        _heatingModePortId = value;
    }

    @Persistable(name="CoolingModePortId")
    public String getCoolingModePortId() {
        return _coolingModePortId;
    }

    public void setCoolingModePortId(String value) {
        _coolingModePortId = value;
    }

    @Persistable(name="ForceManualPortId")
    public String getForceManualPortId() {
        return _forceManualPortId;
    }

    public void setForceManualPortId(String value) {
        _forceManualPortId = value;
    }

    @Persistable(name="TemperatureOutputPortId")
    public String getPowerOutputPortId() {
        return _powerOutputPortId;
    }

    public void setPowerOutputPortId(String value) {
        _powerOutputPortId = value;
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

    public AirConditioner(DescriptiveName name, String heatingModePortId, String coolingModePortId,
                          String forceManualPortId, String powerOutputPortId, String roomId,
                          String temperatureControllerId) {
        super(name, "snow", DeviceCategory.Heating);
        setRoomId(roomId);
        setHeatingModePortId(heatingModePortId);
        setCoolingModePortId(coolingModePortId);
        setForceManualPortId(forceManualPortId);
        setPowerOutputPortId(powerOutputPortId);
        setTemperatureControllerId(temperatureControllerId);
    }

    @Override
    public CollectorCollection<State> buildStates(ILocalizationProvider localizationProvider) {
        CollectorCollection<State> states = new CollectorCollection<>();
        states.add(new State(new DescriptiveName(localizationProvider.getValue("CH:NoDemand"),"nodemand"), StateType.Control, false));
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
        return getHeatingModePortId() + "," + getCoolingModePortId() + "," + getForceManualPortId();
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