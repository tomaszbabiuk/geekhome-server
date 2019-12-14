package com.geekhome.automationmodule.automation;

import com.geekhome.automationmodule.IntensityDevice;
import com.geekhome.common.KeyValue;
import com.geekhome.common.json.JSONArrayList;
import com.geekhome.coremodule.automation.*;
import com.geekhome.coremodule.settings.AutomationSettings;
import com.geekhome.hardwaremanager.IOutputPort;
import com.geekhome.hardwaremanager.IPort;
import com.geekhome.hardwaremanager.IUserChangeable;
import com.geekhome.http.ILocalizationProvider;

import java.util.Calendar;

public class IntensityDeviceAutomationUnit extends MultistateDeviceAutomationUnit<IntensityDevice> implements IDeviceAutomationUnit<String> {

    private IOutputPort<Integer> _controlPort;
    private final AutomationSettings _automationSettings;

    public IntensityDeviceAutomationUnit(final IntensityDevice device,
                                    final IOutputPort<Integer> controlPort,
                                    final ILocalizationProvider localizationProvider,
                                    final AutomationSettings automationSettings) throws Exception {
        super(device, localizationProvider);
        _controlPort = controlPort;
        _automationSettings = automationSettings;
        changeStateInternal("0off", ControlMode.Auto);
    }

    @Override
    public boolean isSignaled() {
        return !getStateId().equals("0off");
    }

    @Override
    public void calculateInternal(Calendar now) throws Exception {
        if (_controlPort instanceof IUserChangeable) {
            IUserChangeable userChangeable = (IUserChangeable)_controlPort;
            if (userChangeable.didUserChangeLastValue()) {
                boolean changingToPreset1 =  getStateId().equals("preset1") && _controlPort.read() == readPresetSetting(1);
                boolean changingToPreset2 =  getStateId().equals("preset2") && _controlPort.read() == readPresetSetting(2);
                boolean changingToPreset3 =  getStateId().equals("preset3") && _controlPort.read() == readPresetSetting(3);
                boolean changingToPreset4 =  getStateId().equals("preset4") && _controlPort.read() == readPresetSetting(4);
                if (_controlPort.read() != 0) {
                    changeStateInternal("custom", ControlMode.Auto);
                } else {
                    changeStateInternal("0off", ControlMode.Auto);
                }
                userChangeable.resetUserChangeLastValue();
                return;
            }
        }

        if (getControlMode() == ControlMode.Auto) {
            if (getStateId().equals("custom")) {
                return;
            }

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
            case "custom":
                break;
            default:
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
            }

            descriptions.add(new KeyValue(getLocalizationProvider().getValue("C:Intensity"), formatIntensity(intensity)));
        }
        return new EvaluationResult(getValue(), interfaceValue, isSignaled(), isConnected(), descriptions, getControlMode(), false);
    }

    @Override
    public IPort[] getUsedPorts() {
        return new IPort[] { _controlPort };
    }

    private String formatIntensity(int intensity) {
        return String.format("%d (%.1f%%)", intensity, intensity/256.0*100.0);
    }
}