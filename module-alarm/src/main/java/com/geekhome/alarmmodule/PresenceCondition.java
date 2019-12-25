package com.geekhome.alarmmodule;

import com.geekhome.common.*;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.configuration.Persistable;
import com.geekhome.coremodule.ConditionBase;

public class PresenceCondition extends ConditionBase {
    private String movementDetectorsIds;
    private PresenceType _presenceType;
    private String _duration;
    private String _sensitivity;

    @Persistable(name="MovementDetectorsIds")
    public String getMovementDetectorsIds() {
        return movementDetectorsIds;
    }

    public void setMovementDetectorsIds(String value) {
        movementDetectorsIds = value;
    }

    @Persistable(name="PresenceType")
    public PresenceType getPresenceType() {
        return _presenceType;
    }

    public void setPresenceType(PresenceType value) {
        _presenceType = value;
    }

    @Persistable(name="Duration")
    public String getDuration() {
        return _duration;
    }

    public void setDuration(String value) {
        _duration = value;
    }

    @Persistable(name="Sensitivity")
    public String getSensitivity() {
        return _sensitivity;
    }

    public void setSensitivity(String value) {
        _sensitivity = value;
    }

    @Override
    public String[] getDevicesIds() {
        return getMovementDetectorsIds().split(",");
    }

    public PresenceCondition(DescriptiveName name, PresenceType presenceType,
                             String movementDetectorsIds, String duration, String sensitivity) {
        super(name);
        setPresenceType(presenceType);
        setMovementDetectorsIds(movementDetectorsIds);
        setDuration(duration);
        setSensitivity(sensitivity);
    }
}