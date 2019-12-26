package com.geekhome.lightsmodule;


import com.geekhome.common.automation.IAutomationModule;
import com.geekhome.common.configuration.Collector;
import com.geekhome.common.configuration.DependenciesCheckerModule;
import com.geekhome.common.configuration.IConfigurationValidator;
import com.geekhome.common.configuration.MasterConfiguration;
import com.geekhome.common.settings.AutomationSettings;
import com.geekhome.http.Resource;
import com.geekhome.coremodule.httpserver.CrudPostHandler;
import com.geekhome.common.hardwaremanager.IHardwareManager;
import com.geekhome.http.IRequestsDispatcher;
import com.geekhome.httpserver.ICrudPostHandler;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.http.jetty.RedirectionResponse;
import com.geekhome.httpserver.modules.*;
import com.geekhome.httpserver.modules.Module;
import com.geekhome.lightsmodule.automation.LightsAutomationModule;
import com.geekhome.lightsmodule.httpserver.LightsJsonRequestsDispatcher;

import java.util.ArrayList;

public class LightsModule extends Module {
    private MasterConfiguration _masterConfiguration;
    private IHardwareManager _hardwareManager;
    private ILocalizationProvider _localizationProvider;
    private final AutomationSettings _automationSettings;
    private LightsConfiguration _lightsConfiguration;

    public LightsModule(final ILocalizationProvider localizationProvider,
                        final MasterConfiguration masterConfiguration,
                        final IHardwareManager hardwareManager,
                        final AutomationSettings automationSettings) {
        _masterConfiguration = masterConfiguration;
        _hardwareManager = hardwareManager;
        _localizationProvider = localizationProvider;
        _automationSettings = automationSettings;
        _lightsConfiguration = new LightsConfiguration(masterConfiguration.getPool(), automationSettings);
    }

    @Override
    public ArrayList<IUnit> createUnits() {
        ArrayList<IUnit> units = new ArrayList<>();
        units.add(new Unit(UnitCategory.ConfigurationDevices, _localizationProvider.getValue("LIGHTS:Blinds"), "blinds", "/config/blinds.htm"));
        units.add(new Unit(UnitCategory.ConfigurationDevices, _localizationProvider.getValue("LIGHTS:RgbLamps"), "rgblamp", "/config/rgblamps.htm"));
        units.add(new Unit(UnitCategory.ConfigurationDevices, _localizationProvider.getValue("LIGHTS:7ColorLamps"), "rgblamp", "/config/7colorlamps.htm"));
        units.add(new Unit(UnitCategory.ConfigurationDevices, _localizationProvider.getValue("LIGHTS:Luxmeters"), "luminosity", "/config/luxmeters.htm"));
        units.add(new Unit(UnitCategory.ConfigurationConditions, _localizationProvider.getValue("LIGHTS:TwilightConditions"), "moon", "/config/twilightconditions.htm"));
        return units;
    }

    @Override
    public Resource[] getResources() {
        return new Resource[]{
                new Resource("LIGHTS:Blind", "Blind", "Roleta"),
                new Resource("LIGHTS:Blinds", "Blinds", "Rolety"),
                new Resource("LIGHTS:AddBlind", "Add blind", "Dodaj roletę"),
                new Resource("LIGHTS:BlindDetails", "Blind details", "Szczegóły rolety"),
                new Resource("LIGHTS:AddTwilightCondition", "Add twilight condition", "Dodaj warunek zmierzchowy"),
                new Resource("LIGHTS:TwilightCondition", "Twilight condition", "Warunek zmierzchowy"),
                new Resource("LIGHTS:TwilightConditionDetails", "Twilight condition details", "Szczegóły warunku zmierzchowego"),
                new Resource("LIGHTS:TwilightConditions", "Twilight conditions", "Warunki zmierzchowe"),
                new Resource("LIGHTS:White", "White", "Biały"),
                new Resource("LIGHTS:Red", "Red", "Czerwony"),
                new Resource("LIGHTS:Green", "Green", "Zielony"),
                new Resource("LIGHTS:Blue", "Blue", "Niebieski"),
                new Resource("LIGHTS:Cyan", "Cyan", "Cyjan"),
                new Resource("LIGHTS:Magenta", "Magenta", "Karmazynowy"),
                new Resource("LIGHTS:Yellow", "Yellow", "Żółty"),
                new Resource("LIGHTS:BlocksEnablingWhiteColor", "Blocks that enable white color", "Bloki włączające kolor biały"),
                new Resource("LIGHTS:BlocksEnablingRedColor", "Blocks that enable red color", "Bloki włączające kolor czerwony"),
                new Resource("LIGHTS:BlocksEnablingGreenColor", "Blocks that enable green color", "Bloki włączające kolor zielony"),
                new Resource("LIGHTS:BlocksEnablingBlueColor", "Blocks that enable blue color", "Bloki włączające kolor niebieski"),
                new Resource("LIGHTS:BlocksEnablingCyanColor", "Blocks that enable cyan color", "Bloki włączające kolor cyjanowy"),
                new Resource("LIGHTS:BlocksEnablingMagentaColor", "Blocks that enable magenta color", "Bloki włączające kolor karmazynowy"),
                new Resource("LIGHTS:BlocksEnablingYellowColor", "Blocks that enable yellow color", "Bloki włączające kolor żółty"),
                new Resource("LIGHTS:RedChannelPort", "Red channel port", "Port kanału czerwonego"),
                new Resource("LIGHTS:GreenChannelPort", "Green channel port", "Port kanału zielonego"),
                new Resource("LIGHTS:BlueChannelPort", "Blue channel port", "Port kanału niebieskiego"),
                new Resource("LIGHTS:AddRgbLamp", " Add rgb lamp", "Dodaj lampę rgb"),
                new Resource("LIGHTS:RgbLamp", "Rgb lamp", "Lampa rgb"),
                new Resource("LIGHTS:RgbLampDetails", "Rgb lamp details", "Szczegóły lampy rgb"),
                new Resource("LIGHTS:RgbLamps", "Rgb lamps", "Lampy rgb"),
                new Resource("LIGHTS:RgbLampPortsAreSame", "Rgb lamp '%s' is using duplicated ports. Make sure that ports of red, green and blue channel are different!",
                        "Lampa rgb '%s' używa zduplikowanych portów. Upewnij się, że porty kanałów: czerwonego, zielonego oraz niebieskiego są różne!"),
                new Resource("LIGHTS:Add7ColorLamp", " Add 7-color lamp", "Dodaj lampę 7-kolorową"),
                new Resource("LIGHTS:7ColorLamp", "7-color lamp", "Lampa 7-kolorowa"),
                new Resource("LIGHTS:7ColorLampDetails", "7-color lamp details", "Szczegóły lampy 7-kolorowej"),
                new Resource("LIGHTS:7ColorLamps", "7-color lamps", "Lampy 7-kolorowe"),
                new Resource("LIGHTS:7ColorLampPortsAreSame", "7-Color lamp '%s' is using duplicated ports. Make sure that ports of red, green and blue channel are different!",
                        "Lampa 7-kolorowa '%s' używa zduplikowanych portów. Upewnij się, że porty kanałów: czerwonego, zielonego oraz niebieskiego są różne!"),
                new Resource("LIGHTS:ComfortPosition", "Comfort position", "Pozycja komfortowa"),
                new Resource("LIGHTS:MotorPort", "Motor port", "Port silnika"),
                new Resource("LIGHTS:AutomaticControlPort", "Automatic control port", "Port automatycznej kontroli"),
                new Resource("LIGHTS:ManualSwitch", "Manual switch", "Przełącznik ręczny"),
                new Resource("LIGHTS:BlindPortsAreSame", "Blind '%s' is using duplicated ports. Make sure that motor port and automatic control port are different!",
                        "Roleta '%s' używa zduplikowanych portów. Upewnij się, że port silnika oraz port automatycznej kontroli są różne!"),
                new Resource("LIGHTS:BlocksOpeningBlind", "Blocks that open the blind", "Bloki otwierające roletę"),
                new Resource("LIGHTS:BlocksClosingBlind", "Blocks that close the blind", "Bloki zamykające roletę"),
                new Resource("LIGHTS:BlocksEnablingComfortPosition", "Blocks enabling comfort position", "Bloki włączające pozycję komfortową"),
                new Resource("LIGHTS:ClosingTime", "Closing time", "Czas zamykania"),
                new Resource("LIGHTS:ComfortSetTime", "Comfort set time", "Czas ustalania pozycji komfortowej"),
                new Resource("LIGHTS:PositionReset", "Position reset", "Ustalanie pozycji"),
                new Resource("LIGHTS:BlindOpen", "Open", "Otwarta"),
                new Resource("LIGHTS:BlindClosed", "Closed", "Zamknięta"),
                new Resource("LIGHTS:Luxmeters", "Luxmeters", "Luksometry"),
                new Resource("LIGHTS:Luxmeter", "Luxmeter", "Luksometr"),
                new Resource("LIGHTS:AddLuxmeter", "Add luxmeter", "Dodaj luksometr"),
                new Resource("LIGHTS:LuxmeterDetails", "Luxmeter details", "Szczegóły luksometru"),
        };
    }

    @Override
    public String getTextResourcesPrefix() {
        return "LIGHTS";
    }

    @Override
    public ArrayList<IRequestsDispatcher> createDispatchers() {
        ArrayList<IRequestsDispatcher> dispatchers = new ArrayList<>();
        dispatchers.add(new LightsJsonRequestsDispatcher(_lightsConfiguration));
        return dispatchers;
    }

    @Override
    public DependenciesCheckerModule createDependenciesCheckerModule() {
        return new LightsDependenciesCheckerModule(_masterConfiguration, _lightsConfiguration);
    }

    @Override
    public IConfigurationValidator createConfigurationValidator() {
        return new LightsConfigurationValidator(_localizationProvider, _lightsConfiguration);
    }

    @Override
    public Collector createCollector() {
        return _lightsConfiguration;
    }

    @Override
    public IAutomationModule createAutomationModule() {
        return new LightsAutomationModule(_lightsConfiguration, _hardwareManager, _localizationProvider,
                _automationSettings);
    }

    @Override
    public String[] listConsolidatedJavascripts() {
        return new String[]
                {
                        "JS\\LIGHTS.RESOURCES.JS",
                        "JS\\LIGHTSCONFIG.JS",
                };
    }

    @Override
    public String[] listConsolidatedStyleSheets() {
        return new String[]
                {
                        "CSS\\LIGHTSNAVIGATION.CSS",
                };
    }

    @Override
    public ArrayList<ICrudPostHandler> createCrudPostHandlers() {
        ArrayList<ICrudPostHandler> handlers = new ArrayList<>();

        CrudPostHandler twilightConditions = new CrudPostHandler(_masterConfiguration, "TWILIGHTCONDITION",
                _lightsConfiguration::modifyTwilightCondition, new RedirectionResponse("/config/TwilightConditions.htm"));
        handlers.add(twilightConditions);

        CrudPostHandler blindsHandler = new CrudPostHandler(_masterConfiguration, "BLIND",
                _lightsConfiguration::modifyBlind, new RedirectionResponse("/config/Blinds.htm"));
        handlers.add(blindsHandler);

        CrudPostHandler rgbLampsHandler = new CrudPostHandler(_masterConfiguration, "RGBLAMP",
                _lightsConfiguration::modifyRgbLamp, new RedirectionResponse("/config/RgbLamps.htm"));
        handlers.add(rgbLampsHandler);

        CrudPostHandler sevenColorLampsHandler = new CrudPostHandler(_masterConfiguration, "7COLORLAMP",
                _lightsConfiguration::modify7ColorLamp, new RedirectionResponse("/config/7ColorLamps.htm"));
        handlers.add(sevenColorLampsHandler);

        CrudPostHandler luxmetersHandler = new CrudPostHandler(_masterConfiguration, "LUXMETER",
                _lightsConfiguration::modifyLuxmeter, new RedirectionResponse("/config/Luxmeters.htm"));
        handlers.add(luxmetersHandler);

        return handlers;
    }
}