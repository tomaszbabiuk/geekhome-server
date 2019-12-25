package com.geekhome.httpserver.modules;

import com.geekhome.common.IHardwareManagerAdapterFactory;
import com.geekhome.common.IMonitorable;
import com.geekhome.common.configuration.Collector;
import com.geekhome.coremodule.commands.IAlertService;
import com.geekhome.http.Resource;
import com.geekhome.http.IRequestsDispatcher;
import com.geekhome.httpserver.ICrudPostHandler;

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