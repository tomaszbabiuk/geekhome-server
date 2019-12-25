package com.geekhome.alarmmodule;

import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.configuration.Persistable;
import com.geekhome.common.State;
import com.geekhome.common.StateType;
import com.geekhome.coremodule.IPortDrivenDevice;
import com.geekhome.coremodule.InactiveState;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.common.configuration.CollectorCollection;

public class SinglePortAlarmSensor extends AlarmSensor implements IPortDrivenDevice {
    private String _portId;

    @Persistable(name = "PortId")
    public String getPortId() {
        return _portId;
    }

    public void setPortId(String portId) {
        _portId = portId;
    }

    protected SinglePortAlarmSensor(DescriptiveName name, String portId, String roomId, InactiveState inactiveState, String delayTime, String iconName) {
        super(name, roomId, inactiveState, delayTime, iconName);
        setPortId(portId);
    }

    protected SinglePortAlarmSensor(DescriptiveName name, String portId, String roomId, InactiveState inactiveState, String delayTime) {
        this(name, portId, roomId, inactiveState, delayTime, "smokedetector");
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
}
