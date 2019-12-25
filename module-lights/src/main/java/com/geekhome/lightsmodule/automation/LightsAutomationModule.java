package com.geekhome.lightsmodule.automation;

import com.geekhome.coremodule.automation.*;
import com.geekhome.coremodule.settings.AutomationSettings;
import com.geekhome.common.hardwaremanager.*;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.httpserver.modules.CollectorCollection;
import com.geekhome.httpserver.modules.IAutomationModule;
import com.geekhome.lightsmodule.*;
import com.geekhome.synchronizationmodule.business.SmartEvent;

public class LightsAutomationModule implements IAutomationModule {
    private LightsConfiguration _lightsConfiguration;
    private IHardwareManager _hardwareManager;
    private ILocalizationProvider _localizationProvider;
    private final AutomationSettings _automationSettings;

    public LightsAutomationModule(final LightsConfiguration lightsConfiguration,
                                  final IHardwareManager hardwareManager,
                                  final ILocalizationProvider localizationProvider,
                                  final AutomationSettings automationSettings) {
        _lightsConfiguration = lightsConfiguration;
        _hardwareManager = hardwareManager;
        _localizationProvider = localizationProvider;
        _automationSettings = automationSettings;
    }

    @Override
    public void addIndependentConditionAutomationUnits(CollectorCollection<IEvaluableAutomationUnit> conditionsList) throws Exception {
        for (TwilightCondition condition : _lightsConfiguration.getTwilightConditions().values()) {
            TwilightConditionAutomationUnit unit = new TwilightConditionAutomationUnit(condition);
            conditionsList.add(unit);
        }
    }

    @Override
    public void addDependentConditionAutomationUnits(CollectorCollection<IEvaluableAutomationUnit> conditionsList) throws Exception {
    }

    @Override
    public void addDeviceAutomationUnits(CollectorCollection<IDeviceAutomationUnit> devicesList) throws Exception {
        for (Blind blind : _lightsConfiguration.getBlinds().values()) {
            IOutputPort<Boolean> automaticControlPort = _hardwareManager.findDigitalOutputPort(blind.getAutomaticControlPortId());
            IOutputPort<Boolean> motorPort = _hardwareManager.findDigitalOutputPort(blind.getMotorPortId());
            BlindAutomationUnit unit = new BlindAutomationUnit(blind, automaticControlPort, motorPort, _localizationProvider);
            devicesList.add(unit);
        }

        for (RgbLamp rgbLamp : _lightsConfiguration.getRgbLamps().values()) {
            IOutputPort<Integer> redPort = _hardwareManager.findPowerOutputPort(rgbLamp.getRedPortId());
            IOutputPort<Integer> greenPort = _hardwareManager.findPowerOutputPort(rgbLamp.getGreenPortId());
            IOutputPort<Integer> bluePort = _hardwareManager.findPowerOutputPort(rgbLamp.getBluePortId());
            RgbLampAutomationUnit unit = new RgbLampAutomationUnit(rgbLamp, redPort, greenPort,bluePort, _localizationProvider, _automationSettings);
            devicesList.add(unit);
        }

        for (SevenColorLamp sevenColorLamp : _lightsConfiguration.get7ColorLamps().values()) {
            IOutputPort<Boolean> redPort = findDigitalOutputOrWrappedTogglePort(sevenColorLamp.getRedPortId());
            IOutputPort<Boolean> greenPort = findDigitalOutputOrWrappedTogglePort(sevenColorLamp.getGreenPortId());
            IOutputPort<Boolean> bluePort = findDigitalOutputOrWrappedTogglePort(sevenColorLamp.getBluePortId());
            SevenColorLampAutomationUnit unit = new SevenColorLampAutomationUnit(sevenColorLamp, redPort, greenPort,bluePort, _localizationProvider);
            devicesList.add(unit);
        }

        for (Luxmeter luxmeter : _lightsConfiguration.getLuxmeters().values()) {
            IInputPort<Double> luminosityPort = _hardwareManager.findLuminosityPort(luxmeter.getPortId());
            LuxmeterAutomationUnit unit = new LuxmeterAutomationUnit(luxmeter, luminosityPort);
            devicesList.add(unit);
        }
    }

    private IOutputPort<Boolean> findDigitalOutputOrWrappedTogglePort(String portId) throws PortNotFoundException {
        IOutputPort<Boolean> port = _hardwareManager.tryFindDigitalOutputPort(portId);
        if (port == null) {
            port = new TogglePortWrapper(_hardwareManager.findTogglePort(portId));
        }
        return port;
    }

    @Override
    public void addModeAutomationUnits(CollectorCollection<ModeAutomationUnit> modesList) {
    }

    @Override
    public void addAlertAutomationUnits(CollectorCollection<AlertAutomationUnit> alertsList) {
    }

    @Override
    public void addBlocksAutomationUnits(CollectorCollection<BlockAutomationUnit> blocksList) {
    }

    @Override
    public void initialized() throws Exception {
    }

    @Override
    public void reportSmartEvent(SmartEvent event, String value, String code) throws Exception {
    }
}

