package com.geekhome.centralheatingmodule.automation;

import com.geekhome.centralheatingmodule.Radiator;
import com.geekhome.common.KeyValue;
import com.geekhome.common.configuration.JSONArrayList;
import com.geekhome.common.configuration.Duration;
import com.geekhome.coremodule.InactiveState;
import com.geekhome.coremodule.automation.*;
import com.geekhome.common.hardwaremanager.IOutputPort;
import com.geekhome.common.hardwaremanager.IPort;
import com.geekhome.http.ILocalizationProvider;

import java.util.Calendar;

public abstract class HeatingCircuitAutomationUnit <R extends Radiator> extends MultistateDeviceAutomationUnit<R> implements ICalculableAutomationUnit {
    private final TemperatureMulticontrollerAutomationUnit _temperatureControllerUnit;
    private final IThermometerAutomationUnit _ambientThermometerUnit;
    private boolean _isCentralHeatingEnabled;
    private long _openningLevel;
    private long _maxActivationTicks;
    private long _counter;
    private boolean _isActive;
    private IOutputPort<Boolean> _outputPort;

    @Override
    public IPort[] getUsedPorts() {
        return new IPort[] { _outputPort };
    }

    protected TemperatureMulticontrollerAutomationUnit getTemperatureControllerUnit() {
        return _temperatureControllerUnit;
    }

    protected IThermometerAutomationUnit getAmbientThermometerUnit() {
        return _ambientThermometerUnit;
    }

    public boolean isActive() {
        return _isActive;
    }

    private void setActive(boolean value) {
        _isActive = value;
    }

    public int calculateOpenningLevel() {
            return (int) Math.round((double) _openningLevel / _maxActivationTicks * 100);
    }

    public void setCentralHeatingEnabled(boolean value) {
        _isCentralHeatingEnabled = value;
    }

    @Override
    public EvaluationResult buildEvaluationResult() {
        String interfaceValue = getState().getName().getName();
        JSONArrayList<KeyValue> descriptions = new JSONArrayList<>();
        double ambientTempDelta = getAmbientThermometerUnit().getValue() - getTemperatureControllerUnit().getValue();
        descriptions.add(new KeyValue(getLocalizationProvider().getValue("CH:ValveOpening"), calculateOpenningLevel() + "%"));
        descriptions.add(new KeyValue(getLocalizationProvider().getValue("CH:AmbientTemperature"), String.format("%.2f°C (%+.2f°C)", getAmbientThermometerUnit().getValue(), ambientTempDelta)));
        return new EvaluationResult(getValue(), interfaceValue, isSignaled(), isConnected(), descriptions, getControlMode(), false);
    }


    @Override
    protected boolean isSignaled() {
        return isActive();
    }
    public HeatingCircuitAutomationUnit(IOutputPort<Boolean> outputPort, R circuitDevice, MasterAutomation masterAutomation,
                                        ILocalizationProvider localizationProvider) throws Exception {
        super(circuitDevice, localizationProvider);
        _isCentralHeatingEnabled = false;
        _outputPort = outputPort;
        _maxActivationTicks = Duration.parse(circuitDevice.getActivationTime());
        _openningLevel = (circuitDevice.getInactiveState() == InactiveState.NO) ? _maxActivationTicks : 0;
        _counter = Calendar.getInstance().getTimeInMillis();
        _temperatureControllerUnit = (TemperatureMulticontrollerAutomationUnit)masterAutomation.findDeviceUnit(circuitDevice.getTemperatureControllerId());
        _ambientThermometerUnit = (IThermometerAutomationUnit)masterAutomation.findDeviceUnit(circuitDevice.getAmbientThermometerId());
        changeStateInternal("off", ControlMode.Auto);
    }

    @Override
    public void calculateInternal(Calendar now) throws Exception {
        forceToActiveIfNeeded();
        calculateOpening(now);
        execute();
    }

    public boolean needsPower() {
        boolean ncActuatorNeedsPower = isActive() && getStateId().equals("open") && getDevice().getInactiveState() == InactiveState.NC;
        boolean noActuatorNeedsPower = isActive() && getStateId().equals("closed") && getDevice().getInactiveState() == InactiveState.NO;
        return noActuatorNeedsPower || ncActuatorNeedsPower;
    }

    private void execute() throws Exception {
        if (getStateId().equals("off")) {
            changeOutputPortStateIfNeeded(_outputPort, false);
        }

        if (getStateId().equals("open")) {
            if (calculateOpenningLevel() != 100) {
                if (getDevice().getInactiveState() == InactiveState.NC) {
                    changeOutputPortStateIfNeeded(_outputPort, true);
                } else {
                    changeOutputPortStateIfNeeded(_outputPort, false);
                }
            }
        }

        if (getStateId().equals("closed")) {
            if (getDevice().getInactiveState() == InactiveState.NO) {
                changeOutputPortStateIfNeeded(_outputPort, true);
            } else {
                changeOutputPortStateIfNeeded(_outputPort, false);
            }
        }
    }

    private void calculateOpening(Calendar now) {
        long ticksDelta = (now.getTimeInMillis() - _counter);
        _counter = now.getTimeInMillis();
        if (_outputPort.read()) {
            if (getDevice().getInactiveState() == InactiveState.NO) {
                //slowly close NO actuator when powered
                if (_openningLevel > 0) {
                    _openningLevel -= ticksDelta;
                }
            } else {
                //slowly open NC actuator when powered
                if (_openningLevel < _maxActivationTicks) {
                    _openningLevel += ticksDelta;
                }
            }
        } else {
            if (getDevice().getInactiveState() == InactiveState.NO) {
                //slowly open NO actuator when powered
                if (_openningLevel < _maxActivationTicks) {
                    _openningLevel += ticksDelta;
                }
            } else {
                //slowly close NC actuator when powered
                if (_openningLevel > 0) {
                    _openningLevel -= ticksDelta;
                }
            }
        }

        if (_openningLevel < 0) {
            _openningLevel = 0;
        }

        if (_openningLevel > _maxActivationTicks) {
            _openningLevel = _maxActivationTicks;
        }
    }

    private void forceToActiveIfNeeded() throws Exception {
        boolean forceDisable = checkIfAnyBlockPasses("forceoff");
        if (forceDisable) {
            setActive(false);
        } else {
            boolean forceEnable = checkIfAnyBlockPasses("forceon");
            boolean roomRequiresHeating = _isCentralHeatingEnabled && calculateActivity();
            setActive(forceEnable || roomRequiresHeating);
        }

        if (isActive()) {
            changeStateInternal("open", ControlMode.Auto);
        }
    }

    protected abstract boolean calculateActivity();
}

