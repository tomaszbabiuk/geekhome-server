package com.geekhome;

import com.geekhome.alarmmodule.AlarmModule;
import com.geekhome.automationmodule.AutomationModule;
import com.geekhome.centralheatingmodule.CentralHeatingModule;
import com.geekhome.common.hardwaremanager.HardwareManager;
import com.geekhome.common.commands.CommandsProcessor;
import com.geekhome.common.commands.Synchronizer;
import com.geekhome.common.configuration.JSONArrayList;
import com.geekhome.common.utils.FileFinder;
import com.geekhome.common.utils.IFileFoundListener;
import com.geekhome.coremodule.CoreModule;
import com.geekhome.common.alerts.DashboardAlertService;
import com.geekhome.common.configuration.MasterConfiguration;
import com.geekhome.emailmodule.EmailModule;
import com.geekhome.extafreemodule.ExtaFreeModule;
import com.geekhome.hardwaremanagermodule.HardwareManagerModule;
import com.geekhome.common.localization.Resource;
import com.geekhome.common.localization.ResourceLocalizationProvider;
import com.geekhome.common.automation.MasterAutomation;
import com.geekhome.common.settings.AutomationSettings;
import com.geekhome.common.settings.TextFileAutomationSettingsPersister;
import com.geekhome.common.hardwaremanager.IHardwareManager;
import com.geekhome.common.localization.ILocalizationProvider;
import com.geekhome.common.OperationMode;
import com.geekhome.common.automation.SystemInfo;
import com.geekhome.coremodule.modules.IModule;
import com.geekhome.coremodule.modules.NavigationTree;
import com.geekhome.lightsmodule.LightsModule;
import com.geekhome.mqttmodule.MqttModule;
import com.geekhome.onewiremodule.OneWireModule;
import com.geekhome.pi4jmodule.Pi4JModule;
import com.geekhome.usersmodule.UsersModule;
import com.geekhome.ventilationmodule.VentilationModule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;


public class ResourcesCleanupTool {

    private static int _warnings;

    public static void main(String[] args)
            throws IOException {

        try {
            TextFileAutomationSettingsPersister settingsPersister = new TextFileAutomationSettingsPersister();
            AutomationSettings automationSettings = new AutomationSettings(settingsPersister);
            ILocalizationProvider localizationProvider = new ResourceLocalizationProvider();
            DashboardAlertService dashboardAlertService = new DashboardAlertService(localizationProvider);
            HardwareManager hardwareManager = new HardwareManager(dashboardAlertService);
            SystemInfo systemInfo = new SystemInfo(OperationMode.Diagnostics, dashboardAlertService);
            NavigationTree navigationTree = new NavigationTree();
            MasterConfiguration masterConfiguration = new MasterConfiguration(localizationProvider);
            MasterAutomation masterAutomation = new MasterAutomation(masterConfiguration, hardwareManager,
                    systemInfo, dashboardAlertService);
            CommandsProcessor commandsProcessor = new CommandsProcessor(systemInfo, masterConfiguration,
                    masterAutomation, localizationProvider);
            Synchronizer synchronizer = new Synchronizer(masterConfiguration, masterAutomation, automationSettings,
                    localizationProvider, systemInfo, commandsProcessor, dashboardAlertService);
            JSONArrayList<IModule> modules = buildModules(hardwareManager, automationSettings,
                    localizationProvider, systemInfo, masterConfiguration, masterAutomation,
                    synchronizer, commandsProcessor, dashboardAlertService, navigationTree);

            _warnings = 0;
            for (IModule module : modules) {
                System.out.println();
                System.out.print(String.format("Checking module %s", module.getTextResourcesPrefix()));
                Resource[] resources = module.getResources();
                for (Resource resource : resources) {
                    final Integer[] foundCounter = {0};
                    final String key = resource.getKey();
                    if (!key.contains(":")) {
                        System.out.println(String.format("[SKIPPING] Key %s is not assigned to specific module.", key));
                    } else {
                        final ArrayList<String> foundInModules = new ArrayList<>();
                        final ITextInFileFoundListener textInFileFound = new ITextInFileFoundListener() {
                            @Override
                            public void textInFileFound(String text, String path, int lineNum) {
                                foundCounter[0] = foundCounter[0] + 1;
                                String module = ResourcesCleanupTool.extractModuleFromPath(path);
                                if (!foundInModules.contains(module) && (!path.endsWith("Module.java"))) {
                                    foundInModules.add(module);
                                }
                            }
                        };

                        FileFinder classFinder = new FileFinder("*.java", new IFileFoundListener() {
                            @Override
                            public void fileFound(Path file) {
                                final File f = file.toFile();
                                ResourcesCleanupTool.findInFile(f, "\"" + key + "\"", textInFileFound);
                            }
                        });

                        FileFinder htmlFinder = new FileFinder("*.{htm,js}", new IFileFoundListener() {
                            @Override
                            public void fileFound(Path file) {
                                final File f = file.toFile();
                                ResourcesCleanupTool.findInFile(f, "RES:" + key, textInFileFound);
                            }
                        });
                        Files.walkFileTree(Paths.get("."), classFinder);
                        Files.walkFileTree(Paths.get("."), htmlFinder);

                        String moduleByKey = key.substring(0, key.indexOf(":"));
                        String moduleBySource = module.getTextResourcesPrefix();
                        if (!moduleByKey.equals(moduleBySource)) {
                            reportFail(String.format("[FAIL] Key %s is declared in wrong module (%s but should be %s)", key, moduleBySource, moduleByKey));
                        } else if (foundInModules.size() == 0) {
                            if (foundCounter[0] == 1) {
                                reportFail(String.format("[FAIL] Key %s is not used!", key));
                            }
                        } else if (foundInModules.size() == 1) {
                            //if it's assigned to correct module - it's ok
                            System.out.print(".");
                        } else {
                            if (!moduleByKey.equals("C")) {
                                reportFail(String.format("[FAIL] Key %s is used in %s modules!", key, foundInModules.size()));
                            }
                        }
                    }
                }
            }

            System.out.println();
            System.out.println(String.format("DONE: %s warnings", _warnings));

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private static void reportFail(String format) {
        System.out.println();
        System.out.println(format);
        _warnings++;
    }

    private static String extractModuleFromPath(String path) {
        int stop = path.indexOf("/", 2);
        return path.substring(2, stop);
    }

    private static void findInFile(File file, String text, ITextInFileFoundListener listener) {
        try {
            Scanner scanner = new Scanner(file);

            //now read the file line by line...
            int lineNum = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineNum++;
                if (line.contains(text)) {
                    listener.textInFileFound(text, file.getPath(), lineNum);
                }
            }
        } catch (FileNotFoundException e) {
            //handle this
        }
    }

    private static JSONArrayList<IModule> buildModules(IHardwareManager hardwareManager, AutomationSettings automationSettings,
                                                       ILocalizationProvider localizationProvider, SystemInfo systemInfo,
                                                       MasterConfiguration masterConfiguration, MasterAutomation masterAutomation,
                                                       Synchronizer synchronizer, CommandsProcessor commandsProcessor,
                                                       DashboardAlertService dashboardAlertService,
                                                       NavigationTree navigationTree) throws Exception {
        JSONArrayList<IModule> modules = new JSONArrayList<>();
        modules.add(new CoreModule(localizationProvider, systemInfo, masterConfiguration, masterAutomation, hardwareManager,
                automationSettings, synchronizer, dashboardAlertService, navigationTree));
        modules.add(new UsersModule(localizationProvider));
        modules.add(new HardwareManagerModule(true, localizationProvider, hardwareManager));
        modules.add(new LightsModule(localizationProvider, masterConfiguration, hardwareManager, automationSettings));
        modules.add(new AlarmModule(localizationProvider, masterConfiguration, masterAutomation, hardwareManager));
        modules.add(new CentralHeatingModule(localizationProvider, masterConfiguration, masterAutomation, hardwareManager, automationSettings));
        modules.add(new ExtaFreeModule(localizationProvider, hardwareManager, masterConfiguration));
        modules.add(new AutomationModule(localizationProvider, masterConfiguration, masterAutomation, hardwareManager, automationSettings));
        modules.add(new VentilationModule(localizationProvider, masterConfiguration, hardwareManager));
        modules.add(new OneWireModule(localizationProvider, hardwareManager, automationSettings));
        modules.add(new EmailModule(localizationProvider, masterAutomation, automationSettings, commandsProcessor));
        modules.add(new Pi4JModule(localizationProvider));
        modules.add(new MqttModule(localizationProvider, hardwareManager, new String[0]));
        return modules;
    }
}
