package com.geekhome.firebasemodule;

import com.geekhome.common.IMonitorable;
import com.geekhome.common.alerts.IAlertService;
import com.geekhome.common.automation.IAutomationHook;
import com.geekhome.common.commands.Synchronizer;
import com.geekhome.common.automation.MasterAutomation;
import com.geekhome.common.settings.AutomationSettings;
import com.geekhome.firebasemodule.httpserver.FirebaseJsonRequestsDispatcher;
import com.geekhome.firebasemodule.httpserver.FirebasePostRequestsDispatcher;
import com.geekhome.common.localization.ILocalizationProvider;
import com.geekhome.coremodule.httpserver.IRequestsDispatcher;
import com.geekhome.common.localization.Resource;
import com.geekhome.coremodule.modules.*;
import com.geekhome.coremodule.modules.Module;

import java.util.ArrayList;

public class FirebaseModule extends Module {
    private ILocalizationProvider _localizationProvider;
    private AutomationSettings _automationSettings;
    private Synchronizer _synchronizer;
    private MasterAutomation _masterAutomation;
    private FirebaseService _firebaseService;

    public FirebaseModule(ILocalizationProvider localizationProvider, AutomationSettings automationSettings, Synchronizer synchronizer,
                          MasterAutomation masterAutomation) {
        _localizationProvider = localizationProvider;
        _automationSettings = automationSettings;
        _synchronizer = synchronizer;
        _masterAutomation = masterAutomation;
    }

    @Override
    public void initialize() throws Exception {
        _firebaseService = new FirebaseService(_automationSettings, _synchronizer, _localizationProvider);
    }

    @Override
    public ArrayList<IUnit> createUnits() {
        ArrayList<IUnit> units = new ArrayList<>();
        units.add(new Unit(UnitCategory.ConfigurationSettings, _localizationProvider.getValue("FIRE:FirebaseControl"), "messaging", "/config/firebase.htm"));
        return units;
    }

    @Override
    public Resource[] getResources() {
        return new Resource[] {
                new Resource("FIRE:FirebaseControl", "Firebase control", "Sterowanie przez Firebase"),
                new Resource("FIRE:FirebaseSettings", "Firebase server settings", "Ustawienia chmury Firebase"),
                new Resource("FIRE:Host", "Host", "Host"),
                new Resource("FIRE:Path", "Path", "Ścieżka"),
        };
    }

    @Override
    public String getTextResourcesPrefix() {
        return "FIRE";
    }

    @Override
    public void addAlertService(ArrayList<IAlertService> alertServices) throws Exception {
    }

    @Override
    public void addMonitorable(ArrayList<IMonitorable> monitorables) throws Exception {
        monitorables.add(_firebaseService);
    }

    @Override
    public ArrayList<IRequestsDispatcher> createDispatchers() {
        ArrayList<IRequestsDispatcher> dispatchers = new ArrayList<>();
        dispatchers.add(new FirebaseJsonRequestsDispatcher(_automationSettings, _localizationProvider));
        dispatchers.add(new FirebasePostRequestsDispatcher(_automationSettings));
        return dispatchers;
    }

    @Override
    public String[] listConsolidatedStyleSheets() {
        return new String[]
            {
                "CSS\\CUSTOMFIREBASE.CSS",
            };
    }

    @Override
    public String[] listConsolidatedJavascripts() {
        return new String[]
            {
                "JS\\CUSTOMFIREBASE.JS",
            };
    }

    @Override
    public IAutomationHook createAutomationHook() {
        return new FirebaseAutomationHook(_masterAutomation, _automationSettings, _synchronizer);
    }
}