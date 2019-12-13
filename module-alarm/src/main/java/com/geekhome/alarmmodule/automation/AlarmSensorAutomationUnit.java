package com.geekhome.alarmmodule.automation;

import com.geekhome.alarmmodule.AlarmSensor;
import com.geekhome.coremodule.Duration;
import com.geekhome.coremodule.InactiveState;
import com.geekhome.coremodule.automation.*;
import com.geekhome.hardwaremanager.IInputPort;
import com.geekhome.hardwaremanager.IPort;
import com.geekhome.http.ILocalizationProvider;

import java.util.Calendar;

public class AlarmSensorAutomationUnit<D extends AlarmSensor> extends MultistateDeviceAutomationUnit<D> implements IMultistateDeviceAutomationUnit {
    private IInputPort<Boolean> _inputPort;
    private AlarmSensor _sensor;
    private ILocalizationProvider _localizationProvider;

    protected ILocalizationProvider getLocalizationProvider() {
        return _localizationProvider;
    }

    protected AlarmSensor getSensor() {
        return _sensor;
    }

    private long _armingTicks;
    private long _armingStartedAtTicks;
    private Calendar _lastBreachedTime;
    private boolean _lineBreached;


    public Calendar getLastBreachedTime() {
        return _lastBreachedTime;
    }

    public AlarmSensorAutomationUnit(IInputPort<Boolean> inputPort, D sensor, ILocalizationProvider localizationProvider) throws Exception {
        super(sensor, localizationProvider);
        _lastBreachedTime = Calendar.getInstance();
        _inputPort = inputPort;
        _localizationProvider = localizationProvider;
        _sensor = sensor;
        _armingTicks = Duration.parse(sensor.getDelayTime());
        disarm();
    }

    public boolean isAlarm() {
        return getStateId().endsWith("alarm");
    }

    public boolean isPrealarm() {
        return getStateId().endsWith("prealarm");
    }

    public boolean isWatching() {
        return getStateId().endsWith("watching");
    }

    protected boolean isLineBreached() {
        boolean signal = _inputPort.read();

        return (getSensor().getInactiveState() == InactiveState.NO && signal) ||
                (getSensor().getInactiveState() == InactiveState.NC && !signal);
    }

    public void arm() throws Exception {
        changeStateInternal("watching", ControlMode.Auto);
    }

    public void disarm() throws Exception {
        changeStateInternal("disarmed", ControlMode.Auto);
    }

    public void alarm() throws Exception {
        changeStateInternal("alarm", ControlMode.Auto);
    }

    public void prealarm() throws Exception {
        changeStateInternal("prealarm", ControlMode.Auto);
    }

    @Override
    protected boolean isSignaled() {
        return isLineBreached();
    }

    protected boolean causeForAlarm() {
        return isLineBreached();
    }

    @Override
    public IPort[] getUsedPorts() {
        return new IPort[]{ _inputPort };
    }

    @Override
    public void calculateInternal(Calendar now) throws Exception {
        if (_lineBreached != isLineBreached()) {
            _lastBreachedTime = now;
        }
        _lineBreached = isLineBreached();

        if (isWatching()) {
            if (causeForAlarm()) {
                prealarm();
                _armingStartedAtTicks = now.getTimeInMillis();
            }
        }

        if (isPrealarm() && now.getTimeInMillis() > _armingStartedAtTicks + _armingTicks) {
            alarm();
        }
    }
}
