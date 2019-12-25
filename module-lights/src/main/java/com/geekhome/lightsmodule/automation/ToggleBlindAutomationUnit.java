package com.geekhome.lightsmodule.automation;

import com.geekhome.coremodule.automation.ControlMode;
import com.geekhome.coremodule.automation.IDeviceAutomationUnit;
import com.geekhome.coremodule.automation.MultistateDeviceAutomationUnit;
import com.geekhome.common.hardwaremanager.IPort;
import com.geekhome.common.hardwaremanager.ITogglePort;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.lightsmodule.Blind;

import java.util.Calendar;

public class ToggleBlindAutomationUnit extends MultistateDeviceAutomationUnit<Blind> implements IDeviceAutomationUnit<String> {
    private ITogglePort _channelUpPort;
    private ITogglePort _channelDownPort;
    private long _stopAt;
    private String _previousStateId;

    public ToggleBlindAutomationUnit(Blind device, ITogglePort portA, ITogglePort portB,
                                     ILocalizationProvider localizationProvider) throws Exception {
        super(device, localizationProvider);
        changeStateInternal("4manual", ControlMode.Auto);
        _channelUpPort = portA;
        _channelDownPort = portB;
        _stopAt = Long.MAX_VALUE;
    }

    @Override
    public boolean isSignaled() {
        return !getStateId().equals("4manual");
    }

    @Override
    public IPort[] getUsedPorts() {
        return new IPort[] { _channelUpPort, _channelDownPort };
    }

    @Override
    public void calculateInternal(Calendar now) throws Exception {
        if (!getStateId().equals(_previousStateId)) {
            if (getStateId().equals("1slightlyup")) {
                _stopAt = now.getTimeInMillis() + 1000 * 5;
                _channelUpPort.toggleOff();
            } else if (getStateId().equals("2slightlydown")) {
                _stopAt = now.getTimeInMillis() + 1000 * 5;
                _channelDownPort.toggleOff();
            } else if (getStateId().equals("0fullopening")) {
                _stopAt = now.getTimeInMillis() + 1000 * 60;
                _channelUpPort.toggleOn();
            } else if (getStateId().equals("3fullclosing")) {
                _stopAt = now.getTimeInMillis() + 1000 * 60;
                _channelUpPort.toggleOn();
            } else if (getStateId().equals("4manual")) {
                _stopAt = Long.MAX_VALUE;
                _channelDownPort.toggleOn();
            }
        }

        _previousStateId = getStateId();

        if (now.getTimeInMillis() > _stopAt) {
            if (_previousStateId.equals("1slightlyup")) {
                _channelUpPort.toggleOff();
            } else if (_previousStateId.equals("2slightlydown")) {
                _channelDownPort.toggleOff();
            }

            changeStateInternal("4manual", ControlMode.Auto);
        }
    }
}