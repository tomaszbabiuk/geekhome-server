package com.geekhome.alarmmodule.automation;

import com.geekhome.alarmmodule.AlarmZone;
import com.geekhome.common.CodeInvalidException;
import com.geekhome.common.KeyValue;
import com.geekhome.common.configuration.JSONArrayList;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;
import com.geekhome.coremodule.Duration;
import com.geekhome.coremodule.automation.*;
import com.geekhome.common.hardwaremanager.IPort;
import com.geekhome.http.ILocalizationProvider;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmZoneAutomationUnit extends MultistateDeviceAutomationUnit<AlarmZone> {
    private static ILogger Logger = LoggingService.getLogger();
    private ArrayList<CodeLockAutomationUnit> _locksUnits;
    private ArrayList<AlarmSensorAutomationUnit> _alarmSensorsUnits;
    private AlarmZone _alarmZone;
    private long _leavingStartedAtTicks;
    private long _leavingTicks;
    private AlarmZoneState _zoneState;
    private AlarmSensorAutomationUnit _sensorThatCausedTheAlarm;

    @Override
    public EvaluationResult buildEvaluationResult() {
        String interfaceValue = _zoneState.translate(getLocalizationProvider());
        JSONArrayList<KeyValue> descriptions = new JSONArrayList<>();
        if (getStateId().equals("alarm") && _sensorThatCausedTheAlarm != null) {
            descriptions.add(new KeyValue(getLocalizationProvider().getValue("ALARM:BreachedSensor"), _sensorThatCausedTheAlarm.getName().getName()));
        }

        return new EvaluationResult(getValue(), interfaceValue, isSignaled(), isConnected(), descriptions);
    }

    @Override
    public IPort[] getUsedPorts() {
        return new IPort[0];
    }

    @Override
    public boolean isSignaled() {
        return getZoneState() != AlarmZoneState.Disarmed;
    }

    public AlarmZoneState getZoneState() {
        return _zoneState;
    }

    public AlarmZoneAutomationUnit(AlarmZone alarmZone, ILocalizationProvider localizationProvider, MasterAutomation masterAutomation) throws Exception {
        super(alarmZone, localizationProvider);
        _alarmZone = alarmZone;
        _zoneState = AlarmZoneState.Disarmed;
        _locksUnits = new ArrayList<>();
        _alarmSensorsUnits = new ArrayList<>();
        _leavingTicks = Duration.parse(alarmZone.getLeavingTime());

        if (alarmZone.getLocksIds() != null && !alarmZone.getLocksIds().isEmpty()) {
            for (String lockId : alarmZone.getLocksIds().split(",")) {
                CodeLockAutomationUnit lockAutomationUnit = (CodeLockAutomationUnit) masterAutomation.findDeviceUnit(lockId);
                _locksUnits.add(lockAutomationUnit);
            }
        }

        for (String alarmSensorsId : alarmZone.getAlarmSensorsIds().split(",")) {
            AlarmSensorAutomationUnit sensorAutomationUnit =
                    (AlarmSensorAutomationUnit) masterAutomation.findDeviceUnit(alarmSensorsId);
            _alarmSensorsUnits.add(sensorAutomationUnit);
        }

        changeStateInternal("disarmed", ControlMode.Auto);
    }

    private boolean checkIfAnyCodeLockIsInState(boolean isArmed) {
        boolean result = false;
        for (CodeLockAutomationUnit codeLockAutomationUnit : _locksUnits) {
            if (codeLockAutomationUnit.isArmed() == isArmed) {
                result = true;
                break;
            }
        }

        return result;
    }

    private void armCodeLocks() throws Exception {
        for (CodeLockAutomationUnit codeLockAutomationUnit : _locksUnits) {
            codeLockAutomationUnit.arm();
        }
    }

    private void disarmCodeLocks() throws Exception {
        for (CodeLockAutomationUnit codeLockAutomationUnit : _locksUnits) {
            codeLockAutomationUnit.disarm();
        }
    }

    private void armAlarmSensors() throws Exception {
        for (AlarmSensorAutomationUnit alarmSensorAutomationUnit : _alarmSensorsUnits) {
            alarmSensorAutomationUnit.arm();
        }
    }

    private void disarmAlarmSensors() throws Exception {
        for (AlarmSensorAutomationUnit alarmSensorAutomationUnit : _alarmSensorsUnits) {
            alarmSensorAutomationUnit.disarm();
        }
    }

    @Override
    public void calculateInternal(Calendar now) throws Exception {
        boolean isArmedAutomatically = checkIfAnyBlockPasses("arm");
        boolean isDisarmedAutomatically = checkIfAnyBlockPasses("disarm") && !isArmedAutomatically;

        if (_zoneState != AlarmZoneState.Disarmed) {
            if (checkIfAnyCodeLockIsInState(false)) {
                changeZoneState(AlarmZoneState.Disarmed);
                return;
            }
        }

        if (_zoneState == AlarmZoneState.Armed) {
            if (isDisarmedAutomatically) {
                changeZoneState(AlarmZoneState.Disarmed);
                return;
            }

            for (AlarmSensorAutomationUnit alarmSensorAutomationUnit : _alarmSensorsUnits) {
                if (alarmSensorAutomationUnit.isAlarm()) {
                    _sensorThatCausedTheAlarm = alarmSensorAutomationUnit;
                    changeZoneState(AlarmZoneState.Alarm);
                    return;
                }
                if (alarmSensorAutomationUnit.isPrealarm()) {
                    changeZoneState(AlarmZoneState.Prealarm);
                    return;
                }
            }
        }

        if (_zoneState == AlarmZoneState.Disarmed) {
            if (checkIfAnyCodeLockIsInState(true) || isArmedAutomatically) {
                _leavingStartedAtTicks = now.getTimeInMillis();
                changeZoneState(AlarmZoneState.Leaving);
                return;
            }
        }

        if (_zoneState == AlarmZoneState.Leaving) {
            if (now.getTimeInMillis() > _leavingStartedAtTicks + _leavingTicks) {
                changeZoneState(AlarmZoneState.Armed);
                return;
            }
        }

        if (_zoneState == AlarmZoneState.Prealarm) {
            for (AlarmSensorAutomationUnit alarmSensorAutomationUnit : _alarmSensorsUnits)
                if (alarmSensorAutomationUnit.isAlarm()) {
                    _sensorThatCausedTheAlarm = alarmSensorAutomationUnit;
                    changeZoneState(AlarmZoneState.Alarm);
                    return;
                }
        }
    }

    private void changeZoneState(AlarmZoneState newState) throws Exception {
        String newStateString = "?";

        if (newState == AlarmZoneState.Disarmed) {
            changeLeavingStateOfCodeLocks(false);
            newStateString = "DISARMED";
            disarmCodeLocks();
            disarmAlarmSensors();
            changeStateInternal("disarmed", ControlMode.Auto);
        } else if (newState == AlarmZoneState.Armed) {
            changeLeavingStateOfCodeLocks(false);
            newStateString = "ARMED";
            armAlarmSensors();
            armCodeLocks();
            _sensorThatCausedTheAlarm = null;
            changeStateInternal("armed", ControlMode.Auto);
        } else if (newState == AlarmZoneState.Leaving) {
            changeLeavingStateOfCodeLocks(true);
            newStateString = "LEAVING";
            armCodeLocks();
            changeStateInternal("leaving", ControlMode.Auto);
        } else if (newState == AlarmZoneState.Prealarm) {
            changeLeavingStateOfCodeLocks(false);
            newStateString = "PREALARM";
            changeStateInternal("prealarm", ControlMode.Auto);
        } else if (newState == AlarmZoneState.Alarm) {
            changeLeavingStateOfCodeLocks(false);
            newStateString = "ALARM";
            changeStateInternal("alarm", ControlMode.Auto);
        }

        Logger.info("Changing state of alarm zone: " + _alarmZone.getName().getName() + " to " + newStateString);

        _zoneState = newState;
    }

    private void changeLeavingStateOfCodeLocks(boolean leaving) throws Exception {
        for (CodeLockAutomationUnit codeLock : _locksUnits) {
            codeLock.setLeaving(leaving);
        }
    }

    @Override
    public void changeState(String state, ControlMode controlMode, String code, String actor) throws Exception {
        if (controlMode == ControlMode.Manual && (state.equals("armed") || state.equals("disarmed"))) {
            if (isCodeValid(code)) {
                if (state.equals("armed")) {
                    changeZoneState(AlarmZoneState.Armed);
                } else {
                    changeZoneState(AlarmZoneState.Disarmed);
                }
            } else {
                throw new CodeInvalidException(getDevice(), getLocalizationProvider());
            }
        }

        super.changeState(state, controlMode, code, actor);
    }

    private boolean isCodeValid(String code) {
        return code.equals(getDevice().getUnlockingCode());
    }
}