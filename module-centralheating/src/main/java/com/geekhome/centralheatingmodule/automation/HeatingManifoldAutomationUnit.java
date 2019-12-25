package com.geekhome.centralheatingmodule.automation;

import com.geekhome.centralheatingmodule.HeatingManifold;
import com.geekhome.common.KeyValue;
import com.geekhome.common.configuration.JSONArrayList;
import com.geekhome.common.configuration.Duration;
import com.geekhome.coremodule.automation.*;
import com.geekhome.common.hardwaremanager.IOutputPort;
import com.geekhome.common.hardwaremanager.IPort;
import com.geekhome.http.ILocalizationProvider;

import java.util.ArrayList;
import java.util.Calendar;

public class HeatingManifoldAutomationUnit extends MultistateDeviceAutomationUnit<HeatingManifold> implements ICalculableAutomationUnit {
    private enum Mode {
        Heating("CH:Heating"),
        Regulation("CH:Regulation"),
        StandBy("CH:NoDemand");

        private String _resourceKey;

        Mode(String resourceKey) {
            _resourceKey = resourceKey;
        }

        public String getResourceKey() {
            return _resourceKey;
        }
    }

    private final MinWorkingTimeCounter _minWorkingTimeCounter;
    private ArrayList<HeatingCircuitAutomationUnit> _circuitsUnits;
    private IOutputPort<Boolean> _pumpOrFurnaceOutputPort;
    private IOutputPort<Boolean> _actuatorsTransformerPortId;
    private Mode _mode;

    public HeatingManifoldAutomationUnit(IOutputPort<Boolean> pumpOrFurnaceOutputPort, IOutputPort<Boolean> actuatorsTransformerPortId, HeatingManifold manifold, MasterAutomation masterAutomation, ILocalizationProvider localizationProvider) throws Exception {
        super(manifold, localizationProvider);
        _pumpOrFurnaceOutputPort = pumpOrFurnaceOutputPort;
        _actuatorsTransformerPortId = actuatorsTransformerPortId;
        _circuitsUnits = new ArrayList<>();
        if (!manifold.getCircuitsIds().equals("")) {
            for (String thermalActuatorId : manifold.getCircuitsIds().split(",")) {
                HeatingCircuitAutomationUnit unit = (HeatingCircuitAutomationUnit) masterAutomation.findDeviceUnit(thermalActuatorId);
                _circuitsUnits.add(unit);
            }
        }
        changeStateInternal("off", ControlMode.Auto);
        _mode = Mode.StandBy;
        _minWorkingTimeCounter = _pumpOrFurnaceOutputPort == null ? null : new MinWorkingTimeCounter(_pumpOrFurnaceOutputPort.read(), Duration.parse(getDevice().getMinimumWorkingTime()));
    }

    @Override
    public EvaluationResult buildEvaluationResult() {
        String interfaceValue = getState().getName().getName();
        JSONArrayList<KeyValue> descriptions = new JSONArrayList<>();
        if (getStateId().equals("on")) {
            String state = getLocalizationProvider().getValue(_mode.getResourceKey());
            descriptions.add(new KeyValue(getLocalizationProvider().getValue("C:State"), state));

            for (HeatingCircuitAutomationUnit circuit : _circuitsUnits) {
                if (circuit.getStateId().equals("open")) {
                    String temperatureValue = String.format("%+.2f°C", circuit.getAmbientThermometerUnit().getValue() - circuit.getTemperatureControllerUnit().getValue());
                    descriptions.add(new KeyValue(circuit.getName().getName(), temperatureValue));
                }
            }
        }

        return new EvaluationResult(getStateId(), interfaceValue, isSignaled(), isConnected(), descriptions, getControlMode(), false);
    }

    @Override
    public IPort[] getUsedPorts() {
        return new IPort[] { _pumpOrFurnaceOutputPort, _actuatorsTransformerPortId};
    }

    @Override
    protected boolean isSignaled() {
        return getStateId().equals("on") && _mode != Mode.StandBy;
    }

    @Override
    public void calculateInternal(Calendar now) throws Exception {
        if (getControlMode() == ControlMode.Auto) {
            if (checkIfAnyBlockPasses("on")) {
                changeStateInternal("on", ControlMode.Auto);
            } else {
                changeStateInternal("off", ControlMode.Auto);
            }
        }

        boolean heatingEnabled = getStateId().equals("on");
        boolean isAnyLineActive = false;
        boolean isAnyActiveLineOpened = false;
        boolean isEveryInactiveLineClosed = true;
        boolean actuatorsNeedPower = false;

        for (HeatingCircuitAutomationUnit circuit : _circuitsUnits) {
            int openingLevel = circuit.calculateOpenningLevel();
            if (circuit.isActive()) {
                isAnyLineActive = true;
                if (openingLevel == 100) {
                    isAnyActiveLineOpened = true;
                }
            } else {
                if (openingLevel != 0) {
                    isEveryInactiveLineClosed = false;
                }
            }

            if (circuit.needsPower()) {
                actuatorsNeedPower = true;
            }
        }

        //control actuators (open or close)
        if (isAnyLineActive) {
            for (HeatingCircuitAutomationUnit actuator : _circuitsUnits) {
                if (actuator.getControlMode() == ControlMode.Auto) {
                    if (actuator.isActive()) {
                        if (actuator.calculateOpenningLevel() < 100) {
                            actuator.changeStateInternal("open", ControlMode.Auto);
                        }
                    } else {
                        if (actuator.calculateOpenningLevel() > 0) {
                            actuator.changeStateInternal("closed", ControlMode.Auto);
                        }
                    }
                }
            }
        }

        boolean enablePump = heatingEnabled && isAnyLineActive && isAnyActiveLineOpened && isEveryInactiveLineClosed;
        boolean enableTransformer = heatingEnabled && (isAnyLineActive || actuatorsNeedPower);
        if (enableTransformer && !enablePump) {
            _mode = Mode.Regulation;
        } else if (enablePump) {
            _mode = Mode.Heating;
        } else {
            _mode = Mode.StandBy;
            for (HeatingCircuitAutomationUnit actuator : _circuitsUnits) {
                if (actuator.getControlMode() == ControlMode.Auto) {
                    actuator.changeStateInternal("off", ControlMode.Auto);
                }
            }
        }

        for (HeatingCircuitAutomationUnit circuit : _circuitsUnits) {
            circuit.setCentralHeatingEnabled(heatingEnabled);
        }

        if (_pumpOrFurnaceOutputPort != null) {
            _minWorkingTimeCounter.signal(_pumpOrFurnaceOutputPort.read());
        }

        execute(heatingEnabled);
    }

    private void execute(boolean heatingEnabled) throws Exception {
        if (heatingEnabled) {
            if (_mode == Mode.Regulation) {
                changeOutputPortStateIfNeeded(_actuatorsTransformerPortId, true);
                if (_pumpOrFurnaceOutputPort != null) {
                    changeOutputPortStateIfNeeded(_pumpOrFurnaceOutputPort, false);
                }
            } else if (_mode == Mode.Heating) {
                changeOutputPortStateIfNeeded(_actuatorsTransformerPortId, true);
                if (_pumpOrFurnaceOutputPort != null) {
                    changeOutputPortStateIfNeeded(_pumpOrFurnaceOutputPort, true);
                }
            }
        }

        if (!heatingEnabled || _mode == Mode.StandBy) {
            changeOutputPortStateIfNeeded(_actuatorsTransformerPortId, false);
            if (_pumpOrFurnaceOutputPort != null) {
                if (_minWorkingTimeCounter != null && _minWorkingTimeCounter.isExceeded()) {
                    changeOutputPortStateIfNeeded(_pumpOrFurnaceOutputPort, false);
                }
            }
        }
    }
}
