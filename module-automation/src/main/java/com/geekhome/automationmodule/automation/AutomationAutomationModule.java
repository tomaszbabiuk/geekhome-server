package com.geekhome.automationmodule.automation;

import com.geekhome.automationmodule.*;
import com.geekhome.coremodule.OnOffDeviceBase;
import com.geekhome.coremodule.automation.*;
import com.geekhome.coremodule.settings.AutomationSettings;
import com.geekhome.hardwaremanager.IHardwareManager;
import com.geekhome.hardwaremanager.IInputPort;
import com.geekhome.hardwaremanager.IOutputPort;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.httpserver.modules.CollectorCollection;
import com.geekhome.httpserver.modules.IAutomationModule;
import com.geekhome.synchronizationmodule.business.SmartEvent;

import java.util.Calendar;

public class AutomationAutomationModule implements IAutomationModule {
    private final MasterAutomation _masterAutomation;
    private AutomationConfiguration _automationConfiguration;
    private IHardwareManager _hardwareManager;
    private ILocalizationProvider _localizationProvider;
    private final AutomationSettings _automationSettings;

    public AutomationAutomationModule(final MasterAutomation masterAutomation,
                                      final AutomationConfiguration automationConfiguration,
                                      final IHardwareManager hardwareManager,
                                      final ILocalizationProvider localizationProvider,
                                      final AutomationSettings automationSettings) {
        _masterAutomation = masterAutomation;
        _automationConfiguration = automationConfiguration;
        _hardwareManager = hardwareManager;
        _localizationProvider = localizationProvider;
        _automationSettings = automationSettings;
    }

    @Override
    public void addIndependentConditionAutomationUnits(CollectorCollection<IEvaluableAutomationUnit> conditionsList) throws Exception {
        for (NfcCondition condition : _automationConfiguration.getNfcConditions().values()) {
            NfcConditionAutomationUnit unit = new NfcConditionAutomationUnit(condition);
            conditionsList.add(unit);
        }
    }

    @Override
    public void addDependentConditionAutomationUnits(CollectorCollection<IEvaluableAutomationUnit> conditionsList) throws Exception {
    }

    @Override
    public void addDeviceAutomationUnits(CollectorCollection<IDeviceAutomationUnit> devicesList) throws Exception {
        for (OnOffDeviceBase onOffDevice : _automationConfiguration.getOnOffDevices().values()) {
            IOutputPort<Boolean> outputPort = _hardwareManager.findDigitalOutputPort(onOffDevice.getPortId());
            OnOffDeviceAutomationUnit unit = new OnOffDeviceAutomationUnit<>(outputPort, onOffDevice, _localizationProvider);
            devicesList.add(unit);
        }

        for (IntensityDevice intensityDevice : _automationConfiguration.getIntensityDevices().values()) {
            IOutputPort<Integer> controlPort = _hardwareManager.findAnalogOutputPort(intensityDevice.getPortId());
            IntensityDeviceAutomationUnit unit = new IntensityDeviceAutomationUnit(intensityDevice, controlPort, _localizationProvider, _automationSettings);
            devicesList.add(unit);
        }

        for (ImpulseSwitch impulseSwitch : _automationConfiguration.getImpulseSwitches().values()) {
            IDeviceAutomationUnit unit;
            if ((impulseSwitch.getPortId() != null) && (!impulseSwitch.getPortId().equals("-"))) {
                IInputPort<Boolean> inputPort = _hardwareManager.findDigitalInputPort(impulseSwitch.getPortId());
                unit = new ImpulseSwitchAutomationUnit(inputPort, impulseSwitch, _localizationProvider);
            } else {
                unit = new VirtualImpulseSwitchAutomationUnit(impulseSwitch, _localizationProvider);
            }
            devicesList.add(unit);
        }
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
        if (event.equals(SmartEvent.NfcTagScanned)) {
            for (NfcCondition nfcCondition : _automationConfiguration.getNfcConditions().values()) {
                if (nfcCondition.getTag().toLowerCase().equals(value.toLowerCase())) {
                    NfcConditionAutomationUnit nfcUnit = (NfcConditionAutomationUnit) _masterAutomation.findConditionUnit(nfcCondition.getName().getUniqueId());
                    nfcUnit.toggle();
                }
            }
        }
    }
}