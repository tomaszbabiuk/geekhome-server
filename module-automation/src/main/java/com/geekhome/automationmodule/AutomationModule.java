package com.geekhome.automationmodule;

import com.geekhome.automationmodule.automation.AutomationAutomationModule;
import com.geekhome.automationmodule.httpserver.AutomationJsonRequestsDispatcher;
import com.geekhome.common.automation.IAutomationModule;
import com.geekhome.common.configuration.Collector;
import com.geekhome.common.configuration.MasterConfiguration;
import com.geekhome.common.settings.AutomationSettings;
import com.geekhome.common.localization.Resource;
import com.geekhome.common.automation.MasterAutomation;
import com.geekhome.coremodule.httpserver.CrudPostHandler;
import com.geekhome.common.hardwaremanager.IHardwareManager;
import com.geekhome.coremodule.httpserver.IRequestsDispatcher;
import com.geekhome.coremodule.httpserver.ICrudPostHandler;
import com.geekhome.common.localization.ILocalizationProvider;
import com.geekhome.coremodule.jetty.RedirectionResponse;
import com.geekhome.coremodule.modules.*;
import com.geekhome.coremodule.modules.Module;

import java.util.ArrayList;

public class AutomationModule extends Module {

    private final MasterConfiguration _masterConfiguration;
    private final MasterAutomation _masterAutomation;
    private final IHardwareManager _hardwareManager;
    private final ILocalizationProvider _localizationProvider;
    private final AutomationSettings _automationSettings;
    private final AutomationConfiguration _automationConfiguration;

    public AutomationModule(final ILocalizationProvider localizationProvider,
                            final MasterConfiguration masterConfiguration,
                            final MasterAutomation masterAutomation,
                            final IHardwareManager hardwareManager,
                            final AutomationSettings automationSettings) throws Exception {
        _masterConfiguration = masterConfiguration;
        _masterAutomation = masterAutomation;
        _hardwareManager = hardwareManager;
        _localizationProvider = localizationProvider;
        _automationSettings = automationSettings;
        _automationConfiguration = new AutomationConfiguration(masterConfiguration.getPool(), masterConfiguration, automationSettings);
    }

    @Override
    public ArrayList<IUnit> createUnits() {
        ArrayList<IUnit> units = new ArrayList<>();
        units.add(new Unit(UnitCategory.ConfigurationDevices, _localizationProvider.getValue("AUTO:OnOffDevices"), "power", "/config/onoffdevices.htm"));
        units.add(new Unit(UnitCategory.ConfigurationDevices, _localizationProvider.getValue("AUTO:IntensityDevices"), "brightness", "/config/intensitydevices.htm"));
        units.add(new Unit(UnitCategory.ConfigurationDevices, _localizationProvider.getValue("AUTO:ImpulseSwitches"), "pulse", "/config/impulseswitches.htm"));
        units.add(new Unit(UnitCategory.ConfigurationDevices, _localizationProvider.getValue("AUTO:PowerMeters"), "meter", "/config/powermeters.htm"));
        units.add(new Unit(UnitCategory.ConfigurationConditions, _localizationProvider.getValue("AUTO:NfcConditions"), "smartphone", "/config/nfcconditions.htm"));
        return units;
    }

    @Override
    public Resource[] getResources() {
        return new Resource[] {
                new Resource("AUTO:OnOffDevice", "On/off device", "Urządzenie wł/wył"),
                new Resource("AUTO:OnOffDeviceDetails", "On/off device details", "Szczegóły urzadzenia wł/wył"),
                new Resource("AUTO:OnOffDevices", "On/off devices", "Urządzenia wł/wył"),
                new Resource("AUTO:AddOnOffDevice", "Add on/off device", "Dodaj urządzenie wł/wył"),
                new Resource("AUTO:AddImpulseSwitch", "Add impulse switch", "Dodaj przełącznik impulsowy"),
                new Resource("AUTO:TimeLeft", "Time left", "Pozostało"),
                new Resource("AUTO:ImpulseSwitch", "Impulse switch", "Przełącznik impulsowy"),
                new Resource("AUTO:ImpulseSwitchDetails", "Impulse switch details", "Szczegóły przełącznika impulsowego"),
                new Resource("AUTO:ImpulseSwitches", "Impulse switches", "Przełączniki impulsowe"),
                new Resource("AUTO:ImpulseTime", "Impulse time", "Czas impulsu"),
                new Resource("AUTO:NfcCondition", "NFC condition", "Tag NFC"),
                new Resource("AUTO:NfcConditionDetails", "NFC condition details", "Szczegóły warunku NFC"),
                new Resource("AUTO:NfcConditions", "NFC conditions", "Warunki NFC"),
                new Resource("AUTO:AddNfcCondition", "Add NFC condition", "Dodaj warunek NFC"),
                new Resource("AUTO:Tag", "Tag", "Tag"),
                new Resource("AUTO:Icon.Power", "Power", "Zasilanie"),
                new Resource("AUTO:Icon.LightBulb", "Lightbulb", "Żarówka"),
                new Resource("AUTO:Icon.EcoFunction", "Eco-function", "Funkcja 'eko'"),
                new Resource("AUTO:Icon.Snowflake", "Snowflake", "Płatek śniegu"),
                new Resource("AUTO:Icon.Valve", "Valve", "Zawór"),
                new Resource("AUTO:Icon.Impeller", "Impeller", "Wirnik"),
                new Resource("AUTO:Icon.Fan", "Fan", "Wentylator"),
                new Resource("AUTO:Icon.LED", "LED", "LED"),
                new Resource("AUTO:Icon.DeskLamp", "Desk lamp", "Lampa stołowa"),
                new Resource("AUTO:Icon.LightBulb", "Light bulb", "Żarówka"),
                new Resource("AUTO:Icon.StandingLamp", "Standing lamp", "Lampa stojąca"),
                new Resource("AUTO:Icon.HangingLamp", "Hanging lamp", "Lampa wisząca"),
                new Resource("AUTO:BlocksEnablingDevice", "Blocks enablind device", "Bloki włączające urządzenie"),
                new Resource("AUTO:AddIntensityDevice", " Add intensity device", "Dodaj urządzenie z regulacją mocy"),
                new Resource("AUTO:IntensityDevice", "Intensity device", "Urządzenia z regulacją mocy"),
                new Resource("AUTO:IntensityDeviceDetails", "Intensity device details", "Szczegóły urządzenia z regulacją mocy"),
                new Resource("AUTO:IntensityDevices", "Intensity device", "Urządzenia z regulacją mocy"),
                new Resource("AUTO:PowerMeters", "Power meters", "Mierniki mocy"),
                new Resource("AUTO:PowerMeter", "Power meter", "Miernik mocy"),
                new Resource("AUTO:AddPowerMeter", "Add power meter", "Dodaj miernik mocy"),
                new Resource("AUTO:PowerMeterDetails", "Power meter details", "Szczegóły miernika mocy"),
                new Resource("AUTO:CannotAddPowerMetersMessage", "Cannot add any power meter since there's no rooms defined yet or there's no power meter port available!", "Aby dodawać nowe mierniki prądu musi być zdefiniowany conajmniej jeden port pomiaru prądu!"),
        };
    }

    @Override
    public String getTextResourcesPrefix() {
        return "AUTO";
    }

    @Override
    public ArrayList<IRequestsDispatcher> createDispatchers() {
        ArrayList<IRequestsDispatcher> dispatchers = new ArrayList<>();
        dispatchers.add(new AutomationJsonRequestsDispatcher(_automationConfiguration));
        return dispatchers;
    }

    @Override
    public Collector createCollector() {
        return _automationConfiguration;
    }

    @Override
    public IAutomationModule createAutomationModule() {
        return new AutomationAutomationModule(_masterAutomation, _automationConfiguration, _hardwareManager, _localizationProvider, _automationSettings);
    }

    @Override
    public String[] listConsolidatedJavascripts() {
        return new String[]
                {
                        "JS\\AUTOMATIONCONFIG.JS",
                };
    }

    @Override
    public String[] listConsolidatedStyleSheets() {
        return new String[]
                {
                        "CSS\\AUTOMATIONNAVIGATION.CSS",
                };
    }

    @Override
    public ArrayList<ICrudPostHandler> createCrudPostHandlers() {
        ArrayList<ICrudPostHandler> handlers = new ArrayList<>();

        CrudPostHandler onOffDevicesHandler = new CrudPostHandler(_masterConfiguration, "ONOFFDEVICE",
                (action, values) -> _automationConfiguration.modifyOnOffDevice(action, values), new RedirectionResponse("/config/OnOffDevices.htm"));
        handlers.add(onOffDevicesHandler);

        CrudPostHandler impulseSwitchesHandler = new CrudPostHandler(_masterConfiguration, "IMPULSESWITCH",
                (action, values) -> _automationConfiguration.modifyImpulseSwitch(action, values), new RedirectionResponse("/config/ImpulseSwitches.htm"));
        handlers.add(impulseSwitchesHandler);

        CrudPostHandler nfcConditionsHandler = new CrudPostHandler(_masterConfiguration, "NFCCONDITION",
                (action, values) -> _automationConfiguration.modifyNfcCondition(action, values), new RedirectionResponse("/config/NfcConditions.htm"));
        handlers.add(nfcConditionsHandler);

        CrudPostHandler intensityDevicesHandler = new CrudPostHandler(_masterConfiguration, "INTENSITYDEVICE",
                _automationConfiguration::modifyIntensityDevice, new RedirectionResponse("/config/IntensityDevices.htm"));
        handlers.add(intensityDevicesHandler);

        CrudPostHandler powerMetersHandler = new CrudPostHandler(_masterConfiguration, "POWERMETER",
                _automationConfiguration::modifyPowerMeter, new RedirectionResponse("/config/PowerMeters.htm"));
        handlers.add(powerMetersHandler);

        return handlers;
    }
}

