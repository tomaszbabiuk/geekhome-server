package com.geekhome.lightsmodule;

import com.geekhome.common.*;
import com.geekhome.coremodule.IPortsDrivenDevice;
import com.geekhome.coremodule.IRoomDevice;
import com.geekhome.coremodule.MultistateDevice;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.httpserver.modules.CollectorCollection;

public class SevenColorLamp extends MultistateDevice implements IRoomDevice, IPortsDrivenDevice {
    private String _redPortId;
    private String _greenPortId;
    private String _bluePortId;
    private String _roomId;

    public DeviceCategory getCategory() { return super.getCategory(); }

    @Override
    public String getGroupName(ILocalizationProvider localizationProvider) {
        return localizationProvider.getValue("LIGHTS:RgbLamps");
    }

    @Persistable(name = "RedPortId")
    public String getRedPortId() {
        return _redPortId;
    }

    public void setRedPortId(String value) {
        _redPortId = value;
    }

    @Persistable(name = "GreenPortId")
    public String getGreenPortId() {
        return _greenPortId;
    }

    public void setGreenPortId(String value) {
        _greenPortId = value;
    }

    @Persistable(name = "BluePortId")
    public String getBluePortId() {
        return _bluePortId;
    }

    public void setBluePortId(String value) {
        _bluePortId = value;
    }

    @Persistable(name = "RoomId")
    public String getRoomId() {
        return _roomId;
    }

    public void setRoomId(String value) {
        _roomId = value;
    }

    @Override
    public CollectorCollection<State> buildStates(ILocalizationProvider localizationProvider) {
        CollectorCollection<State> states = new CollectorCollection<>();
        states.add(new State(new DescriptiveName(localizationProvider.getValue("C:Off"), "0off", true), StateType.Control, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("LIGHTS:White"), "1white", true), StateType.Control, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("LIGHTS:Red"),"2red", true), StateType.Control, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("LIGHTS:Green"), "3green", true), StateType.Control, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("LIGHTS:Blue"), "4blue", true), StateType.Control, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("LIGHTS:Cyan"), "5cyan", true), StateType.Control, false)); //green + blue
        states.add(new State(new DescriptiveName(localizationProvider.getValue("LIGHTS:Magenta"), "6magenta", true), StateType.Control, false)); // red + blue
        states.add(new State(new DescriptiveName(localizationProvider.getValue("LIGHTS:Yellow"), "7yellow", true), StateType.Control, false)); // red + green
        states.add(new State(new DescriptiveName(localizationProvider.getValue("C:NA"), "unknown", true), StateType.ReadOnly, false)); // red + green

        return states;
    }

    @Override
    @Persistable(name="HasNonReadonlyStates")
    public boolean hasNonReadonlyStates() {
        return true;
    }

    public SevenColorLamp(DescriptiveName name, String redPortId, String greenPortId, String bluePortId, String roomId) {
        super(name, "rgblamp", DeviceCategory.Lights);
        setRedPortId(redPortId);
        setGreenPortId(greenPortId);
        setBluePortId(bluePortId);
        setRoomId(roomId);
    }

    @Override
    public String getPortsIds() {
        return getRedPortId() + "," + getGreenPortId() + "," + getBluePortId();
    }
}