package com.geekhome.alarmmodule;

import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.configuration.Persistable;
import com.geekhome.common.State;
import com.geekhome.common.StateType;
import com.geekhome.common.configuration.InactiveState;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.common.configuration.CollectorCollection;

public class MagneticDetector extends SinglePortAlarmSensor {
    private String _disarmingMovementDetectorId;

    @Persistable(name="DisarmingMovementDetectorId")
    public String getDisarmingMovementDetectorId() {
        return _disarmingMovementDetectorId;
    }

    public void setDisarmingMovementDetectorId(String value) {
        _disarmingMovementDetectorId = value;
    }

    public MagneticDetector(DescriptiveName name, String portId, String roomId, InactiveState inactiveState, String delayTime,
                      String disarmingMovementDetectorId, String iconName) {
        super(name, portId, roomId, inactiveState, delayTime, iconName);
        setDisarmingMovementDetectorId(disarmingMovementDetectorId);
        setIconName(iconName);
    }

    @Override
    public String getGroupName(ILocalizationProvider localizationProvider) {
        return localizationProvider.getValue("ALARM:MagneticDetectors");
    }

    @Override
    public CollectorCollection<State> buildStates(ILocalizationProvider localizationProvider) {
        CollectorCollection<State> states = new CollectorCollection<>();
        states.add(new State(new DescriptiveName(localizationProvider.getValue("ALARM:MagneticDetectorState0Open.Disarmed"), "open-disarmed"), StateType.ReadOnly, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("ALARM:MagneticDetectorState0Closed.Disarmed"), "closed-disarmed"), StateType.ReadOnly, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("ALARM:MagneticDetectorState1Open.Watching"),"open-watching"), StateType.ReadOnly, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("ALARM:MagneticDetectorState1Closed.Watching"),"closed-watching"), StateType.ReadOnly, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("ALARM:MagneticDetectorState2Open.Prealarm"),"open-prealarm"), StateType.ReadOnly, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("ALARM:MagneticDetectorState2Closed.Prealarm"),"closed-prealarm"), StateType.ReadOnly, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("ALARM:MagneticDetectorState3Open.Alarm"),"open-alarm"), StateType.ReadOnly, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("ALARM:MagneticDetectorState3Closed.Alarm"),"closed-alarm"), StateType.ReadOnly, false));

        return states;
    }
}