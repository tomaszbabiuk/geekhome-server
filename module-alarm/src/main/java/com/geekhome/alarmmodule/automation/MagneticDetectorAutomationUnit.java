package com.geekhome.alarmmodule.automation;

import com.geekhome.alarmmodule.AlarmSensor;
import com.geekhome.common.automation.ControlMode;
import com.geekhome.common.hardwaremanager.IInputPort;
import com.geekhome.http.ILocalizationProvider;

import java.util.Calendar;

public class MagneticDetectorAutomationUnit<D extends AlarmSensor> extends AlarmSensorAutomationUnit<D> {
    public MagneticDetectorAutomationUnit(IInputPort<Boolean> inputPort, D sensor, ILocalizationProvider localizationProvider) throws Exception {
        super(inputPort, sensor, localizationProvider);
    }

    @Override
    public boolean isAlarm() {
        return getStateId().endsWith("-alarm");
    }

    @Override
    public boolean isPrealarm() {
        return getStateId().endsWith("-prealarm");
    }

    @Override
    public boolean isWatching() {
        return getStateId().endsWith("-watching");
    }

    @Override
    public void arm() throws Exception {
        if (isLineBreached()) {
            changeStateInternal("open-watching", ControlMode.Auto);
        } else {
            changeStateInternal("closed-watching", ControlMode.Auto);
        }
    }

    @Override
    public void disarm() throws Exception {
        if (isLineBreached()) {
            changeStateInternal("open-disarmed", ControlMode.Auto);
        } else {
            changeStateInternal("closed-disarmed", ControlMode.Auto);
        }
    }

    @Override
    public void alarm() throws Exception {
        if (isLineBreached()) {
            changeStateInternal("open-alarm", ControlMode.Auto);
        } else {
            changeStateInternal("closed-alarm", ControlMode.Auto);
        }
    }

    @Override
    public void prealarm() throws Exception {
        if (isLineBreached()) {
            changeStateInternal("open-prealarm", ControlMode.Auto);
        } else {
            changeStateInternal("closed-prealarm", ControlMode.Auto);
        }
    }

    @Override
    public void calculateInternal(Calendar now) throws Exception {
        super.calculateInternal(now);
        if (isLineBreached()) {
            if (getStateId().equals("closed-disarmed")) {
                changeStateInternal("open-disarmed", ControlMode.Auto);
            } else if (getStateId().equals("closed-watching")) {
                changeStateInternal("open-watching", ControlMode.Auto);
            } else if (getStateId().equals("closed-prealarm")) {
                changeStateInternal("open-prealarm", ControlMode.Auto);
            } else  if (getStateId().equals("closed-alarm")) {
                changeStateInternal("open-alarm", ControlMode.Auto);
            }
        } else {
            if (getStateId().equals("open-disarmed")) {
                changeStateInternal("closed-disarmed", ControlMode.Auto);
            } else if (getStateId().equals("open-watching")) {
                changeStateInternal("closed-watching", ControlMode.Auto);
            } else if (getStateId().equals("open-prealarm")) {
                changeStateInternal("closed-prealarm", ControlMode.Auto);
            } else  if (getStateId().equals("open-alarm")) {
                changeStateInternal("closed-alarm", ControlMode.Auto);
            }
        }
    }
}