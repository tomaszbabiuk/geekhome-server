package com.geekhome.ventilationmodule.automation;

import com.geekhome.common.automation.*;
import com.geekhome.common.hardwaremanager.IHardwareManager;
import com.geekhome.common.hardwaremanager.IOutputPort;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.common.configuration.CollectorCollection;
import com.geekhome.common.automation.IAutomationModule;
import com.geekhome.synchronizationmodule.business.SmartEvent;
import com.geekhome.ventilationmodule.Recuperator;
import com.geekhome.ventilationmodule.VentilationConfiguration;

public class VentilationAutomationModule implements IAutomationModule {
    private VentilationConfiguration _automationConfiguration;
    private IHardwareManager _hardwareManager;
    private ILocalizationProvider _localizationProvider;

    public VentilationAutomationModule(VentilationConfiguration automationConfiguration, IHardwareManager hardwareManager, ILocalizationProvider localizationProvider) {
        _automationConfiguration = automationConfiguration;
        _hardwareManager = hardwareManager;
        _localizationProvider = localizationProvider;
    }

    @Override
    public void addIndependentConditionAutomationUnits(CollectorCollection<IEvaluableAutomationUnit> conditionsList) throws Exception {
    }

    @Override
    public void addDependentConditionAutomationUnits(CollectorCollection<IEvaluableAutomationUnit> conditionsList) throws Exception {
    }

    @Override
    public void addDeviceAutomationUnits(CollectorCollection<IDeviceAutomationUnit> devicesList) throws Exception {
        for (Recuperator recuperator : _automationConfiguration.getRecuperators().values()) {
            IOutputPort<Boolean> automaticControlPort = _hardwareManager.findDigitalOutputPort(recuperator.getAutomaticControlPortId());
            IOutputPort<Boolean> secondGearPort = _hardwareManager.findDigitalOutputPort(recuperator.getSecondGearPortId());
            IOutputPort<Boolean> thirdGearPort = _hardwareManager.findDigitalOutputPort(recuperator.getThirdGearPortId());
            RecuperatorAutomationUnit unit = new RecuperatorAutomationUnit(recuperator, automaticControlPort, secondGearPort, thirdGearPort, _localizationProvider);
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
    }
}