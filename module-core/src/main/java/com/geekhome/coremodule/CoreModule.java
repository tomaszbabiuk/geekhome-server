package com.geekhome.coremodule;

import com.geekhome.common.automation.IAutomationModule;
import com.geekhome.common.configuration.Collector;
import com.geekhome.common.alerts.DashboardAlertService;
import com.geekhome.common.alerts.IAlertService;
import com.geekhome.common.configuration.DependenciesCheckerModule;
import com.geekhome.common.configuration.MasterConfiguration;
import com.geekhome.common.configuration.MasterDependenciesCheckerModule;
import com.geekhome.common.commands.Synchronizer;
import com.geekhome.common.automation.CoreAutomationModule;
import com.geekhome.common.automation.MasterAutomation;
import com.geekhome.coremodule.httpserver.*;
import com.geekhome.common.settings.AutomationSettings;
import com.geekhome.common.hardwaremanager.IHardwareManager;
import com.geekhome.http.IRequestsDispatcher;
import com.geekhome.http.Resource;
import com.geekhome.coremodule.httpserver.ICrudPostHandler;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.http.jetty.RedirectionResponse;
import com.geekhome.common.automation.SystemInfo;
import com.geekhome.coremodule.modules.*;
import com.geekhome.coremodule.modules.Module;

import java.io.InputStream;
import java.util.ArrayList;

public class CoreModule extends Module {
    private ILocalizationProvider _localizationProvider;
    private SystemInfo _systemInfo;
    private MasterAutomation _masterAutomation;
    private IHardwareManager _hardwareManager;
    private MasterConfiguration _masterConfiguration;
    private AutomationSettings _settings;
    private Synchronizer _synchronizer;
    private DashboardAlertService _dashboardAlertService;
    private NavigationTree _navigationTree;

    public CoreModule(ILocalizationProvider localizationProvider, SystemInfo systemInfo, MasterConfiguration masterConfiguration,
                      MasterAutomation masterAutomation, IHardwareManager hardwareManager, AutomationSettings settings,
                      Synchronizer synchronizer, DashboardAlertService dashboardAlertService, NavigationTree navigationTree) {
        _localizationProvider = localizationProvider;
        _systemInfo = systemInfo;
        _masterAutomation = masterAutomation;
        _hardwareManager = hardwareManager;
        _masterConfiguration = masterConfiguration;
        _settings = settings;
        _synchronizer = synchronizer;
        _dashboardAlertService = dashboardAlertService;
        _navigationTree = navigationTree;
    }

    @Override
    public void addAlertService(ArrayList<IAlertService> alertServices) throws Exception {
        alertServices.add(_dashboardAlertService);
    }

    @Override
    public ArrayList<IUnit> createUnits() {
        ArrayList<IUnit> units = new ArrayList<>();
        units.add(new Unit(UnitCategory.Configuration, _localizationProvider.getValue("C:Backups"), "fileexchange", "/config/backups.htm"));
        units.add(new Unit(UnitCategory.Configuration, _localizationProvider.getValue("C:RoomsAndFloors"), "floorplan", "/config/roomsandfloors.htm"));
        units.add(new Unit(UnitCategory.Configuration, _localizationProvider.getValue("C:Modes"), "robot", "/config/modes.htm"));
        units.add(new Unit(UnitCategory.Configuration, _localizationProvider.getValue("C:Alerts"), "bell", "/config/alerts.htm"));
        units.add(new Unit(UnitCategory.Configuration, _localizationProvider.getValue("C:Geofences"), "map", "/config/geofences.htm"));
        units.add(new Unit(UnitCategory.ConfigurationConditions, _localizationProvider.getValue("C:TimeConditions"), "clock", "/config/timeconditions.htm"));
        units.add(new Unit(UnitCategory.ConfigurationConditions, _localizationProvider.getValue("C:DateConditions"), "calendar", "/config/dateconditions.htm"));
        units.add(new Unit(UnitCategory.ConfigurationConditions, _localizationProvider.getValue("C:MultistateConditions"), "multicategory", "/config/multistateconditions.htm"));
        units.add(new Unit(UnitCategory.ConfigurationConditions, _localizationProvider.getValue("C:ValueConditions"), "greaterthanorequal", "/config/valueconditions.htm"));
        units.add(new Unit(UnitCategory.ConfigurationConditions, _localizationProvider.getValue("C:DeltaConditions"), "triangle", "/config/deltaconditions.htm"));
        units.add(new Unit(UnitCategory.ConfigurationConditions, _localizationProvider.getValue("C:GroupConditions"), "coordination", "/config/groupconditions.htm"));
        units.add(new Unit(UnitCategory.ConfigurationConditions, _localizationProvider.getValue("C:GeofenceConditions"), "map", "/config/geofenceconditions.htm"));
        units.add(new Unit(UnitCategory.ConfigurationCommands, _localizationProvider.getValue("C:ReadValueCommands"), "readvaluecommand", "/config/readvaluecommands.htm"));
        units.add(new Unit(UnitCategory.ConfigurationCommands, _localizationProvider.getValue("C:ChangeStateCommands"), "changestatecommand", "/config/changestatecommands.htm"));
        units.add(new Unit(UnitCategory.AutomaticCalculations, _localizationProvider.getValue("C:DevicesEvaluations"), "plugin", "/calculations/devicesevaluations.htm"));
        units.add(new Unit(UnitCategory.AutomaticCalculations, _localizationProvider.getValue("C:ModesEvaluations"), "robot", "/calculations/modesevaluations.htm"));
        units.add(new Unit(UnitCategory.AutomaticCalculations, _localizationProvider.getValue("C:AlertsEvaluations"), "bell", "/calculations/alertsevaluations.htm"));
        units.add(new Unit(UnitCategory.AutomaticCalculations, _localizationProvider.getValue("C:MulticontrollersEvaluations"), "button", "/calculations/multicontrollersevaluations.htm"));
        units.add(new Unit(UnitCategory.ConfigurationDevices, _localizationProvider.getValue("C:KeySwitches"), "trigger", "/config/keyswitches.htm"));
        units.add(new Unit(UnitCategory.Diagnostics, _localizationProvider.getValue("C:Errors"), "error", "/diagnostics/errors.htm"));
        units.add(new Unit(UnitCategory.Diagnostics, _localizationProvider.getValue("C:Activities"), "list", "/diagnostics/activities.htm"));
        units.add(new Unit(UnitCategory.Automatic, _localizationProvider.getValue("C:Copyright"), "copyright", "/copyright.htm"));
        return units;
    }

    @Override
    public Resource[] getResources() {
        return new Resource[]{
                new Resource("C:GeekHomeLicense", "geekHOME license (Polish only)", "Licencja geekHOME"),
                new Resource("C:GeekHomeCopyright", loadTextFromResources("copyright/applicationcopyright.htm"), loadTextFromResources("copyright/applicationcopyright.htm")),
                new Resource("C:JettyLicense", "Jetty license", "Licencja Jetty"),
                new Resource("C:JettyCopyright", loadTextFromResources("copyright/en/jettycopyright.htm"), loadTextFromResources("copyright/pl/jettycopyright.htm")),
                new Resource("C:IconsLicense", "Icons license", "Licencje dla ikon"),
                new Resource("C:IconsCopyright", loadTextFromResources("copyright/en/iconscopyright.htm"), loadTextFromResources("copyright/pl/iconscopyright.htm")),
                new Resource("C:Actual", "Actual", "Aktualnie"),
                new Resource("C:AddAlert", "Add Alert", "Dodaj alarm"),
                new Resource("C:AddChangeStateCommand", "Add change state command", "Dodaj komendę zmiany stanu"),
                new Resource("C:AddFloor", "Add floor", "Dodaj piętro"),
                new Resource("C:AddGroupCondition", "Add group condition", "Dodaj warunek grupowy"),
                new Resource("C:AddKeySwitch", "Add key switch", "Dodaj przełącznik klawiszowy"),
                new Resource("C:AddMode", "Add block", "Dodaj tryb"),
                new Resource("C:AddMultistateCondition", "Add multistate condition", "Dodaj warunek wielostanowy"),
                new Resource("C:AddNewBlock", "Add new block", "Dodaj nowy blok"),
                new Resource("C:AddReadValueCommand", "Add read value command", "Dodaj komendę odczytu wartości"),
                new Resource("C:AddTimeCondition", "Add time condition", "Dodaj warunek czasowy"),
                new Resource("C:Alert", "Alart", "Alarm"),
                new Resource("C:AlertDetails", "Alert details", "Szczegóły alarmu"),
                new Resource("C:Alerts", "Alerts", "Alarmy"),
                new Resource("C:AlertsEvaluations", "Alerts evaluations", "Obliczenia dla alarmów"),
                new Resource("C:AlwaysOn", "Always on", "Zawsze włączony"),
                new Resource("C:Automatic", "Automatycznie", "Automatycznie"),
                new Resource("C:Automation", "Automation", "Automatyka"),
                new Resource("C:Back", "Back", "Wstecz"),
                new Resource("C:Block", "Block", "Blok"),
                new Resource("C:BlockDetails", "Block details", "Szczegóły bloku"),
                new Resource("C:Blocks", "Blocks", "Bloki"),
                new Resource("C:BlocksEnablingAutomaticControl", "Blocks enabling automatic control", "Bloki włączające tryb automatycznej kontroli"),
                new Resource("C:Calculations", "Calculations", "Obliczenia"),
                new Resource("C:Cancel", "Cancel", "Anuluj"),
                new Resource("C:NoPortsForDevice",
                        "Cannot found ports that are required to add this type of device",
                        "Nie można odnaleźć portów, które są niezbędne do konfiguracji tego typu urządzenia"),
                new Resource("C:HardwareCompletionInfo",
                        "Make sure all extension boards are connected or contact with your installer.",
                        "Upewnij się, że wszystkie karty rozszerzeń są podpięte do systemu lub skontaktuj się z instalatorem."),
                new Resource("C:CannotAddChangeStateCommandsMessage",
                        "No devices that can be controlled with change state commands!",
                        "Brak urządzeń, którymi można sterować komendami zmiany stanu!"),
                new Resource("C:CannotAddDevicesMessage",
                        "Cannot add any device since there's no rooms defined yet!",
                        "Aby dodawać nowe urządzenia musi być zdefiniowany conajmniej jeden pokój!"),
                new Resource("C:CannotAddGroupConditionsMessage",
                        "Cannot add any group state conditionssince there're no state conditions defined yet!",
                        "Aby dodawać nowe warunki grupowe musi być zdefiniowany conajmniej jeden warunek!"),
                new Resource("C:CannotAddMultistateConditionsMessage",
                        "In order to add multistate condition there must be at lest one multistate device defined!",
                        "Aby dodawać warunki stanu musi być zdefiniowane conajmniej jedno urządzenie wielostanowe!"),
                new Resource("C:CannotAddPresenceConditionsMessage",
                        "Cannot add any presence conditions since there're no movement detector defined yet!",
                        "Aby dodawać nowe warunki obecności musi być zdefiniowany conajmniej jeden czujnik ruchu!"),
                new Resource("C:CannotAddReadValueCommandsMessage",
                        "In order to add read value commands there must be at least one device defined!",
                        "Aby dodawać komendy odczytu wartości musi być zdefiniowane conajmniej jedno urządzenie!"),
                new Resource("C:Change", "Change", "Zmień"),
                new Resource("C:ChangeOperationMode", "Change operation mode", "Zmień tryb operacyjny"),
                new Resource("C:ChangeOperationModeToAutomaticQuestion", "Are you sure to enter automation mode?", "Czy chcesz włączyć tryb automatyczny?"),
                new Resource("C:ChangeOperationModeToConfigurationQuestion", "Are you sure to enter configuration mode?", "Czy chcesz włączyć tryb konfiguracyjny?"),
                new Resource("C:ChangeOperationModeToDiagnosticsQuestion", "Are you sure to enter diagnostics mode?", "Czy chcesz włączyć tryb diagnostyczny?"),
                new Resource("C:ChangeState", "Change state", "Zmień stan"),
                new Resource("C:ChangeStateCommand", "Change state command", "Komenda zmiany stanu"),
                new Resource("C:ChangeStateCommandDetails", "Change state command details", "Szczegóły komendy zmiany stanu"),
                new Resource("C:ChangeStateCommands", "Change state commands", "Komendy zmiany stanu"),
                new Resource("C:Changing", "Changing", "Ustawianie"),
                new Resource("C:ChangingToAutomaticModeWarning",
                        "In automation mode the building is controlled automatically by the blocks and conditions defined in configuration mode. Additionally you can control the state of all devices and change its working parameters.",
                        "W trybie automatycznym budynek jest sterowany na podstawie bloków i warunków zdefiniowanych w trybie konfiguracji. Dodatkowo możesz monitorować stan wszystkich urządzań oraz modyfikować parametry ich pracy."),
                new Resource("C:ChangingToConfigurationModeWarning",
                        "Configuration mode is to set up: floors, rooms, devices, conditions and automation blocks - everything what's needed to run the automation. You can also manage user accounts and other system settings.",
                        "Tryb konfiguracyjny służy do zestawiania: pięter, pokoi, urządzeń, warunków oraz bloków automatyki - wszystkiego co jest niezbędne do uruchomienia automatyki. Możesz także zarządzać użytkonikami oraz innymi ustawieniami systemu."),
                new Resource("C:ChangingToDiagnosticsModeWarning",
                        "Diagnostics mode should be used by installers only! Installer can browse system errors, change the states of the outputs and read all input signals.",
                        "Tryb diagnostyczny jest przeznaczony wyłącznie dla instalatorów systemu! Instalator może przeglądać błędy systemowe, zmieniać stan wyjść oraz odczytywać sygnały wejściowe."),
                new Resource("C:AdditionalChannels", "Additional channels", "Dodatkowe kanały"),
                new Resource("C:Closed", "Closed", "Zamknięte"),
                new Resource("C:Code", "Code", "Kod"),
                new Resource("C:CommandFooter", "The house is controlled by geekHOME! See more at http://geekHOME.pl/", "Tym domem steruje geekHOME! Zobacz więcej na http://geekHOME.pl/"),
                new Resource("C:Commands", "Commands", "Komendy"),
                new Resource("C:Condition", "Condition", "Warunek"),
                new Resource("C:SelectConditions", "Select conditions", "Wybierz warunki"),
                new Resource("C:NegatedConditions", "Negated conditions", "Warunki zanegowane"),
                new Resource("C:Conditions", "Conditions", "Warunki"),
                new Resource("C:Configuration", "Configuration", "Konfiguracja"),
                new Resource("C:ConfigurationIsModified", "Configuration modified!", "Konfiguracja zmodyfikowana!"),
                new Resource("C:CreateMultistateCondition", "Create multistate condition", "Utwórz warunek wielostanowy"),
                new Resource("C:CreateNew", "Create new", "Stwórz nowy"),
                new Resource("C:Dashboard", "Dashboard", "Panel główny"),
                new Resource("C:Default", "Default", "Domyślnie"),
                new Resource("C:Dependencies", "Dependencies", "Zależności"),
                new Resource("C:LoadingDependencies", "Loading dependencies", "Ładowanie zależności"),
                new Resource("C:DependencyType0.Room", "Room", "Pokój"),
                new Resource("C:DependencyType1.Device", "Device", "Urządzenie"),
                new Resource("C:DependencyType3.Condition", "Condition", "Warunek"),
                new Resource("C:DependencyType4.Block", "Block", "Blok"),
                new Resource("C:DependencyType5.Mode", "Mode", "Tryb"),
                new Resource("C:DependencyType6.Other", "Other", "Inne"),
                new Resource("C:DependencyType7.Zone", "Zone", "Strefa"),
                new Resource("C:DependencyType8.Command", "Command", "Komenda"),
                new Resource("C:Description", "Description", "Opis"),
                new Resource("C:Device", "Device", "Urządzenie"),
                new Resource("C:DeviceControl", "Device control", "Kontrola urządzenia"),
                new Resource("C:Devices", "Devices", "Urządzenia"),
                new Resource("C:DevicesEvaluations", "Devices evaluations", "Obliczenia dla urządzeń"),
                new Resource("C:DeviceStateChanged",
                        "Device '%s' state has been changed to '%s'",
                        "Stan urządzenia '%s' został zmieniony na '%s'"),
                new Resource("C:DeviceStates", "Device states", "Stany urządzenia"),
                new Resource("C:Diagnostics", "Diagnostics", "Diagnostyka"),
                new Resource("C:Edit", "Edit", "Edytuj"),
                new Resource("C:EqualityOperator", "Equality operator", "Operator równości"),
                new Resource("C:EqualityOperator0.GreaterOrEqual", "Greater or equal", "Wiekszy lub równy"),
                new Resource("C:EqualityOperator1.Lower", "Lower than", "Mniejszy niż"),
                new Resource("C:Error403", "Error: 403", "Błąd: 403"),
                new Resource("C:Error403Description",
                        "The page is not available in current operation mode",
                        "Strona nie jest dostępna w wybranym trybie operacyjnym"),
                new Resource("C:Error404", "Error: 404", "Błąd: 404"),
                new Resource("C:Error404Description", "Requested resource not found", "Nie można odnaleźć żądanego zasobu"),
                new Resource("C:Error500", "Error: 500", "Błąd: 500"),
                new Resource("C:Error500Description", "Internal server error!", "Wewnętrzny błąd systemu!"),
                new Resource("C:UnknownErrorProcessingCommand", "Error processing command '%s'", "Błąd przetwarzania komendy '%s'"),
                new Resource("C:InvalidCodeProcessingCommand",
                        "Invalid code. Device state hasn't been changed!",
                        "Nieprawidłowy kod. Stan urządzenia nie został zmieniony!"),
                new Resource("C:Errors", "Errors", "Błędy"),
                new Resource("C:Failed", "Failed", "Niespełniony"),
                new Resource("C:Favorites", "Favorites", "Ulubione"),
                new Resource("C:Floor", "Floor", "Piętro"),
                new Resource("C:FloorDetails", "Floor details", "Szczegóły piętra"),
                new Resource("C:GroupCondition", "Group condition", "Warunek grupowy"),
                new Resource("C:GroupConditionDetails", "Group condition details", "Szczegóły warunku grupowego"),
                new Resource("C:GroupConditions", "Group conditions", "Warunki grupowe"),
                new Resource("C:GroupMatch0.AllInTheGroup", "All in the group", "Wszystkie w grupie"),
                new Resource("C:GroupMatch1.OneOfTheGroup", "One of the group", "Jeden z grupy"),
                new Resource("C:Active", "Active", "Aktywny"),
                new Resource("C:Icon", "Icon", "Ikona"),
                new Resource("C:Icon.Attic", "Attic", "Poddasze"),
                new Resource("C:Icon.BabyCrib", "Baby crib", "Kołyska"),
                new Resource("C:Icon.Boiler", "Boiler", "Piec"),
                new Resource("C:Icon.Curtain", "Curtain", "Zasłony"),
                new Resource("C:Icon.Desk", "Desk", "Biurko"),
                new Resource("C:Icon.Door", "Door", "Drzwi"),
                new Resource("C:Icon.Dreams", "Dreams", "Sny"),
                new Resource("C:Icon.Dresser", "Dresser", "Komoda"),
                new Resource("C:Icon.Dumbbell", "Dumbbell", "Hantle"),
                new Resource("C:Icon.Garage", "Garage", "Garaż"),
                new Resource("C:Icon.House", "House", "Dom"),
                new Resource("C:Icon.Kitchen", "Kitchen", "Kuchnia"),
                new Resource("C:Icon.Library", "Library", "Biblioteczka"),
                new Resource("C:Icon.Locker", "Locker", "Szafka"),
                new Resource("C:Icon.PlaceSetting", "Place setting", "Nakrycie stołu"),
                new Resource("C:Icon.Plant", "Plant", "Roślina"),
                new Resource("C:Icon.Shower", "Shower", "Prysznic"),
                new Resource("C:Icon.Sofa", "Sofa", "Sofa"),
                new Resource("C:Icon.Speakers", "Speakers", "Głośniki"),
                new Resource("C:Icon.Stairs", "Stairs", "Schody"),
                new Resource("C:Icon.SweepingBrush", "Sweeping brush", "Miotła"),
                new Resource("C:Icon.Toilet", "Toilet", "Toaleta"),
                new Resource("C:Icon.TV", "TV", "TV"),
                new Resource("C:Icon.WashingMachine", "Washing machine", "Pralka"),
                new Resource("C:InactiveState", "Inactive state", "Stan bezczynności"),
                new Resource("C:InactiveState0.NC", "NC", "NZ"),
                new Resource("C:InactiveState1.NO", "NO", "NO"),
                new Resource("C:Information", "Information", "Informacja"),
                new Resource("C:InvalidCode", "Invalid code!", "Nieprawidłowy kod!"),
                new Resource("C:KeySwitch", "Key switch", "Przełącznik klawiszowy"),
                new Resource("C:KeySwitchDetails", "Key switch details", "Szczegóły przełącznika klawiszowego"),
                new Resource("C:KeySwitches", "Key switches", "Przełączniki klawiszowe"),
                new Resource("C:Level", "Level", "Poziom"),
                new Resource("C:Activities", "Activities", "Aktywności"),
                new Resource("C:Loading", "Loading", "Ładowanie"),
                new Resource("C:LongTimeInfo", "This may take a while...", "To może trochę potrwać..."),
                new Resource("C:Copyright", "Copyright", "Prawa autorskie"),
                new Resource("C:Inactive", "Inactive", "Niaktywny"),
                new Resource("C:Match", "Match", "Dopasowanie"),
                new Resource("C:MinimumWorkingTime", "Minimum working time", "Minimalny czas pracy"),
                new Resource("C:Mode", "Mode", "Tryb"),
                new Resource("C:ModeDetails", "Mode details", "Szczegóły trybu"),
                new Resource("C:Modes", "Modes", "Tryby"),
                new Resource("C:ModesEvaluations", "Modes evaluations", "Obliczenia dla trybów"),
                new Resource("C:MulticontrollersEvaluations", "Multicontrollers evaluations", "Obliczenia multicontrollerów"),
                new Resource("C:MultistateCondition", "Multistate condition", "Warunek wielostanowy"),
                new Resource("C:MultistateConditionDetails", "Multistate condition details", "Szczegóły warunki wielostanowego"),
                new Resource("C:MultistateConditions", "Multistate conditions", "Warunki wielostanowe"),
                new Resource("C:Name", "Name", "Nazwa"),
                new Resource("C:NewBlock", "New block", "Nowy blok"),
                new Resource("C:NoAlertsDefined", "No alerts defined", "Brak zdefiniowanych alarmów"),
                new Resource("C:NoAutomaticallyControlledDevicesDefined", "No automatically controlled devices defined", "Brak urządzeń sterowanych blokowo"),
                new Resource("C:NoBlocksDefined", "No blocks defined", "Brak zdefiniowanych bloków"),
                new Resource("C:NoCommand", "No command", "Brak komendy"),
                new Resource("C:NoDependencies", "No dependencies", "Brak zależności"),
                new Resource("C:NoDevicesFound", "No devices found", "Brak urządzeń"),
                new Resource("C:NoFavoriteDevices", "No favorite devices", "Brak ulubionych urządzeń"),
                new Resource("C:NoActivities", "No activities", "Brak aktywności"),
                new Resource("C:NoErrors", "No errors", "Brak błędów"),
                new Resource("C:NoModesDefined", "No modes defined", "Brak zdefiniowanych trybów"),
                new Resource("C:NoMulticontrollersDefined", "No multicontrollers defined!", "Brak zdefiniowanych multikontrolerów!"),
                new Resource("C:None", "None", "Brak"),
                new Resource("C:Off", "Off", "Wył"),
                new Resource("C:OK", "OK", "OK"),
                new Resource("C:On", "On", "Wł"),
                new Resource("C:Open", "Open", "Otwarte"),
                new Resource("C:OperationMode", "Operation mode", "Tryb operacyjny"),
                new Resource("C:OperationMode1.Automatic", "Automatic mode", "Tryb automatyczny"),
                new Resource("C:OperationMode3.Diagnostics", "Diagnostics mode", "Tryb diagnostyczny"),
                new Resource("C:OperationMode4.Configuration", "Configuration mode", "Tryb konfiguracji"),
                new Resource("C:OperationMode5.Settings", "Settings mode", "Tryb ustawień"),
                new Resource("C:Passed", "Passed", "Spełniony"),
                new Resource("C:PleaseWait", "Please wait", "Proszę czekać"),
                new Resource("C:Port", "Port", "Port"),
                new Resource("C:Priority", "Priority", "Priorytet"),
                new Resource("C:ReadValueCommand", "Read value command", "Komenda odczytu wartości"),
                new Resource("C:ReadValueCommandDetails", "Read value command details", "Szczegółt komendy odczytu wartości"),
                new Resource("C:ReadValueCommands", "Read value commands", "Komendy odczytu wartości"),
                new Resource("C:Refresh", "Refresh", "Odśwież"),
                new Resource("C:Remove", "Remove", "Usuń"),
                new Resource("C:Room", "Room", "Pokój"),
                new Resource("C:RoomDetails", "Room details", "Szczegóły pokoju"),
                new Resource("C:Rooms", "Rooms", "Pokoje"),
                new Resource("C:RoomsAndFloors", "Rooms and Floors", "Pokoje i kondygnacje"),
                new Resource("C:Save", "Save", "Zapisz"),
                new Resource("C:SaveChanges", "Save changes", "Zapisz zmiany"),
                new Resource("C:SaveInfo",
                        "Please wait... saving an automation copy of the configuration is in progress.",
                        "Proszę czekać, zapisywanie konfiguracji w trakcie."),
                new Resource("C:Setting", "Setting", "Ustawienie"),
                new Resource("C:Settings", "Settings", "Ustawienia"),
                new Resource("C:ShowDetails", "Show details", "Pokaż szczegóły"),
                new Resource("C:StartTime", "Start time", "Czas rozpoczęcia"),
                new Resource("C:State", "State", "Stan"),
                new Resource("C:States", "States", "States"),
                new Resource("C:StopTime", "Stop time", "Czas zakończenia"),
                new Resource("C:SwitchingOff", "Switching off", "Wyłączanie"),
                new Resource("C:SwitchOff", "Switch off", "Wyłącz"),
                new Resource("C:SwitchOn", "Switch on", "Włącz"),
                new Resource("C:TimeCondition", "Time condition", "Warunek czasowy"),
                new Resource("C:TimeConditionDetails", "Time condition details", "Szczegóły warunku czasowego"),
                new Resource("C:TimeConditions", "Time conditions", "Warunki czasowe"),
                new Resource("C:TriggeredManually", "Triggered manually", "Włączanie ręcznie"),
                new Resource("C:ManualWarning", "Manual mode", "Tryb ręczny!"),
                new Resource("C:ToggledDeviceWarning", "The state of the device could have been changed manually!", "Stan urządzenia mógł zostać zmieniony ręcznie!"),
                new Resource("C:Type", "Type", "Typ"),
                new Resource("C:CommandExpired",
                        "Command '%s' is expired! It was receiver too late due to connection error.",
                        "Komenda '%s' wygasła (została odebrana za późno z powodu błędów połączenia internetowego)!"),
                new Resource("C:SystemNotInAutomationMode", "System is not in automation mode.", "System nie pracuje w trybie automatyki."),
                new Resource("C:UnknownCommand", "We're sorry. The command was not recognized.", "Niestety. Komenda nie została rozpoznana."),
                new Resource("C:ViewMayContainUntranslatedTechnicalData",
                        "This view may contains untranslated technical data. Contact your system installer for details.",
                        "Ten widok może zawierać nieprzetłumaczone dane techniczne. Skontaktuj się z instalatorem w celu poznania szczegółów."),
                new Resource("C:Virtual", "Virtual", "Wirtualny"),
                new Resource("C:Wait", "Wait", "Czekaj"),
                new Resource("C:Warning", "Warning!", "Ostrzeżenie!"),
                new Resource("C:WhenOff", "When off", "Gdy wyłączony"),
                new Resource("C:WhenOn", "When on", "Gdy właczony"),
                new Resource("C:YesNo0.No", "No", "Nie"),
                new Resource("C:YesNo1.Yes", "Yes", "Tak"),
                new Resource("C:April", "April", "Kwiecień"),
                new Resource("C:August", "August", "Sierpień"),
                new Resource("C:Clear", "Clear", "Wyczyść"),
                new Resource("C:Day", "Day", "Dzień"),
                new Resource("C:Day.Fr", "Fr", "Pt"),
                new Resource("C:Day.Mo", "Mo", "Po"),
                new Resource("C:Day.Sa", "Sa", "So"),
                new Resource("C:Day.Su", "Su", "Nd"),
                new Resource("C:Day.Th", "Th", "Cz"),
                new Resource("C:Day.Tu", "Tu", "Wt"),
                new Resource("C:Day.We", "We", "Śr"),
                new Resource("C:Days", "Days", "Dni"),
                new Resource("C:December", "December", "Grudzień"),
                new Resource("C:February", "February", "Luty"),
                new Resource("C:Friday", "Friday", "Piątek"),
                new Resource("C:Hours", "Hours", "Godziny"),
                new Resource("C:January", "January", "Styczeń"),
                new Resource("C:July", "July", "Lipiec"),
                new Resource("C:June", "June", "Czerwiec"),
                new Resource("C:March", "March", "Marzec"),
                new Resource("C:May", "May", "Maj"),
                new Resource("C:Minutes", "Minutes", "Minuty"),
                new Resource("C:Monday", "Monday", "Poniedziałek"),
                new Resource("C:Month.Apr", "Apr", "Kwi"),
                new Resource("C:Month.Aug", "Aug", "Sie"),
                new Resource("C:Month.Dec", "Dev", "Gru"),
                new Resource("C:Month.Feb", "Feb", "Lut"),
                new Resource("C:Month.Jan", "Jan", "Sty"),
                new Resource("C:Month.Jul", "Jul", "Lip"),
                new Resource("C:Month.Jun", "Jun", "Cze"),
                new Resource("C:Month.Mar", "Mar", "Mar"),
                new Resource("C:Month.May", "May", "Maj"),
                new Resource("C:Month.Nov", "Nov", "Lis"),
                new Resource("C:Month.Oct", "Oct", "Paź"),
                new Resource("C:Month.Sep", "Sep", "Wrz"),
                new Resource("C:NextMonth", "Next month", "Następny miesiąc"),
                new Resource("C:November", "November", "Listopad"),
                new Resource("C:October", "October", "Październik"),
                new Resource("C:OpenDatePicker", "Open date picker", "Wybierz datę"),
                new Resource("C:OtherDates", "Other dates", "Inne daty"),
                new Resource("C:Saturday", "Saturday", "Sobota"),
                new Resource("C:Seconds", "Seconds", "Sekundy"),
                new Resource("C:September", "September", "Wrzesień"),
                new Resource("C:SetDate", "Set Date", "Ustaw datę"),
                new Resource("C:SetDuration", "Set Duration", "Ustaw czas trwania"),
                new Resource("C:SetTime", "Set Time", "Ustaw czas"),
                new Resource("C:Sunday", "Sunday", "Niedziela"),
                new Resource("C:Thursday", "Thursday", "Czwartek"),
                new Resource("C:Tuesday", "Tuesday", "Wtorek"),
                new Resource("C:Wednesday", "Wednesday", "Środa"),
                new Resource("C:MinLength", "Please enter at least {0} characters.", "Wybierz przynajmniej {0} opcje."),
                new Resource("C:Number", "Please enter a valid number.", "Wprowadź prawidłową liczbę."),
                new Resource("C:Required", "This field is required.", "To pole jest wymagane."),
                new Resource("C:System", "System", "System"),
                new Resource("C:Reboot", "Reboot", "Uruchom ponownie"),
                new Resource("C:Rebooting", "Rebooting", "Restart systemu"),
                new Resource("C:Shutdown", "Shutdown", "Wyłącz"),
                new Resource("C:ChangingPowerInfo", "System is running.", "System jest włączony."),
                new Resource("C:WaitRebooting",
                        "Rebooting the system may take up to 2 minutes. Don't close the browser window.",
                        "Ponowne uruchomienie systemu może trwać nawet 2 minuty. Podczas restartu nie wyłączaj okna przeglądatki."),
                new Resource("C:ShutdownDescription",
                        "Shuting down signal has been sent. Good bye.",
                        "Sygnał zamknięcia systemu został wysłany. Do widzenia."),
                new Resource("C:SystemRestartScheduled",
                        "Automatic system restart has been scheduled. The system will reboot in next 5 minutes.",
                        "Został zaplanowany automatyczny restart systemu. System zostanie ponownie uruchomiony w ciągu najbliższych 5 minut."),
                new Resource("C:PowerInputPorts", "Power input ports", "Porty wejścia mocy"),
                new Resource("C:PowerOutputPorts", "Power output ports", "Porty wyjścia mocy"),
                new Resource("C:DigitalInputPorts", "Digital input ports", "Cyfrowe porty wejścia"),
                new Resource("C:DigitalOutputPorts", "Digital output ports", "Cyfrowe porty wyjścia"),
                new Resource("C:TemperaturePorts", "Temperature ports", "Porty temperaturowe"),
                new Resource("C:TogglePorts", "Stateless output ports", "Bezstanowe porty wyjścia"),
                new Resource("C:DeviceCategory", "Category", "Kategoria"),
                new Resource("C:DeviceCategory1.Heating", "Heating", "Ogrzewanie"),
                new Resource("C:DeviceCategory2.HotWater", "Hot water", "Ciepła woda"),
                new Resource("C:DeviceCategory3.Locks", "Locks", "Zamki"),
                new Resource("C:DeviceCategory4.Lights", "Lights", "Oświetlenie"),
                new Resource("C:DeviceCategory5.Map", "Map", "Mapa"),
                new Resource("C:DeviceCategory6.Ventilation", "Ventilation", "Wentylacja"),
                new Resource("C:DeviceCategory7.Monitoring", "Monitoring", "Monitoring"),
                new Resource("C:MessageMayBeExpired",
                        "Warning! This message may not be up-to-date. It should be sent at '%s'. There was a internet connection error during original sending attempt.",
                        "Uwaga! Treść tej wiadomości może być już nieaktualna. Powinna ona zostać doręczona około godziny '%s'. Podczas pierwszej próby wysłyłania wystąpił błąd połączenia internetowego."),
                new Resource("C:InvalidCodeError", "Cannot change device '%s' state - invalid code!", "Nie można zmienić stanu urządzenia '%s' - nieprawidłowy kod!"),
                new Resource("C:Password", "Password", "Hasło"),
                new Resource("C:User", "User", "Użytkownik"),
                new Resource("C:Comment", "Comment", "Komentarz"),
                new Resource("C:SampleComment", "Configuration changes", "Zmiany w konfiguracji"),
                new Resource("C:DiscardChanges", "Discard changes", "Wyczyść zmiany"),
                new Resource("C:Discard", "Discard", "Wyczyść"),
                new Resource("C:DiscardInfo", "Click 'Discard' to cancel all the changes being made to the configuration in this session (since last save).",
                        "Kliknij 'Wyczyść' aby anulować wszystkie zmiany w konfiguracji w tej sesji (od czasu ostatniego zapisu)."),
                new Resource("C:EnterSameValue", "Please enter same value again.", "Proszą wprowadź taką samą wartość."),
                new Resource("C:CommandNotFound", "Command not found!", "Nieznana komenda!"),
                new Resource("C:Operational", "Operational", "Pracuje"),
                new Resource("C:ToggleOff", "Toggle off", "Wyłącz"),
                new Resource("C:ToggleOn", "Toggle on", "Włącz"),
                new Resource("C:Duration", "Duration", "Czas trwania"),
                new Resource("C:Initialization", "Initialization", "Inicjalizacja"),
                new Resource("C:ConfigurationError", "Connfiguration error", "Błąd konfiguracji"),
                new Resource("C:ConnectionError", "Connection error", "Błąd połączenia"),
                new Resource("C:ProcessingError", "Processing error", "Błąd przetwarzania"),
                new Resource("C:AdapterError", "Adapter error", "Błąd adaptera"),
                new Resource("C:NA", "N/a", "N/d"),
                new Resource("C:DelayTime", "Delay time", "Czas opóźnienia"),
                new Resource("C:ByCategory", "By category", "Wg kategorii"),
                new Resource("C:LoadingCategory", "Loading category", "Ładowanie kategorii"),
                new Resource("C:NoDevicesInSelectedCategory", "There's no devices in selected category", "Brak urządzeń w wybranej kategorii"),
                new Resource("C:AddDateCondition", "Add date condition", "Dodaj warunek kalendarzowy"),
                new Resource("C:DateCondition", "Date condition", "Warunek kalendarzowy"),
                new Resource("C:DateConditionDetails", "Date condition details", "Szczegóły warunku kalendarzowego"),
                new Resource("C:DateConditions", "Date conditions", "Warunki kalendarzowe"),
                new Resource("C:StartDate", "Start date", "Data początkowa"),
                new Resource("C:StopDate", "Stop date", "Data końcowa"),
                new Resource("C:DoOpen", "Open", "Otwórz"),
                new Resource("C:DoClose", "Close", "Zamknij"),
                new Resource("C:Opening", "Opening", "Otwieranie"),
                new Resource("C:Closing", "Closing", "Zamykanie"),
                new Resource("C:Latitude", "Latitude", "Szerokość geograficzna"),
                new Resource("C:Longitude", "Longitude", "Długość geograficzna"),
                new Resource("C:RadiusInM", "Radius [m]", "Promień [m]"),
                new Resource("C:GeofenceCondition", "Geofence condition", "Warunek lokalizacyjny"),
                new Resource("C:GeofenceConditions", "Geofence conditions", "Warunki lokalizacyjne"),
                new Resource("C:GeofenceConditionDetails", "Geofence condition details", "Szczegóły warunku lokalizacyjnego"),
                new Resource("C:AddGeofenceCondition", "Add geofence condition", "Dodaj warunek lokalizacyjny"),
                new Resource("C:Geofence", "Geofence", "Obszar mapy"),
                new Resource("C:Geofences", "Geofences", "Obszary map"),
                new Resource("C:GeofenceDetails", "Geofence details", "Szczegóły obszaru mapy"),
                new Resource("C:AddGeofence", "Add geofence", "Dodaj obszar mapy"),
                new Resource("C:CannotAddGeofenceConditionsMessage",
                        "Cannot add any geofence confidions since there's no geofences defined yet!",
                        "Aby dodawać nowe warunki lokalizacyjne musi być zdefiniowany conajmniej jeden obszar mapy!"),
                new Resource("C:Icon.MarkerOther", "Other place", "Inne miejsce"),
                new Resource("C:Icon.MarkerHouse", "House", "Dom"),
                new Resource("C:Icon.MarkerOffice", "Office", "Biuro"),
                new Resource("C:Icon.MarkerFactory", "Factory", "Fabryka"),
                new Resource("C:Icon.MarkerAcademy", "Academy", "Uczelnia"),
                new Resource("C:Icon.MarkerSchool", "School", "Szkoła"),
                new Resource("C:Icon.MarkerCountry", "Country", "Wieś"),
                new Resource("C:UserAgreement", "User agreement", "Umowa licencyjna"),
                new Resource("C:IAgree", "I agree", "Zgadzam się"),
                new Resource("C:IDisagree", "I disagree", "Nie zgadzam się"),
                new Resource("C:Goodbye", "Goodbye", "Do widzenia"),
                new Resource("C:UserAgreementNotAccepted",
                        "Unfortunately, geekHOME cannot be used without accepting user agreement.",
                        "Niestety, system nie może zostać użyty bez zaakceptowania umowy licencyjnej."),
                new Resource("C:ThankYouForConsideringGeekHOME",
                        "Thank you for considering geekHOME as your Home Automation system.",
                        "Dziękujemy za rozważenie systemu geekHOME jako systemu inteligentnego sterowania waszym domem."),
                new Resource("C:HumidityPorts", "Humidity ports", "Porty wilgotności"),
                new Resource("C:LuminosityPorts", "Luminosity ports", "Porty luminacji"),
                new Resource("C:NoMappings", "No mappings", "Brak mapowania"),
                new Resource("C:ValueCondition", "Value condition", "Warunek wartościowy"),
                new Resource("C:ValueConditions", "Value conditions", "Warunki wartościowe"),
                new Resource("C:AddValueCondition", "Add value condition", "Dodaj warunek wartościowy"),
                new Resource("C:ValueConditionDetails", "Value condition details", "Szczegóły warunku wartościowego"),
                new Resource("C:DeltaCondition", "Delta condition", "Warunek delta"),
                new Resource("C:DeltaConditions", "Delta conditions", "Warunki delta"),
                new Resource("C:AddDeltaCondition", "Add delta condition", "Dodaj warunek delta"),
                new Resource("C:DeltaConditionDetails", "Delta condition details", "Szczegóły warunku delta"),
                new Resource("C:TemperatureDevices", "Temperature devices", "Mierniki temperatury"),
                new Resource("C:HumidityDevices", "Humidity devices", "Mierniki wilgotności"),
                new Resource("C:LuminosityDevices", "Luminosity devices", "Mierniki jasności"),
                new Resource("C:Value", "Value", "Wartość"),
                new Resource("C:CannotAddDeltaConditionsMessage", "Cannot add any delta conditions since there're no measure devices like thermometers, hygrometers, luxmeters, etc.!", "Aby dodawać nowe warunki typu delta musi być zdefiniowane conajmniej jeden miernik np. termometr, higrometr, luxometr, itp.!"),
                new Resource("C:Delta", "Delta", "Delta"),
                new Resource("C:Hysteresis", "Hysteresis", "Histereza"),
                new Resource("C:FirstDevice", "First device", "Pierwsze urządzenie"),
                new Resource("C:SecondDevice", "Second device", "Drugie urządzenie"),
                new Resource("C:Version", "Version", "Wersja"),
                new Resource("C:FromDate", "From date", "Z dnia"),
                new Resource("C:Restore", "Restore", "Przywróć"),
                new Resource("C:NoBackups", "No backups", "Brak kopii zapasowych"),
                new Resource("C:Backups", "Backups", "Kopie zapasowe"),
                new Resource("C:RestoreThisVersion", "Restore to this version?", "Przywróć tę wersję?"),
                new Resource("C:RestoredFromFormat", "Restored from: %s", "Przywrócono z: %s"),
                new Resource("C:Version", "Version", "Wersja"),
                new Resource("C:Activation", "Activation", "Aktywacja"),
                new Resource("C:Activate", "Activate", "Aktywuj"),
                new Resource("C:Adapters", "Adapters", "Adaptery"),
                new Resource("C:ActivateAdapterInfo",
                        "Please enter activation code which is provided with the adapter (on the activation sticker).",
                        "Podaj kod aktywacyjny dostarczony razem z adapterem (umieszczany na naklejce aktywacyjnej)"),
                new Resource("C:Adapters", "Adapters", "Adaptery"),
                new Resource("C:ActivationFailureInfo",
                        "We're sorry, the adapter activation code seems to be invalid. Please contact GeekHOME support to get valid activation code.",
                        "Niestety, kod aktywacyjny dla adaptera jest nieprawidłowy. Prosimy o kontakt ze wsparciem firmy GeekHOME w celu uzyskania prawidłowego kodu aktywacyjnego."),
                new Resource("C:Presets", "Presets", "Nastawy"),
                new Resource("C:BlocksEnablingPreset1", "Blocks that activate preset 1", "Bloki aktywujące ustawienie 1"),
                new Resource("C:BlocksEnablingPreset2", "Blocks that activate preset 2", "Bloki aktywujące ustawienie 2"),
                new Resource("C:BlocksEnablingPreset3", "Blocks that activate preset 3", "Bloki aktywujące ustawienie 3"),
                new Resource("C:BlocksEnablingPreset4", "Blocks that activate preset 4", "Bloki aktywujące ustawienie 4"),
                new Resource("C:Preset1", "Preset 1", "Ustawienie 1"),
                new Resource("C:Preset2", "Preset 2", "Ustawienie 2"),
                new Resource("C:Preset3", "Preset 3", "Ustawienie 3"),
                new Resource("C:Preset4", "Preset 4", "Ustawienie 4"),
                new Resource("C:Intensity", "Intensity", "Intensywność"),
                new Resource("C:CannotAddValueConditionsMessage", "Cannot add any value conditions since there're no value devices or value controllers defined yet!", "Aby dodawać nowe warunki wartościowe musi być zdefiniowany conajmniej jedno urządzenie wartościowe (np. termometr, higrometr) oraz sterownik (np. temperatury, wilgotności)!"),
                new Resource("C:ShadowPortsInUse", "Shadow ports in use! System will try to heal in 30 minutes.", "Nie można było stworzyć portów dla niektórych urządzeń (użyto \"cieni\"), system spróbuje naprawić usterkę za ok 30 minut"),
                new Resource("C:DeviceDisconnectedWarning", "Disconnected", "Rozłączono"),
                new Resource("C:ForcedManual", "Forced manual", "Wymuszony tryb ręczny"),
                new Resource("C:AutomationWillContinueIn", "Automation will continue in [sec]", "Automatyzacja będzie kontunuowana za [sek]"),
                new Resource("C:CustomPreset", "Ustawienie indydualne", "Inne ustawienie"),
        };
    }

    @Override
    public String getTextResourcesPrefix() {
        return "C";
    }

    private String loadTextFromResources(String path) {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(path);
        java.util.Scanner s = new java.util.Scanner(stream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    @Override
    public ArrayList<IRequestsDispatcher> createDispatchers() {
        ArrayList<IRequestsDispatcher> dispatchers = new ArrayList<>();
        dispatchers.add(new CoreJsonRequestsDispatcher(_masterConfiguration, _dashboardAlertService));
        dispatchers.add(new BackupPostRequestsDispatcher(_masterConfiguration));
        dispatchers.add(new BackupJsonRequestsDispatcher(_masterConfiguration));
        dispatchers.add(new SynchronizeJsonRequestsDispatcher(_synchronizer, _localizationProvider));
        dispatchers.add(new SystemInfoJsonRequestsDispatcher(_systemInfo, _settings));
        dispatchers.add(new NavigationTreeJsonRequestsDispatcher(_navigationTree));
        dispatchers.add(new SystemInfoPostRequestsDispatcher(_systemInfo));
        dispatchers.add(new MasterConfigurationPostRequestsDispatcher(_masterConfiguration));
        dispatchers.add(new AutomationPostRequestsDispatcher(_masterAutomation));
        dispatchers.add(new AutomationJsonRequestsDispatcher(_masterConfiguration, _masterAutomation, _localizationProvider));
        dispatchers.add(new AutomationSettingsJsonRequestsDispatcher(_settings, _masterConfiguration));
        dispatchers.add(new AutomationSettingsPostRequestsDispatcher(_settings));
        return dispatchers;
    }

    @Override
    public DependenciesCheckerModule createDependenciesCheckerModule() {
        return new MasterDependenciesCheckerModule(_masterConfiguration);
    }

    @Override
    public Collector createCollector() {
        return _masterConfiguration;
    }

    @Override
    public IAutomationModule createAutomationModule() {
        return new CoreAutomationModule(_localizationProvider, _hardwareManager, _systemInfo, _masterAutomation, _masterConfiguration);
    }

    @Override
    public ArrayList<ICrudPostHandler> createCrudPostHandlers() {
        ArrayList<ICrudPostHandler> handlers = new ArrayList<>();

        CrudPostHandler floorsHandler = new CrudPostHandler(_masterConfiguration, "FLOOR",
                (action, values) -> _masterConfiguration.modifyFloor(action, values), new RedirectionResponse("/config/RoomsAndFloors.htm"));
        handlers.add(floorsHandler);

        CrudPostHandler roomsHandler = new CrudPostHandler(_masterConfiguration, "ROOM",
                (action, values) -> _masterConfiguration.modifyRoom(action, values), new RedirectionResponse("/config/RoomsAndFloors.htm"));
        handlers.add(roomsHandler);

        CrudPostHandler multistateConditionsHandler = new CrudPostHandler(_masterConfiguration, "MULTISTATECONDITION",
                (action, values) -> _masterConfiguration.modifyMultistateCondition(action, values), new RedirectionResponse("/config/MultistateConditions.htm"));
        handlers.add(multistateConditionsHandler);

        CrudPostHandler valueConditionsHandler = new CrudPostHandler(_masterConfiguration, "VALUECONDITION",
                (action, values) -> _masterConfiguration.modifyValueCondition(action, values), new RedirectionResponse("/config/ValueConditions.htm"));
        handlers.add(valueConditionsHandler);

        CrudPostHandler deltaConditionsHandler = new CrudPostHandler(_masterConfiguration, "DELTACONDITION",
                (action, values) -> _masterConfiguration.modifyDeltaCondition(action, values), new RedirectionResponse("/config/DeltaConditions.htm"));
        handlers.add(deltaConditionsHandler);

        CrudPostHandler groupConditionsHandler = new CrudPostHandler(_masterConfiguration, "GROUPCONDITION",
                (action, values) -> _masterConfiguration.modifyGroupCondition(action, values), new RedirectionResponse("/config/GroupConditions.htm"));
        handlers.add(groupConditionsHandler);

        CrudPostHandler timeConditionsHandler = new CrudPostHandler(_masterConfiguration, "TIMECONDITION",
                (action, values) -> _masterConfiguration.modifyTimeCondition(action, values), new RedirectionResponse("/config/TimeConditions.htm"));
        handlers.add(timeConditionsHandler);

        CrudPostHandler geofenceConditionsHandler = new CrudPostHandler(_masterConfiguration, "GEOFENCECONDITION",
                (action, values) -> _masterConfiguration.modifyGeofenceCondition(action, values), new RedirectionResponse("/config/GeofenceConditions.htm"));
        handlers.add(geofenceConditionsHandler);

        CrudPostHandler dateConditionsHandler = new CrudPostHandler(_masterConfiguration, "DATECONDITION",
                (action, values) -> _masterConfiguration.modifyDateCondition(action, values), new RedirectionResponse("/config/DateConditions.htm"));
        handlers.add(dateConditionsHandler);

        CrudPostHandler modesHandler = new CrudPostHandler(_masterConfiguration, "MODE",
                (action, values) -> _masterConfiguration.modifyMode(action, values), new RedirectionResponse("/config/Modes.htm"));
        handlers.add(modesHandler);

        CrudPostHandler alertsHandler = new CrudPostHandler(_masterConfiguration, "ALERT",
                (action, values) -> _masterConfiguration.modifyAlert(action, values), new RedirectionResponse("/config/Alerts.htm"));
        handlers.add(alertsHandler);

        CrudPostHandler blocksHandler = new CrudPostHandler(_masterConfiguration, "BLOCK",
                (action, values) -> _masterConfiguration.modifyBlock(action, values), null);
        handlers.add(blocksHandler);

        CrudPostHandler keySwitchesHandler = new CrudPostHandler(_masterConfiguration, "KEYSWITCH",
                (action, values) -> _masterConfiguration.modifyKeySwitch(action, values), new RedirectionResponse("/config/KeySwitches.htm"));
        handlers.add(keySwitchesHandler);

        CrudPostHandler readValueCommandsHandler = new CrudPostHandler(_masterConfiguration, "READVALUECOMMAND",
                (action, values) -> _masterConfiguration.modifyReadValueCommand(action, values), new RedirectionResponse("/config/ReadValueCommands.htm"));
        handlers.add(readValueCommandsHandler);

        CrudPostHandler changeStateCommandsHandler = new CrudPostHandler(_masterConfiguration, "CHANGESTATECOMMAND",
                (action, values) -> _masterConfiguration.modifyChangeStateCommand(action, values), new RedirectionResponse("/config/ChangeStateCommands.htm"));
        handlers.add(changeStateCommandsHandler);

        CrudPostHandler geofencesHandler = new CrudPostHandler(_masterConfiguration, "GEOFENCE",
                (action, values) -> _masterConfiguration.modifyGeofence(action, values), new RedirectionResponse("/config/Geofences.htm"));
        handlers.add(geofencesHandler);

        return handlers;
    }

    @Override
    public String[] listConsolidatedJavascripts() {
        return new String[]
                {
                        "JS\\JQUERY-1.10.2.MIN.JS",
                        "JS\\JQUERY.MOBILE-1.3.2.MIN.JS",
                        "JS\\JQMPROGRESSBAR.JS",
                        "JS\\JQM-DATEBOX.CORE.MIN.JS",
                        "JS\\JQM-DATEBOX.MODE.CALBOX.MIN.JS",
                        "JS\\JQM-DATEBOX.MODE.DATEBOX.MIN.JS",
                        "JS\\JQM-DATEBOX.MODE.DURATIONBOX.MIN.JS",
                        "JS\\JQUERY.METADATA.JS",
                        "JS\\JQUERY.TMPL.JS",
                        "JS\\JQUERY.VALIDATE.JS",
                        "JS\\JQUERY.ISOTOPE.MIN.JS",
                        "JS\\VARIANTCONFIG.JS",
                        "JS\\CORE.RESOURCES.JS",
                        "JS\\FUNCTIONS.JS",
                        "JS\\SYSTEMINFO.JS",
                        "JS\\NAVIGATIONTREE.JS",
                        "JS\\CORECONFIG.JS",
                        "JS\\BACKUPS.JS",
                        "JS\\ACTIVATION.JS"
                };
    }

    @Override
    public String[] listConsolidatedStyleSheets() {
        return new String[]
                {
                        "CSS\\ORANGE3.MIN.CSS",
                        "CSS\\JQUERY.MOBILE.STRUCTURE-1.3.2.MIN.CSS",
                        "CSS\\JQMPROGRESSBAR.CSS",
                        "CSS\\JQM-DATEBOX.MIN.CSS",
                        "CSS\\CUSTOM.CSS",
                        "CSS\\CORENAVIGATION.CSS"
                };
    }
}