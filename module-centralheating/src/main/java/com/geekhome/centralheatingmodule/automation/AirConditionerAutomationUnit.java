package com.geekhome.centralheatingmodule.automation;

import com.geekhome.centralheatingmodule.AirConditioner;
import com.geekhome.common.automation.ControlMode;
import com.geekhome.common.automation.MasterAutomation;
import com.geekhome.common.automation.MultistateDeviceAutomationUnit;
import com.geekhome.common.hardwaremanager.IOutputPort;
import com.geekhome.common.hardwaremanager.IPort;
import com.geekhome.http.ILocalizationProvider;

import java.util.Calendar;

public class AirConditionerAutomationUnit extends MultistateDeviceAutomationUnit<AirConditioner> {

    private final TemperatureMulticontrollerAutomationUnit _temperatureController;
    private IOutputPort<Integer> _temperatureControlPort;

    public AirConditionerAutomationUnit(IOutputPort<Integer> temperatureControlPort,
                                        AirConditioner airConditioner, MasterAutomation masterAutomation,
                                        ILocalizationProvider localizationProvider) throws Exception {
        super(airConditioner, localizationProvider);
        _temperatureControlPort = temperatureControlPort;
        _temperatureController = (TemperatureMulticontrollerAutomationUnit)masterAutomation
                .findDeviceUnit(airConditioner.getTemperatureControllerId());

        changeStateInternal("manual", ControlMode.Auto);
    }

    @Override
    protected boolean isSignaled() {
        return getStateId().equals("heating") || getStateId().equals("cooling");
    }

    @Override
    public IPort[] getUsedPorts() {
        return new IPort[] { _temperatureControlPort };
    }

    @Override
    public void calculateInternal(Calendar now) throws Exception {
        if (getControlMode() == ControlMode.Auto) {
            if (checkIfAnyBlockPasses("manual")) {
                changeStateInternal("manual", ControlMode.Auto);
            } else if (checkIfAnyBlockPasses("heating")) {
                changeStateInternal("heating", ControlMode.Auto);
            } else if (checkIfAnyBlockPasses("cooling")) {
                changeStateInternal("cooling", ControlMode.Auto);
            } else {
                changeStateInternal("nodemand", ControlMode.Auto);
            }
        }

        execute();
    }

    private void execute() throws Exception {
        if (getStateId().equals("manual")) {
            changeOutputPortStateIfNeeded(_temperatureControlPort, null);
            return;
        }

        int targetTemperature = (int)(double)_temperatureController.getValue();

        if (getStateId().equals("heating")) {
            changeOutputPortStateIfNeeded(_temperatureControlPort, targetTemperature);
        }

        if (getStateId().equals("cooling")) {
            changeOutputPortStateIfNeeded(_temperatureControlPort, -targetTemperature);
        }

        if (getStateId().equals("nodemand")) {
            changeOutputPortStateIfNeeded(_temperatureControlPort, 0);
        }
    }
}
