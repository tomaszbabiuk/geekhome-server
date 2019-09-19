package com.geekhome.httpserver.modules;

import com.geekhome.coremodule.automation.*;
import com.geekhome.synchronizationmodule.business.SmartEvent;

import java.util.Calendar;

public interface IAutomationModule {
    void addIndependentConditionAutomationUnits(CollectorCollection<IEvaluableAutomationUnit> conditionsList) throws Exception;

    void addDependentConditionAutomationUnits(CollectorCollection<IEvaluableAutomationUnit> conditionsList) throws Exception;

    void addDeviceAutomationUnits(CollectorCollection<IDeviceAutomationUnit> devicesList) throws Exception;

    void addModeAutomationUnits(CollectorCollection<ModeAutomationUnit> modesList);

    void addAlertAutomationUnits(CollectorCollection<AlertAutomationUnit> alertsList);

    void addBlocksAutomationUnits(CollectorCollection<BlockAutomationUnit> blocksList);

    void initialized() throws Exception;

    void reportSmartEvent(SmartEvent event, String value, String code) throws Exception;
}