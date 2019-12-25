package com.geekhome.alarmmodule.automation;

import com.geekhome.alarmmodule.PresenceCondition;
import com.geekhome.common.PresenceType;
import com.geekhome.common.configuration.Duration;
import com.geekhome.coremodule.automation.EvaluableAutomationUnit;
import com.geekhome.coremodule.automation.MasterAutomation;

import java.util.Calendar;

public class PresenceConditionAutomationUnit extends EvaluableAutomationUnit {
    private PresenceCondition _condition;
    private long _durationParsed;
    private long _lastValue;
    private long _presenceTime;
    private long _absenceTime;
    private long _intervalInTicks;
    private Calendar _lastPeakTime;
    private Calendar _lastReadTime;
    private boolean _countAbsenceTime;
    private long _durationTicks;
    private AlarmSensorAutomationUnit[] _movementDetectors;

    public PresenceConditionAutomationUnit(PresenceCondition condition, MasterAutomation masterAutomation) throws Exception {
        super(condition.getName());
        _condition = condition;
        _durationParsed = Duration.parse(_condition.getDuration());
        _durationTicks = _durationParsed;
        _countAbsenceTime = true;
        _presenceTime = 0;
        _absenceTime = 0;
        _lastReadTime = Calendar.getInstance();
        _lastPeakTime = _lastReadTime;
        _intervalInTicks = Duration.parse(_condition.getSensitivity());
        String[] movementDetectorsIds = _condition.getMovementDetectorsIds().split(",");

        _movementDetectors = new AlarmSensorAutomationUnit[movementDetectorsIds.length];
        for (int i = 0; i < _movementDetectors.length; i++) {
            _movementDetectors[i] = (AlarmSensorAutomationUnit) masterAutomation.findDeviceUnit(movementDetectorsIds[i]);
        }
    }

    private long calculateStatesChecksum() {
        long sum = 0;
        for (int i = 0; i < _movementDetectors.length; i++) {
            int iState = _movementDetectors[i].isSignaled() ? 1 : 0;
            sum += iState * 2 ^ i;
        }

        return sum;
    }

    private boolean peakTimeWithinInterval(Calendar impulseTime) {
        return impulseTime.getTimeInMillis() - _lastPeakTime.getTimeInMillis() < _intervalInTicks ||
                (impulseTime.getTimeInMillis() - _lastPeakTime.getTimeInMillis() < _durationTicks) && (_durationTicks < _intervalInTicks);
    }

    @Override
    protected boolean doEvaluate(Calendar now) {
        long currentValue = calculateStatesChecksum();

        if (currentValue != _lastValue) {
            _lastPeakTime = now;
            _countAbsenceTime = !peakTimeWithinInterval(now);
        } else {
            if (!peakTimeWithinInterval(now)) {
                _countAbsenceTime = true;
            }
        }

        long delta = (now.getTimeInMillis() - _lastReadTime.getTimeInMillis());
        if (_countAbsenceTime) {
            _absenceTime += delta;
            _presenceTime = 0;
        } else {
            _presenceTime += delta;
            _absenceTime = 0;
        }

        _lastReadTime = now;
        _lastValue = currentValue;

        long timeTaken = _condition.getPresenceType() == PresenceType.Presence ? _presenceTime : _absenceTime;

        return timeTaken >= _durationParsed;
    }
}
