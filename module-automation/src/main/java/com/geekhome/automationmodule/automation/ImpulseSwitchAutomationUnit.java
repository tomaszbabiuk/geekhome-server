package com.geekhome.automationmodule.automation;

import com.geekhome.automationmodule.ImpulseSwitch;
import com.geekhome.coremodule.automation.ControlMode;
import com.geekhome.hardwaremanager.IInputPort;
import com.geekhome.hardwaremanager.IPort;
import com.geekhome.http.ILocalizationProvider;

import java.util.Calendar;

public class ImpulseSwitchAutomationUnit extends VirtualImpulseSwitchAutomationUnit {
    private final IInputPort<Boolean> _inputPort;
    private boolean _lastReading;

    public ImpulseSwitchAutomationUnit(IInputPort<Boolean> inputPort, ImpulseSwitch device, ILocalizationProvider localizationProvider) throws Exception {
        super(device, localizationProvider);
        _inputPort = inputPort;
        _lastReading = inputPort.read();
    }

    @Override
    public void changeState(String state, ControlMode controlMode, String code, String actor) throws Exception {
        super.changeState(state, ControlMode.Auto, code, actor);
    }

    @Override
    public void calculateInternal(Calendar now) throws Exception {
        boolean newReading =_inputPort.read();
        if (_lastReading && !newReading) {
            toggleState();
        }
        _lastReading = newReading;
        super.calculate(now);
    }

    private void toggleState() throws Exception {
        if (getStateId().equals("off")) {
            changeStateInternal("on", ControlMode.Auto);
        } else {
            changeStateInternal("off", ControlMode.Auto);
        }
    }

    @Override
    public IPort[] getUsedPorts() {
        return new IPort[] { _inputPort };
    }
}
