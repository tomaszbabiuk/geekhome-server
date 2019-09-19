package com.geekhome.httpserver.modules;

import com.geekhome.common.IHardwareManagerAdapterFactory;
import com.geekhome.common.IMonitorable;
import com.geekhome.common.Persistable;
import com.geekhome.common.commands.IAlertService;
import com.geekhome.common.json.JSONAwareBase;
import com.geekhome.http.IRequestsDispatcher;
import com.geekhome.httpserver.ICrudPostHandler;

import java.util.ArrayList;

public abstract class Module extends JSONAwareBase implements IModule {
    private ArrayList<IUnit> _units;

    @Override
    public ArrayList<IRequestsDispatcher> createDispatchers() {
        return null;
    }

    @Override
    public IAutomationModule createAutomationModule() {
        return null;
    }

    @Override
    public IAutomationHook createAutomationHook() {
        return null;
    }

    @Override
    public Collector createCollector() {
        return null;
    }

    @Override
    public void addAlertService(ArrayList<IAlertService> alertServices) throws Exception {
    }

    @Override
    public void addMonitorable(ArrayList<IMonitorable> monitorables) throws Exception {
    }

    @Override
    public IConfigurationValidator createConfigurationValidator() {
        return null;
    }

    @Override
    public DependenciesCheckerModule createDependenciesCheckerModule() {
        return null;
    }

    @Persistable(name="Units")
    @Override
    public ArrayList<IUnit> getUnits() {
        if (_units == null) {
            _units = createUnits();
        }
        return _units;
    }

    @Override
    public ArrayList<IUnit> createUnits() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<ICrudPostHandler> createCrudPostHandlers() {
        return new ArrayList<>();
    }

    @Override
    public String[] listConsolidatedJavascripts() {
        return new String[]{};
    }

    @Override
    public String[] listConsolidatedStyleSheets() {
        return new String[]{};
    }

    @Override
    public void addSerialAdaptersFactory(ArrayList<IHardwareManagerAdapterFactory> factories) {
    }

    @Override
    public void initialize() throws Exception {
    }
}