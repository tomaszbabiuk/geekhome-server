package com.geekhome;

import com.geekhome.centralheatingmodule.CentralHeatingModule;
import com.geekhome.common.HardwareManager;
import com.geekhome.common.ILicenseManager;
import com.geekhome.common.commands.CommandsProcessor;
import com.geekhome.common.commands.Synchronizer;
import com.geekhome.common.json.JSONArrayList;
import com.geekhome.coremodule.CoreModule;
import com.geekhome.coremodule.DashboardAlertService;
import com.geekhome.coremodule.MasterConfiguration;
import com.geekhome.coremodule.automation.MasterAutomation;
import com.geekhome.coremodule.settings.AutomationSettings;
import com.geekhome.emailmodule.EmailModule;
import com.geekhome.hardwaremanagermodule.HardwareManagerModule;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.httpserver.SystemInfo;
import com.geekhome.httpserver.jetty.HomeServerStarter;
import com.geekhome.httpserver.modules.IModule;
import com.geekhome.onewiremodule.OneWireModule;
import com.geekhome.usersmodule.UsersModule;

public class Main {
    public static void main(String[] args) throws Exception {
        int port = HomeServerStarter.extractPortFromArgs(args);

        HomeServerStarter.start(port, new HomeServerStarter.BuildModulesDelegate() {
            @Override
            public JSONArrayList<IModule> buildModules(HardwareManager hardwareManager, AutomationSettings automationSettings,
                                                       ILocalizationProvider localizationProvider, SystemInfo systemInfo,
                                                       MasterConfiguration masterConfiguration, MasterAutomation masterAutomation,
                                                       Synchronizer synchronizer, CommandsProcessor commandsProcessor,
                                                       DashboardAlertService dashboardAlertService, ILicenseManager licenseManager) throws Exception {
                JSONArrayList<IModule> modules = new JSONArrayList<>();
                modules.add(new HeatingAdaptationModule());
                modules.add(new CoreModule(localizationProvider, systemInfo, masterConfiguration, masterAutomation, hardwareManager,
                        automationSettings, synchronizer, dashboardAlertService, licenseManager));
                modules.add(new UsersModule(localizationProvider));
                modules.add(new HardwareManagerModule(true, localizationProvider, hardwareManager, licenseManager));
                modules.add(new CentralHeatingModule(localizationProvider, masterConfiguration, masterAutomation, hardwareManager, automationSettings));
                modules.add(new OneWireModule(localizationProvider, hardwareManager, automationSettings));
                modules.add(new EmailModule(localizationProvider, masterAutomation, automationSettings, commandsProcessor));
                return modules;
            }
        });
    }
}
