package com.geekhome.lightsmodule;

import com.geekhome.common.*;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.configuration.ICondition;
import com.geekhome.common.configuration.IDevice;
import com.geekhome.common.settings.AutomationSettings;
import com.geekhome.common.INameValueSet;
import com.geekhome.common.configuration.Collector;
import com.geekhome.common.configuration.CollectorCollection;
import com.geekhome.common.configuration.ObjectNotFoundException;

import java.util.ArrayList;

public class LightsConfiguration extends Collector {
    private final CollectorCollection<TwilightCondition> _twilightConditions;
    private final CollectorCollection<Blind> _blinds;
    private final CollectorCollection<RgbLamp> _rgbLamps;
    private final CollectorCollection<SevenColorLamp> _sevenColorLamps;
    private final CollectorCollection<Luxmeter> _luxmeters;
    private final AutomationSettings _automationSettings;

    @ConfigurationSaver(sectionName = "TwilightCondition", hasChildren = false)
    public CollectorCollection<TwilightCondition> getTwilightConditions() {
        return _twilightConditions;
    }

    @ConfigurationSaver(sectionName = "Blind", hasChildren = false)
    public CollectorCollection<Blind> getBlinds() {
        return _blinds;
    }

    @ConfigurationSaver(sectionName = "RgbLamp", hasChildren = false)
    public CollectorCollection<RgbLamp> getRgbLamps() {
        return _rgbLamps;
    }

    @ConfigurationSaver(sectionName = "7ColorLamp", hasChildren = false)
    public CollectorCollection<SevenColorLamp> get7ColorLamps() {
        return _sevenColorLamps;
    }

    @ConfigurationSaver(sectionName = "Luxmeter", hasChildren = false)
    public CollectorCollection<Luxmeter> getLuxmeters() {
        return _luxmeters;
    }

    public LightsConfiguration(IdPool pool, AutomationSettings automationSettings) {
        super(pool);
        _automationSettings = automationSettings;
        _twilightConditions = new CollectorCollection<>();
        _blinds = new CollectorCollection<>();
        _rgbLamps = new CollectorCollection<>();
        _sevenColorLamps = new CollectorCollection<>();
        _luxmeters = new CollectorCollection<>();
    }

    @Override
    public void clear() {
        getTwilightConditions().clear();
        getRgbLamps().clear();
        get7ColorLamps().clear();
        getLuxmeters().clear();
        getBlinds().clear();
    }

    @Override
    public String getPoolPrefix() {
        return "lights";
    }

    @ConfigurationLoader(sectionName = "Blind", parentId = "")
    public void modifyBlind(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String automaticControlPortId = values.getValue("automaticcontrolportid");
        String motorPortId = values.getValue("motorportid");
        String roomId = values.getValue("roomid");
        String closingTime = values.getValue("closingtime");
        String comfortSetTime = values.getValue("comfortsettime");
        if (action == CrudAction.AddOrCreate) {
            addBlind(name, description, automaticControlPortId, motorPortId, roomId, closingTime, comfortSetTime, uniqueId);
        } else {
            editBlind(name, description, automaticControlPortId, motorPortId, roomId, closingTime, comfortSetTime, uniqueId);
        }
        onInvalidateCache("/CONFIG/BLINDS.JSON");
        onInvalidateCache("/CONFIG/ALLDEVICES.JSON");
        onModified();
    }

    private void addBlind(String name, String description, String automaticControlPortId, String motorPortId, String roomId, String closingTime, String comfortSetTime, String uniqueId) throws Exception {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName deviceName = new DescriptiveName(name, uniqueId, description);
        Blind device = new Blind(deviceName, roomId, closingTime, comfortSetTime, automaticControlPortId, motorPortId);
        getBlinds().add(device);
        onInvalidateCache("/DIGITALOUTPUTPORTS.JSON");
    }

    private void editBlind(String name, String description, String automaticControlPortId, String motorPortId, String roomId, String closingTime, String comfortSetTime, String uniqueId) throws Exception {
        Blind device = getBlinds().find(uniqueId);
        if (!device.getAutomaticControlPortId().equals(automaticControlPortId) || (!device.getMotorPortId().equals(motorPortId))) {
            onInvalidateCache("/DIGITALOUTPUTPORTS.JSON");
        }
        device.getName().setName(name);
        device.getName().setDescription(description);
        device.setAutomaticControlPortId(automaticControlPortId);
        device.setMotorPortId(motorPortId);
        device.setClosingTime(closingTime);
        device.setComfortSetTime(comfortSetTime);
        device.setRoomId(roomId);
    }

    @ConfigurationLoader(sectionName = "RgbLamp", parentId = "")
    public void modifyRgbLamp(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String redPortId = values.getValue("redportid");
        String greenPortId = values.getValue("greenportid");
        String bluePortId = values.getValue("blueportid");
        String roomId = values.getValue("roomid");
        String preset1 = values.getValue("preset1");
        String preset2 = values.getValue("preset2");
        String preset3 = values.getValue("preset3");
        String preset4 = values.getValue("preset4");

        if (action == CrudAction.AddOrCreate) {
            addRgbLamp(name, description, redPortId, greenPortId, bluePortId, roomId, preset1, preset2, preset3, preset4, uniqueId);
        } else {
            editRgbLamp(name, description, redPortId, greenPortId, bluePortId, roomId, preset1, preset2, preset3, preset4, uniqueId);
        }
        onInvalidateCache("/CONFIG/RGBLAMPS.JSON");
        onInvalidateCache("/CONFIG/ALLDEVICES.JSON");
        onModified();
    }

    private void addRgbLamp(String name, String description, String redPortId, String greenPortId, String bluePortId,
                            String roomId, String preset1, String preset2, String preset3, String preset4, String uniqueId) throws Exception {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName deviceName = new DescriptiveName(name, uniqueId, description);
        RgbLamp device = new RgbLamp(deviceName, redPortId, greenPortId, bluePortId, roomId);
        getRgbLamps().add(device);
        onInvalidateCache("/POWEROUTPUTPORTS.JSON");
        onInvalidateCache("/TOGGLEPORTS.JSON");

        changeRgbPresetValue(device.getName().getUniqueId(), 1, preset1);
        changeRgbPresetValue(device.getName().getUniqueId(), 2, preset2);
        changeRgbPresetValue(device.getName().getUniqueId(), 3, preset3);
        changeRgbPresetValue(device.getName().getUniqueId(), 4, preset4);

        onModified();
    }

    private void editRgbLamp(String name, String description, String redPortId, String greenPortId, String bluePortId,
                             String roomId, String preset1, String preset2, String preset3, String preset4, String uniqueId) throws Exception {
        RgbLamp device = getRgbLamps().find(uniqueId);
        if (!device.getRedPortId().equals(redPortId) || !device.getBluePortId().equals(bluePortId) ||
                !device.getGreenPortId().equals(greenPortId)) {
            onInvalidateCache("/POWEROUTPUTPORTS.JSON");
            onInvalidateCache("/TOGGLEPORTS.JSON");
        }
        device.getName().setName(name);
        device.getName().setDescription(description);
        device.setRedPortId(redPortId);
        device.setGreenPortId(greenPortId);
        device.setBluePortId(bluePortId);
        device.setRoomId(roomId);

        changeRgbPresetValue(device.getName().getUniqueId(), 1, preset1);
        changeRgbPresetValue(device.getName().getUniqueId(), 2, preset2);
        changeRgbPresetValue(device.getName().getUniqueId(), 3, preset3);
        changeRgbPresetValue(device.getName().getUniqueId(), 4, preset4);
    }

    private void changeRgbPresetValue(String deviceId, int presetNo, String rawValue) {
        if (rawValue != null) {
            _automationSettings.changeSetting(deviceId + "_" + presetNo, String.valueOf(rawValue));
        }
    }


    @ConfigurationLoader(sectionName = "7ColorLamp", parentId = "")
    public void modify7ColorLamp(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String redPortId = values.getValue("redportid");
        String greenPortId = values.getValue("greenportid");
        String bluePortId = values.getValue("blueportid");
        String roomId = values.getValue("roomid");
        if (action == CrudAction.AddOrCreate) {
            add7ColorLamp(name, description, redPortId, greenPortId, bluePortId, roomId, uniqueId);
        } else {
            edit7ColorLamp(name, description, redPortId, greenPortId, bluePortId, roomId, uniqueId);
        }
        onInvalidateCache("/CONFIG/7COLORLAMPS.JSON");
        onInvalidateCache("/CONFIG/ALLDEVICES.JSON");
        onModified();
    }

    private void add7ColorLamp(String name, String description, String redPortId, String greenPortId, String bluePortId, String roomId, String uniqueId) throws Exception {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName deviceName = new DescriptiveName(name, uniqueId, description);
        SevenColorLamp device = new SevenColorLamp(deviceName, redPortId, greenPortId, bluePortId, roomId);
        get7ColorLamps().add(device);
        onInvalidateCache("/DIGITALOUTPUTPORTS.JSON");
        onInvalidateCache("/TOGGLEPORTS.JSON");
    }

    private void edit7ColorLamp(String name, String description, String redPortId, String greenPortId, String bluePortId, String roomId, String uniqueId) throws Exception {
        SevenColorLamp device = get7ColorLamps().find(uniqueId);
        if (!device.getRedPortId().equals(redPortId) || !device.getBluePortId().equals(bluePortId) ||
                !device.getGreenPortId().equals(greenPortId)) {
            onInvalidateCache("/DIGITALOUTPUTPORTS.JSON");
            onInvalidateCache("/TOGGLEPORTS.JSON");
        }
        device.getName().setName(name);
        device.getName().setDescription(description);
        device.setRedPortId(redPortId);
        device.setGreenPortId(greenPortId);
        device.setBluePortId(bluePortId);
        device.setRoomId(roomId);
    }

    @ConfigurationLoader(sectionName = "Luxmeter", parentId = "")
    public void modifyLuxmeter(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String portId = values.getValue("portid");
        String roomId = values.getValue("roomid");
        String deviceCategory = values.getValue("devicecategory");
        DeviceCategory deviceCategoryParsed = DeviceCategory.fromInt(Integer.parseInt(deviceCategory));

        if (action == CrudAction.AddOrCreate) {
            addLuxmeter(name, description, portId, roomId, deviceCategoryParsed, uniqueId);
        } else {
            editLuxmeter(name, description, portId, roomId, deviceCategoryParsed, uniqueId);
        }
        onInvalidateCache("/CONFIG/LUXMETERS.JSON");
        onInvalidateCache("/CONFIG/ALLDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLVALUEDEVICES.JSON");
        onModified();
    }

    private void addLuxmeter(String name, String description, String portId, String roomId, DeviceCategory deviceCategory, String uniqueId) throws Exception {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName deviceName = new DescriptiveName(name, uniqueId, description);
        Luxmeter device = new Luxmeter(deviceName, portId, roomId, deviceCategory);
        getLuxmeters().add(device);
        onInvalidateCache("/LUMINOSITYPORTS.JSON");
        onModified();
    }

    private void editLuxmeter(String name, String description, String portId, String roomId, DeviceCategory deviceCategory, String uniqueId) throws Exception {
        Luxmeter device = getLuxmeters().find(uniqueId);
        if (!device.getPortId().equals(portId)) {
            onInvalidateCache("/LUMINOSITYPORTS.JSON");
        }
        device.getName().setName(name);
        device.getName().setDescription(description);
        device.setPortId(portId);
        device.setRoomId(roomId);
        device.setDeviceCategory(deviceCategory);
    }

    @ConfigurationLoader(sectionName = "TwilightCondition", parentId = "")
    public void modifyTwilightCondition(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String longitude = values.getValue("longitude");
        double longitudeParsed = Double.parseDouble(longitude);
        String latitude = values.getValue("latitude");
        double latitudeParsed = Double.parseDouble(latitude);
        if (action == CrudAction.AddOrCreate) {
            addTwilightCondition(name, description, longitudeParsed, latitudeParsed, uniqueId);
        } else {
            editTwilightCondition(name, description, longitudeParsed, latitudeParsed, uniqueId);
        }
        onInvalidateCache("/CONFIG/TWILIGHTCONDITIONS.JSON");
        onInvalidateCache("/CONFIG/ALLCONDITIONS.JSON");
        onModified();
    }

    private void addTwilightCondition(String name, String description, double longitudeParsed, double latitudeParsed, String uniqueId) {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName conditionName = new DescriptiveName(name, uniqueId, description);
        TwilightCondition condition = new TwilightCondition(conditionName, longitudeParsed, latitudeParsed);
        getTwilightConditions().add(condition);
    }

    private void editTwilightCondition(String name, String description, double longitudeParsed, double latitudeParsed, String uniqueId) throws ObjectNotFoundException {
        TwilightCondition condition = getTwilightConditions().find(uniqueId);
        condition.getName().setName(name);
        condition.getName().setDescription(description);
        condition.setLatitude(latitudeParsed);
        condition.setLongitude(longitudeParsed);
    }

    @Override
    public void addDevicesCollectors(ArrayList<CollectorCollection<? extends IDevice>> devicesCollectors) {
        devicesCollectors.add(getBlinds());
        devicesCollectors.add(getRgbLamps());
        devicesCollectors.add(get7ColorLamps());
        devicesCollectors.add(getLuxmeters());
    }

    @Override
    public void addConditionsCollectors(ArrayList<CollectorCollection<? extends ICondition>> conditionCollectors) {
        conditionCollectors.add(getTwilightConditions());
    }

    @Override
    public ArrayList<CollectorCollection<? extends INamedObject>> buildAllCollections() {
        ArrayList<CollectorCollection<? extends INamedObject>> result = new ArrayList<>();
        result.add(getTwilightConditions());
        result.add(getBlinds());
        result.add(getRgbLamps());
        result.add(get7ColorLamps());
        result.add(getLuxmeters());

        return result;
    }
}

