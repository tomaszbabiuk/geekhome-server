package com.geekhome.automationmodule;

import com.geekhome.common.*;
import com.geekhome.common.configuration.*;
import com.geekhome.http.ILocalizationProvider;

public class IntensityDevice extends MultistateDevice implements IRoomDevice, IPortsDrivenDevice, IBlocksTarget {

    private String _roomId;
    private String _portId;

    @Persistable(name = "RoomId")
    @Override
    public String getRoomId() {
        return _roomId;
    }

    public void setRoomId(String value) {
        _roomId = value;
    }

    @Persistable(name = "PortId")
    public String getPortId() {
        return _portId;
    }

    public void setPortId(String value) {
        _portId = value;
    }

    @Persistable(name = "IntensityPresetsIds")
    public String getPresetsIds() {
        return "1,2,3,4";
    }

    public IntensityDevice(final DescriptiveName name,
                           final String portId,
                           final String roomId,
                           final String iconName) {
        super(name, iconName, DeviceCategory.Lights);
        _portId = portId;
        _roomId = roomId;
    }

    @Override
    public String getGroupName(ILocalizationProvider localizationProvider) {
        return localizationProvider.getValue("AUTO:IntensityDevices");
    }

    @Override
    public CollectorCollection<State> buildStates(final ILocalizationProvider localizationProvider) {
        CollectorCollection<State> states = new CollectorCollection<>();
        states.add(new State(new DescriptiveName(localizationProvider.getValue("C:Off"), "0off"), StateType.Control, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("C:Preset1"), "1preset1"), StateType.Control, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("C:Preset2"), "2preset2"), StateType.Control, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("C:Preset3"), "3preset3"), StateType.Control, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("C:Preset4"), "4preset4"), StateType.Control, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("C:CustomPreset"), "5custom"), StateType.ReadOnly, false));

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
    public boolean hasNonReadonlyStates() {
        return false;
    }

    @Override
    public String getPortsIds() {
        return getPortId();
    }
}
