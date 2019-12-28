package com.geekhome.lightsmodule;

import com.geekhome.common.*;
import com.geekhome.common.configuration.*;
import com.geekhome.common.localization.ILocalizationProvider;

public class RgbLamp extends MultistateDevice implements IRoomDevice, IPortsDrivenDevice, IBlocksTarget {
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
    public CollectorCollection<State> buildStates(final ILocalizationProvider localizationProvider) {
        CollectorCollection<State> states = new CollectorCollection<>();
        states.add(new State(new DescriptiveName(localizationProvider.getValue("C:Off"), "0off"), StateType.Control, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("C:Preset1"), "1preset1"), StateType.Control, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("C:Preset2"), "2preset2"), StateType.Control, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("C:Preset3"), "3preset3"), StateType.Control, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("C:Preset4"), "4preset4"), StateType.Control, false));

        return states;
    }

    @Override
    public JSONArrayList<DescriptiveName> buildBlockCategories(final ILocalizationProvider localizationProvider) {
        JSONArrayList<DescriptiveName> categories = new JSONArrayList<>();
        categories.add(new DescriptiveName(localizationProvider.getValue("C:BlocksEnablingPreset1"), "preset1"));
        categories.add(new DescriptiveName(localizationProvider.getValue("C:BlocksEnablingPreset2"), "preset2"));
        categories.add(new DescriptiveName(localizationProvider.getValue("C:BlocksEnablingPreset3"), "preset3"));
        categories.add(new DescriptiveName(localizationProvider.getValue("C:BlocksEnablingPreset4"), "preset4"));
        return categories;
    }

    @Override
    @Persistable(name="HasNonReadonlyStates")
    public boolean hasNonReadonlyStates() {
        return true;
    }

    public RgbLamp(DescriptiveName name, String redPortId, String greenPortId, String bluePortId, String roomId) {
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

    @Persistable(name = "RgbPresetsIds")
    public String getPresetsIds() {
        return "1,2,3,4";
    }
}