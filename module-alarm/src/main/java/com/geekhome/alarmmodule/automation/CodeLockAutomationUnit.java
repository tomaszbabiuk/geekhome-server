package com.geekhome.alarmmodule.automation;

import com.geekhome.alarmmodule.CodeLock;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.automation.DeviceAutomationUnit;
import com.geekhome.common.automation.EvaluationResult;
import com.geekhome.common.hardwaremanager.IInputPort;
import com.geekhome.common.hardwaremanager.IOutputPort;
import com.geekhome.common.hardwaremanager.IPort;
import com.geekhome.http.ILocalizationProvider;

import java.util.Calendar;

public class CodeLockAutomationUnit extends DeviceAutomationUnit<Boolean, CodeLock> {
    private final IOutputPort<Boolean> _statusPort;
    private ILogger _logger = LoggingService.getLogger();
    private IInputPort<Boolean> _armingPort;
    private ILocalizationProvider _localizationProvider;
    private boolean _lastReading;
    private boolean _isArmed;
    private boolean _leaving;


    public void setLeaving(boolean value) {
        _leaving = value;
    }

    public boolean isArmed() {
        return _isArmed;
    }

    public boolean isLeaving() {
        return _leaving;
    }

    private void setArmed(boolean value) throws Exception {
        _isArmed = value;
    }

    private void toggleStatusPort(boolean value) throws Exception {
        _statusPort.write(value);
    }

    private void toggleStatusPort() throws Exception {
        toggleStatusPort(!_statusPort.read());
    }

    public CodeLockAutomationUnit(IInputPort<Boolean> armingPort, IOutputPort<Boolean> statusPort, CodeLock codeLock, ILocalizationProvider localizationProvider) throws Exception {
        super(codeLock);
        _armingPort = armingPort;
        _statusPort = statusPort;
        _localizationProvider = localizationProvider;
        _lastReading = armingPort.read();
        setArmed(false);
        setLeaving(false);
    }

    @Override
    public Boolean getValue() {
        return isArmed();
    }

    @Override
    public EvaluationResult buildEvaluationResult() {
        String interfaceValue = isArmed()
                ? _localizationProvider.getValue("ALARM:Armed")
                : _localizationProvider.getValue("ALARM:Disarmed");

        return new EvaluationResult(isArmed(), interfaceValue, isArmed(), isConnected());
    }

    @Override
    public IPort[] getUsedPorts() {
        return new IPort[] { _armingPort };
    }

    public void arm() throws Exception {
        if (!isArmed()) {
            _logger.info("Arming code lock: " + getName().getName());
            setArmed(true);
        }
    }

    public void disarm() throws Exception {
        if (isArmed()) {
            _logger.info("Disarming code lock: " + getName().getName());
            setArmed(false);
        }
    }

    @Override
    public void calculateInternal(Calendar now) throws Exception {
        boolean newReading = _armingPort.read();
        if (_lastReading != newReading && !newReading) {
            if (isArmed()) {
                disarm();
            } else {
                arm();
            }
            toggleStatusPort(isArmed());
        }

        if (isLeaving()) {
            toggleStatusPort();
        } else {
            toggleStatusPort(isArmed());
        }

        _lastReading = newReading;
    }
}

