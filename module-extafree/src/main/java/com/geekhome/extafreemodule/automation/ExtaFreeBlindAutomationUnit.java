package com.geekhome.extafreemodule.automation;

import com.geekhome.coremodule.automation.ControlMode;
import com.geekhome.coremodule.automation.ICalculableAutomationUnit;
import com.geekhome.coremodule.automation.IDeviceAutomationUnit;
import com.geekhome.coremodule.automation.MultistateDeviceAutomationUnit;
import com.geekhome.extafreemodule.ExtaFreeBlind;
import com.geekhome.hardwaremanager.ITogglePort;
import com.geekhome.http.ILocalizationProvider;

import java.util.Calendar;

public class ExtaFreeBlindAutomationUnit extends MultistateDeviceAutomationUnit<ExtaFreeBlind> implements ICalculableAutomationUnit, IDeviceAutomationUnit<String> {
    private final ITogglePort _upPort;
    private final ITogglePort _downPort;
    private String _previousStateId;
    private long _stopAt;

    public ExtaFreeBlindAutomationUnit(ExtaFreeBlind device, ITogglePort upPort, ITogglePort downPort,
                                       ILocalizationProvider localizationProvider) throws Exception {
        super(device, localizationProvider);
        _upPort = upPort;
        _downPort = downPort;
        _stopAt = Long.MAX_VALUE;
        changeStateInternal("4unknown", ControlMode.Auto);
    }

    @Override
    public boolean isSignaled() {
        return false;
    }

    @Override
    public void calculate(Calendar now) throws Exception {
        if (!getStateId().equals(_previousStateId)) {
            if (getStateId().equals("1slightlyup")) {
                _stopAt = now.getTimeInMillis() + 1000 * 5;
                simulatePressUp();
            } else if (getStateId().equals("2slightlydown")) {
                _stopAt = now.getTimeInMillis() + 1000 * 5;
                simulatePressDown();
            } else if (getStateId().equals("0fullopening")) {
                _stopAt = now.getTimeInMillis() + 1000 * 60;
                simulatePressUp();
            } else if (getStateId().equals("3fullclosing")) {
                _stopAt = now.getTimeInMillis() + 1000 * 60;
                simulatePressDown();
            } else if (getStateId().equals("4unknown")) {
                _stopAt = Long.MAX_VALUE;
            }
        }

        _previousStateId = getStateId();

        if (now.getTimeInMillis() > _stopAt) {
            if (getStateId().equals("1slightlyup") || getStateId().equals("0fullopening")) {
                simulatePressUp();
            }

            if (getStateId().equals("2slightlydown") || getStateId().equals("3fullclosing")) {
                simulatePressDown();
            }
            changeStateInternal("4unknown", ControlMode.Auto);
        }
    }

    private void simulatePressUp() throws Exception {
        _upPort.toggleOn();
        _upPort.toggleOff();
    }

    private void simulatePressDown() throws Exception {
        _downPort.toggleOn();
        Thread.sleep(100);
        _downPort.toggleOff();
    }
}