package com.geekhome.alarmmodule;

import com.geekhome.common.*;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.coremodule.ICondition;
import com.geekhome.common.configuration.IDevice;
import com.geekhome.coremodule.InactiveState;
import com.geekhome.http.INameValueSet;
import com.geekhome.httpserver.modules.Collector;
import com.geekhome.httpserver.modules.CollectorCollection;
import com.geekhome.httpserver.modules.ObjectNotFoundException;

import java.util.ArrayList;

public class AlarmConfiguration extends Collector {
    private CollectorCollection<SinglePortAlarmSensor> _alarmSensors;
    private CollectorCollection<PresenceCondition> _presenceConditions;
    private CollectorCollection<MagneticDetector> _magneticDetectors;
    private CollectorCollection<MovementDetector> _movementDetectors;
    private CollectorCollection<CodeLock> _codeLocks;
    private CollectorCollection<AlarmZone> _alarmZones;
    private CollectorCollection<Signalizator> _signalizators;
    private CollectorCollection<Gate> _gates;

    @ConfigurationSaver(sectionName = "AlarmSensor", hasChildren = false)
    public CollectorCollection<SinglePortAlarmSensor> getAlarmSensors() {
        return _alarmSensors;
    }

    @ConfigurationSaver(sectionName = "PresenceCondition", hasChildren = false)
    public CollectorCollection<PresenceCondition> getPresenceConditions() {
        return _presenceConditions;
    }

    @ConfigurationSaver(sectionName = "MagneticDetector", hasChildren = false)
    public CollectorCollection<MagneticDetector> getMagneticDetectors() {
        return _magneticDetectors;
    }

    @ConfigurationSaver(sectionName = "MovementDetector", hasChildren = false)
    public CollectorCollection<MovementDetector> getMovementDetectors() {
        return _movementDetectors;
    }

    @ConfigurationSaver(sectionName = "CodeLock", hasChildren = false)
    public CollectorCollection<CodeLock> getCodeLocks() {
        return _codeLocks;
    }

    @ConfigurationSaver(sectionName = "AlarmZone", hasChildren = false)
    public CollectorCollection<AlarmZone> getAlarmZones() {
        return _alarmZones;
    }

    @ConfigurationSaver(sectionName = "Signalizator", hasChildren = false)
    public CollectorCollection<Signalizator> getSignalizators() {
        return _signalizators;
    }

    @ConfigurationSaver(sectionName = "Gate", hasChildren = false)
    public CollectorCollection<Gate> getGates() {
        return _gates;
    }

    @Override
    public String getPoolPrefix() {
        return "alarm";
    }

    public AlarmConfiguration(IdPool pool) {
        super(pool);
        _alarmSensors = new CollectorCollection<>();
        _presenceConditions = new CollectorCollection<>();
        _magneticDetectors = new CollectorCollection<>();
        _movementDetectors = new CollectorCollection<>();
        _codeLocks = new CollectorCollection<>();
        _alarmZones = new CollectorCollection<>();
        _signalizators = new CollectorCollection<>();
        _gates = new CollectorCollection<>();
    }

    @Override
    public void clear() {
        getAlarmSensors().clear();
        getPresenceConditions().clear();
        getMagneticDetectors().clear();
        getMovementDetectors().clear();
        getCodeLocks().clear();
        getAlarmZones().clear();
        getSignalizators().clear();
        getGates().clear();
    }

    @ConfigurationLoader(sectionName = "PresenceCondition", parentId = "")
    public void modifyPresenceCondition(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String movementDetectorId = values.getValues("movementdetectorsids");
        String presenceType = values.getValue("presencetype");
        String duration = values.getValue("duration");
        String sensitivity = values.getValue("sensitivity");
        PresenceType presenceTypeParsed = PresenceType.fromInt(Integer.parseInt(presenceType));
        if (action == CrudAction.AddOrCreate) {
            addPresenceCondition(name, description, presenceTypeParsed, movementDetectorId, duration, sensitivity, uniqueId);
        } else {
            editPresenceCondition(name, description, presenceTypeParsed, movementDetectorId, duration, sensitivity, uniqueId);
        }
        onInvalidateCache("/CONFIG/PRESENCECONDITIONS.JSON");
        onInvalidateCache("/CONFIG/ALLCONDITIONS.JSON");
        onModified();
    }


    private void addPresenceCondition(String name, String description, PresenceType presenceType, String movementDetectorsIds, String duration, String sensitivity, String uniqueId) {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName conditionName = new DescriptiveName(name, uniqueId, description);
        PresenceCondition condition = new PresenceCondition(conditionName, presenceType, movementDetectorsIds, duration, sensitivity);
        getPresenceConditions().add(condition);
    }

    private void editPresenceCondition(String name, String description, PresenceType presenceType, String movementDetectorsIds, String duration, String sensitivity, String uniqueId) throws ObjectNotFoundException {
        PresenceCondition condition = getPresenceConditions().find(uniqueId);
        condition.getName().setName(name);
        condition.getName().setDescription(description);
        condition.setPresenceType(presenceType);
        condition.setMovementDetectorsIds(movementDetectorsIds);
        condition.setDuration(duration);
        condition.setSensitivity(sensitivity);
    }

    @ConfigurationLoader(sectionName = "AlarmSensor", parentId = "")
    public void modifyAlarmSensor(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String portId = values.getValue("portid");
        String roomId = values.getValue("roomid");
        String inactiveState = values.getValue("inactivestate");
        InactiveState inactiveStateParsed = InactiveState.fromInt(Integer.parseInt(inactiveState));
        String delayTime = values.getValue("delaytime");
        if (action == CrudAction.AddOrCreate) {
            addAlarmSensor(name, description, portId, roomId, inactiveStateParsed, delayTime, uniqueId);
        } else {
            editAlarmSensor(name, description, portId, roomId, inactiveStateParsed, delayTime, uniqueId);
        }
        onInvalidateCache("/CONFIG/ALARMSENSORS.JSON");
        onInvalidateCache("/CONFIG/ALLDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLALARMSENSORS.JSON");
        onModified();
    }

    private void addAlarmSensor(String name, String description, String portId, String roomId, InactiveState inactiveState, String delayTime, String uniqueId) throws Exception {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);

        DescriptiveName deviceName = new DescriptiveName(name, uniqueId, description);
        SinglePortAlarmSensor device = new SinglePortAlarmSensor(deviceName, portId, roomId, inactiveState, delayTime);
        getAlarmSensors().add(device);
        onInvalidateCache("/DIGITALINPUTPORTS.JSON");
    }

    private void editAlarmSensor(String name, String description, String portId, String roomId, InactiveState inactiveState, String delayTime, String uniqueId) throws Exception {
        SinglePortAlarmSensor device = getAlarmSensors().find(uniqueId);
        if (!device.getPortId().equals(portId)) {
            onInvalidateCache("/DIGITALINPUTPORTS.JSON");
        }

        device.getName().setName(name);
        device.getName().setDescription(description);
        device.setPortId(portId);
        device.setRoomId(roomId);
        device.setInactiveState(inactiveState);
        device.setDelayTime(delayTime);
    }

    @ConfigurationLoader(sectionName = "MagneticDetector", parentId = "")
    public void modifyMagneticDetector(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String portId = values.getValue("portid");
        String roomId = values.getValue("roomid");
        String inactiveState = values.getValue("inactivestate");
        InactiveState inactiveStateParsed = InactiveState.fromInt(Integer.parseInt(inactiveState));
        String delayTime = values.getValue("delaytime");
        String disarmingMovementDetectorId = values.getValue("disarmingmovementdetectorid");
        String iconName = values.getValue("iconname");
        if (action == CrudAction.AddOrCreate) {
            addMagneticDetector(name, description, portId, roomId, inactiveStateParsed, delayTime, disarmingMovementDetectorId, iconName, uniqueId);
        } else {
            editMagneticDetector(name, description, portId, roomId, inactiveStateParsed, delayTime, disarmingMovementDetectorId, iconName, uniqueId);
        }
        onInvalidateCache("/CONFIG/MAGNETICDETECTORS.JSON");
        onInvalidateCache("/CONFIG/ALLDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLALARMSENSORS.JSON");
        onModified();
    }

    private void addMagneticDetector(String name, String description, String portId, String roomId, InactiveState inactiveState, String delayTime,
                               String disarmingMovementDetectorId, String iconName, String uniqueId) throws Exception {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);

        DescriptiveName deviceName = new DescriptiveName(name, uniqueId, description);
        MagneticDetector device = new MagneticDetector(deviceName, portId, roomId, inactiveState, delayTime, disarmingMovementDetectorId, iconName);
        getMagneticDetectors().add(device);
        onInvalidateCache("/DIGITALINPUTPORTS.JSON");
    }

    private void editMagneticDetector(String name, String description, String portId, String roomId, InactiveState inactiveState, String delayTime,
                                String disarmingMovementDetectorId, String iconName, String uniqueId) throws Exception {
        MagneticDetector device = getMagneticDetectors().find(uniqueId);
        if (!device.getPortId().equals(portId)) {
            onInvalidateCache("/DIGITALINPUTPORTS.JSON");
        }

        device.getName().setName(name);
        device.getName().setDescription(description);
        device.setPortId(portId);
        device.setRoomId(roomId);
        device.setInactiveState(inactiveState);
        device.setDelayTime(delayTime);
        device.setDisarmingMovementDetectorId(disarmingMovementDetectorId);
        device.setIconName(iconName);
    }

    @ConfigurationLoader(sectionName = "Gate", parentId = "")
    public void modifyGate(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String magneticDetectorPortId = values.getValue("magneticdetectorportid");
        String gateControlPortId = values.getValue("gatecontrolportid");
        String roomId = values.getValue("roomid");
        String code = values.getValue("code");
        String iconName = values.getValue("iconname");
        String inactiveState = values.getValue("inactivestate");
        String delayTime = values.getValue("delaytime");
        InactiveState inactiveStateParsed = InactiveState.fromInt(Integer.parseInt(inactiveState));
        if (action == CrudAction.AddOrCreate) {
            addGate(name, description, magneticDetectorPortId, gateControlPortId, roomId, inactiveStateParsed,
                    delayTime, code, iconName, uniqueId);
        } else {
            editGate(name, description, magneticDetectorPortId, gateControlPortId, roomId, inactiveStateParsed,
                    delayTime, code, iconName, uniqueId);
        }

        onInvalidateCache("/CONFIG/GATES.JSON");
        onInvalidateCache("/CONFIG/ALLDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLBLOCKSTARGETDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLMULTISTATEDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLALARMSENSORS.JSON");

        onModified();
    }

    private void addGate(String name, String description, String magneticDetectorPortId, String gateControlPortId, String roomId,
                                  InactiveState inactiveState, String delayTime, String code, String iconName, String uniqueId) throws Exception {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);

        DescriptiveName deviceName = new DescriptiveName(name, uniqueId, description);
        Gate device = new Gate(deviceName, magneticDetectorPortId, gateControlPortId, roomId, inactiveState,
                delayTime, code, iconName);
        getGates().add(device);
        onInvalidateCache("/DIGITALOUTPUTPORTS.JSON");
        onInvalidateCache("/DIGITALINPUTPORTS.JSON");
    }

    private void editGate(String name, String description, String magneticDetectorPortId, String gateControlPortId,
                                   String roomId, InactiveState inactiveState, String delayTime,
                                   String code, String iconName, String uniqueId) throws Exception {
        Gate device = getGates().find(uniqueId);
        if (!device.getGateControlPortId().equals(gateControlPortId)) {
            onInvalidateCache("/DIGITALOUTPUTPORTS.JSON");
        }

        if (!device.getMagneticDetectorPortId().equals(magneticDetectorPortId)) {
            onInvalidateCache("/DIGITALINPUTPORTS.JSON");
        }

        device.getName().setName(name);
        device.getName().setDescription(description);
        device.setInactiveState(inactiveState);
        device.setRoomId(roomId);
        device.setCode(code);
        device.setDelayTime(delayTime);
        device.setIconName(iconName);
    }

    @ConfigurationLoader(sectionName = "MovementDetector", parentId = "")
    public void modifyMovementDetector(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String portId = values.getValue("portid");
        String roomId = values.getValue("roomid");
        String inactiveState = values.getValue("inactivestate");
        InactiveState inactiveStateParsed = InactiveState.fromInt(Integer.parseInt(inactiveState));
        String delayTime = values.getValue("delaytime");
        if (action == CrudAction.AddOrCreate) {
            addMovementDetector(name, description, portId, roomId, inactiveStateParsed, delayTime, uniqueId);
        } else {
            editMovementDetector(name, description, portId, roomId, inactiveStateParsed, delayTime, uniqueId);
        }
        onInvalidateCache("/CONFIG/MOVEMENTDETECTORS.JSON");
        onInvalidateCache("/CONFIG/ALLDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLALARMSENSORS.JSON");
        onModified();
    }

    private void addMovementDetector(String name, String description, String portId, String roomId, InactiveState inactiveState, String delayTime,
                                     String uniqueId) throws Exception {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);

        DescriptiveName deviceName = new DescriptiveName(name, uniqueId, description);
        MovementDetector device = new MovementDetector(deviceName, portId, roomId, inactiveState, delayTime);
        getMovementDetectors().add(device);
        onInvalidateCache("/DIGITALINPUTPORTS.JSON");
    }

    private void editMovementDetector(String name, String description, String portId, String roomId, InactiveState inactiveState, String delayTime,
                                      String uniqueId) throws Exception {
        MovementDetector device = getMovementDetectors().find(uniqueId);
        if (!device.getPortId().equals(portId)) {
            onInvalidateCache("/DIGITALINPUTPORTS.JSON");
        }

        device.getName().setName(name);
        device.getName().setDescription(description);
        device.setPortId(portId);
        device.setRoomId(roomId);
        device.setInactiveState(inactiveState);
        device.setDelayTime(delayTime);
    }

    @ConfigurationLoader(sectionName = "Signalizator", parentId = "")
    public void modifySignalizator(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String portId = values.getValue("portid");
        String roomId = values.getValue("roomid");
        String minimumWorkingTime = values.getValue("minimumworkingtime");
        String maximumWorkingTime = values.getValue("maximumworkingtime");
        String alarmTypesIds = values.getValues("alarmtypesids");
        if (action == CrudAction.AddOrCreate) {
            addSignalizator(name, description, portId, roomId, alarmTypesIds, minimumWorkingTime, maximumWorkingTime, uniqueId);
        } else {
            editSignalizator(name, description, portId, roomId, alarmTypesIds, minimumWorkingTime, maximumWorkingTime, uniqueId);
        }
        onInvalidateCache("/CONFIG/SIGNALIZATORS.JSON");
        onInvalidateCache("/CONFIG/ALLDEVICES.JSON");
        onInvalidateCache("/DIGITALOUTPUTPORTS.JSON");
        onModified();
    }

    private void addSignalizator(String name, String description, String portId, String roomId, String alarmTypesIds, String minimumWorkingTime,
                                 String maximumWorkingTime, String uniqueId) throws Exception {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);

        DescriptiveName deviceName = new DescriptiveName(name, uniqueId, description);
        Signalizator device = new Signalizator(deviceName, portId, roomId, alarmTypesIds, minimumWorkingTime, maximumWorkingTime);
        getSignalizators().add(device);
        onInvalidateCache("/DIGITALINPUTPORTS.JSON");
    }

    private void editSignalizator(String name, String description, String portId, String roomId, String alarmTypesIds, String minimumWorkingTime,
                                  String maximumWorkingTime, String uniqueId) throws Exception {
        Signalizator device = getSignalizators().find(uniqueId);
        if (!device.getPortId().equals(portId)) {
            onInvalidateCache("/DIGITALINPUTPORTS.JSON");
        }

        device.getName().setName(name);
        device.getName().setDescription(description);
        device.setPortId(portId);
        device.setRoomId(roomId);
        device.setMinimumWorkingTime(minimumWorkingTime);
        device.setMaximumWorkingTime(maximumWorkingTime);
        device.setAlarmTypesIds(alarmTypesIds);
    }

    @ConfigurationLoader(sectionName = "CodeLock", parentId = "")
    public void modifyCodeLock(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String armingPortId = values.getValue("armingportid");
        String statusPortId = values.getValue("statusportid");
        String roomId = values.getValue("roomid");
        if (action == CrudAction.AddOrCreate) {
            addCodeLock(name, description, armingPortId, statusPortId, roomId, uniqueId);
        } else {
            editCodeLock(name, description, armingPortId, statusPortId, roomId, uniqueId);
        }
        onInvalidateCache("/CONFIG/CODELOCKS.JSON");
        onInvalidateCache("/CONFIG/ALLDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLLOCKS.JSON");
        onModified();
    }

    private void addCodeLock(String name, String description, String armingPortId, String statusPortId, String roomId, String uniqueId) throws Exception {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);

        DescriptiveName deviceName = new DescriptiveName(name, uniqueId, description);
        CodeLock device = new CodeLock(deviceName, roomId, armingPortId, statusPortId);
        getCodeLocks().add(device);
        onInvalidateCache("/DIGITALOUTPUTPORTS.JSON");
        onInvalidateCache("/DIGITALINPUTPORTS.JSON");
    }

    private void editCodeLock(String name, String description, String armingPortId, String statusPortId, String roomId, String uniqueId) throws Exception {
        CodeLock device = getCodeLocks().find(uniqueId);
        if (!device.getArmingPortId().equals(armingPortId)) {
            onInvalidateCache("/DIGITALINPUTPORTS.JSON");
        }

        if (!device.getStatusPortId().equals(statusPortId)) {
            onInvalidateCache("/DIGITALOUTPUTPORTS.JSON");
        }

        device.getName().setName(name);
        device.getName().setDescription(description);
        device.setArmingPortId(armingPortId);
        device.setStatusPortId(statusPortId);
        device.setRoomId(roomId);
    }

    @ConfigurationLoader(sectionName = "AlarmZone", parentId = "")
    public void modifyAlarmZone(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String roomsIds = values.getValues("roomsids");
        String alarmSensorsIds = values.getValues("alarmsensorsids");
        String locksIds = values.getValues("locksids");
        String alarmType = values.getValue("alarmtype");
        AlarmType alarmTypeParsed = AlarmType.fromInt(Integer.parseInt(alarmType));
        String leavingTime = values.getValue("leavingtime");
        String unlockingCode = values.getValue("unlockingcode");

        if (action == CrudAction.AddOrCreate) {
            addAlarmZone(name, description, roomsIds, alarmTypeParsed, alarmSensorsIds, locksIds, leavingTime, unlockingCode, uniqueId);
        } else {
            editAlarmZone(name, description, roomsIds, alarmTypeParsed, alarmSensorsIds, locksIds, leavingTime, unlockingCode, uniqueId);
        }
        onInvalidateCache("/CONFIG/ALARMZONES.JSON");
        onInvalidateCache("/CONFIG/ALLDEVICES.JSON");
        onInvalidateCache("/DIGITALOUTPUTPORTS.JSON");
        onInvalidateCache("/CONFIG/ALLBLOCKSTARGETDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLMULTISTATEDEVICES.JSON");
        onModified();
    }

    private void addAlarmZone(String name, String description, String roomsIds, AlarmType alarmType, String alarmSensorsIds, String locksIds, String leavingTime, String unlockingCode, String uniqueId) {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);

        DescriptiveName deviceName = new DescriptiveName(name, uniqueId, description);
        AlarmZone device = new AlarmZone(deviceName, roomsIds, alarmSensorsIds, locksIds, alarmType, leavingTime, unlockingCode);
        getAlarmZones().add(device);
    }

    private void editAlarmZone(String name, String description, String roomsIds, AlarmType alarmType, String alarmSensorsIds, String locksIds, String leavingTime, String unlockingCode, String uniqueId) throws ObjectNotFoundException {
        AlarmZone device = getAlarmZones().find(uniqueId);
        device.getName().setName(name);
        device.getName().setDescription(description);
        device.setRoomsIds(roomsIds);
        device.setAlarmType(alarmType);
        device.setAlarmSensorsIds(alarmSensorsIds);
        device.setLocksIds(locksIds);
        device.setLeavingTime(leavingTime);
        device.setUnlockingCode(unlockingCode);
    }

    @Override
    public void addDevicesCollectors(ArrayList<CollectorCollection<? extends IDevice>> devicesCollectors) {
        devicesCollectors.add(getAlarmSensors());
        devicesCollectors.add(getMagneticDetectors());
        devicesCollectors.add(getSignalizators());
        devicesCollectors.add(getMovementDetectors());
        devicesCollectors.add(getCodeLocks());
        devicesCollectors.add(getAlarmZones());
        devicesCollectors.add(getGates());
    }

    @Override
    public void addConditionsCollectors(ArrayList<CollectorCollection<? extends ICondition>> conditionCollectors) {
        conditionCollectors.add(getPresenceConditions());
    }

    @Override
    public ArrayList<CollectorCollection<? extends INamedObject>> buildAllCollections() {
        ArrayList<CollectorCollection<? extends INamedObject>> result = new ArrayList<>();
        result.add(getAlarmSensors());
        result.add(getPresenceConditions());
        result.add(getMagneticDetectors());
        result.add(getMovementDetectors());
        result.add(getCodeLocks());
        result.add(getSignalizators());
        result.add(getAlarmZones());
        result.add(getGates());

        return result;
    }
}