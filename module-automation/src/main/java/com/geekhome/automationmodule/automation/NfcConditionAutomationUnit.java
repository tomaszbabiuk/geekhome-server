package com.geekhome.automationmodule.automation;

import com.geekhome.automationmodule.NfcCondition;
import com.geekhome.coremodule.Duration;
import com.geekhome.coremodule.automation.EvaluableAutomationUnit;

import java.util.Calendar;

public class NfcConditionAutomationUnit extends EvaluableAutomationUnit {
    private final long _durationMs;
    private boolean _toggling;
    private long _toggledAt;

    public NfcConditionAutomationUnit(NfcCondition condition) {
        super(condition.getName());
        _durationMs = Duration.parse(condition.getDuration());
    }

    @Override
    protected boolean doEvaluate(Calendar now) {
        if (_toggling && _toggledAt + _durationMs < now.getTimeInMillis()) {
            _toggling = false;
        }
        return _toggling;
    }

    public void toggle() {
        _toggling = true;
        _toggledAt = Calendar.getInstance().getTimeInMillis();
    }
}

