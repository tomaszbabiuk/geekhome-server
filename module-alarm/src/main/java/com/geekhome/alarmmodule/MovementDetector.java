package com.geekhome.alarmmodule;

import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.configuration.InactiveState;
import com.geekhome.common.localization.ILocalizationProvider;

public class MovementDetector extends SinglePortAlarmSensor {
    public MovementDetector(DescriptiveName name, String portId, String roomId, InactiveState inactiveState, String delayTime) {
        super(name, portId, roomId, inactiveState, delayTime, "running");
    }

    @Override
    public String getGroupName(ILocalizationProvider localizationProvider) {
        return localizationProvider.getValue("ALARM:MovementDetectors");
    }
}
