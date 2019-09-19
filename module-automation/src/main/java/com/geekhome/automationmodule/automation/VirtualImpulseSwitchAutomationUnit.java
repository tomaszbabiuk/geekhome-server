package com.geekhome.automationmodule.automation;

import com.geekhome.automationmodule.ImpulseSwitch;
import com.geekhome.common.KeyValue;
import com.geekhome.common.json.JSONArrayList;
import com.geekhome.coremodule.Duration;
import com.geekhome.coremodule.automation.*;
import com.geekhome.http.ILocalizationProvider;

import java.util.Calendar;

public class VirtualImpulseSwitchAutomationUnit extends MultistateDeviceAutomationUnit<ImpulseSwitch> implements ICalculableAutomationUnit {
    private ILocalizationProvider _localizationProvider;
    private long _lastPushed;
    private long _impulseTime;

    @Override
    public EvaluationResult buildEvaluationResult() {
        EvaluationResult result = super.buildEvaluationResult();
        if (getStateId().equals("on")) {
            JSONArrayList<KeyValue> descriptions = new JSONArrayList<>();
            long timeLeft = _lastPushed + _impulseTime - Calendar.getInstance().getTimeInMillis();
            descriptions.add(new KeyValue(_localizationProvider.getValue("AUTO:TimeLeft"), formatTimeLeft(timeLeft / 1000)));
            result.setDescriptions(descriptions);
        }
        return result;
    }

    private static String formatTimeLeft(long seconds) {
        long sec = seconds % 60;
        long min = (seconds - sec) / 60;
        if (min > 60) {
            long hours = min / 60;
            return String.format("~ %dh", hours);
        } else if (min >0) {
            return String.format("~ %dm", min);
        } else {
            return String.format("%ds", sec);
        }
    }

    @Override
    protected boolean isSignaled() {
        return getStateId().equals("on");
    }

    public VirtualImpulseSwitchAutomationUnit(ImpulseSwitch device, ILocalizationProvider localizationProvider) throws Exception {
        super(device, localizationProvider);
        _localizationProvider = localizationProvider;
        _impulseTime = Duration.parse(device.getImpulseTime());
        changeStateInternal("off", ControlMode.Manual);
    }

    @Override
    public void changeState(String state, ControlMode controlMode, String code, String actor) throws Exception {
        if (state.equals("on")) {
            _lastPushed = Calendar.getInstance().getTimeInMillis();
        }

        super.changeState(state, controlMode, code, actor);
    }

    @Override
    public void calculate(Calendar now) throws Exception {
        if (getStateId().equals("on") && _lastPushed + _impulseTime < now.getTimeInMillis()) {
            changeStateInternal("off", ControlMode.Manual);
        }
    }
}
