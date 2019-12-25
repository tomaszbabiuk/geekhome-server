package com.geekhome.lightsmodule.automation;

import com.geekhome.coremodule.automation.*;
import com.geekhome.coremodule.settings.AutomationSettings;
import com.geekhome.common.hardwaremanager.IOutputPort;
import com.geekhome.common.hardwaremanager.IPort;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.lightsmodule.RgbLamp;

import java.awt.*;
import java.util.Calendar;

public class RgbLampAutomationUnit extends MultistateDeviceAutomationUnit<RgbLamp> implements IDeviceAutomationUnit<String> {

    private final IOutputPort<Integer> _redPort;
    private final IOutputPort<Integer> _greenPort;
    private final IOutputPort<Integer> _bluePort;
    private final AutomationSettings _automationSettings;
    private String _currentColor;

    public RgbLampAutomationUnit(final RgbLamp device,
                                 final IOutputPort<Integer> redPort,
                                 final IOutputPort<Integer> greenPort,
                                 final IOutputPort<Integer> bluePort,
                                 final ILocalizationProvider localizationProvider,
                                 final AutomationSettings automationSettings) throws Exception {
        super(device, localizationProvider);
        _redPort = redPort;
        _greenPort = greenPort;
        _bluePort = bluePort;
        _automationSettings = automationSettings;
        changeStateInternal("0off", ControlMode.Auto);
        _currentColor = null;
    }

    @Override
    public boolean isSignaled() {
        return !getStateId().equals("0off");
    }

    @Override
    public void calculateInternal(Calendar now) throws Exception {
        if (getControlMode() == ControlMode.Auto) {
            if (checkIfAnyBlockPasses("preset1")) {
                changeStateInternal("1preset1", ControlMode.Auto);
            } else if (checkIfAnyBlockPasses("preset2")) {
                changeStateInternal("2preset2", ControlMode.Auto);
            } else if (checkIfAnyBlockPasses("preset3")) {
                changeStateInternal("3preset3", ControlMode.Auto);
            } else if (checkIfAnyBlockPasses("preset4")) {
                changeStateInternal("4preset4", ControlMode.Auto);
            } else {
                changeStateInternal("0off", ControlMode.Auto);
            }
        }

        switch (getStateId()) {
            case "1preset1":
                changeColor(readPresetSetting(1));
                break;
            case "2preset2":
                changeColor(readPresetSetting(2));
                break;
            case "3preset3":
                changeColor(readPresetSetting(3));
                break;
            case "4preset4":
                changeColor(readPresetSetting(4));
                break;
            default:
                changeColor("#000000");
                _currentColor = null;
                break;
        }
    }

    private void changeColor(final String colorStartingWithHash) throws Exception {
        _currentColor = colorStartingWithHash;
        Color color = Color.BLACK;
        try {
            color = Color.decode(colorStartingWithHash);
        } catch (Exception ignored) {
        }
        changeOutputPortStateIfNeeded(_redPort, color.getRed());
        changeOutputPortStateIfNeeded(_greenPort, color.getGreen());
        changeOutputPortStateIfNeeded(_bluePort, color.getBlue());
    }

    private String readPresetSetting(final int presetNo) {
        return _automationSettings.readSetting(getDevice().getName().getUniqueId() + "_" + presetNo);
    }

    @Override
    public EvaluationResult buildEvaluationResult() {
        EvaluationResult evaluationResult = super.buildEvaluationResult();
        evaluationResult.setColorValue(_currentColor);
        return evaluationResult;
    }

    @Override
    public IPort[] getUsedPorts() {
        return new IPort[] { _redPort, _greenPort, _bluePort };
    }
}