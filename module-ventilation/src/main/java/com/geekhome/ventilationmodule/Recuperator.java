package com.geekhome.ventilationmodule;

import com.geekhome.common.*;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.configuration.Persistable;
import com.geekhome.common.configuration.JSONArrayList;
import com.geekhome.coremodule.*;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.httpserver.modules.CollectorCollection;

public class Recuperator extends MultistateDevice implements IRoomDevice, IPortsDrivenDevice, IBlocksTarget {
    private String _automaticControlPortId;
    private String _secondGearPortId;
    private String _thirdGearPortId;
    private String _roomId;

    @Persistable(name = "AutomaticControlPortId")
    public String getAutomaticControlPortId() {
        return _automaticControlPortId;
    }

    public void setAutomaticControlPortId(String value) {
        _automaticControlPortId = value;
    }

    @Persistable(name = "SecondGearPortId")
    public String getSecondGearPortId() {
        return _secondGearPortId;
    }

    public void setSecondGearPortId(String value) {
        _secondGearPortId = value;
    }

    @Persistable(name = "ThirdGearPortId")
    public String getThirdGearPortId() {
        return _thirdGearPortId;
    }

    public void setThirdGearPortId(String value) {
        _thirdGearPortId = value;
    }

    @Persistable(name = "RoomId")
    public String getRoomId() {
        return _roomId;
    }

    public void setRoomId(String value) {
        _roomId = value;
    }

    @Override
    public String getGroupName(ILocalizationProvider localizationProvider) {
        return localizationProvider.getValue("VENT:Recuperators");
    }

    @Override
    public CollectorCollection<State> buildStates(ILocalizationProvider localizationProvider) {
        CollectorCollection<State> states = new CollectorCollection<>();
        states.add(new State(new DescriptiveName(localizationProvider.getValue("VENT:FirstGear"), "1st"), StateType.Control, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("VENT:SecondGear"),"2nd"), StateType.Control, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("VENT:ThirdGear"), "3rd"), StateType.Control, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("VENT:3StepSwitch"), "3stepswitch"), StateType.Control, false));

        return states;
    }

    @Override
    @Persistable(name="HasNonReadonlyStates")
    public boolean hasNonReadonlyStates() {
        return true;
    }

    public Recuperator(DescriptiveName name, String automaticControlPortId, String secondGearPortId, String thirdGearPortId, String roomId) {
        super(name, "weatherizedhome", DeviceCategory.Ventilation);
        setAutomaticControlPortId(automaticControlPortId);
        setSecondGearPortId(secondGearPortId);
        setThirdGearPortId(thirdGearPortId);
        setRoomId(roomId);
    }

    @Override
    public String getPortsIds() {
        return getAutomaticControlPortId() + "," + getSecondGearPortId() + "," + getThirdGearPortId();
    }

    @Override
    public JSONArrayList<DescriptiveName> buildBlockCategories(ILocalizationProvider localizationProvider) {
        JSONArrayList<DescriptiveName> categories = new JSONArrayList<>();
        categories.add(new DescriptiveName(localizationProvider.getValue("VENT:BlocksEnablingSecondGear"), "2nd"));
        categories.add(new DescriptiveName(localizationProvider.getValue("VENT:BlocksEnablingThirdGear"), "3rd"));
        return categories;
    }
}