package com.geekhome;

import com.geekhome.alarmmodule.AlarmModule;
import com.geekhome.automationmodule.AutomationModule;
import com.geekhome.centralheatingmodule.CentralHeatingModule;
import com.geekhome.common.configuration.JSONArrayList;
import com.geekhome.coremodule.CoreModule;
import com.geekhome.emailmodule.EmailModule;
import com.geekhome.extafreemodule.ExtaFreeModule;
import com.geekhome.hardwaremanagermodule.HardwareManagerModule;
import com.geekhome.coremodule.jetty.HomeServerStarter;
import com.geekhome.coremodule.modules.IModule;
import com.geekhome.lightsmodule.LightsModule;
import com.geekhome.onewiremodule.OneWireModule;
import com.geekhome.shellymodule.GreeModule;
import com.geekhome.shellymodule.ShellyModule;
import com.geekhome.usersmodule.UsersModule;
import com.geekhome.ventilationmodule.VentilationModule;
import com.geekhome.firebasemodule.FirebaseModule;

public class Main {
    public static void main(String[] args) {
        final int port = HomeServerStarter.extractPortFromArgs(args);

        HomeServerStarter.start(port, (hardwareManager, automationSettings, localizationProvider, systemInfo, masterConfiguration,
                                       masterAutomation, synchronizer, commandsProcessor, dashboardAlertService, mqttBroker, navigationTree) -> {
            JSONArrayList<IModule> modules = new JSONArrayList<>();
            modules.add(new FullEditionAdaptationModule());
            modules.add(new CoreModule(localizationProvider, systemInfo, masterConfiguration, masterAutomation, hardwareManager,
                    automationSettings, synchronizer, dashboardAlertService, navigationTree));
            modules.add(new UsersModule(localizationProvider));
            modules.add(new HardwareManagerModule(false, localizationProvider, hardwareManager));
            modules.add(new LightsModule(localizationProvider, masterConfiguration, hardwareManager, automationSettings));
            modules.add(new AlarmModule(localizationProvider, masterConfiguration, masterAutomation, hardwareManager));
            modules.add(new CentralHeatingModule(localizationProvider, masterConfiguration, masterAutomation, hardwareManager, automationSettings));
//            modules.add(new ExtaFreeModule
//                    (localizationProvider, hardwareManager, masterConfiguration));
            modules.add(new AutomationModule(localizationProvider, masterConfiguration, masterAutomation, hardwareManager, automationSettings));
            modules.add(new VentilationModule(localizationProvider, masterConfiguration, hardwareManager));
            modules.add(new OneWireModule(localizationProvider, hardwareManager, automationSettings));
//            modules.add(new EmailModule(localizationProvider, masterAutomation, automationSettings, commandsProcessor));
//            modules.add(new FirebaseModule(localizationProvider, automationSettings, synchronizer, masterAutomation));
//            modules.add(new MqttModule(localizationProvider, hardwareManager, args));
//            modules.add(new GreeModule(hardwareManager, localizationProvider));
//            modules.add(new ShellyModule(mqttBroker));
            return modules;
        });
    }
}
