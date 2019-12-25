package com.geekhome.alarmmodule;

import com.geekhome.common.*;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.configuration.Persistable;
import com.geekhome.coremodule.PortRoomMultistateDevice;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.common.configuration.CollectorCollection;

import java.util.Arrays;

public class Signalizator extends PortRoomMultistateDevice {
    private String _minimumWorkingTime;
    private String _maximumWorkingTime;
    private String _alarmTypeIds;

    @Persistable(name = "MinimumWorkingTime")
    public String getMinimumWorkingTime() {
        return _minimumWorkingTime;
    }

    public void setMinimumWorkingTime(String value) {
        _minimumWorkingTime = value;
    }

    @Persistable(name = "MaximumWorkingTime")
    public String getMaximumWorkingTime() {
        return _maximumWorkingTime;
    }

    public void setMaximumWorkingTime(String value) {
        _maximumWorkingTime = value;
    }

    @Persistable(name = "AlarmTypesIds")
    public String getAlarmTypesIds() {
        return _alarmTypeIds;
    }

    public void setAlarmTypesIds(String value) {
        _alarmTypeIds = value;
    }


    public boolean supportsAlarmType(AlarmType alarmType) {
        return Arrays.asList(_alarmTypeIds.split(",")).contains(alarmType.toString());
    }

    public Signalizator(DescriptiveName name, String portId, String roomId, String alarmTypesIds, String minimumWorkingTime,
                        String maximumWorkingTime) {
        super(name, "bullhorn", DeviceCategory.Monitoring, portId, roomId);
        setAlarmTypesIds(alarmTypesIds);
        setMinimumWorkingTime(minimumWorkingTime);
        setMaximumWorkingTime(maximumWorkingTime);
    }

    @Override
    public CollectorCollection<State> buildStates(ILocalizationProvider localizationProvider) {
        CollectorCollection<State> states = new CollectorCollection<>();
        states.add(new State(new DescriptiveName(localizationProvider.getValue("C:On"), "on"), StateType.ReadOnly, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("C:Off"), "off"), StateType.ReadOnly, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("ALARM:Muted"), "muted"), StateType.ReadOnly, false));
        return states;
    }

    @Override
    @Persistable(name="HasNonReadonlyStates")
    public boolean hasNonReadonlyStates() {
        return false;
    }

    @Override
    public String getGroupName(ILocalizationProvider localizationProvider) {
        return localizationProvider.getValue("ALARM:Signalizators");
    }
}
