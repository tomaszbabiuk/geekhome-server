package com.geekhome.automationmodule.automation;

import com.geekhome.automationmodule.IntensityDevice;
import com.geekhome.common.KeyValue;
import com.geekhome.common.json.JSONArrayList;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.coremodule.automation.*;
import com.geekhome.coremodule.settings.AutomationSettings;
import com.geekhome.hardwaremanager.IOutputPort;
import com.geekhome.hardwaremanager.IPort;
import com.geekhome.http.ILocalizationProvider;

import java.util.Calendar;

public class IntensityDeviceAutomationUnit extends MultistateDeviceAutomationUnit<IntensityDevice> implements IDeviceAutomationUnit<String> {

    private IOutputPort<Integer> _controlPort;
    private final AutomationSettings _automationSettings;
    private final ILogger _logger = LoggingService.getLogger();
    private int _lastValue;
    private String _lastStateId;

    public IntensityDeviceAutomationUnit(final IntensityDevice device,
                                         final IOutputPort<Integer> controlPort,
                                         final ILocalizationProvider localizationProvider,
                                         final AutomationSettings automationSettings) throws Exception {
        super(device, localizationProvider);
        _controlPort = controlPort;
        _automationSettings = automationSettings;
        _lastValue = _controlPort.read();
        _lastStateId = "0off";
        changeStateInternal("0off", ControlMode.Auto);
    }

    @Override
    public boolean isSignaled() {
        return !getStateId().equals("0off");
    }

    @Override
    public void calculateInternal(Calendar now) throws Exception {
        boolean valueHasChanged = false;
        boolean stateHasChanged = false;
        if (_controlPort.read() != _lastValue) {
            valueHasChanged = true;
        }
        if (!getStateId().equals(_lastStateId)) {
            stateHasChanged = true;
        }
        _lastValue = _controlPort.read();
        _lastStateId = getStateId();

        boolean externalChange = valueHasChanged && !stateHasChanged;

        if (externalChange && getControlMode() == ControlMode.Manual) {
            boolean changingToOff = _controlPort.read() == 0;
            boolean changingToPreset1 = _controlPort.read() == readPresetSetting(1);
            boolean changingToPreset2 = _controlPort.read() == readPresetSetting(2);
            boolean changingToPreset3 = _controlPort.read() == readPresetSetting(3);
            boolean changingToPreset4 = _controlPort.read() == readPresetSetting(4);
            if (changingToOff) {
                changeStateInternal("0off", ControlMode.Manual);
            } else if (changingToPreset1) {
                changeStateInternal("1preset1", ControlMode.Manual);
            } else if (changingToPreset2) {
                changeStateInternal("2preset2", ControlMode.Manual);
            } else if (changingToPreset3) {
                changeStateInternal("3preset3", ControlMode.Manual);
            } else if (changingToPreset4) {
                changeStateInternal("4preset4", ControlMode.Manual);
            } else {
                changeStateInternal("5custom", ControlMode.Manual);
            }
        }

        if (externalChange && getControlMode() == ControlMode.ForcedManual) {
            if (_controlPort.read() == 0) {
                changeStateInternal("0off", ControlMode.Auto);
            }
        }

        if (externalChange && getControlMode() == ControlMode.Auto) {
            if (_controlPort.read() > 0) {
                changeStateInternal("5custom", ControlMode.ForcedManual);
            }
        }

        //automatic control (button presses are ignored)
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

        if (getControlMode() != ControlMode.ForcedManual) {
            execute();
        }
    }

    private void execute() throws Exception {
        switch (getStateId()) {
            case "1preset1":
                changeOutputPortStateIfNeeded(_controlPort, readPresetSetting(1));
                break;
            case "2preset2":
                changeOutputPortStateIfNeeded(_controlPort, readPresetSetting(2));
                break;
            case "3preset3":
                changeOutputPortStateIfNeeded(_controlPort, readPresetSetting(3));
                break;
            case "4preset4":
                changeOutputPortStateIfNeeded(_controlPort, readPresetSetting(4));
                break;
            case "5custom":
                break;
            default: // 0off
                changeOutputPortStateIfNeeded(_controlPort, 0);
                break;
        }
    }

    private int readPresetSetting(final int presetNo) {
        String presetRaw = _automationSettings.readSetting(getDevice().getName().getUniqueId() + "_" + presetNo);
        return Integer.valueOf(presetRaw);
    }

    @Override
    public EvaluationResult buildEvaluationResult() {
        String interfaceValue = getState().getName().getName();
        JSONArrayList<KeyValue> descriptions = new JSONArrayList<>();
        if (!getState().getName().getUniqueId().equals("0off")) {
            int intensity = 0;
            switch (getStateId()) {
                case "1preset1":
                    intensity = readPresetSetting(1);
                    break;
                case "2preset2":
                    intensity = readPresetSetting(2);
                    break;
                case "3preset3":
                    intensity = readPresetSetting(3);
                    break;
                case "4preset4":
                    intensity = readPresetSetting(4);
                    break;
                case "5custom":
                    intensity = _controlPort.read();
                    break;
            }

            descriptions.add(new KeyValue(getLocalizationProvider().getValue("C:Intensity"), formatIntensity(intensity)));
        }
        return new EvaluationResult(getValue(), interfaceValue, isSignaled(), isConnected(), descriptions, getControlMode(), false);
    }

    @Override
    public IPort[] getUsedPorts() {
        return new IPort[]{_controlPort};
    }

    private String formatIntensity(int intensity) {
        return String.format("%d %%", intensity);
    }
}