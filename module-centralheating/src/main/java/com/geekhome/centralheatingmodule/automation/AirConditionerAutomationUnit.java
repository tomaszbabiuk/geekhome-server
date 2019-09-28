package com.geekhome.centralheatingmodule.automation;

import com.geekhome.centralheatingmodule.AirConditioner;
import com.geekhome.common.IConnectable;
import com.geekhome.coremodule.automation.ControlMode;
import com.geekhome.coremodule.automation.ICalculableAutomationUnit;
import com.geekhome.coremodule.automation.MasterAutomation;
import com.geekhome.coremodule.automation.MultistateDeviceAutomationUnit;
import com.geekhome.hardwaremanager.IOutputPort;
import com.geekhome.http.ILocalizationProvider;

import java.util.Calendar;

public class AirConditionerAutomationUnit extends MultistateDeviceAutomationUnit<AirConditioner> implements ICalculableAutomationUnit {

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
    public void calculate(Calendar now) throws Exception {
        if (_temperatureControlPort == null) {
            changeStateInternal("discoveryError", ControlMode.Auto);
            return;
        }

        if (_temperatureControlPort instanceof IConnectable) {
            boolean connected = ((IConnectable) _temperatureControlPort).isConnected();
            if (!connected) {
                changeStateInternal("communicationError", ControlMode.Auto);
                return;
            }
        }

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
