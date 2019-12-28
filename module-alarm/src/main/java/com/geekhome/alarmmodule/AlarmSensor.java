package com.geekhome.alarmmodule;

import com.geekhome.common.*;
import com.geekhome.common.configuration.*;
import com.geekhome.common.localization.ILocalizationProvider;

public class AlarmSensor extends MultistateDevice implements IRoomDevice {
    private InactiveState _inactiveState;
    private String _delayTime;
    private String _roomId;

    @Persistable(name = "DelayTime")
    public String getDelayTime() {
        return _delayTime;
    }

    public void setDelayTime(String delayTime) {
        _delayTime = delayTime;
    }

    @Persistable(name = "RoomId")
    public String getRoomId() {
        return _roomId;
    }

    public void setRoomId(String roomId) {
        _roomId = roomId;
    }

    @Persistable(name = "InactiveState")
    public InactiveState getInactiveState() {
        return _inactiveState;
    }

    public void setInactiveState(InactiveState inactiveState) {
        _inactiveState = inactiveState;
    }

    public AlarmSensor(DescriptiveName name, String roomId, InactiveState inactiveState, String delayTime, String iconName) {
        super(name, iconName, DeviceCategory.Monitoring);
        setName(name);
        setRoomId(roomId);
        setInactiveState(inactiveState);
        setDelayTime(delayTime);
    }

    public AlarmSensor(DescriptiveName name, String roomId, InactiveState inactiveState, String delayTime) {
        this(name, roomId, inactiveState, delayTime, "smokedetector");
    }

    @Override
    public CollectorCollection<State> buildStates(ILocalizationProvider localizationProvider) {
        CollectorCollection<State> states = new CollectorCollection<>();
        states.add(new State(new DescriptiveName(localizationProvider.getValue("ALARM:AlarmSensorState0.Disarmed"), "disarmed"), StateType.ReadOnly, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("ALARM:AlarmSensorState1.Watching"),"watching"), StateType.ReadOnly, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("ALARM:AlarmSensorState2.Prealarm"),"prealarm"), StateType.ReadOnly, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("ALARM:AlarmSensorState3.Alarm"),"alarm"), StateType.ReadOnly, false));

        return states;
    }

    @Override
    @Persistable(name="HasNonReadonlyStates")
    public boolean hasNonReadonlyStates() {
        return false;
    }

    @Override
    public String getGroupName(ILocalizationProvider localizationProvider) {
        return localizationProvider.getValue("ALARM:AlarmSensors");
    }
}