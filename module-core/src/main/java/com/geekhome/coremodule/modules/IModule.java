package com.geekhome.coremodule.modules;

import com.geekhome.common.IHardwareManagerAdapterFactory;
import com.geekhome.common.IMonitorable;
import com.geekhome.common.automation.IAutomationHook;
import com.geekhome.common.automation.IAutomationModule;
import com.geekhome.common.configuration.Collector;
import com.geekhome.common.alerts.IAlertService;
import com.geekhome.common.configuration.DependenciesCheckerModule;
import com.geekhome.common.configuration.IConfigurationValidator;
import com.geekhome.common.localization.Resource;
import com.geekhome.coremodule.httpserver.IRequestsDispatcher;
import com.geekhome.coremodule.httpserver.ICrudPostHandler;

import java.util.ArrayList;

public interface IModule {
    ArrayList<IUnit> getUnits();
    ArrayList<IRequestsDispatcher> createDispatchers();
    DependenciesCheckerModule createDependenciesCheckerModule();
    String[] listConsolidatedJavascripts();
    String[] listConsolidatedStyleSheets();
    Collector createCollector();
    IConfigurationValidator createConfigurationValidator();
    IAutomationModule createAutomationModule();
    IAutomationHook createAutomationHook();
    ArrayList<ICrudPostHandler> createCrudPostHandlers();
    ArrayList<IUnit> createUnits();
    void addSerialAdaptersFactory(ArrayList<IHardwareManagerAdapterFactory> factories);
    void addAlertService(ArrayList<IAlertService> alertServices) throws Exception;
    void addMonitorable(ArrayList<IMonitorable> monitorables) throws Exception;
    Resource[] getResources();
    void initialize() throws Exception;
    String getTextResourcesPrefix();
}