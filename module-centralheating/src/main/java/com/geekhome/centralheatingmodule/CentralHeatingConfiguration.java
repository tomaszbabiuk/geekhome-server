package com.geekhome.centralheatingmodule;

import com.geekhome.common.*;
import com.geekhome.coremodule.ICondition;
import com.geekhome.coremodule.IDevice;
import com.geekhome.coremodule.InactiveState;
import com.geekhome.http.INameValueSet;
import com.geekhome.httpserver.modules.Collector;
import com.geekhome.httpserver.modules.CollectorCollection;
import com.geekhome.httpserver.modules.ObjectNotFoundException;

import java.util.ArrayList;

public class CentralHeatingConfiguration extends Collector {
    private CollectorCollection<Thermometer> _thermometers;
    private CollectorCollection<Hygrometer> _hygrometers;
    private CollectorCollection<AveragingThermometer> _averagingThermometers;
    private CollectorCollection<Comfortmeter> _comfortmeters;
    private CollectorCollection<HeatingManifold> _heatingManifolds;
    private CollectorCollection<AirConditioner> _airConditioners;
    private CollectorCollection<CirculationPump> _circulationPumps;
    private CollectorCollection<TemperatureController> _temperatureControllers;
    private CollectorCollection<ThermostatCondition> _thermostatConditions;
    private CollectorCollection<Radiator> _radiators;
    private CollectorCollection<UnderfloorCircuit> _underfloorCircuits;
    private CollectorCollection<RTLCircuit> _rtlCircuits;

    @ConfigurationSaver(sectionName = "Thermometer", hasChildren = false)
    public CollectorCollection<Thermometer> getThermometers() {
        return _thermometers;
    }

    @ConfigurationSaver(sectionName = "Hygrometer", hasChildren = false)
    public CollectorCollection<Hygrometer> getHygrometers() {
        return _hygrometers;
    }

    @ConfigurationSaver(sectionName = "AveragingThermometer", hasChildren = false)
    public CollectorCollection<AveragingThermometer> getAveragingThermometers() {
        return _averagingThermometers;
    }

    @ConfigurationSaver(sectionName = "Comfortmeter", hasChildren = false)
    public CollectorCollection<Comfortmeter> getComfortmeters() {
        return _comfortmeters;
    }

    @ConfigurationSaver(sectionName = "HeatingManifold", hasChildren = false)
    public CollectorCollection<HeatingManifold> getHeatingManifolds() {
        return _heatingManifolds;
    }

    @ConfigurationSaver(sectionName = "AirConditioner", hasChildren = false)
    public CollectorCollection<AirConditioner> getAirConditioners() {
        return _airConditioners;
    }

    @ConfigurationSaver(sectionName = "CirculationPump", hasChildren = false)
    public CollectorCollection<CirculationPump> getCirculationPumps() {
        return _circulationPumps;
    }

    @ConfigurationSaver(sectionName = "TemperatureController", hasChildren = false)
    public CollectorCollection<TemperatureController> getTemperatureControllers() {
        return _temperatureControllers;
    }

    @ConfigurationSaver(sectionName = "ThermostatCondition", hasChildren = false)
    public CollectorCollection<ThermostatCondition> getThermostatConditions() {
        return _thermostatConditions;
    }

    @ConfigurationSaver(sectionName = "Radiator", hasChildren = false)
    public CollectorCollection<Radiator> getRadiators() {
        return _radiators;
    }

    @ConfigurationSaver(sectionName = "UnderfloorCircuit", hasChildren = false)
    public CollectorCollection<UnderfloorCircuit> getUnderfloorCircuits() {
        return _underfloorCircuits;
    }

    @ConfigurationSaver(sectionName = "RTLCircuit", hasChildren = false)
    public CollectorCollection<RTLCircuit> getRTLCircuits() {
        return _rtlCircuits;
    }

    CentralHeatingConfiguration(IdPool pool) {
        super(pool);
        _thermometers = new CollectorCollection<>();
        _hygrometers = new CollectorCollection<>();
        _averagingThermometers = new CollectorCollection<>();
        _comfortmeters = new CollectorCollection<>();
        _heatingManifolds = new CollectorCollection<>();
        _airConditioners = new CollectorCollection<>();
        _circulationPumps = new CollectorCollection<>();
        _temperatureControllers = new CollectorCollection<>();
        _thermostatConditions = new CollectorCollection<>();
        _radiators = new CollectorCollection<>();
        _underfloorCircuits = new CollectorCollection<>();
        _rtlCircuits = new CollectorCollection<>();
    }

    @Override
    public void clear() {
        getThermometers().clear();
        getHygrometers().clear();
        getAveragingThermometers().clear();
        getComfortmeters().clear();
        getHeatingManifolds().clear();
        getAirConditioners().clear();
        getCirculationPumps().clear();
        getTemperatureControllers().clear();
        getThermostatConditions().clear();
        getRadiators().clear();
        getUnderfloorCircuits().clear();
        getRTLCircuits().clear();
    }

    @Override
    public String getPoolPrefix() {
        return "heating";
    }

    @SuppressWarnings("WeakerAccess")
    @ConfigurationLoader(sectionName = "Thermometer", parentId = "")
    public void modifyThermometer(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String portId = values.getValue("portid");
        String roomId = values.getValue("roomid");
        String deviceCategory = values.getValue("devicecategory");
        DeviceCategory deviceCategoryParsed = DeviceCategory.fromInt(Integer.parseInt(deviceCategory));

        if (action == CrudAction.AddOrCreate) {
            addThermometer(name, description, portId, roomId, deviceCategoryParsed, uniqueId);
        } else {
            editThermometer(name, description, portId, roomId, deviceCategoryParsed, uniqueId);
        }
        onInvalidateCache("/CONFIG/THERMOMETERS.JSON");
        onInvalidateCache("/CONFIG/ALLTHERMOMETERS.JSON");
        onInvalidateCache("/CONFIG/ALLDEVICES.JSON");
        onModified();
    }

    private void addThermometer(String name, String description, String portId, String roomId, DeviceCategory deviceCategory, String uniqueId) throws Exception {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName deviceName = new DescriptiveName(name, uniqueId, description);
        Thermometer device = new Thermometer(deviceName, portId, roomId, deviceCategory);
        getThermometers().add(device);
        onInvalidateCache("/TEMPERATUREPORTS.JSON");
        onModified();
    }

    private void editThermometer(String name, String description, String portId, String roomId, DeviceCategory deviceCategory, String uniqueId) throws Exception {
        Thermometer device = getThermometers().find(uniqueId);
        if (!device.getPortId().equals(portId)) {
            onInvalidateCache("/TEMPERATUREPORTS.JSON");
        }
        device.getName().setName(name);
        device.getName().setDescription(description);
        device.setPortId(portId);
        device.setRoomId(roomId);
        device.setDeviceCategory(deviceCategory);
    }

    @SuppressWarnings("WeakerAccess")
    @ConfigurationLoader(sectionName = "Hygrometer", parentId = "")
    public void modifyHygrometer(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String portId = values.getValue("portid");
        String roomId = values.getValue("roomid");
        String deviceCategory = values.getValue("devicecategory");
        DeviceCategory deviceCategoryParsed = DeviceCategory.fromInt(Integer.parseInt(deviceCategory));

        if (action == CrudAction.AddOrCreate) {
            addHygrometer(name, description, portId, roomId, deviceCategoryParsed, uniqueId);
        } else {
            editHygrometer(name, description, portId, roomId, deviceCategoryParsed, uniqueId);
        }
        onInvalidateCache("/CONFIG/HYGROMETERS.JSON");
        onInvalidateCache("/CONFIG/ALLDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLVALUEDEVICES.JSON");
        onModified();
    }

    private void addHygrometer(String name, String description, String portId, String roomId, DeviceCategory deviceCategory, String uniqueId) throws Exception {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName deviceName = new DescriptiveName(name, uniqueId, description);
        Hygrometer device = new Hygrometer(deviceName, portId, roomId, deviceCategory);
        getHygrometers().add(device);
        onInvalidateCache("/HUMIDITYPORTS.JSON");
        onModified();
    }

    private void editHygrometer(String name, String description, String portId, String roomId, DeviceCategory deviceCategory, String uniqueId) throws Exception {
        Hygrometer device = getHygrometers().find(uniqueId);
        if (!device.getPortId().equals(portId)) {
            onInvalidateCache("/HUMIDITYPORTS.JSON");
        }
        device.getName().setName(name);
        device.getName().setDescription(description);
        device.setPortId(portId);
        device.setRoomId(roomId);
        device.setDeviceCategory(deviceCategory);
    }

    @SuppressWarnings("WeakerAccess")
    @ConfigurationLoader(sectionName = "Comfortmeter", parentId = "")
    public void modifyComfortmeter(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String temperaturePortId = values.getValue("temperatureportid");
        String humidityPortId = values.getValue("humidityportid");
        String roomId = values.getValue("roomid");
        String deviceCategory = values.getValue("devicecategory");
        DeviceCategory deviceCategoryParsed = DeviceCategory.fromInt(Integer.parseInt(deviceCategory));

        if (action == CrudAction.AddOrCreate) {
            addComfortmeter(name, description, temperaturePortId, humidityPortId, roomId, deviceCategoryParsed, uniqueId);
        } else {
            editComfortmeter(name, description, temperaturePortId, humidityPortId, roomId, deviceCategoryParsed, uniqueId);
        }
        onInvalidateCache("/CONFIG/COMFORTMETERS.JSON");
        onInvalidateCache("/CONFIG/ALLTHERMOMETERS.JSON");
        onInvalidateCache("/CONFIG/ALLDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLVALUEDEVICES.JSON");
        onModified();
    }

    private void addComfortmeter(String name, String description, String temperaturePortId, String humidityPortId,
                                 String roomId, DeviceCategory deviceCategory, String uniqueId) throws Exception {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName deviceName = new DescriptiveName(name, uniqueId, description);
        Comfortmeter device = new Comfortmeter(deviceName, temperaturePortId, humidityPortId, roomId, deviceCategory);
        getComfortmeters().add(device);
        onInvalidateCache("/TEMPERATUREPORTS.JSON");
        onInvalidateCache("/HUMIDITYPORTS.JSON");
        onModified();
    }

    private void editComfortmeter(String name, String description, String temperaturePortId, String humidityPortId,
                                  String roomId, DeviceCategory deviceCategory, String uniqueId) throws Exception {
        Comfortmeter device = getComfortmeters().find(uniqueId);
        if (!device.getTemperaturePortId().equals(temperaturePortId)) {
            onInvalidateCache("/TEMPERATUREPORTS.JSON");
        }
        if (!device.getHumidityPortId().equals(humidityPortId)) {
            onInvalidateCache("/HUMIDITYPORTS.JSON");
        }
        device.getName().setName(name);
        device.getName().setDescription(description);
        device.setTemperaturePortId(temperaturePortId);
        device.setHumidityPortId(humidityPortId);
        device.setRoomId(roomId);
        device.setDeviceCategory(deviceCategory);
    }

    @SuppressWarnings("WeakerAccess")
    @ConfigurationLoader(sectionName = "AveragingThermometer", parentId = "")
    public void modifyAveragingThermometer(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String thermometersIds = values.getValues("thermometersids");
        String roomId = values.getValue("roomid");
        String deviceCategory = values.getValue("devicecategory");
        DeviceCategory deviceCategoryParsed = DeviceCategory.fromInt(Integer.parseInt(deviceCategory));
        if (action == CrudAction.AddOrCreate) {
            addAveragingThermometer(name, description,roomId, thermometersIds, deviceCategoryParsed, uniqueId);
        } else {
            editAveragingThermometer(name, description, roomId, thermometersIds, deviceCategoryParsed, uniqueId);
        }
        onInvalidateCache("/CONFIG/AVERAGINGTHERMOMETERS.JSON");
        onInvalidateCache("/CONFIG/ALLTHERMOMETERS.JSON");
        onInvalidateCache("/CONFIG/ALLDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLVALUEDEVICES.JSON");
        onModified();
    }

    private void addAveragingThermometer(String name, String description, String roomId, String thermometersIds, DeviceCategory deviceCategory, String uniqueId) {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName deviceName = new DescriptiveName(name, uniqueId, description);
        AveragingThermometer device = new AveragingThermometer(deviceName, roomId, thermometersIds, deviceCategory);
        getAveragingThermometers().add(device);
    }

    private void editAveragingThermometer(String name, String description, String roomId, String thermometersIds, DeviceCategory deviceCategory, String uniqueId) throws ObjectNotFoundException {
        AveragingThermometer device = getAveragingThermometers().find(uniqueId);
        device.getName().setName(name);
        device.getName().setDescription(description);
        device.setThermometersIds(thermometersIds);
        device.setRoomId(roomId);
        device.setDeviceCategory(deviceCategory);
    }

    @SuppressWarnings("WeakerAccess")
    @ConfigurationLoader(sectionName = "HeatingManifold", parentId = "")
    public void modifyHeatingManifold(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String pumpOrFurnacePortId = values.getValue("pumporfurnaceportid");
        String actuatorsTransformerPortId = values.getValue("actuatorstransformerportid");
        String roomId = values.getValue("roomid");
        String minimumWorkingTime = values.getValue("minimumworkingtime");
        String circuitsIds = values.getValues("circuitsids");
        if (action == CrudAction.AddOrCreate) {
            addHeatingManifold(name, description, pumpOrFurnacePortId, actuatorsTransformerPortId, roomId, minimumWorkingTime, circuitsIds, uniqueId);
        } else {
            editHeatingManifold(name, description, pumpOrFurnacePortId, actuatorsTransformerPortId, roomId, minimumWorkingTime, circuitsIds, uniqueId);
        }
        onInvalidateCache("/CONFIG/HEATINGMANIFOLDS.JSON");
        onInvalidateCache("/CONFIG/ALLDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLMULTISTATEDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLBLOCKSTARGETDEVICES.JSON");
        onModified();
    }

    private void addHeatingManifold(String name, String description, String pumpOrFurnacePortId, String actuatorsTransformerPortId, String roomId,
                                    String minimumWorkingTime, String circuitsIds, String uniqueId) throws Exception {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName deviceName = new DescriptiveName(name, uniqueId, description);
        HeatingManifold device = new HeatingManifold(deviceName, pumpOrFurnacePortId, actuatorsTransformerPortId, roomId, minimumWorkingTime, circuitsIds);
        getHeatingManifolds().add(device);
        onInvalidateCache("/DIGITALOUTPUTPORTS.JSON");
    }

    private void editHeatingManifold(String name, String description, String pumpOrFurnacePortId, String actuatorsTransformerPortId, String roomId,
                                     String minimumWorkingTime, String circuitsIds, String uniqueId) throws Exception {
        HeatingManifold device = getHeatingManifolds().find(uniqueId);
        if (!pumpOrFurnacePortId.equals(device.getPumpOrFurnacePortId()) || !actuatorsTransformerPortId.equals(device.getActuatorsTransformerPortId())) {
            onInvalidateCache("/DIGITALOUTPUTPORTS.JSON");
        }
        device.getName().setName(name);
        device.getName().setDescription(description);
        device.setPumpOrFurnacePortId(pumpOrFurnacePortId);
        device.setActuatorsTransformerPortId(actuatorsTransformerPortId);
        device.setRoomId(roomId);
        device.setMinimumWorkingTime(minimumWorkingTime);
        device.setCircuitsIds(circuitsIds);
    }

    @SuppressWarnings("WeakerAccess")
    @ConfigurationLoader(sectionName = "AirConditioner", parentId = "")
    public void modifyAirConditioner(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String heatingPortId = values.getValue("heatingportid");
        String coolingPortId = values.getValue("coolingportid");
        String forceManualPortId = values.getValue("forcemanualportid");
        String powerOutputPortId = values.getValue("powerportid");
        String roomId = values.getValue("roomid");
        String temperatureControllerId = values.getValue("temperaturecontrollerid");
        if (temperatureControllerId.equals("createnew")) {
            temperatureControllerId = addTemperatureController(name, description, roomId, 5, 25, 20, "", DeviceCategory.Heating, "");
            onInvalidateCache("/CONFIG/TEMPERATURECONTROLLERS.JSON");
        }

        if (action == CrudAction.AddOrCreate) {
            addAirConditioner(name, description, heatingPortId, coolingPortId, forceManualPortId, powerOutputPortId, roomId, temperatureControllerId, uniqueId);
        } else {
            editAirConditioner(name, description, heatingPortId, coolingPortId, forceManualPortId, powerOutputPortId, roomId, temperatureControllerId, uniqueId);
        }
        onInvalidateCache("/CONFIG/AIRCONDITIONERS.JSON");
        onInvalidateCache("/CONFIG/ALLDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLMULTISTATEDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLBLOCKSTARGETDEVICES.JSON");
        onModified();
    }

    private void addAirConditioner(String name, String description, String heatingPortId, String coolingPortId,
                                   String forceManualPortId, String powerOutputPortId, String roomId,
                                   String temperatureControllerId, String uniqueId) throws Exception {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName deviceName = new DescriptiveName(name, uniqueId, description);
        AirConditioner device = new AirConditioner(deviceName, heatingPortId, coolingPortId, forceManualPortId, powerOutputPortId, roomId, temperatureControllerId);
        getAirConditioners().add(device);
        onInvalidateCache("/DIGITALOUTPUTPORTS.JSON");
    }

    private void editAirConditioner(String name, String description, String heatingPortId, String coolingPortId,
                                    String forceManualPortId, String powerOutputPortId, String roomId,
                                    String temperatureControllerId, String uniqueId) throws Exception {
        AirConditioner device = getAirConditioners().find(uniqueId);
        if (!heatingPortId.equals(device.getHeatingModePortId()) ||
            !coolingPortId.equals(device.getCoolingModePortId()) ||
            !forceManualPortId.equals(device.getForceManualPortId())) {
            onInvalidateCache("/DIGITALOUTPUTPORTS.JSON");
        }

        if (!powerOutputPortId.equals(device.getPowerOutputPortId())) {
            onInvalidateCache("/POWEROUTPUTPORTS.JSON");
        }

        device.getName().setName(name);
        device.getName().setDescription(description);
        device.setHeatingModePortId(heatingPortId);
        device.setCoolingModePortId(coolingPortId);
        device.setForceManualPortId(forceManualPortId);
        device.setPowerOutputPortId(powerOutputPortId);
        device.setTemperatureControllerId(temperatureControllerId);
        device.setRoomId(roomId);
    }


    @SuppressWarnings("WeakerAccess")
    @ConfigurationLoader(sectionName = "CirculationPump", parentId = "")
    public void modifyCirculationPump(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String portId = values.getValue("portid");
        String roomId = values.getValue("roomid");
        String minimumWorkingTime = values.getValue("minimumworkingtime");
        String thermometerId = values.getValue("thermometerid");
        if (action == CrudAction.AddOrCreate) {
            addCirculationPump(name, description, portId, roomId, minimumWorkingTime, thermometerId, uniqueId);
        } else {
            editCirculationPump(name, description, portId, roomId, minimumWorkingTime, thermometerId, uniqueId);
        }
        onInvalidateCache("/CONFIG/CIRCULATIONPUMPS.JSON");
        onInvalidateCache("/CONFIG/ALLDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLMULTISTATEDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLBLOCKSTARGETDEVICES.JSON");
        onModified();
    }

    private void addCirculationPump(String name, String description, String portId, String roomId, String minimumWorkingTime,
                                    String thermometerId, String uniqueId) throws Exception {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName deviceName = new DescriptiveName(name, uniqueId, description);
        CirculationPump device = new CirculationPump(deviceName, portId, roomId, minimumWorkingTime, thermometerId);
        getCirculationPumps().add(device);
        onInvalidateCache("/DIGITALOUTPUTPORTS.JSON");
    }

    private void editCirculationPump(String name, String description, String portId, String roomId, String minimumWorkingTime,
                                     String thermometerId, String uniqueId) throws Exception {
        CirculationPump device = getCirculationPumps().find(uniqueId);
        if (!portId.equals(device.getPortId())) {
            onInvalidateCache("/DIGITALOUTPUTPORTS.JSON");
        }
        device.getName().setName(name);
        device.getName().setDescription(description);
        device.setPortId(portId);
        device.setRoomId(roomId);
        device.setMinimumWorkingTime(minimumWorkingTime);
        device.setThermometerId(thermometerId);
    }

    @SuppressWarnings("WeakerAccess")
    @ConfigurationLoader(sectionName = "TemperatureController", parentId = "")
    public void modifyTemperatureController(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String roomId = values.getValue("roomid");
        String modesIds = values.getValues("modesids");
        String minValue = values.getValue("minvalue");
        double minValueParsed = Double.valueOf(minValue);
        String maxValue = values.getValue("maxvalue");
        double maxValueParsed = Double.valueOf(maxValue);
        String initialValue = values.getValue("initialvalue");
        double initialValueParsed = Double.valueOf(initialValue);
        String deviceCategory = values.getValue("devicecategory");
        DeviceCategory deviceCategoryParsed = DeviceCategory.fromInt(Integer.parseInt(deviceCategory));
        if (action == CrudAction.AddOrCreate) {
            addTemperatureController(name, description, roomId, minValueParsed, maxValueParsed, initialValueParsed, modesIds, deviceCategoryParsed, uniqueId);
        } else {
            editTemperatureController(name, description, roomId, minValueParsed, maxValueParsed, initialValueParsed, modesIds, deviceCategoryParsed, uniqueId);
        }
        onInvalidateCache("/CONFIG/TEMPERATURECONTROLLERS.JSON");
        onInvalidateCache("/CONFIG/ALLDEVICES.JSON");
        onModified();
    }

    private String addTemperatureController(String name, String description, String roomId, double minValue, double maxValue,
                                          double initialValue, String modesIds, DeviceCategory deviceCategory, String uniqueId) {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName deviceName = new DescriptiveName(name, uniqueId, description);
        TemperatureController device = new TemperatureController(deviceName, roomId, minValue, maxValue, initialValue, modesIds, deviceCategory);
        getTemperatureControllers().add(device);
        return uniqueId;
    }

    private void editTemperatureController(String name, String description, String roomId, double minValue, double maxValue,
                                           double initialValue, String modesIds, DeviceCategory deviceCategory, String uniqueId) throws ObjectNotFoundException {
        TemperatureController device = getTemperatureControllers().find(uniqueId);
        device.getName().setName(name);
        device.getName().setDescription(description);
        device.setRoomId(roomId);
        device.setModesIds(modesIds);
        device.setMinValue(minValue);
        device.setMaxValue(maxValue);
        device.setInitialValue(initialValue);
        device.setDeviceCategory(deviceCategory);
    }

    @SuppressWarnings("WeakerAccess")
    @ConfigurationLoader(sectionName = "ThermostatCondition", parentId = "")
    public void modifyThermostatCondition(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String thermometerId = values.getValue("thermometerid");
        String temperatureControllerId = values.getValue("temperaturecontrollerid");
        String equalityOperator = values.getValue("operator");
        EqualityOperator equalityOperatorParsed = EqualityOperator.fromInt(Integer.parseInt(equalityOperator));
        if (action == CrudAction.AddOrCreate) {
            addThermostatCondition(name, description, equalityOperatorParsed, thermometerId, temperatureControllerId, uniqueId);
        } else {
            editThermostatCondition(name, description, equalityOperatorParsed, thermometerId, temperatureControllerId, uniqueId);
        }
        onInvalidateCache("/CONFIG/THERMOSTATCONDITIONS.JSON");
        onInvalidateCache("/CONFIG/ALLCONDITIONS.JSON");
        onModified();
    }

    private void addThermostatCondition(String name, String description, EqualityOperator equalityOperator,
                                        String thermometerId, String temperatureControllerId, String uniqueId) {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName thermostatConditionName = new DescriptiveName(name, uniqueId, description);
        ThermostatCondition thermostatCondition = new ThermostatCondition(thermostatConditionName, equalityOperator, thermometerId, temperatureControllerId);
        getThermostatConditions().add(thermostatCondition);
    }

    private void editThermostatCondition(String name, String description, EqualityOperator equalityOperator,
                                         String thermometerId, String temperatureControllerId, String uniqueId) throws ObjectNotFoundException {
        ThermostatCondition condition = getThermostatConditions().find(uniqueId);
        condition.getName().setName(name);
        condition.getName().setDescription(description);
        condition.setOperator(equalityOperator);
        condition.setTemperatureControllerId(temperatureControllerId);
        condition.setThermometerId(thermometerId);
    }

    @SuppressWarnings("WeakerAccess")
    @ConfigurationLoader(sectionName = "Radiator", parentId = "")
    public void modifyRadiator(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String roomId = values.getValue("roomid");
        String actuatorPortId = values.getValue("actuatorportid");
        String inactiveState = values.getValue("inactivestate");
        InactiveState inactiveStateParsed = InactiveState.fromInt(Integer.parseInt(inactiveState));
        String ambientThermometeId = values.getValue("ambientthermometerid");
        String activationTime = values.getValue("activationtime");
        String temperatureControllerId = values.getValue("temperaturecontrollerid");
        if (temperatureControllerId.equals("createnew")) {
            temperatureControllerId = addTemperatureController(name, description, roomId, 5, 25, 20, "", DeviceCategory.Heating, "");
            onInvalidateCache("/CONFIG/TEMPERATURECONTROLLERS.JSON");
        }
        if (action == CrudAction.AddOrCreate) {
            addRadiator(name, description, actuatorPortId, roomId, inactiveStateParsed, activationTime, ambientThermometeId, temperatureControllerId, uniqueId);
        } else {
            editRadiator(name, description, actuatorPortId, roomId, inactiveStateParsed, activationTime, ambientThermometeId, temperatureControllerId, uniqueId);
        }
        onInvalidateCache("/CONFIG/RADIATORS.JSON");
        onInvalidateCache("/CONFIG/ALLCIRCUITS.JSON");
        onInvalidateCache("/CONFIG/ALLDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLMULTISTATEDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLBLOCKSTARGETDEVICES.JSON");
        onModified();
    }

    private void addRadiator(String name, String description, String actuatorPortId, String roomIds, InactiveState inactiveState, String activationTime,
                                String ambientThermometerId, String temperatureControllerId, String uniqueId) {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName deviceName = new DescriptiveName(name, uniqueId, description);
        Radiator device = new Radiator(deviceName, actuatorPortId, roomIds, inactiveState, activationTime, ambientThermometerId, temperatureControllerId);
        getRadiators().add(device);
        onInvalidateCache("/DIGITALOUTPUTPORTS.JSON");
    }

    private void editRadiator(String name, String description, String actuatorPortId, String roomId, InactiveState inactiveState, String activationTime,
                                 String ambientThermometerId, String temperatureControllerId, String uniqueId) throws Exception {
        Radiator device = getRadiators().find(uniqueId);
        device.getName().setName(name);
        device.getName().setDescription(description);
        if (!device.getPortId().equals(actuatorPortId)) {
            onInvalidateCache("/DIGITALOUTPUTPORTS.JSON");
        }
        device.setPortId(actuatorPortId);
        device.setRoomId(roomId);
        device.setInactiveState(inactiveState);
        device.setActivationTime(activationTime);
        device.setAmbientThermometerId(ambientThermometerId);
        device.setTemperatureControllerId(temperatureControllerId);
    }

    @SuppressWarnings("WeakerAccess")
    @ConfigurationLoader(sectionName = "UnderfloorCircuit", parentId = "")
    public void modifyUnderfloorCircuit(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String roomId = values.getValue("roomid");
        String actuatorPortId = values.getValue("actuatorportid");
        String inactiveState = values.getValue("inactivestate");
        InactiveState inactiveStateParsed = InactiveState.fromInt(Integer.parseInt(inactiveState));
        String ambientThermometerId = values.getValue("ambientthermometerid");
        String floorThermometerId = values.getValue("floorthermometerid");
        String minFloorTemperature = values.getValue("minfloortemperature");
        double minFloorTemperatureParsed = Double.valueOf(minFloorTemperature);
        String maxFloorTemperature = values.getValue("maxfloortemperature");
        double maxFloorTemperatureParsed = Double.valueOf(maxFloorTemperature);
        String activationTime = values.getValue("activationtime");
        String temperatureControllerId = values.getValue("temperaturecontrollerid");
        if (temperatureControllerId.equals("createnew")) {
            temperatureControllerId = addTemperatureController(name, description, roomId, 5, 25, 20, "", DeviceCategory.Heating, "");
            onInvalidateCache("/CONFIG/TEMPERATURECONTROLLERS.JSON");
        }
        if (action == CrudAction.AddOrCreate) {
            addUnderfloorCircuit(name, description, actuatorPortId, roomId, inactiveStateParsed, activationTime, ambientThermometerId, floorThermometerId,
                    temperatureControllerId, minFloorTemperatureParsed, maxFloorTemperatureParsed, uniqueId);
        } else {
            editUnderfloorCircuit(name, description, actuatorPortId, roomId, inactiveStateParsed, activationTime, ambientThermometerId, floorThermometerId,
                    temperatureControllerId, minFloorTemperatureParsed, maxFloorTemperatureParsed, uniqueId);
        }
        onInvalidateCache("/CONFIG/UNDERFLOORCIRCUITS.JSON");
        onInvalidateCache("/CONFIG/ALLCIRCUITS.JSON");
        onInvalidateCache("/CONFIG/ALLDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLMULTISTATEDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLBLOCKSTARGETDEVICES.JSON");
        onModified();
    }

    private void addUnderfloorCircuit(String name, String description, String actuatorPortId, String roomIds, InactiveState inactiveState, String activationTime,
                                      String ambientThermometerId, String floorThermometerId, String temperatureControllerId,
                                      Double minFloorTemperature, Double maxFloorTemperature, String uniqueId) {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName deviceName = new DescriptiveName(name, uniqueId, description);
        UnderfloorCircuit device = new UnderfloorCircuit(deviceName, actuatorPortId, roomIds, inactiveState, activationTime, ambientThermometerId, floorThermometerId,
                temperatureControllerId, minFloorTemperature, maxFloorTemperature);
        getUnderfloorCircuits().add(device);
    }

    private void editUnderfloorCircuit(String name, String description, String actuatorPortId, String roomId, InactiveState inactiveState, String activationTime,
                                       String ambientThermometerId, String floorThermometerId, String temperatureControllerId,
                                       Double minFloorTemperature, Double maxFloorTemperature, String uniqueId) throws Exception {
        UnderfloorCircuit device = getUnderfloorCircuits().find(uniqueId);
        device.getName().setName(name);
        device.getName().setDescription(description);
        if (!device.getPortId().equals(actuatorPortId)) {
            onInvalidateCache("/DIGITALOUTPUTPORTS.JSON");
        }
        device.setPortId(actuatorPortId);
        device.setRoomId(roomId);
        device.setInactiveState(inactiveState);
        device.setActivationTime(activationTime);
        device.setAmbientThermometerId(ambientThermometerId);
        device.setFloorThermometerId(floorThermometerId);
        device.setTemperatureControllerId(temperatureControllerId);
        device.setMinFloorTemperature(minFloorTemperature);
        device.setMaxFloorTemperature(maxFloorTemperature);
    }

    @SuppressWarnings("WeakerAccess")
    @ConfigurationLoader(sectionName = "RTLCircuit", parentId = "")
    public void modifyRTLCircuit(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String roomId = values.getValue("roomid");
        String actuatorPortId = values.getValue("actuatorportid");
        String inactiveState = values.getValue("inactivestate");
        InactiveState inactiveStateParsed = InactiveState.fromInt(Integer.parseInt(inactiveState));
        String ambientThermometeId = values.getValue("ambientthermometerid");
        String returnThermometerId = values.getValue("returnthermometerid");
        String minReturnTemperature = values.getValue("minreturntemperature");
        double minReturnTemperatureParsed = Double.valueOf(minReturnTemperature);
        String maxReturnTemperature = values.getValue("maxreturntemperature");
        double maxReturnTemperatureParsed = Double.valueOf(maxReturnTemperature);
        String activationTime = values.getValue("activationtime");
        String temperatureControllerId = values.getValue("temperaturecontrollerid");
        if (temperatureControllerId.equals("createnew")) {
            temperatureControllerId = addTemperatureController(name, description, roomId, 5, 25, 20, "", DeviceCategory.Heating, "");
            onInvalidateCache("/CONFIG/TEMPERATURECONTROLLERS.JSON");
        }
        if (action == CrudAction.AddOrCreate) {
            addRtlCircuit(name, description, actuatorPortId, roomId, inactiveStateParsed, activationTime, ambientThermometeId, returnThermometerId,
                    temperatureControllerId, minReturnTemperatureParsed, maxReturnTemperatureParsed, uniqueId);
        } else {
            editRtlCircuit(name, description, actuatorPortId, roomId, inactiveStateParsed, activationTime, ambientThermometeId, returnThermometerId,
                    temperatureControllerId, minReturnTemperatureParsed, maxReturnTemperatureParsed, uniqueId);
        }
        onInvalidateCache("/CONFIG/RTLCIRCUITS.JSON");
        onInvalidateCache("/CONFIG/ALLDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLMULTISTATEDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLCIRCUITS.JSON");
        onInvalidateCache("/CONFIG/ALLBLOCKSTARGETDEVICES.JSON");
        onModified();
    }

    private void addRtlCircuit(String name, String description, String actuatorPortId, String roomIds, InactiveState inactiveState, String activationTime,
                                String ambientThermometerId, String returnThermometerId, String temperatureControllerId,
                                Double minReturnTemperature, Double maxReturnTemperature, String uniqueId) {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName deviceName = new DescriptiveName(name, uniqueId, description);
        RTLCircuit device = new RTLCircuit(deviceName, actuatorPortId, roomIds, inactiveState, activationTime, ambientThermometerId, returnThermometerId,
                temperatureControllerId, minReturnTemperature, maxReturnTemperature);
        getRTLCircuits().add(device);
    }

    private void editRtlCircuit(String name, String description, String actuatorPortId, String roomId, InactiveState inactiveState, String activationTime,
                                 String ambientThermometerId, String returnThermometerId,  String temperatureControllerId,
                                 Double minReturnTemperature, Double maxReturnTemperature, String uniqueId) throws Exception {
        RTLCircuit device = getRTLCircuits().find(uniqueId);
        device.getName().setName(name);
        device.getName().setDescription(description);
        if (!device.getPortId().equals(actuatorPortId)) {
            onInvalidateCache("/DIGITALOUTPUTPORTS.JSON");
        }
        device.setPortId(actuatorPortId);
        device.setRoomId(roomId);
        device.setInactiveState(inactiveState);
        device.setActivationTime(activationTime);
        device.setAmbientThermometerId(ambientThermometerId);
        device.setReturnThermometerId(returnThermometerId);
        device.setTemperatureControllerId(temperatureControllerId);
        device.setMinReturnTemperature(minReturnTemperature);
        device.setMaxReturnTemperature(maxReturnTemperature);
    }

    @Override
    public void addDevicesCollectors(ArrayList<CollectorCollection<? extends IDevice>> devicesCollectors) {
        devicesCollectors.add(getCirculationPumps());
        devicesCollectors.add(getHeatingManifolds());
        devicesCollectors.add(getAirConditioners());
        devicesCollectors.add(getThermometers());
        devicesCollectors.add(getHygrometers());
        devicesCollectors.add(getAveragingThermometers());
        devicesCollectors.add(getComfortmeters());
        devicesCollectors.add(getTemperatureControllers());
        devicesCollectors.add(getRadiators());
        devicesCollectors.add(getUnderfloorCircuits());
        devicesCollectors.add(getRTLCircuits());
    }

    @Override
    public void addConditionsCollectors(ArrayList<CollectorCollection<? extends ICondition>> conditionCollectors) {
        conditionCollectors.add(getThermostatConditions());
    }

    public CollectorCollection<Radiator> buildAllCircuits() {
        CollectorCollection<Radiator> devices = new CollectorCollection<>();
        devices.putAll(getRadiators());
        devices.putAll(getUnderfloorCircuits());
        devices.putAll(getRTLCircuits());
        return devices;
    }

    public CollectorCollection<IDevice> buildAllThermometers() {
        CollectorCollection<IDevice> devices = new CollectorCollection<>();
        devices.putAll(getThermometers());
        devices.putAll(getAveragingThermometers());
        devices.putAll(getComfortmeters());
        return devices;
    }

    @Override
    public ArrayList<CollectorCollection<? extends INamedObject>> buildAllCollections() {
        ArrayList<CollectorCollection<? extends INamedObject>> result = new ArrayList<>();
        result.add(getHeatingManifolds());
        result.add(getAirConditioners());
        result.add(getCirculationPumps());
        result.add(getThermometers());
        result.add(getHygrometers());
        result.add(getAveragingThermometers());
        result.add(getComfortmeters());
        result.add(getTemperatureControllers());
        result.add(getThermostatConditions());
        result.add(getRadiators());
        result.add(getUnderfloorCircuits());
        result.add(getRTLCircuits());
        return result;
    }
}