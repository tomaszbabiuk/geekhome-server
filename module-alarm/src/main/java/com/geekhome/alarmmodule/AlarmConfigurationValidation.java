package com.geekhome.alarmmodule;

public class AlarmConfigurationValidation {
    public static boolean canAddPresenceConditions(AlarmConfiguration alarmConfiguration) {
        return isAnyMovementDetectorDefined(alarmConfiguration);
    }

    public static boolean canAddAlarmZones(AlarmConfiguration alarmConfiguration) {
        return (isAnyMagneticDetectorDefined(alarmConfiguration) || isAnyAlarmSensorDefined(alarmConfiguration) ||
                isAnyMovementDetectorDefined(alarmConfiguration) || isAnyGateDefined(alarmConfiguration));
    }

    private static boolean isAnyMovementDetectorDefined(AlarmConfiguration alarmConfiguration) {
        return alarmConfiguration.getMovementDetectors().size() > 0;
    }

    private static boolean isAnyMagneticDetectorDefined(AlarmConfiguration alarmConfiguration) {
        return alarmConfiguration.getMagneticDetectors().size() > 0;
    }

    private static boolean isAnyAlarmSensorDefined(AlarmConfiguration alarmConfiguration) {
        return alarmConfiguration.getAlarmSensors().size() > 0;
    }

    private static boolean isAnyGateDefined(AlarmConfiguration alarmConfiguration) {
        return alarmConfiguration.getGates().size() > 0;
    }
}
