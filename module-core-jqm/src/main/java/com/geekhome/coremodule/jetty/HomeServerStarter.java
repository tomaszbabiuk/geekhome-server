package com.geekhome.coremodule.jetty;

import com.geekhome.common.IHardwareManagerAdapterFactory;
import com.geekhome.common.IMonitorable;
import com.geekhome.common.alerts.IAlertService;
import com.geekhome.common.automation.IAutomationHook;
import com.geekhome.common.automation.IAutomationModule;
import com.geekhome.common.configuration.*;
import com.geekhome.common.hardwaremanager.HardwareManager;
import com.geekhome.common.commands.CommandsProcessor;
import com.geekhome.common.commands.Synchronizer;
import com.geekhome.common.configuration.CorePortMapper;
import com.geekhome.common.alerts.DashboardAlertService;
import com.geekhome.common.automation.MasterAutomation;
import com.geekhome.common.settings.AutomationSettings;
import com.geekhome.common.settings.TextFileAutomationSettingsPersister;
import com.geekhome.common.hardwaremanager.IPortMapper;
import com.geekhome.common.localization.ILocalizationProvider;
import com.geekhome.common.OperationMode;
import com.geekhome.common.automation.SystemInfo;
import com.geekhome.coremodule.modules.IModule;
import com.geekhome.coremodule.modules.NavigationTree;
import com.geekhome.moquettemodule.MoquetteBroker;
import com.geekhome.moquettemodule.MqttBroker;

import java.util.ArrayList;

public class HomeServerStarter {
    public interface BuildModulesDelegate {
        JSONArrayList<IModule> buildModules(HardwareManager hardwareManager, AutomationSettings automationSettings,
                                            ILocalizationProvider localizationProvider, SystemInfo systemInfo,
                                            MasterConfiguration masterConfiguration, MasterAutomation masterAutomation,
                                            Synchronizer synchronizer, CommandsProcessor commandsProcessor,
                                            DashboardAlertService dashboardAlertService,
                                            MqttBroker mqttBroker, NavigationTree navigationTree) throws Exception;
    }

    public static void start(int port, BuildModulesDelegate buildModulesDelegate) {
        try {
            TextFileAutomationSettingsPersister settingsPersister = new TextFileAutomationSettingsPersister();
            AutomationSettings automationSettings = new AutomationSettings(settingsPersister);
            ILocalizationProvider localizationProvider = new ResourceLocalizationProvider();
            DashboardAlertService dashboardAlertService = new DashboardAlertService(localizationProvider);
            HardwareManager hardwareManager = new HardwareManager(dashboardAlertService);
            SystemInfo systemInfo = new SystemInfo(OperationMode.Diagnostics, dashboardAlertService);
            NavigationTree navigationTree = new NavigationTree();
            final MasterConfiguration masterConfiguration = new MasterConfiguration(localizationProvider);
            final MasterAutomation masterAutomation = new MasterAutomation(masterConfiguration, hardwareManager, systemInfo, dashboardAlertService);
            CommandsProcessor commandsProcessor = new CommandsProcessor(systemInfo, masterConfiguration, masterAutomation, localizationProvider);
            Synchronizer synchronizer = new Synchronizer(masterConfiguration, masterAutomation, automationSettings,
                    localizationProvider, systemInfo, commandsProcessor, dashboardAlertService);

            final MoquetteBroker mqttBroker = new MoquetteBroker();
            mqttBroker.start();

            JSONArrayList<IModule> modules = buildModulesDelegate.buildModules(hardwareManager,
                    automationSettings, localizationProvider, systemInfo, masterConfiguration,
                    masterAutomation, synchronizer, commandsProcessor, dashboardAlertService,
                    mqttBroker, navigationTree);

            navigationTree.setModules(modules);

            for (IModule module : modules) {
                localizationProvider.load(module.getResources());
            }

            for (IModule module : modules) {
                module.initialize();
            }

            hardwareManager.initialize(extractAdaptersFactories(modules));
            systemInfo.initialize(extractMonitorables(modules), extractAlertServices(modules));
            masterConfiguration.initialize(extractDependenciesCheckers(modules), extractCollectors(modules),
                    extractConfigurationValidators(modules));
            masterAutomation.initialize(extractAutomationModules(modules), extractAutomationHooks(modules));

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

    //TODO: refactor all extract functions to one lambda
    private static ArrayList<IAutomationModule> extractAutomationModules(JSONArrayList<IModule> modules) {
        ArrayList<IAutomationModule> result = new ArrayList<>();

        for (IModule module : modules) {
            addToCollectionIfNotNull(module.createAutomationModule(), result);
        }

        return result;
    }

    private static ArrayList<IAutomationHook> extractAutomationHooks(JSONArrayList<IModule> modules) {
        ArrayList<IAutomationHook> result = new ArrayList<>();

        for (IModule module : modules) {
            addToCollectionIfNotNull(module.createAutomationHook(), result);
        }

        return result;
    }

    private static ArrayList<IHardwareManagerAdapterFactory> extractAdaptersFactories(JSONArrayList<IModule> modules) {
        ArrayList<IHardwareManagerAdapterFactory> factories = new ArrayList<>();
        for (IModule module : modules) {
            module.addSerialAdaptersFactory(factories);
        }
        return factories;
    }

    private static JSONArrayList<IAlertService> extractAlertServices(JSONArrayList<IModule> modules) throws Exception {
        JSONArrayList<IAlertService> result = new JSONArrayList<>();
        for (IModule module : modules) {
            module.addAlertService(result);
        }

        return result;
    }

    private static JSONArrayList<IMonitorable> extractMonitorables(JSONArrayList<IModule> modules) throws Exception {
        JSONArrayList<IMonitorable> result = new JSONArrayList<>();
        for (IModule module : modules) {
            module.addMonitorable(result);
        }

        return result;
    }

    private static ArrayList<IConfigurationValidator> extractConfigurationValidators(ArrayList<IModule> modules) {
        JSONArrayList<IConfigurationValidator> result = new JSONArrayList<>();
        for (IModule module : modules) {
            addToCollectionIfNotNull(module.createConfigurationValidator(), result);
        }

        return result;
    }

    private static ArrayList<Collector> extractCollectors(ArrayList<IModule> modules) {
        JSONArrayList<Collector> result = new JSONArrayList<>();
        for (IModule module : modules) {
            addToCollectionIfNotNull(module.createCollector(), result);
        }

        return result;
    }

    private static ArrayList<DependenciesCheckerModule> extractDependenciesCheckers(ArrayList<IModule> modules) {
        JSONArrayList<DependenciesCheckerModule> result = new JSONArrayList<>();
        for (IModule module : modules) {
            addToCollectionIfNotNull(module.createDependenciesCheckerModule(), result);
        }

        return result;
    }

    private static <T> void addToCollectionIfNotNull(T obj, ArrayList<T> collection) {
        if (obj != null) {
            collection.add(obj);
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
