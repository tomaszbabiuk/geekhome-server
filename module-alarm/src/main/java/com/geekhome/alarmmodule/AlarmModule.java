package com.geekhome.alarmmodule;

import com.geekhome.alarmmodule.automation.AlarmAutomationModule;
import com.geekhome.alarmmodule.httpserver.AlarmJsonRequestsDispatcher;
import com.geekhome.common.CrudAction;
import com.geekhome.common.automation.IAutomationModule;
import com.geekhome.common.configuration.Collector;
import com.geekhome.common.configuration.DependenciesCheckerModule;
import com.geekhome.common.configuration.MasterConfiguration;
import com.geekhome.http.Resource;
import com.geekhome.common.automation.MasterAutomation;
import com.geekhome.coremodule.httpserver.CrudPostHandler;
import com.geekhome.common.hardwaremanager.IHardwareManager;
import com.geekhome.http.INameValueSet;
import com.geekhome.http.IRequestsDispatcher;
import com.geekhome.coremodule.httpserver.ICrudPostHandler;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.http.jetty.RedirectionResponse;
import com.geekhome.coremodule.modules.*;
import com.geekhome.coremodule.modules.Module;

import java.util.ArrayList;

public class AlarmModule extends Module {
    private ILocalizationProvider _localizationProvider;
    private MasterConfiguration _masterConfiguration;
    private MasterAutomation _masterAutomation;
    private IHardwareManager _hardwareManager;
    private AlarmConfiguration _alarmConfiguration;

    public AlarmModule(ILocalizationProvider localizationProvider, MasterConfiguration masterConfiguration, MasterAutomation masterAutomation,
                       IHardwareManager hardwareManager) throws Exception {
        _localizationProvider = localizationProvider;
        _masterConfiguration = masterConfiguration;
        _masterAutomation = masterAutomation;
        _hardwareManager = hardwareManager;
        _alarmConfiguration = new AlarmConfiguration(masterConfiguration.getPool());
    }


    @Override
    public ArrayList<IUnit> createUnits() {
        ArrayList<IUnit> units = new ArrayList<>();
        units.add(new Unit(UnitCategory.ConfigurationDevices, _localizationProvider.getValue("ALARM:AlarmSensors"), "smokedetector", "/config/alarmsensors.htm"));
        units.add(new Unit(UnitCategory.ConfigurationDevices, _localizationProvider.getValue("ALARM:MovementDetectors"), "running", "/config/movementdetectors.htm"));
        units.add(new Unit(UnitCategory.ConfigurationDevices, _localizationProvider.getValue("ALARM:MagneticDetectors"), "magneticdetector", "/config/magneticdetectors.htm"));
        units.add(new Unit(UnitCategory.ConfigurationDevices, _localizationProvider.getValue("ALARM:Signalizators"), "bullhorn", "/config/signalizators.htm"));
        units.add(new Unit(UnitCategory.ConfigurationDevices, _localizationProvider.getValue("ALARM:CodeLocks"), "keyboard", "/config/codelocks.htm"));
        units.add(new Unit(UnitCategory.ConfigurationDevices, _localizationProvider.getValue("ALARM:Gates"), "gate", "/config/gates.htm"));
        units.add(new Unit(UnitCategory.ConfigurationDevices, _localizationProvider.getValue("ALARM:AlarmZones"), "shield", "/config/alarmzones.htm"));
        units.add(new Unit(UnitCategory.ConfigurationConditions, _localizationProvider.getValue("ALARM:PresenceConditions"), "radar", "/config/presenceconditions.htm"));
        return units;
    }

    @Override
    public Resource[] getResources() {
        return new Resource[] {
            new Resource("ALARM:Alarm", "Alarm", "Alarm"),
            new Resource("ALARM:ArmingPort", "Arming port", "Port uzbrajania"),
            new Resource("ALARM:BlocksArmingAlarmZone", "Blocks arming alarm zone", "Bloki uzbrajające strefę alarmową"),
            new Resource("ALARM:BlocksDisarmingAlarmZone", "Blocks disarming alarm zone", "Bloki rozbrajające strefę alarmową"),
            new Resource("ALARM:BreachedFrom", "Breached from", "Naruszono od"),
            new Resource("ALARM:Inside", "Inside", "środka"),
            new Resource("ALARM:NoSignalizatorForAlarmType", "There's no signalizator for '%s' alarm type!", "Nie odnaleziono sygnalizatora dla alarmu typu '%s'!"),
            new Resource("ALARM:Outside", "Outside", "zewnątrz"),
            new Resource("ALARM:StatusPort", "Status port", "Port statusu"),
            new Resource("ALARM:AddAlarmSensor", "Add alarm sensor", "Dodaj czujnik alarmowy"),
            new Resource("ALARM:AddAlarmZone", "Add alarm zone", "Dodaj strefę alarmową"),
            new Resource("ALARM:AddGate", "Add gate", "Dodaj bramę"),
            new Resource("ALARM:AddCodeLock", "Add code lock", "Dodaj zamek szyfrowy"),
            new Resource("ALARM:AddMagneticDetector", "Add contactrion", "Dodaj kontaktron"),
            new Resource("ALARM:AddMovementDetector", "Add movement detector", "Dodaj czujnik ruchu"),
            new Resource("ALARM:AddPresenceCondition", "Add presence condition", "Dodaj warunek obecności"),
            new Resource("ALARM:AddSignalizator", "Add signalizator", "Dodaj sygnalizator"),
            new Resource("ALARM:AlarmSensor", "Alarm sensor", "Czujnik alarmowy"),
            new Resource("ALARM:AlarmSensorDetails", "Alarm sensor details", "Szczegóły czujnika alarmowego"),
            new Resource("ALARM:AlarmSensors", "Alarm sensors", "Czujniki alarmowe"),
            new Resource("ALARM:AlarmSensorState0.Disarmed", "Disarmed", "Rozbrojony"),
            new Resource("ALARM:AlarmSensorState1.Watching", "Watching", "Czuwanie"),
            new Resource("ALARM:AlarmSensorState2.Prealarm", "Prealarm", "Prealarm"),
            new Resource("ALARM:AlarmSensorState3.Alarm", "Alarm", "Alarm"),
            new Resource("ALARM:MagneticDetectorState0Open.Disarmed","Open (Disarmed)", "Otwarte (Rozbrojony)"),
            new Resource("ALARM:MagneticDetectorState0Closed.Disarmed", "Closed (Disarmed)", "Zamknięte (Rozbrojony)"),
            new Resource("ALARM:MagneticDetectorState1Open.Watching", "Open (Watching)", "Otwarte (Czuwanie)"),
            new Resource("ALARM:MagneticDetectorState1Closed.Watching", "Closed (Watching)", "Zamknięte (Czuwanie)"),
            new Resource("ALARM:MagneticDetectorState2Open.Prealarm", "Open (Prealarm)", "Otwarte (Prealarm)"),
            new Resource("ALARM:MagneticDetectorState2Closed.Prealarm", "Closed (Prealarm)", "Zamknięte (Prealarm)"),
            new Resource("ALARM:MagneticDetectorState3Open.Alarm", "Open (Alarm)", "Otwarte (Alarm)"),
            new Resource("ALARM:MagneticDetectorState3Closed.Alarm", "Closed (Alarm)", "Zamknięte (Alarm)"),
            new Resource("ALARM:AlarmType", "Alarm type", "Typ alarmu"),
            new Resource("ALARM:AlarmType0.Burglary", "Burglary", "Włamanie"),
            new Resource("ALARM:AlarmType1.Silent", "Silent", "Cichy"),
            new Resource("ALARM:AlarmType100.Other", "Other", "Inny"),
            new Resource("ALARM:AlarmType2.Fire", "Fire", "Ogień"),
            new Resource("ALARM:AlarmType3.Flood", "Flood", "Powódź"),
            new Resource("ALARM:AlarmType4.Medical", "Medical", "Medyczny"),
            new Resource("ALARM:AlarmType5.Prealarm", "Prealarm", "Prealarm"),
            new Resource("ALARM:AlarmType6.Sabotage", "Sabotage", "Sabotaż"),
            new Resource("ALARM:AlarmType7.SecurityCall", "Security call", "Wezwanie ochrony"),
            new Resource("ALARM:AlarmTypes", "Alarm types", "Typy alarmu"),
            new Resource("ALARM:AlarmZone", "Alarm zone", "Strefa alarmowa"),
            new Resource("ALARM:AlarmZoneDetails", "Alarm zone details", "Szczegóły strefy alarmowej"),
            new Resource("ALARM:AlarmZones", "Alarm zones", "Strefy alarmowe"),
            new Resource("ALARM:AlarmZoneState0.Disarmed", "Disarmed", "Strefa rozbrojona"),
            new Resource("ALARM:AlarmZoneState1.Leaving", "Leaving", "Opuszczanie strefy"),
            new Resource("ALARM:AlarmZoneState2.Armed", "Armed", "Strefa uzbrojona"),
            new Resource("ALARM:AlarmZoneState3.Prealarm", "Prealarm", "Prealarm"),
            new Resource("ALARM:AlarmZoneState4.Alarm", "Alarm", "Alarm"),
            new Resource("ALARM:CannotAddAlarmZonesMessage", "Cannot add any alarm zone since there're no alarm sensors (ie. magnetic detectors or movement detectors) defined yet!", "Aby dodawać strefy alarmowe musi być zdefiniowane conajmniej jeden czujnik alarmowy (np. kontaktron lub czujnik ruchu)!"),
            new Resource("ALARM:Arm", "Arm", "Uzbrojenie"),
            new Resource("ALARM:Armed", "Armed", "Uzbrojony"),
            new Resource("ALARM:Disarm", "Disarm", "Rozbrojenie"),
            new Resource("ALARM:Disarmed", "Disarmed", "Rozbrojony"),
            new Resource("ALARM:Gate", "Gate", "Brama"),
            new Resource("ALARM:GateDetails", "Gate details", "Szczegóły bramy"),
            new Resource("ALARM:Gates", "Gates", "Bramy"),
            new Resource("ALARM:GateClosed", "Gate closed", "Brama zamknięta"),
            new Resource("ALARM:GateOpen", "Gate open", "Brama otwarta"),
            new Resource("ALARM:LastArmingTime", "Last arming time", "Czas ostatniego uzbrojenia"),
            new Resource("ALARM:LastDisarmingTime", "Last disarming time", "Czas ostatniego rozbrojenia"),
            new Resource("ALARM:Muted", "Muted", "Wyciszony"),
            new Resource("ALARM:LastArmingTime", "Last arming time", "Czas ostatniego uzbrojenia"),
            new Resource("ALARM:LastDisarmingTime", "Last disarming time", "Czas ostatniego rozbrojenia"),
            new Resource("ALARM:CodeLock", "Code lock", "Zamek szyfrowy"),
            new Resource("ALARM:CodeLockDetails", "Code lock details", "Szczegóły zamku szyfrowego"),
            new Resource("ALARM:CodeLocks", "Code locks", "Zamki szyfrowe"),
            new Resource("ALARM:MagneticDetector", "MagneticDetector", "Kontaktron"),
            new Resource("ALARM:MagneticDetectorDetails", "MagneticDetector details", "Szczegóły kontaktronu"),
            new Resource("ALARM:MagneticDetectors", "Magnetic Detectors", "Kontaktrony"),
            new Resource("ALARM:DisarmingMovementDetector", "Disarming movement detector", "Czujnik ruchu rozbrajający"),
            new Resource("ALARM:Icon.GarageDoor", "Garage door", "Brama garażowa"),
            new Resource("ALARM:Icon.Gate", "Gate", "Brama wjazdowa"),
            new Resource("ALARM:Icon.Window", "Window", "Okno"),
            new Resource("ALARM:Icon.Door", "Door", "Drzwi"),
            new Resource("ALARM:IsPetFriendly", "Is pet friendly", "Ignoruje zwierzęta"),
            new Resource("ALARM:LeavingTime", "Leaving time", "Czas wyjścia"),
            new Resource("ALARM:Locks", "Locks", "Szyfratory"),
            new Resource("ALARM:MaximumWorkingTime", "Maximum working time", "Maksymalny czas pracy"),
            new Resource("ALARM:MovementDetector", "Movement detector", "Czujnik ruchu"),
            new Resource("ALARM:MovementDetectorDetails", "Movement detector details", "Szczegóły czujnika ruchu"),
            new Resource("ALARM:MovementDetectors", "Movement detectors", "Czujniki ruchu"),
            new Resource("ALARM:Optional", "Optional", "Opcjonalny"),
            new Resource("ALARM:PresenceCondition", "Presence condition", "Warunek obecności"),
            new Resource("ALARM:PresenceConditionDetails", "Presence condition details", "Szczegóły warunku obecności"),
            new Resource("ALARM:PresenceConditions", "Presence conditions", "Warunki obecności"),
            new Resource("ALARM:PresenceType", "Presence type", "Typ obecności"),
            new Resource("ALARM:PresenceType0.Presence", "Presence", "Obecność"),
            new Resource("ALARM:PresenceType1.Absence", "Absence", "Nieobecność"),
            new Resource("ALARM:Sensitivity", "Sensitivity", "Czułość"),
            new Resource("ALARM:Signalizator", "Signalizator", "Sygnalizator"),
            new Resource("ALARM:SignalizatorDetails", "Signalizator details", "Szczegóły sygnalizatora"),
            new Resource("ALARM:Signalizators", "Signalizators", "Sygnalizatory"),
            new Resource("ALARM:UnlockingCode", "Unlocking/ Locking code", "Kod rozbrajający/ uzbrajający"),
            new Resource("ALARM:BreachedSensor", "Breached sensor","Naruszony czujnik"),
            new Resource("ALARM:OpeningError", "Opening error","Błąd otwierania!"),
            new Resource("ALARM:ClosingError", "Closing error","Błąd zamykania"),
            new Resource("ALARM:BlocksClosingGate", "Auto-closing gate blocks","Bloki automatycznie zamykające bramę"),
            new Resource("ALARM:BlocksOpeningGate", "Auto-opening gate blocks","Bloki automatycznie otwierające bramę"),
            new Resource("ALARM:GateControlPort", "Gate control port", "Port sterujący bramą"),
            new Resource("ALARM:MagneticDetectorPort", "Magnetic detector port", "Port kontaktronu")
        };
    }

    @Override
    public String getTextResourcesPrefix() {
        return "ALARM";
    }

    @Override
    public ArrayList<IRequestsDispatcher> createDispatchers() {
        ArrayList<IRequestsDispatcher> dispatchers = new ArrayList<>();
        dispatchers.add(new AlarmJsonRequestsDispatcher(_alarmConfiguration));
        return dispatchers;
    }

    @Override
    public AlarmConfigurationValidator createConfigurationValidator() {
        return new AlarmConfigurationValidator(_localizationProvider, _alarmConfiguration);
    }

    @Override
    public DependenciesCheckerModule createDependenciesCheckerModule() {
        return new AlarmDependenciesCheckerModule(_masterConfiguration, _alarmConfiguration);
    }

    @Override
    public Collector createCollector() {
        return _alarmConfiguration;
    }

    @Override
    public IAutomationModule createAutomationModule() {
        return new AlarmAutomationModule(_masterAutomation, _alarmConfiguration, _hardwareManager, _localizationProvider);
    }


    @Override
    public ArrayList<ICrudPostHandler> createCrudPostHandlers() {
        ArrayList<ICrudPostHandler> handlers = new ArrayList<>();

        CrudPostHandler alarmSensorsHandler = new CrudPostHandler(_masterConfiguration, "ALARMSENSOR",
                new CrudPostHandler.ICrudModificationFunction() {
                    @Override
                    public void execute(CrudAction action, INameValueSet values) throws Exception {
                        _alarmConfiguration.modifyAlarmSensor(action, values);
                    }
                }, new RedirectionResponse("/config/AlarmSensors.htm"));
        handlers.add(alarmSensorsHandler);

        CrudPostHandler presenceConditionsHandler = new CrudPostHandler(_masterConfiguration, "PRESENCECONDITION",
                new CrudPostHandler.ICrudModificationFunction() {
                    @Override
                    public void execute(CrudAction action, INameValueSet values) throws Exception {
                        _alarmConfiguration.modifyPresenceCondition(action, values);
                    }
                }, new RedirectionResponse("/config/PresenceConditions.htm"));
        handlers.add(presenceConditionsHandler);

        CrudPostHandler magneticDetectorsHandler = new CrudPostHandler(_masterConfiguration, "MAGNETICDETECTOR",
                new CrudPostHandler.ICrudModificationFunction() {
                    @Override
                    public void execute(CrudAction action, INameValueSet values) throws Exception {
                        _alarmConfiguration.modifyMagneticDetector(action, values);
                    }
                }, new RedirectionResponse("/config/MagneticDetectors.htm"));
        handlers.add(magneticDetectorsHandler);

        CrudPostHandler movementDetectorsHandler = new CrudPostHandler(_masterConfiguration, "MOVEMENTDETECTOR",
                new CrudPostHandler.ICrudModificationFunction() {
                    @Override
                    public void execute(CrudAction action, INameValueSet values) throws Exception {
                        _alarmConfiguration.modifyMovementDetector(action, values);
                    }
                }, new RedirectionResponse("/config/MovementDetectors.htm"));
        handlers.add(movementDetectorsHandler);

        CrudPostHandler signalizatorsHandler = new CrudPostHandler(_masterConfiguration, "SIGNALIZATOR",
                new CrudPostHandler.ICrudModificationFunction() {
                    @Override
                    public void execute(CrudAction action, INameValueSet values) throws Exception {
                        _alarmConfiguration.modifySignalizator(action, values);
                    }
                }, new RedirectionResponse("/config/Signalizators.htm"));
        handlers.add(signalizatorsHandler);

        CrudPostHandler alarmZonesHandler = new CrudPostHandler(_masterConfiguration, "ALARMZONE",
                new CrudPostHandler.ICrudModificationFunction() {
                    @Override
                    public void execute(CrudAction action, INameValueSet values) throws Exception {
                        _alarmConfiguration.modifyAlarmZone(action, values);
                    }
                }, new RedirectionResponse("/config/AlarmZones.htm"));
        handlers.add(alarmZonesHandler);

        CrudPostHandler codeLocksHandler = new CrudPostHandler(_masterConfiguration, "CODELOCK",
                new CrudPostHandler.ICrudModificationFunction() {
                    @Override
                    public void execute(CrudAction action, INameValueSet values) throws Exception {
                        _alarmConfiguration.modifyCodeLock(action, values);
                    }
                }, new RedirectionResponse("/config/CodeLocks.htm"));
        handlers.add(codeLocksHandler);

        CrudPostHandler gatesHandler = new CrudPostHandler(_masterConfiguration, "GATE",
                new CrudPostHandler.ICrudModificationFunction() {
                    @Override
                    public void execute(CrudAction action, INameValueSet values) throws Exception {
                        _alarmConfiguration.modifyGate(action, values);
                    }
                }, new RedirectionResponse("/config/Gates.htm"));
        handlers.add(gatesHandler);

        return handlers;
    }

    @Override
    public String[] listConsolidatedJavascripts() {
        return new String[]
                {
                        "JS\\ALARM.RESOURCES.JS",
                        "JS\\ALARMCONFIG.JS",
                };
    }

    @Override
    public String[] listConsolidatedStyleSheets() {
        return new String[]
                {
                        "CSS\\CUSTOMALARM.CSS",
                };
    }

}

