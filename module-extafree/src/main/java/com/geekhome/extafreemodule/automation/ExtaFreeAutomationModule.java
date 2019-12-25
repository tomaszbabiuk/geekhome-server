package com.geekhome.extafreemodule.automation;

import com.geekhome.coremodule.automation.*;
import com.geekhome.extafreemodule.ExtaFreeBlind;
import com.geekhome.extafreemodule.ExtaFreeConfiguration;
import com.geekhome.common.hardwaremanager.IHardwareManager;
import com.geekhome.common.hardwaremanager.ITogglePort;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.common.configuration.CollectorCollection;
import com.geekhome.httpserver.modules.IAutomationModule;
import com.geekhome.synchronizationmodule.business.SmartEvent;

public class ExtaFreeAutomationModule implements IAutomationModule {
    private ExtaFreeConfiguration _extaFreeConfiguration;
    private IHardwareManager _hardwareManager;
    private ILocalizationProvider _localizationProvider;

    public ExtaFreeAutomationModule(ExtaFreeConfiguration extaFreeConfiguration, IHardwareManager hardwareManager, ILocalizationProvider localizationProvider) {
        _extaFreeConfiguration = extaFreeConfiguration;
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
        for (ExtaFreeBlind blind : _extaFreeConfiguration.getExtaFreeBlinds().values()) {
            ITogglePort upPort = _hardwareManager.findTogglePort(blind.getUpPortId());
            ITogglePort downPort = _hardwareManager.findTogglePort(blind.getDownPortId());
            ExtaFreeBlindAutomationUnit unit = new ExtaFreeBlindAutomationUnit(blind, upPort, downPort, _localizationProvider);
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

