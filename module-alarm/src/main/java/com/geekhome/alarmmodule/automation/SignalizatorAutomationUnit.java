package com.geekhome.alarmmodule.automation;

import com.geekhome.alarmmodule.AlarmConfiguration;
import com.geekhome.alarmmodule.AlarmType;
import com.geekhome.alarmmodule.AlarmZone;
import com.geekhome.alarmmodule.Signalizator;
import com.geekhome.coremodule.Duration;
import com.geekhome.coremodule.automation.ControlMode;
import com.geekhome.coremodule.automation.ICalculableAutomationUnit;
import com.geekhome.coremodule.automation.MasterAutomation;
import com.geekhome.coremodule.automation.SwitchableAutomationUnit;
import com.geekhome.common.hardwaremanager.IOutputPort;
import com.geekhome.http.ILocalizationProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class SignalizatorAutomationUnit extends SwitchableAutomationUnit<Signalizator> implements ICalculableAutomationUnit {
    private final long _maximumWorkingTime;
    private final List<AlarmType> _alarmTypes;
    private final ArrayList<AlarmZoneAutomationUnit> _zoneUnits;
    private long _switchedOnMillis;

    private boolean supportsAlarmType(AlarmType alarmType) {
        return _alarmTypes.contains(alarmType);
    }

    public SignalizatorAutomationUnit(IOutputPort<Boolean> outputPort, Signalizator signalizator, ILocalizationProvider localizationProvider,
                                      MasterAutomation masterAutomation, AlarmConfiguration alarmConfiguration) throws Exception {
        super(outputPort, signalizator, localizationProvider);
        _zoneUnits = new ArrayList<>(alarmConfiguration.getAlarmZones().size());
        for (AlarmZone alarmZone : alarmConfiguration.getAlarmZones().values()) {
            AlarmZoneAutomationUnit alarmZoneUnit = (AlarmZoneAutomationUnit) masterAutomation.findDeviceUnit(alarmZone.getName().getUniqueId());
            _zoneUnits.add(alarmZoneUnit);
        }

        _alarmTypes = Arrays.asList(parseAlarmTypes(signalizator.getAlarmTypesIds()));
        _maximumWorkingTime = Duration.parse(signalizator.getMaximumWorkingTime());
    }

    private AlarmType[] parseAlarmTypes(String alarmTypesIds) {
        String[] alarmTypesIdsSplitted = alarmTypesIds.split(",");
        AlarmType[] alarmTypesParsed = new AlarmType[alarmTypesIdsSplitted.length];
        if (alarmTypesIdsSplitted.length > 0) {
            for (int i=0; i<alarmTypesParsed.length; i++) {
                String alarmTypeId = alarmTypesIdsSplitted[i];
                AlarmType alarmType = AlarmType.fromInt(Integer.parseInt(alarmTypeId));
                alarmTypesParsed[i] = alarmType;
            }
        }

        return alarmTypesParsed;
    }


    private boolean alarmPending() {
        for (AlarmZoneAutomationUnit zoneUnit : _zoneUnits) {
            if (supportsAlarmType(AlarmType.Prealarm)) {
                if (zoneUnit.getStateId().equals("prealarm")) {
                    return true;
                }
            }

            if (zoneUnit.getStateId().equals("alarm")) {
                return supportsAlarmType(zoneUnit.getDevice().getAlarmType());
            }
        }

        return false;
    }

    @Override
    public void calculateInternal(Calendar now) throws Exception {
        if (alarmPending()) {
            if (getStateId().equals("off")) {
                changeStateInternal("on", ControlMode.Auto);
            }
        } else {
            changeStateInternal("off", ControlMode.Auto);
        }

        if (getStateId().equals("on")) {
            if (!getOutputPort().read()) {
                _switchedOnMillis = now.getTimeInMillis();
            }

            if (_maximumWorkingTime != 0 && getStateId().equals("on")) {
                if (now.getTimeInMillis() > _switchedOnMillis + _maximumWorkingTime) {
                    changeStateInternal("muted", ControlMode.Auto);
                }
            }
        }

        if (getStateId().equals("on")) {
            changeOutputPortStateIfNeeded(getOutputPort(), true);
        } else {
            changeOutputPortStateIfNeeded(getOutputPort(), false);
        }
    }
}