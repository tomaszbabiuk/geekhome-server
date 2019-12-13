package com.geekhome.centralheatingmodule.automation;

import com.geekhome.coremodule.Multicontroller;
import com.geekhome.coremodule.automation.DeviceAutomationUnit;
import com.geekhome.coremodule.automation.EvaluationResult;
import com.geekhome.coremodule.automation.ModeAutomationUnit;
import com.geekhome.coremodule.settings.AutomationSettings;
import com.geekhome.hardwaremanager.IPort;

import java.util.ArrayList;
import java.util.Calendar;

public class TemperatureMulticontrollerAutomationUnit extends DeviceAutomationUnit<Double, Multicontroller> {
    private Multicontroller _temperatureController;
    private AutomationSettings _automationSettings;
    private double _temperature;
    private ArrayList<ModeAutomationUnit> _modesUnits;

    public ArrayList<ModeAutomationUnit> getModesUnits() {
        return _modesUnits;
    }

    public void setModesUnits(ArrayList<ModeAutomationUnit> value) {
        _modesUnits = value;
    }

    @Override
    public Double getValue() {
        return _temperature;
    }

    public TemperatureMulticontrollerAutomationUnit(Multicontroller temperatureController, AutomationSettings automationSettings) {
        super(temperatureController);
        _temperatureController = temperatureController;
        _automationSettings = automationSettings;
        _temperature = temperatureController.getInitialValue();
    }

    private double readTemperatureFromSetting(String settingName) {
        String setting = _automationSettings.readSetting(settingName);
        try {
            return (!setting.equals("") && !setting.equals("1.#INF") && !setting.equals("undefined")) ? Double.parseDouble(setting) : _temperatureController.getInitialValue();
        } catch (Exception ex)
        {
            return _temperatureController.getInitialValue();
        }
    }

    @Override
    public void calculateInternal(Calendar now) {
        if (getModesUnits() != null) {
            for (ModeAutomationUnit unit : getModesUnits()) {
                if (unit.isPassed()) {
                    String settingWithModeName = getName().getUniqueId() + "_" + unit.getName().getUniqueId();
                    _temperature = readTemperatureFromSetting(settingWithModeName);
                    return;
                }
            }
        }

        _temperature = readTemperatureFromSetting(getName().getUniqueId());
    }

    @Override
    public EvaluationResult buildEvaluationResult() {
        String interfaceValue = getValue() + "°C";
        return new EvaluationResult(getValue(), interfaceValue, false, isConnected());
    }

    @Override
    public IPort[] getUsedPorts() {
        return new IPort[0];
    }
}

