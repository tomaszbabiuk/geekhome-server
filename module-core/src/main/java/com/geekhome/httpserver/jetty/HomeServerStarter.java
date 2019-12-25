package com.geekhome.httpserver.jetty;

import com.geekhome.httpserver.HardwareManager;
import com.geekhome.coremodule.commands.CommandsProcessor;
import com.geekhome.coremodule.commands.Synchronizer;
import com.geekhome.common.configuration.JSONArrayList;
import com.geekhome.coremodule.CorePortMapper;
import com.geekhome.coremodule.DashboardAlertService;
import com.geekhome.coremodule.MasterConfiguration;
import com.geekhome.http.jetty.ResourceLocalizationProvider;
import com.geekhome.coremodule.automation.MasterAutomation;
import com.geekhome.coremodule.settings.AutomationSettings;
import com.geekhome.coremodule.settings.TextFileAutomationSettingsPersister;
import com.geekhome.common.hardwaremanager.IPortMapper;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.common.OperationMode;
import com.geekhome.httpserver.SystemInfo;
import com.geekhome.httpserver.modules.IModule;
import com.geekhome.moquettemodule.MoquetteBroker;
import com.geekhome.moquettemodule.MqttBroker;

public class HomeServerStarter {
    public interface BuildModulesDelegate {
        JSONArrayList<IModule> buildModules(HardwareManager hardwareManager, AutomationSettings automationSettings,
                                            ILocalizationProvider localizationProvider, SystemInfo systemInfo,
                                            MasterConfiguration masterConfiguration, MasterAutomation masterAutomation,
                                            Synchronizer synchronizer, CommandsProcessor commandsProcessor,
                                            DashboardAlertService dashboardAlertService,
                                            MqttBroker mqttBroker) throws Exception;
    }

    public static void start(int port, BuildModulesDelegate buildModulesDelegate) {
        try {
            TextFileAutomationSettingsPersister settingsPersister = new TextFileAutomationSettingsPersister();
            AutomationSettings automationSettings = new AutomationSettings(settingsPersister);
            ILocalizationProvider localizationProvider = new ResourceLocalizationProvider();
            DashboardAlertService dashboardAlertService = new DashboardAlertService(localizationProvider);
            HardwareManager hardwareManager = new HardwareManager(dashboardAlertService);
            SystemInfo systemInfo = new SystemInfo(OperationMode.Diagnostics, localizationProvider, dashboardAlertService);
            final MasterConfiguration masterConfiguration = new MasterConfiguration(localizationProvider);
            final MasterAutomation masterAutomation = new MasterAutomation(masterConfiguration, hardwareManager, systemInfo, dashboardAlertService, localizationProvider);
            CommandsProcessor commandsProcessor = new CommandsProcessor(systemInfo, masterConfiguration, masterAutomation, localizationProvider);
            Synchronizer synchronizer = new Synchronizer(masterConfiguration, masterAutomation, automationSettings,
                    localizationProvider, systemInfo, commandsProcessor, dashboardAlertService);

            final MoquetteBroker mqttBroker = new MoquetteBroker();
            mqttBroker.start();

            JSONArrayList<IModule> modules = buildModulesDelegate.buildModules(hardwareManager,
                    automationSettings, localizationProvider, systemInfo, masterConfiguration,
                    masterAutomation, synchronizer, commandsProcessor, dashboardAlertService,
                    mqttBroker);

            for (IModule module : modules) {
                localizationProvider.load(module.getResources());
            }

            for (IModule module : modules) {
                module.initialize();
            }

            systemInfo.initialize(modules);
            hardwareManager.initialize(modules, systemInfo);
            masterConfiguration.initialize(modules);
            masterAutomation.initialize(modules);

            hardwareManager.discover();

            HomeServer server = new HomeServer(port, masterConfiguration, hardwareManager, modules, systemInfo, localizationProvider);
            server.start();

            IPortMapper portMapper = new CorePortMapper(masterConfiguration);
            hardwareManager.setPortMapper(portMapper);

            systemInfo.setOperationModeChangedListener(operationMode -> {
                masterAutomation.systemInfoChanged(operationMode);
                masterConfiguration.systemInfoChanged();
            });
            systemInfo.setOperationMode(OperationMode.Automatic);
            systemInfo.startMonitorables();

            masterAutomation.startAutomationLoop();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int extractPortFromArgs(String[] args) {
        if (args.length > 0) {
            try {
                return Integer.parseInt(args[0]);
            } catch (NumberFormatException ex) {
                return 80;
            }
        }

        return 80;
    }
}
