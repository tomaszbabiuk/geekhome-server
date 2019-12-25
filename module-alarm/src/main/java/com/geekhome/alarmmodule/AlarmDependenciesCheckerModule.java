package com.geekhome.alarmmodule;

import com.geekhome.coremodule.MasterConfiguration;
import com.geekhome.common.configuration.Room;
import com.geekhome.httpserver.modules.DependenciesCheckerModule;
import com.geekhome.httpserver.modules.Dependency;
import com.geekhome.httpserver.modules.DependencyType;

import java.util.ArrayList;

public class AlarmDependenciesCheckerModule extends DependenciesCheckerModule {
    private AlarmConfiguration _alarmConfiguration;

    public AlarmDependenciesCheckerModule(MasterConfiguration masterConfiguration, AlarmConfiguration alarmConfiguration) {
        super(masterConfiguration);
        _alarmConfiguration = alarmConfiguration;
    }

    @Override
    public void checkDependency(Object source, ArrayList<Dependency> dependencies, int level) {
        if (source instanceof CodeLock) {
            level++;
            CodeLock codeLock = (CodeLock)source;
            for (AlarmZone alarmZone : _alarmConfiguration.getAlarmZones().values()) {
                if (alarmZone.getLocksIds() != null && !alarmZone.getLocksIds().equals("")) {
                    for (String lockId : alarmZone.getLocksIds().split(",")) {
                        if (lockId.equals(codeLock.getName().getUniqueId()))
                            addDependency(dependencies, DependencyType.Device, alarmZone, level);
                    }
                }
            }
        }

        if (source instanceof AlarmSensor) {
            level++;
            AlarmSensor alarmSensor = (AlarmSensor)source;
            for (AlarmZone alarmZone : _alarmConfiguration.getAlarmZones().values()) {
                if (alarmZone.getAlarmSensorsIds() != null && !alarmZone.getAlarmSensorsIds().equals("")) {
                    for (String alarmSensorId : alarmZone.getAlarmSensorsIds().split(",")) {
                        if (alarmSensorId.equals(alarmSensor.getName().getUniqueId()))
                        addDependency(dependencies, DependencyType.Device, alarmZone, level);
                    }
                }
            }
        }

        if (source instanceof MovementDetector) {
            level++;
            MovementDetector movementDetector = (MovementDetector) source;
            for (PresenceCondition presenceCondition : _alarmConfiguration.getPresenceConditions().values()) {
                if (presenceCondition.getMovementDetectorsIds() != null && !presenceCondition.getMovementDetectorsIds().equals("")) {
                    for (String movementDetectorId : presenceCondition.getMovementDetectorsIds().split(",")) {
                        if (movementDetectorId.equals(movementDetector.getName().getUniqueId())) {
                            addDependency(dependencies, DependencyType.Condition, presenceCondition, level);
                        }
                    }
                }
            }

            for (MagneticDetector magneticDetector : _alarmConfiguration.getMagneticDetectors().values()) {
                if (magneticDetector.getDisarmingMovementDetectorId().equals(movementDetector.getName().getUniqueId())) {
                    addDependency(dependencies, DependencyType.Device, magneticDetector, level);
                }
            }
        }

        if (source instanceof Room) {
            level++;
            Room room = (Room) source;
            for (AlarmZone alarmZone : _alarmConfiguration.getAlarmZones().values()) {
                for (String roomInZoneId : alarmZone.getRoomsIds().split(",")) {
                    if (roomInZoneId.equals(room.getName().getUniqueId())) {
                        addDependency(dependencies, DependencyType.Zone, alarmZone, level);
                    }
                }
            }
        }
    }
}
