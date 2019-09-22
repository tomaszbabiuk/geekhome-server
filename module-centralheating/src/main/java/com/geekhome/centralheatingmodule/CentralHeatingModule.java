package com.geekhome.centralheatingmodule;

import com.geekhome.centralheatingmodule.automation.CentralHeatingAutomationModule;
import com.geekhome.centralheatingmodule.httpserver.CentralHeatingJsonRequestsDispatcher;
import com.geekhome.common.CrudAction;
import com.geekhome.coremodule.MasterConfiguration;
import com.geekhome.http.Resource;
import com.geekhome.coremodule.automation.MasterAutomation;
import com.geekhome.coremodule.httpserver.CrudPostHandler;
import com.geekhome.coremodule.settings.AutomationSettings;
import com.geekhome.hardwaremanager.IHardwareManager;
import com.geekhome.http.INameValueSet;
import com.geekhome.http.IRequestsDispatcher;
import com.geekhome.httpserver.ICrudPostHandler;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.http.jetty.RedirectionResponse;
import com.geekhome.httpserver.modules.*;
import com.geekhome.httpserver.modules.Module;

import java.util.ArrayList;

public class CentralHeatingModule extends Module {
    private ILocalizationProvider _localizationProvider;
    private MasterConfiguration _masterConfiguration;
    private MasterAutomation _masterAutomation;
    private IHardwareManager _hardwareManager;
    private AutomationSettings _automationsSettings;

    private CentralHeatingConfiguration _centralHeatingConfiguration;

    public CentralHeatingModule(ILocalizationProvider localizationProvider, MasterConfiguration masterConfiguration,
                                MasterAutomation masterAutomation, IHardwareManager hardwareManager, AutomationSettings automationsSettings) throws Exception {
        _localizationProvider = localizationProvider;
        _masterConfiguration = masterConfiguration;
        _masterAutomation = masterAutomation;
        _hardwareManager = hardwareManager;
        _automationsSettings = automationsSettings;
        _centralHeatingConfiguration = new CentralHeatingConfiguration(masterConfiguration.getPool());
    }

    @Override
    public ArrayList<IUnit> createUnits() {
        ArrayList<IUnit> units = new ArrayList<>();
        units.add(new Unit(UnitCategory.ConfigurationDevices, _localizationProvider.getValue("CH:Thermometers"), "thermometer", "/config/thermometers.htm"));
        units.add(new Unit(UnitCategory.ConfigurationDevices, _localizationProvider.getValue("CH:AveragingThermometers"), "averagingthermometer", "/config/averagingthermometers.htm"));
        units.add(new Unit(UnitCategory.ConfigurationDevices, _localizationProvider.getValue("CH:Comfortmeters"), "comfortmeter", "/config/comfortmeters.htm"));
        units.add(new Unit(UnitCategory.ConfigurationDevices, _localizationProvider.getValue("CH:Hygrometers"), "hygrometer", "/config/hygrometers.htm"));
        units.add(new Unit(UnitCategory.ConfigurationDevices, _localizationProvider.getValue("CH:TemperatureControllers"), "thermostat", "/config/temperaturecontrollers.htm"));
        units.add(new Unit(UnitCategory.ConfigurationDevices, _localizationProvider.getValue("CH:HeatingManifolds"), "sun", "/config/heatingmanifolds.htm"));
        units.add(new Unit(UnitCategory.ConfigurationDevices, _localizationProvider.getValue("CH:CirculationPumps"), "watertreatmentplant", "/config/circulationpumps.htm"));
        units.add(new Unit(UnitCategory.ConfigurationDevices, _localizationProvider.getValue("CH:Radiators"), "radiator", "/config/radiators.htm"));
        units.add(new Unit(UnitCategory.ConfigurationDevices, _localizationProvider.getValue("CH:UnderfloorCircuits"), "heat", "/config/underfloorcircuits.htm"));
        units.add(new Unit(UnitCategory.ConfigurationDevices, _localizationProvider.getValue("CH:AirConditioners"), "snow", "/config/airconditioners.htm"));
//        units.add(new Unit(UnitCategory.ConfigurationDevices, _localizationProvider.getValue("CH:RtlCircuits"), "rtl", "/config/rtlcircuits.htm"));
        units.add(new Unit(UnitCategory.ConfigurationConditions, _localizationProvider.getValue("CH:ThermostatConditions"), "thermostat", "/config/thermostatconditions.htm"));
        return units;
    }

    @Override
    public Resource[] getResources() {
        return new Resource[] {
                new Resource("CH:ActivationTime", "Activation time", "Czas aktywacji"),
                new Resource("CH:ActuatorPort", "Actuator's port", "Port siłownika"),
                new Resource("CH:ActuatorsTransformerPort", "Actuators transformer's port", "Port transformatora termosiłowników"),
                new Resource("CH:AddAveragingThermometer", "Add averaging thermometer", "Dodaj termometr uśredniający"),
                new Resource("CH:AddHeatingManifold", "Add central heating pump", "Dodaj rozdzielacz c.o."),
                new Resource("CH:AddRadiator", "Add radiator", "Dodaj grzejnik"),
                new Resource("CH:AddRtlCircuit", "Add rtl circuit", "Dodaj obieg RTL"),
                new Resource("CH:AddUnderfloorCircuit", "Add underfloor circuit", "Dodaj obieg podłogówki"),
                new Resource("CH:AmbientTemperature", "Ambient temperature", "Temperatura otoczenia"),
                new Resource("CH:AmbientThermometer", "Ambient thermometer", "Termometr otoczenia"),
                new Resource("CH:AveragingThermometer", "Averaging thermometer", "Termometr uśredniający"),
                new Resource("CH:AveragingThermometerDetails", "Averaging thermometer details", "Szczegóły termometru uśredniającego"),
                new Resource("CH:AveragingThermometers", "Averaging thermometers", "Termometry uśredniające"),
                new Resource("CH:BlocksEnablingFloorTemperaturePriority", "Blocks enabling floor temperature priority", "Bloki wymuszające priorytet temperatury podłogi"),
                new Resource("CH:BlocksEnablingReturnTemperaturePriority", "Blocks enabling return temperature priority", "Bloki wymuszające priorytet temperatury powrotu"),
                new Resource("CH:BlocksForcingCircuitToDisable", "Blocks forcing circuit to disable", "Bloki wymuszające wyłączenie obiegu"),
                new Resource("CH:BlocksForcingCircuitToEnable", "Blocks forcing circuit to enable", "Bloki wymuszające włączenie obiegu"),
                new Resource("CH:CannotAddAveragingThermometersMessage", "In order to add new averaging thermometer there must be at least one room and 2 thermometers defined first!", "Aby dodawać nowe termometry sumujące, musi być zefiniowany conajmniej pokój oraz 2 termometry!"),
                new Resource("CH:CannotAddHeatingManifoldsMessage", "In order to add a heating manifold there must be at least one room and a circuit (radiator, underfloor or rtl) defined first!", "Aby dodawać rozdzielacze c.o. musi być zdefiniowany conajmniej jeden pokój oraz jeden obieg (grzejnik, podłogówka lub rtl)!"),
                new Resource("CH:CannotAddRadiatorsMessage", "In order to add a radiator there must be at least one room and a thermometer defined first!", "Aby dodawać grzejniki musi być zdefiniowany conajmniej jeden pokój oraz jeden termometr!"),
                new Resource("CH:CannotAddRtlCircuitsMessage", "In order to add a RTL circuit there must be at least one room and a thermometer defined first!", "Aby dodawać obiegi RTL musi być zdefiniowany conajmniej jeden pokój oraz jeden termometr!"),
                new Resource("CH:CannotAddUnderfloorCircuitsMessage", "In order to add a underfloor circuit there must be at least one room and a thermometer defined first!", "Aby dodawać obiegi podłogówki musi być zdefiniowany conajmniej jeden pokój oraz jeden termometr!"),
                new Resource("CH:CircuitNotAssignedToManifold", "Circuit '%s' is not assigned to any heating manifold. The device will always be disabled!", "Obieg grzewczy '%s' nie jest przydzielony do jakiegokolwiek rozdzielacza c.o. Urządzenie będzie zawsze wyłączone!"),
                new Resource("CH:Circuits", "Circuits", "Obiegi"),
                new Resource("CH:FloorTemperature", "Floor temperature", "Temperatura podłogi"),
                new Resource("CH:FloorTemperatureToHigh", "Floor temperature to high", "Zbyt wysoka temperatura podłogi"),
                new Resource("CH:FloorTemperatureToLow", "Floor temperature to low", "Zbyt niska temperatura podłogi"),
                new Resource("CH:FloorThermometer", "Floor thermometer", "Termometr podłogi"),
                new Resource("CH:HeatingManifold", "Central Heating Pump", "Rozdzielacze c.o."),
                new Resource("CH:HeatingManifoldDetails", "Central heating Pump details", "Szczegóły rozdzielacza c.o."),
                new Resource("CH:HeatingManifoldPortsAreSame", "Heating manifold '%s' is using duplicated ports. Make sure pump or furnace's port and actuators transformer's ports!", "Rozdzielacz c.o. '%s' używa zduplikowanych portów. Upewnij się, że porty: pompy lub pieca oraz transformatora siłowników są różne!"),
                new Resource("CH:HeatingManifolds", "Central Heating Pumps", "Rozdzielacze c.o."),
                new Resource("CH:MaxFloorTemperature", "Maximum floor temperature", "Maksymalna temperatura podłogi"),
                new Resource("CH:MaxReturnTemperature", "Max return temperature", "Max. temperatura powrotu"),
                new Resource("CH:MinFloorTemperature", "Minimum floor temperature", "Minimalna temperatura podłogi"),
                new Resource("CH:MinReturnTemperature", "Min return temperature", "Min. temperatura powrotu"),
                new Resource("CH:Pumping", "Pumping", "Pompowanie"),
                new Resource("CH:PumpOrFurnacePort", "Pump or furnace's port", "Port pompy c.o. lub pieca"),
                new Resource("CH:Radiator", "Radiator", "Grzejnik"),
                new Resource("CH:RadiatorDetails", "Radiator details", "Szczegóły grzejnika"),
                new Resource("CH:Radiators", "Radiators", "Grzejniki"),
                new Resource("CH:ReturnTemperature", "Return temperature", "Temperatura powrotu"),
                new Resource("CH:ReturnTemperatureToHigh", "Return temperature to high", "Zbyt wysoka temperatura powrotu"),
                new Resource("CH:ReturnTemperatureToLow", "Return temperature to low", "Zbyt niska temperatura powrotu"),
                new Resource("CH:ReturnThermometer", "Return thermometer", "Termometr powrotny"),
                new Resource("CH:RtlCircuit", "Rtl circuit", "Obieg RTL"),
                new Resource("CH:RtlCircuitDetails", "Rtl circuit details", "Szczegóły obiegu RTL"),
                new Resource("CH:RtlCircuits", "Rtl circuits", "Obiegi RTL"),
                new Resource("CH:UnderfloorCircuit", "Underfloor circuit", "Obieg podłogówki"),
                new Resource("CH:UnderfloorCircuitDetails", "Underfloor circuit details", "Szczegóły obiegu podłogówki"),
                new Resource("CH:UnderfloorCircuits", "Underfloor circuits", "Obiegi podłogówki"),
                new Resource("CH:ValveOpening", "Valve opening", "Otwarcie zaworu"),
                new Resource("CH:WaterHeated", "Water heated", "Woda podgrzana"),
                new Resource("CH:AddCirculationPump", "Add circulation pump", "Dodaj pompę cyrkulacyjną"),
                new Resource("CH:AddTemperatureController", "Add temperature controller", "Dodaj sterownik temperatury"),
                new Resource("CH:AddThermometer", "Add thermometer", "Dodaj termometr"),
                new Resource("CH:AddThermostatCondition", "Add thermostat condition", "Dodaj warunek termostatyczny"),
                new Resource("CH:CannotAddThermometersMessage", "Cannot add any thermometer since there's no rooms defined yet or there's no temperature port available!", "Aby dodawać nowe termometry musi być zdefiniowany conajmniej jeden pokój oraz dostępny conajmniej jeden port temperaturowy!"),
                new Resource("CH:CannotAddHygrometersMessage", "Cannot add any hygrometer since there's no rooms defined yet or there's no humidity port available!", "Aby dodawać nowe wilgotnościomierze musi być zdefiniowany conajmniej jeden pokój oraz dostępny conajmniej jeden port wilgotnościowy!"),
                new Resource("CH:Heating", "Heating", "Grzanie"),
                new Resource("CH:Regulation", "Regulation", "Regulacja"),
                new Resource("CH:Thermometers", "Thermometers", "Termometry"),
                new Resource("CH:TemperatureControllers", "Temperature controllers", "Sterowniki temperatury"),
                new Resource("CH:CirculationPumps", "Circulation pumps", "Pompy cyrkulacyjne"),
                new Resource("CH:ThermostatConditions", "Thermostat conditions", "Warunki termostatyczne"),
                new Resource("CH:CannotAddCirculationPumpsMessage", "Cannot add any circulation pumps since there're no thermometers defined yet!", "Aby dodawać nowe pompy cyrkulacyjne musi być zdefiniowany conajmniej jeden termometr!"),
                new Resource("CH:CannotAddThermostatConditionsMessage", "Cannot add any thermostat conditions since there're no thermometers or temperature controllers defined yet!", "Aby dodawać nowe warunki termostatyczne musi być zdefiniowany conajmniej jeden termometr oraz sterownik temperatury!"),
                new Resource("CH:CirculationPump", "Circulation pump", "Pompa cyrkulacyjna"),
                new Resource("CH:DeltaTInC", "Delta T [°C]", "Delta T [°C]"),
                new Resource("CH:HysteresisInC", "Hysteresis [°C]", "Histereza [°C]"),
                new Resource("CH:FirstThermometer", "First thermometer", "Pierwszy termometr"),
                new Resource("CH:InitialValue", "Initial value", "Wartość startowa"),
                new Resource("CH:MaxValue", "Maximum value", "Wartość maksymalna"),
                new Resource("CH:MinValue", "Minimum value", "Wartość minimalna"),
                new Resource("CH:SecondThermometer", "Second thermometer", "Drugi termometr"),
                new Resource("CH:Temperature", "Temperature", "Temperatura"),
                new Resource("CH:TemperatureController", "Temperature controller", "Sterownik temperatury"),
                new Resource("CH:TemperatureControllerDetails", "Temperature controller details", "Szczegóły sterownika temperatury"),
                new Resource("CH:Thermometer", "Thermometer", "Termometr"),
                new Resource("CH:ThermometerDetails", "Thermometer details", "Szczegóły termometru"),
                new Resource("CH:ThermostatCondition", "Thermostat condition", "Warunek termostatyczny"),
                new Resource("CH:ThermostatConditionDetails", "Thermostat condition details", "Szczegóły warunku termostatycznego"),
                new Resource("CH:Hygrometer", "Hydrometer", "Wilgotnościomierz"),
                new Resource("CH:Hygrometers", "Hydrometers", "Wilgotnościomierze"),
                new Resource("CH:AddHygrometer", "Add hydrometer", "Dodaj wilgotnościomierz"),
                new Resource("CH:HygrometerDetails", "Hydrometer details", "Szczegóły wilgotnościomierza"),
                new Resource("CH:Comfortmeters", "Comfortmeters", "Komfortomierze"),
                new Resource("CH:Comfortmeter", "Comfortmeter", "Komfortomierz"),
                new Resource("CH:AddComfortmeter", "Add comfortmeter", "Dodaj komfortomierz"),
                new Resource("CH:ComfortmeterDetails", "Comfortmeter details", "Szczegóły komfortomierze"),
                new Resource("CH:CannotAddComfortmetersMessage", "In order to add a comfortmeter there must be at least one room defined and two ports available: temperature one and humidity one!", "Aby dodawać komfortomierze musi być zdefiniowany conajmniej jeden pokój oraz muszą być dostępne dwa porty: temperaturowy oraz wilgotności!"),
                new Resource("CH:HumidityPort", "Humidity port", "Port wilgotności"),
                new Resource("CH:TemperaturePort", "Temperature port", "Port temperaturowy"),
                new Resource("CH:Humidity", "Humidity", "Wilgotność"),
                new Resource("CH:NoDemand", "No demand", "Brak zapotrzebowania"),
                new Resource("CH:Cooling", "Cooling", "Chłodzenie"),
                new Resource("CH:Manual", "Manual", "Tryb ręczny"),
                new Resource("CH:BlocksEnablingCooling", "Blocks enabling cooling", "Bloki włączające chłodzenie"),
                new Resource("CH:BlocksEnablingHeating", "Blocks enabling heating", "Bloki włączające ogrzewanie"),
                new Resource("CH:AirConditioner", "Air conditioner", "Klimatyzator"),
                new Resource("CH:AirConditioners", "Air conditioners", "Klimatyzatory"),
                new Resource("CH:AirConditionerDetails", "Air conditioner details", "Szczegóły klimatyzatora"),
                new Resource("CH:AddAirConditioner", "Add air conditioner", "Dodaj klimatyzator"),
                new Resource("CH:CannotAddAirConditionersMessage", "In order to add an air conditioner there must be at least one room defined and four ports available: power output one and three digital output ones!", "Aby dodawać klimatyzatory musi być zdefiniowany conajmniej jeden pokój oraz muszą być dostępne cztery porty: jeden port wyjścia mocy oraz trzy cyfrowe porty wyjścia!"),
        };
    }

    @Override
    public String getTextResourcesPrefix() {
        return "CH";
    }

    @Override
    public ArrayList<IRequestsDispatcher> createDispatchers() {
        ArrayList<IRequestsDispatcher> dispatchers = new ArrayList<>();
        dispatchers.add(new CentralHeatingJsonRequestsDispatcher(_masterConfiguration, _centralHeatingConfiguration));
        return dispatchers;
    }

    @Override
    public DependenciesCheckerModule createDependenciesCheckerModule() {
        return new CentralHeatingDependenciesCheckerModule(_masterConfiguration, _centralHeatingConfiguration);
    }

    @Override
    public IConfigurationValidator createConfigurationValidator() {
        return new CentralHeatingConfigurationValidator(_centralHeatingConfiguration, _localizationProvider);
    }

    @Override
    public Collector createCollector() {
        return _centralHeatingConfiguration;
    }


    @Override
    public String[] listConsolidatedJavascripts() {
        return new String[]
                {
                        "JS\\CENTRALHEATING.JS",
                };
    }

    @Override
    public String[] listConsolidatedStyleSheets() {
        return new String[]
                {
                        "CSS\\CUSTOMCENTRALHEATING.CSS",
                };
    }

    @Override
    public IAutomationModule createAutomationModule() {
        return new CentralHeatingAutomationModule(_masterAutomation, _hardwareManager, _automationsSettings,
                _centralHeatingConfiguration, _localizationProvider);
    }

    @Override
    public ArrayList<ICrudPostHandler> createCrudPostHandlers() {
        ArrayList<ICrudPostHandler> handlers = new ArrayList<>();

        CrudPostHandler heatingManifoldsHandler = new CrudPostHandler(_masterConfiguration, "HEATINGMANIFOLD",
                (action, values) -> _centralHeatingConfiguration.modifyHeatingManifold(action, values), new RedirectionResponse("/config/HeatingManifolds.htm"));
        handlers.add(heatingManifoldsHandler);

        CrudPostHandler airConditionersHandler = new CrudPostHandler(_masterConfiguration, "AIRCONDITIONER",
                (action, values) -> _centralHeatingConfiguration.modifyAirConditioner(action, values), new RedirectionResponse("/config/AirConditioners.htm"));
        handlers.add(airConditionersHandler);

        CrudPostHandler circulationPump = new CrudPostHandler(_masterConfiguration, "CIRCULATIONPUMP",
                (action, values) -> _centralHeatingConfiguration.modifyCirculationPump(action, values), new RedirectionResponse("/config/CirculationPumps.htm"));
        handlers.add(circulationPump);

        CrudPostHandler thermometersHandler = new CrudPostHandler(_masterConfiguration, "THERMOMETER",
                (action, values) -> _centralHeatingConfiguration.modifyThermometer(action, values), new RedirectionResponse("/config/Thermometers.htm"));
        handlers.add(thermometersHandler);

        CrudPostHandler hygrometersHandler = new CrudPostHandler(_masterConfiguration, "HYGROMETER",
                (action, values) -> _centralHeatingConfiguration.modifyHygrometer(action, values), new RedirectionResponse("/config/Hygrometers.htm"));
        handlers.add(hygrometersHandler);

        CrudPostHandler averagingThermometersHandler = new CrudPostHandler(_masterConfiguration, "AVERAGINGTHERMOMETER",
                (action, values) -> _centralHeatingConfiguration.modifyAveragingThermometer(action, values), new RedirectionResponse("/config/AveragingThermometers.htm"));
        handlers.add(averagingThermometersHandler);

        CrudPostHandler comfortmetersHandler = new CrudPostHandler(_masterConfiguration, "COMFORTMETER",
                (action, values) -> _centralHeatingConfiguration.modifyComfortmeter(action, values), new RedirectionResponse("/config/Comfortmeters.htm"));
        handlers.add(comfortmetersHandler);

        CrudPostHandler temperatureControllersHandler = new CrudPostHandler(_masterConfiguration, "TEMPERATURECONTROLLER",
                (action, values) -> _centralHeatingConfiguration.modifyTemperatureController(action, values), new RedirectionResponse("/config/TemperatureControllers.htm"));
        handlers.add(temperatureControllersHandler);

        CrudPostHandler thermostatConditionsHandler = new CrudPostHandler(_masterConfiguration, "THERMOSTATCONDITION",
                (action, values) -> _centralHeatingConfiguration.modifyThermostatCondition(action, values), new RedirectionResponse("/config/ThermostatConditions.htm"));
        handlers.add(thermostatConditionsHandler);

        CrudPostHandler radiatorsHandler = new CrudPostHandler(_masterConfiguration, "RADIATOR",
                (action, values) -> _centralHeatingConfiguration.modifyRadiator(action, values), new RedirectionResponse("/config/Radiators.htm"));
        handlers.add(radiatorsHandler);

        CrudPostHandler underfloorCircuitsHandler = new CrudPostHandler(_masterConfiguration, "UNDERFLOORCIRCUIT",
                (action, values) -> _centralHeatingConfiguration.modifyUnderfloorCircuit(action, values), new RedirectionResponse("/config/UnderfloorCircuits.htm"));
        handlers.add(underfloorCircuitsHandler);

        CrudPostHandler rtlCircuitsHandler = new CrudPostHandler(_masterConfiguration, "RTLCIRCUIT",
                (action, values) -> _centralHeatingConfiguration.modifyRTLCircuit(action, values), new RedirectionResponse("/config/RtlCircuits.htm"));
        handlers.add(rtlCircuitsHandler);

        return handlers;
    }
}
