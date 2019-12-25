package com.geekhome.extafreemodule;

import com.geekhome.common.*;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.configuration.Persistable;
import com.geekhome.common.configuration.IPortsDrivenDevice;
import com.geekhome.common.configuration.IRoomDevice;
import com.geekhome.common.configuration.MultistateDevice;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.common.configuration.CollectorCollection;

public class ExtaFreeBlind extends MultistateDevice implements IRoomDevice, IPortsDrivenDevice {
    private String _roomId;
    private String _upPortId;
    private String _downPortId;

    public DeviceCategory getCategory() { return super.getCategory(); }

    @Override
    public String getGroupName(ILocalizationProvider localizationProvider) {
        return localizationProvider.getValue("EXF:ExtaFreeBlinds");
    }

    @Persistable(name = "UpPortId")
    public String getUpPortId() {
        return _upPortId;
    }

    public void setUpPortId(String value) {
        _upPortId = value;
    }

    @Persistable(name = "DownPortId")
    public String getDownPortId() {
        return _downPortId;
    }

    public void setDownPortId(String value) {
        _downPortId = value;
    }

    @Persistable(name = "RoomId")
    public String getRoomId() {
        return _roomId;
    }

    public void setRoomId(String value) {
        _roomId = value;
    }

    public ExtaFreeBlind(DescriptiveName name, String roomId, String upPortId, String downPortId) {
        super(name, "extafreeblinds", DeviceCategory.Lights);
        setRoomId(roomId);
        setUpPortId(upPortId);
        setDownPortId(downPortId);
    }

    @Override
    public CollectorCollection<State> buildStates(ILocalizationProvider localizationProvider) {
        CollectorCollection<State> states = new CollectorCollection<>();
        states.add(new State(new DescriptiveName(localizationProvider.getValue("EXF:FullOpening"), "0fullopening",
                localizationProvider.getValue("EXF:FullUp")), StateType.Control, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("EXF:SlightlyUp"), "1slightlyup",
                localizationProvider.getValue("EXF:SlightlyUp")), StateType.Control, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("EXF:SlightlyDown"), "2slightlydown",
                localizationProvider.getValue("EXF:SlightlyDown")), StateType.Control, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("EXF:FullClosing"), "3fullclosing",
                localizationProvider.getValue("EXF:FullClosing")), StateType.Control, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("C:NA"), "4unknown",
                localizationProvider.getValue("C:NA")), StateType.ReadOnly, false));
        return states;
    }

    @Override
    @Persistable(name="HasNonReadonlyStates")
    public boolean hasNonReadonlyStates() {
        return true;
    }

    @Override
    public String getPortsIds() {
        return getUpPortId() + "," + getDownPortId();
    }
}