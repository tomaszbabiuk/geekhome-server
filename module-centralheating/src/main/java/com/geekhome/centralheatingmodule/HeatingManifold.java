package com.geekhome.centralheatingmodule;

import com.geekhome.common.*;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.configuration.Persistable;
import com.geekhome.common.configuration.JSONArrayList;
import com.geekhome.common.configuration.IBlocksTarget;
import com.geekhome.coremodule.IPortsDrivenDevice;
import com.geekhome.coremodule.IRoomDevice;
import com.geekhome.coremodule.MultistateDevice;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.common.configuration.CollectorCollection;

public class HeatingManifold extends MultistateDevice implements IRoomDevice, IPortsDrivenDevice, IBlocksTarget {
    private String _minimumWorkingTime;
    private String _circuitsIds;
    private String _pompOrFurnacePortId;
    private String _actuatorsTransformerPortId;
    private String _roomId;

    @Persistable(name="MinimumWorkingTime")
    public String getMinimumWorkingTime() {
        return _minimumWorkingTime;
    }

    public void setMinimumWorkingTime(String value) {
        _minimumWorkingTime = value;
    }

    @Persistable(name="PumpOrFurnacePortId")
    public String getPumpOrFurnacePortId() {
        return _pompOrFurnacePortId;
    }

    public void setPumpOrFurnacePortId(String value) {
        _pompOrFurnacePortId = value;
    }

    @Persistable(name="ActuatorsTransformerPortId")
    public String getActuatorsTransformerPortId() {
        return _actuatorsTransformerPortId;
    }

    public void setActuatorsTransformerPortId(String value) {
        _actuatorsTransformerPortId = value;
    }

    @Persistable(name="RoomId")
    public String getRoomId() {
        return _roomId;
    }

    public void setRoomId(String value) {
        _roomId = value;
    }

    @Persistable(name="CircuitsIds")
    public String getCircuitsIds() {
        return _circuitsIds;
    }

    public void setCircuitsIds(String value) {
        _circuitsIds = value;
    }

    public HeatingManifold(DescriptiveName name, String pumpOrFurnacePortId, String actuatorsTransformerPortId, String roomId, String minimumWorkingTime, String circuitsIds) {
        super(name, "sun", DeviceCategory.Heating);
        setRoomId(roomId);
        setPumpOrFurnacePortId(pumpOrFurnacePortId);
        setActuatorsTransformerPortId(actuatorsTransformerPortId);
        setMinimumWorkingTime(minimumWorkingTime);
        setCircuitsIds(circuitsIds);
    }

    @Override
    public CollectorCollection<State> buildStates(ILocalizationProvider localizationProvider) {
        CollectorCollection<State> states = new CollectorCollection<>();
        states.add(new State(new DescriptiveName(localizationProvider.getValue("C:On"),"on"), StateType.ReadOnly, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("C:Off"),"off"), StateType.ReadOnly, false));

        return states;
    }

    @Override
    @Persistable(name="HasNonReadonlyStates")
    public boolean hasNonReadonlyStates() {
        return false;
    }

    @Override
    public String getPortsIds() {
        String result = getPumpOrFurnacePortId();
        if (!result.equals("") && !getActuatorsTransformerPortId().equals("")) {
            result += "," + getActuatorsTransformerPortId();
        }
        return result;
    }

    @Override
    public JSONArrayList<DescriptiveName> buildBlockCategories(ILocalizationProvider localizationProvider) {
        JSONArrayList<DescriptiveName> categories = new JSONArrayList<>();
        categories.add(new DescriptiveName(localizationProvider.getValue("C:BlocksEnablingAutomaticControl"), "on"));
        return categories;
    }

    @Override
    public String getGroupName(ILocalizationProvider localizationProvider) {
        return localizationProvider.getValue("CH:HeatingManifolds");
    }
}