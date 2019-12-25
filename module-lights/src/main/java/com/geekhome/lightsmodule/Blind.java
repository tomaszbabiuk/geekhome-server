package com.geekhome.lightsmodule;

import com.geekhome.common.*;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.configuration.IBlocksTarget;
import com.geekhome.common.configuration.Persistable;
import com.geekhome.common.configuration.JSONArrayList;
import com.geekhome.coremodule.*;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.common.configuration.CollectorCollection;

public class Blind  extends MultistateDevice implements IRoomDevice, IPortsDrivenDevice, IBlocksTarget {
    private String _automaticControlPortId;
    private String _motorPortId;
    private String _roomId;
    private String _closingTime;
    private String _comfortSetTime;

    public DeviceCategory getCategory() { return super.getCategory(); }

    @Override
    public String getGroupName(ILocalizationProvider localizationProvider) {
        return localizationProvider.getValue("LIGHTS:Blinds");
    }

    @Persistable(name = "AutomaticControlPortId")
    public String getAutomaticControlPortId() {
        return _automaticControlPortId;
    }

    public void setAutomaticControlPortId(String value) {
        _automaticControlPortId = value;
    }

    @Persistable(name = "MotorPortId")
    public String getMotorPortId() {
        return _motorPortId;
    }

    public void setMotorPortId(String value) {
        _motorPortId = value;
    }

    @Persistable(name = "ClosingTime")
    public String getClosingTime() {
        return _closingTime;
    }

    public void setClosingTime(String closingTime) {
        _closingTime = closingTime;
    }

    @Persistable(name = "ComfortSetTime")
    public String getComfortSetTime() {
        return _comfortSetTime;
    }

    public void setComfortSetTime(String comfortSetTime) {
        _comfortSetTime = comfortSetTime;
    }

    @Persistable(name = "RoomId")
    public String getRoomId() {
        return _roomId;
    }

    public void setRoomId(String value) {
        _roomId = value;
    }

    public Blind(DescriptiveName name, String roomId, String closingTime, String comfortSetTime, String automaticControlPortId, String motorPortId) {
        super(name, "blinds", DeviceCategory.Lights);
        setRoomId(roomId);
        setMotorPortId(motorPortId);
        setAutomaticControlPortId(automaticControlPortId);
        setClosingTime(closingTime);
        setComfortSetTime(comfortSetTime);
    }

    @Override
    public CollectorCollection<State> buildStates(ILocalizationProvider localizationProvider) {
        CollectorCollection<State> states = new CollectorCollection<>();
        states.add(new State(new DescriptiveName(localizationProvider.getValue("LIGHTS:BlindOpen"), "0opening",
                true), StateType.Control, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("LIGHTS:ComfortPosition"), "1comfortposition",
                true), StateType.Control, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("LIGHTS:BlindClosed"), "2closing",
                true), StateType.Control, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("LIGHTS:ManualSwitch"), "3manual",
                true), StateType.Control, false));
        return states;
    }

    @Override
    public JSONArrayList<DescriptiveName> buildBlockCategories(ILocalizationProvider localizationProvider) {
        JSONArrayList<DescriptiveName> categories = new JSONArrayList<>();
        categories.add(new DescriptiveName(localizationProvider.getValue("LIGHTS:BlocksClosingBlind"), "close"));
        categories.add(new DescriptiveName(localizationProvider.getValue("LIGHTS:BlocksOpeningBlind"), "open"));
        categories.add(new DescriptiveName(localizationProvider.getValue("LIGHTS:BlocksEnablingComfortPosition"), "comfort"));
        return categories;
    }

    @Override
    @Persistable(name="HasNonReadonlyStates")
    public boolean hasNonReadonlyStates() {
        return true;
    }

    @Override
    public String getPortsIds() {
        return getAutomaticControlPortId() + "," + getMotorPortId();
    }
}