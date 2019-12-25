package com.geekhome.alarmmodule;

import com.geekhome.common.*;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.configuration.IBlocksTarget;
import com.geekhome.common.configuration.Persistable;
import com.geekhome.common.configuration.JSONArrayList;
import com.geekhome.coremodule.*;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.common.configuration.CollectorCollection;

public class AlarmZone extends MultistateDevice implements IMultiroomDevice, IBlocksTarget {
    private String _roomsIds;
    private String _alarmSensorsIds;
    private String _locksIds;
    private AlarmType _alarmType;
    private String _leavingTime;
    private String _unlockingCode;

    @Persistable(name="RoomsIds")
    public String getRoomsIds() {
        return _roomsIds;
    }

    public void setRoomsIds(String value) {
        _roomsIds = value;
    }

    @Persistable(name="AlarmSensorsIds")
    public String getAlarmSensorsIds() {
        return _alarmSensorsIds;
    }

    public void setAlarmSensorsIds(String value) {
        _alarmSensorsIds = value;
    }

    @Persistable(name="LocksIds")
    public String getLocksIds() {
        return _locksIds;
    }

    public void setLocksIds(String value) {
        _locksIds = value;
    }

    @Persistable(name="AlarmType")
    public AlarmType getAlarmType() {
        return _alarmType;
    }

    public void setAlarmType(AlarmType value) {
        _alarmType = value;
    }

    @Persistable(name="LeavingTime")
    public String getLeavingTime() {
        return _leavingTime;
    }

    public void setLeavingTime(String value) {
        _leavingTime = value;
    }

    @Persistable(name="UnlockingCode")
    public String getUnlockingCode() {
        return _unlockingCode;
    }

    public void setUnlockingCode(String value) {
        _unlockingCode = value;
    }

    @Override
    public String getGroupName(ILocalizationProvider localizationProvider) {
        return localizationProvider.getValue("ALARM:AlarmZones");
    }

    public AlarmZone(DescriptiveName name, String roomsIds, String alarmSensorsIds, String locksIds,
                     AlarmType alarmType, String leavingTime, String unlockingCode) {
        super(name, "shield", DeviceCategory.Locks);
        setRoomsIds(roomsIds);
        setAlarmSensorsIds(alarmSensorsIds);
        setLocksIds(locksIds);
        setAlarmType(alarmType);
        setLeavingTime(leavingTime);
        setUnlockingCode(unlockingCode);
    }

    @Override
    protected ControlType calculateControlType() {
        return ControlType.Code;
    }

    @Override
    public CollectorCollection<State> buildStates(ILocalizationProvider localizationProvider) {
        CollectorCollection<State> states = new CollectorCollection<>();
        states.add(new State(new DescriptiveName(localizationProvider.getValue("ALARM:AlarmZoneState0.Disarmed"), "disarmed",
                localizationProvider.getValue("ALARM:Disarm")), StateType.SignaledAction, true));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("ALARM:AlarmZoneState1.Leaving"),"leaving"), StateType.ReadOnly, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("ALARM:AlarmZoneState2.Armed"),"armed",
                localizationProvider.getValue("ALARM:Arm")), StateType.NonSignaledAction, true));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("ALARM:AlarmZoneState3.Prealarm"),"prealarm"), StateType.ReadOnly, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("ALARM:AlarmZoneState4.Alarm"),"alarm"), StateType.ReadOnly, false));

        return states;
    }

    @Override
    @Persistable(name="HasNonReadonlyStates")
    public boolean hasNonReadonlyStates() {
        return true;
    }

    @Override
    public JSONArrayList<DescriptiveName> buildBlockCategories(ILocalizationProvider localizationProvider) {
        JSONArrayList<DescriptiveName> categories = new JSONArrayList<>();
        categories.add(new DescriptiveName(localizationProvider.getValue("ALARM:BlocksArmingAlarmZone"), "arm"));
        categories.add(new DescriptiveName(localizationProvider.getValue("ALARM:BlocksDisarmingAlarmZone"), "disarm"));
        return categories;
    }
}