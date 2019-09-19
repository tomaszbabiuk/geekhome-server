package com.geekhome.alarmmodule.automation;

import com.geekhome.alarmmodule.Gate;
import com.geekhome.common.CodeInvalidException;
import com.geekhome.common.KeyValue;
import com.geekhome.common.json.JSONArrayList;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;
import com.geekhome.coremodule.automation.ControlMode;
import com.geekhome.coremodule.automation.EvaluationResult;
import com.geekhome.coremodule.automation.ICalculableAutomationUnit;
import com.geekhome.hardwaremanager.IInputPort;
import com.geekhome.hardwaremanager.IOutputPort;
import com.geekhome.http.ILocalizationProvider;

import java.util.Calendar;

public class GateAutomationUnit extends MagneticDetectorAutomationUnit<Gate> implements ICalculableAutomationUnit {
    private ILogger _logger = LoggingService.getLogger();
    private long _toggledAt;
    private IOutputPort<Boolean> _gateControlPort;
    private boolean _toggling;
    private boolean _pleaseWait;
    private boolean _expectedState;
    private boolean _error;

    public GateAutomationUnit(IInputPort<Boolean> magneticDetectorPort, IOutputPort<Boolean> gateControlPort, Gate device,
                                       ILocalizationProvider localizationProvider) throws Exception {
        super(magneticDetectorPort, device, localizationProvider);
        _gateControlPort = gateControlPort;
        _toggling = false;
        _error = false;
        _pleaseWait = false;
        _toggledAt = 0;
    }

    @Override
    public EvaluationResult buildEvaluationResult() {
        EvaluationResult result =  super.buildEvaluationResult();
        if (result.getDescriptions() == null) {
            result.setDescriptions(new JSONArrayList<KeyValue>());
        }

        boolean opening = _expectedState;

        if (_pleaseWait) {
            String pleaseWaitKey = getLocalizationProvider().getValue("C:PleaseWait");
            String pleaseWaitValue = getLocalizationProvider().getValue(opening ? "C:Opening" : "C:Closing");
            result.getDescriptions().add(new KeyValue(pleaseWaitKey, pleaseWaitValue));
        }

        if (_error) {
            String errorKey = getLocalizationProvider().getValue("C:Warning");
            String errorValue = getLocalizationProvider().getValue(opening ? "ALARM:OpeningError" : "ALARM:ClosingError");
            result.getDescriptions().add(new KeyValue(errorKey, errorValue));
        }

        return result;
    }

    @Override
    public void changeState(String state, ControlMode controlMode, String code, String actor) throws Exception {
        if (state.startsWith("*")) {
            boolean isOpen = isLineBreached();
            if (!isOpen && !isCodeValid(code)){
                throw new CodeInvalidException(getDevice(), getLocalizationProvider());
            }
            if (state.equals("*doopen") && !isOpen) {
                setControlMode(ControlMode.Manual);
                _expectedState = true;
                toggle();
            }

            if (state.equals("*doclose") && isOpen) {
                _expectedState = false;
                setControlMode(ControlMode.Manual);
                toggle();
            }

        } else {
            super.changeState(state, controlMode, code, actor);
        }
    }

    private void toggle() throws Exception {
        if (!_toggling && !_pleaseWait) {
            _toggling = true;
            _pleaseWait = true;
            _toggledAt = Calendar.getInstance().getTimeInMillis();
            _gateControlPort.write(true);
        }
    }

    private boolean isCodeValid(String code) {
        return code.equals(getDevice().getCode());
    }

    @Override
    public void calculate(Calendar now) throws Exception {
        super.calculate(now);

        if (_toggling && _toggledAt + 2000 < now.getTimeInMillis()) {
            _toggling = false;
            _gateControlPort.write(false);
        }

        if (_pleaseWait) {
            if (_toggledAt + 60000 < now.getTimeInMillis()) {
                _logger.info(String.format("Closing gate %s takes too long", getDevice().getName()));
                _pleaseWait = false;
                _error = true;
            }
        }

        if (isLineBreached() == _expectedState) {
            _pleaseWait = false;
            _error = false;
        }

        if (!_toggling && !_pleaseWait && !_error) {
            boolean openAutomatically = checkIfAnyBlockPasses("open");
            boolean closeAutomatically = checkIfAnyBlockPasses("close");
            boolean isOpen = isLineBreached();

            if (openAutomatically && !closeAutomatically && !isOpen) {
                toggle();
                _expectedState = true;
                setControlMode(ControlMode.Auto);
            }

            if (closeAutomatically && isOpen) {
                toggle();
                setControlMode(ControlMode.Auto);
                _expectedState = false;
            }
        }
    }
}