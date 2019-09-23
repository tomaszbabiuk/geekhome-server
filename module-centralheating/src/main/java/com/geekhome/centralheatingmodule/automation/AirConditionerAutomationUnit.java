package com.geekhome.centralheatingmodule.automation;

import com.geekhome.centralheatingmodule.AirConditioner;
import com.geekhome.common.KeyValue;
import com.geekhome.common.json.JSONArrayList;
import com.geekhome.coremodule.Duration;
import com.geekhome.coremodule.Mode;
import com.geekhome.coremodule.automation.*;
import com.geekhome.hardwaremanager.IInputPort;
import com.geekhome.hardwaremanager.IOutputPort;
import com.geekhome.http.ILocalizationProvider;

import java.util.Calendar;

public class AirConditionerAutomationUnit extends MultistateDeviceAutomationUnit<AirConditioner> implements ICalculableAutomationUnit {

    private final TemperatureMulticontrollerAutomationUnit _temperatureController;
    private IOutputPort<Boolean> _heatingModePort;
    private IOutputPort<Boolean> _coolingModePort;
    private IInputPort<Boolean> _forceManualPort;
    private IOutputPort<Integer> _temperatureControlPort;

    public AirConditionerAutomationUnit(IOutputPort<Boolean> heatingModePort, IOutputPort<Boolean> coolingModePort,
                                        IInputPort<Boolean> forceManualPort, IOutputPort<Integer> temperatureControlPort,
                                        String temperatureControllerId, MasterAutomation masterAutomation,
                                        AirConditioner airConditioner, ILocalizationProvider localizationProvider) throws Exception {
        super(airConditioner, localizationProvider);
        _heatingModePort = heatingModePort;
        _coolingModePort = coolingModePort;
        _forceManualPort = forceManualPort;
        _temperatureControlPort = temperatureControlPort;
        _temperatureController = (TemperatureMulticontrollerAutomationUnit)masterAutomation.findDeviceUnit(temperatureControllerId);

        changeStateInternal("off", ControlMode.Auto);
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
            double targetTemperature = _temperatureController.getValue();
            changeOutputPortStateIfNeeded(_temperatureControlPort, (int)targetTemperature);
            _coolingModePort.write(false);
            _heatingModePort.write(true);
        }

        if (getStateId().equals("cooling")) {
            double targetTemperature = _temperatureController.getValue();
            changeOutputPortStateIfNeeded(_temperatureControlPort, (int)targetTemperature);
            _coolingModePort.write(false);
            _heatingModePort.write(true);
        }

        if (getStateId().equals("nodemand")) {
            //TODO: just turn AC off
//            _coolingModePort.write(false);
//            _heatingModePort.write(true);
        }
    }
}
