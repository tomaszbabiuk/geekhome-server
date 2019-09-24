package com.geekhome.centralheatingmodule.automation;

import com.geekhome.centralheatingmodule.AirConditioner;
import com.geekhome.coremodule.automation.ControlMode;
import com.geekhome.coremodule.automation.ICalculableAutomationUnit;
import com.geekhome.coremodule.automation.MasterAutomation;
import com.geekhome.coremodule.automation.MultistateDeviceAutomationUnit;
import com.geekhome.hardwaremanager.IInputPort;
import com.geekhome.hardwaremanager.IOutputPort;
import com.geekhome.http.ILocalizationProvider;

import java.util.Calendar;

public class AirConditionerAutomationUnit extends MultistateDeviceAutomationUnit<AirConditioner> implements ICalculableAutomationUnit {

    private final TemperatureMulticontrollerAutomationUnit _temperatureController;
    private IOutputPort<Boolean> _powerOnPort;
    private IOutputPort<Boolean> _heatingCoolingTogglePort;
    private IInputPort<Boolean> _forceManualPort;
    private IOutputPort<Integer> _temperatureControlPort;

    public AirConditionerAutomationUnit(IOutputPort<Boolean> powerOnPort, IOutputPort<Boolean> heatingCoolingTogglePort,
                                        IInputPort<Boolean> forceManualPort, IOutputPort<Integer> temperatureControlPort,
                                        AirConditioner airConditioner, MasterAutomation masterAutomation,
                                        ILocalizationProvider localizationProvider) throws Exception {
        super(airConditioner, localizationProvider);
        _powerOnPort = powerOnPort;
        _heatingCoolingTogglePort = heatingCoolingTogglePort;
        _forceManualPort = forceManualPort;
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
        if (getControlMode() == ControlMode.Auto) {
            if (_forceManualPort.read()) {
                changeStateInternal("manual", ControlMode.Auto);
            } else if (checkIfAnyBlockPasses("heating")) {
                changeStateInternal("heating", ControlMode.Auto);
            } else if (checkIfAnyBlockPasses("cooling")) {
                changeStateInternal("cooling", ControlMode.Auto);
            } else {
                changeStateInternal("nodemand", ControlMode.Auto);
            }

            execute();
        }
    }

    private void execute() throws Exception {
        if (getStateId().equals("heating")) {
            _heatingCoolingTogglePort.write(true);
            _powerOnPort.write(true);
        }

        if (getStateId().equals("cooling")) {
            _heatingCoolingTogglePort.write(false);
            _powerOnPort.write(true);
        }

        if (getStateId().equals("nodemand")) {
            _powerOnPort.write(false);
        }

        if (getStateId().equals("heating") || getStateId().equals("cooling")) {
            double targetTemperature = _temperatureController.getValue();
            changeOutputPortStateIfNeeded(_temperatureControlPort, (int)targetTemperature);
        }
    }
}