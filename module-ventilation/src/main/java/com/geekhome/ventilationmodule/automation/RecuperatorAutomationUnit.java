package com.geekhome.ventilationmodule.automation;

import com.geekhome.coremodule.automation.*;
import com.geekhome.common.hardwaremanager.IOutputPort;
import com.geekhome.common.hardwaremanager.IPort;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.ventilationmodule.Recuperator;

import java.util.Calendar;

public class RecuperatorAutomationUnit extends MultistateDeviceAutomationUnit<Recuperator> implements IDeviceAutomationUnit<String> {
    private IOutputPort<Boolean> _automationControlPort;
    private IOutputPort<Boolean> _secondGearPort;
    private IOutputPort<Boolean> _thirdGearPort;

    public RecuperatorAutomationUnit(Recuperator device, IOutputPort<Boolean> automationControlPort, IOutputPort<Boolean> secondGearPort,
                                     IOutputPort<Boolean> thirdGearPort, ILocalizationProvider localizationProvider) throws Exception {
        super(device, localizationProvider);
        changeStateInternal("3stepswitch", ControlMode.Auto);
        _automationControlPort = automationControlPort;
        _secondGearPort = secondGearPort;
        _thirdGearPort = thirdGearPort;
    }

    @Override
    public boolean isSignaled() {
        return getStateId().equals("2nd") || getStateId().equals("3rd");
    }

    @Override
    public IPort[] getUsedPorts() {
        return new IPort[] { _automationControlPort, _secondGearPort, _thirdGearPort };
    }

    @Override
    public void calculateInternal(Calendar now) throws Exception {
        if (getControlMode() == ControlMode.Auto) {
            boolean enableSecondGear = checkIfAnyBlockPasses("2nd");
            boolean enableThirdGear = checkIfAnyBlockPasses("3rd");

            if (enableThirdGear) {
                changeStateInternal("3rd", ControlMode.Auto);
            } else if (enableSecondGear) {
                changeStateInternal("2nd", ControlMode.Auto);
            } else {
                changeStateInternal("1st", ControlMode.Auto);
            }
        }

        switch (getStateId()) {
            case "3stepswitch":
                changeOutputPortStateIfNeeded(_automationControlPort, false);
                changeOutputPortStateIfNeeded(_secondGearPort, false);
                changeOutputPortStateIfNeeded(_thirdGearPort, false);
                break;
            case "1st":
                changeOutputPortStateIfNeeded(_automationControlPort, true);
                changeOutputPortStateIfNeeded(_secondGearPort, false);
                changeOutputPortStateIfNeeded(_thirdGearPort, false);
                break;
            case "2nd":
                changeOutputPortStateIfNeeded(_automationControlPort, true);
                changeOutputPortStateIfNeeded(_secondGearPort, true);
                changeOutputPortStateIfNeeded(_thirdGearPort, false);
                break;
            case "3rd":
                changeOutputPortStateIfNeeded(_automationControlPort, true);
                changeOutputPortStateIfNeeded(_secondGearPort, false);
                changeOutputPortStateIfNeeded(_thirdGearPort, true);
                break;
        }
    }
}