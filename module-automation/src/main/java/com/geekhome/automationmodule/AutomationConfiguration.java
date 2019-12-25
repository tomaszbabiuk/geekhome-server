package com.geekhome.automationmodule;

import com.geekhome.common.*;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.configuration.IDevice;
import com.geekhome.coremodule.*;
import com.geekhome.coremodule.settings.AutomationSettings;
import com.geekhome.http.INameValueSet;
import com.geekhome.httpserver.modules.Collector;
import com.geekhome.httpserver.modules.CollectorCollection;
import com.geekhome.httpserver.modules.ObjectNotFoundException;

import java.util.ArrayList;

public class AutomationConfiguration extends Collector {
    private MasterConfiguration _masterConfiguration;
    private final AutomationSettings _automationSettings;
    private CollectorCollection<OnOffDeviceBase> _onOffDevices;
    private CollectorCollection<ImpulseSwitch> _impulseSwitches;
    private CollectorCollection<NfcCondition> _nfcConditions;
    private final CollectorCollection<IntensityDevice> _intensityDevices;
    private final CollectorCollection<PowerMeter> _powerMeters;

    @ConfigurationSaver(sectionName = "IntensityDevice", hasChildren = false)
    public CollectorCollection<IntensityDevice> getIntensityDevices() {
        return _intensityDevices;
    }

    @ConfigurationSaver(sectionName = "OnOffDevice", hasChildren = false)
    public CollectorCollection<OnOffDeviceBase> getOnOffDevices() {
        return _onOffDevices;
    }

    @ConfigurationSaver(sectionName = "NfcCondition", hasChildren = false)
    public CollectorCollection<NfcCondition> getNfcConditions() {
        return _nfcConditions;
    }

    @ConfigurationSaver(sectionName = "ImpulseSwitch", hasChildren = false)
    public CollectorCollection<ImpulseSwitch> getImpulseSwitches() {
        return _impulseSwitches;
    }

    @ConfigurationSaver(sectionName = "PowerMeter", hasChildren = false)
    public CollectorCollection<PowerMeter> getPowerMeters() {
        return _powerMeters;
    }

    public AutomationConfiguration(final IdPool pool,
                                   final MasterConfiguration masterConfiguration,
                                   final AutomationSettings automationSettings) {
        super(pool);
        _masterConfiguration = masterConfiguration;
        _automationSettings = automationSettings;
        _onOffDevices = new CollectorCollection<>();
        _impulseSwitches = new CollectorCollection<>();
        _nfcConditions = new CollectorCollection<>();
        _intensityDevices = new CollectorCollection<>();
        _powerMeters = new CollectorCollection<>();
    }

    @Override
    public void clear() {
        getOnOffDevices().clear();
        getImpulseSwitches().clear();
        getNfcConditions().clear();
        getIntensityDevices().clear();
        getPowerMeters().clear();
    }

    @Override
    public String getPoolPrefix() {
        return "auto";
    }

    @ConfigurationLoader(sectionName = "OnOffDevice", parentId = "")
    public void modifyOnOffDevice(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String portId = values.getValue("portid");
        String roomId = values.getValue("roomid");
        String triggeredManually = values.getValue("triggeredmanually");
        YesNo triggeredManuallyParsed = YesNo.fromInt(Integer.parseInt(triggeredManually));
        String deviceCategory = values.getValue("devicecategory");
        DeviceCategory deviceCategoryParsed = DeviceCategory.fromInt(Integer.parseInt(deviceCategory));
        String iconName = values.getValue("iconname");
        if (action == CrudAction.AddOrCreate) {
            addOnOffDevice(name, description, iconName, portId, roomId, triggeredManuallyParsed, deviceCategoryParsed, uniqueId);
        } else {
            editOnOffDevice(name, description, iconName, portId, roomId, triggeredManuallyParsed,deviceCategoryParsed, uniqueId);
        }
        onInvalidateCache("/CONFIG/ONOFFDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLMULTISTATEDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLBLOCKSTARGETDEVICES.JSON");
        onModified();
    }

    private void addOnOffDevice(String name, String description, String iconName, String portId, String roomId, YesNo triggeredManually, DeviceCategory deviceCategory, String uniqueId) throws Exception {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName deviceName = new DescriptiveName(name, uniqueId, description);
        OnOffDeviceBase device = new OnOffDevice(deviceName, iconName, portId, roomId, triggeredManually, deviceCategory);
        getOnOffDevices().add(device);
        onInvalidateCache("/DIGITALOUTPUTPORTS.JSON");
    }

    private void editOnOffDevice(String name, String description, String iconName, String portId, String roomId, YesNo triggeredManually, DeviceCategory deviceCategory, String uniqueId) throws Exception {
        OnOffDeviceBase device = getOnOffDevices().find(uniqueId);
        if (!device.getPortId().equals(portId)) {
            onInvalidateCache("/DIGITALOUTPUTPORTS.JSON");
        }
        device.getName().setName(name);
        device.getName().setDescription(description);
        device.setPortId(portId);
        device.setRoomId(roomId);
        device.setTriggeredManually(triggeredManually);
        device.setIconName(iconName);
        device.setDeviceCategory(deviceCategory);
    }

    @ConfigurationLoader(sectionName = "ImpulseSwitch", parentId = "")
    public void modifyImpulseSwitch(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String roomId = values.getValue("roomid");
        String portId = values.getValue("portid");
        String impulseTime = values.getValue("impulsetime");
        String createMultistateCondition = values.getValues("createmultistatecondition");
        String deviceCategory = values.getValue("devicecategory");
        DeviceCategory deviceCategoryParsed = DeviceCategory.fromInt(Integer.parseInt(deviceCategory));
        if (action == CrudAction.AddOrCreate) {
            addImpulseSwitch(name, description, portId, roomId, impulseTime, deviceCategoryParsed, createMultistateCondition, uniqueId);
        } else {
            editImpulseSwitch(name, description, portId, roomId, impulseTime, deviceCategoryParsed, uniqueId);
        }
        onInvalidateCache("/CONFIG/IMPULSESWITCHES.JSON");
        onInvalidateCache("/CONFIG/ALLDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLMULTISTATEDEVICES.JSON");
        onModified();
    }

    private void addImpulseSwitch(String name, String description, String portId, String roomId, String impulseTime, DeviceCategory deviceCategory, String createMultistateCondition, String uniqueId) throws Exception {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName deviceName = new DescriptiveName(name, uniqueId, description);
        ImpulseSwitch device = new ImpulseSwitch(deviceName, portId, roomId, impulseTime, deviceCategory);
        getImpulseSwitches().add(device);

        _masterConfiguration.createMultistateConditionWhenRequested(name, uniqueId, createMultistateCondition);
    }

    private void editImpulseSwitch(String name, String description, String portId, String roomId, String impulseTime, DeviceCategory deviceCategory, String uniqueId) throws ObjectNotFoundException {
        ImpulseSwitch device = getImpulseSwitches().find(uniqueId);
        device.getName().setName(name);
        device.getName().setDescription(description);
        device.setRoomId(roomId);
        device.setPortId(portId);
        device.setImpulseTime(impulseTime);
        device.setDeviceCategory(deviceCategory);
    }

    @ConfigurationLoader(sectionName = "NfcCondition", parentId = "")
    public void modifyNfcCondition(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String duration = values.getValue("duration");
        String tag = values.getValue("tag");
        if (action == CrudAction.AddOrCreate) {
            addNfcCondition(name, description, tag, duration, uniqueId);
        } else {
            editNfcCondition(name, description, tag, duration, uniqueId);
        }
        onInvalidateCache("/CONFIG/NFCCONDITIONS.JSON");
        onInvalidateCache("/CONFIG/ALLCONDITIONS.JSON");
        onModified();
    }

    private void addNfcCondition(String name, String description, String tag, String duration, String uniqueId) {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName conditionName = new DescriptiveName(name, uniqueId, description);
        NfcCondition condition = new NfcCondition(conditionName, tag, duration);
        getNfcConditions().add(condition);
    }

    private void editNfcCondition(String name, String description, String tag, String duration, String uniqueId) throws ObjectNotFoundException {
        NfcCondition condition = getNfcConditions().find(uniqueId);
        condition.getName().setName(name);
        condition.getName().setDescription(description);
        condition.setTag(tag);
        condition.setDuration(duration);
    }


    @ConfigurationLoader(sectionName = "IntensityDevice", parentId = "")
    public void modifyIntensityDevice(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String portId = values.getValue("portid");
        String preset1 = values.getValue("preset1");
        Integer preset1Parsed = preset1 == null ? null : Integer.parseInt(preset1);
        String preset2 = values.getValue("preset2");
        Integer preset2Parsed = preset2 == null ? null : Integer.parseInt(preset2);
        String preset3 = values.getValue("preset3");
        Integer preset3Parsed = preset3 == null ? null : Integer.parseInt(preset3);
        String preset4 = values.getValue("preset4");
        Integer preset4Parsed = preset4 == null ? null : Integer.parseInt(preset4);
        String roomId = values.getValue("roomid");
        String iconName = values.getValue("iconname");
        if (action == CrudAction.AddOrCreate) {
            addIntensityDevice(name, description, portId, iconName, roomId, preset1Parsed, preset2Parsed, preset3Parsed, preset4Parsed, uniqueId);
        } else {
            editIntensityDevice(name, description, portId, iconName, roomId, preset1Parsed, preset2Parsed, preset3Parsed, preset4Parsed, uniqueId);
        }
        onInvalidateCache("/CONFIG/INTENSITYDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLDEVICES.JSON");
        onModified();
    }

    private void addIntensityDevice(String name, String description, String portId, String iconName,
                               String roomId, Integer preset1, Integer preset2, Integer preset3, Integer preset4, String uniqueId) throws Exception {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName deviceName = new DescriptiveName(name, uniqueId, description);
        IntensityDevice device = new IntensityDevice(deviceName, portId, roomId, iconName);
        getIntensityDevices().add(device);
        onInvalidateCache("/POWEROUTPUTPORTS.JSON");

        changeIntensityPresetValue(device.getName().getUniqueId(), 1, preset1);
        changeIntensityPresetValue(device.getName().getUniqueId(), 2, preset2);
        changeIntensityPresetValue(device.getName().getUniqueId(), 3, preset3);
        changeIntensityPresetValue(device.getName().getUniqueId(), 4, preset4);

        onModified();
    }

    private void editIntensityDevice(String name, String description, String portId, String iconName,
                                String roomId, Integer preset1, Integer preset2, Integer preset3, Integer preset4, String uniqueId) throws Exception {
        IntensityDevice device = getIntensityDevices().find(uniqueId);
        if (!device.getPortId().equals(portId)) {
            onInvalidateCache("/POWEROUTPUTPORTS.JSON");
        }
        device.getName().setName(name);
        device.getName().setDescription(description);
        device.setPortId(portId);
        device.setIconName(iconName);
        device.setRoomId(roomId);

        changeIntensityPresetValue(device.getName().getUniqueId(), 1, preset1);
        changeIntensityPresetValue(device.getName().getUniqueId(), 2, preset2);
        changeIntensityPresetValue(device.getName().getUniqueId(), 3, preset3);
        changeIntensityPresetValue(device.getName().getUniqueId(), 4, preset4);
    }

    private void changeIntensityPresetValue(String deviceId, int presetNo, Integer value) {
        if (value != null) {
            _automationSettings.changeSetting(deviceId + "_" + presetNo, String.valueOf(value));
        }
    }

    @SuppressWarnings("WeakerAccess")
    @ConfigurationLoader(sectionName = "PowerMeter", parentId = "")
    public void modifyPowerMeter(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String portId = values.getValue("portid");
        String roomId = values.getValue("roomid");
        String deviceCategory = values.getValue("devicecategory");
        DeviceCategory deviceCategoryParsed = DeviceCategory.fromInt(Integer.parseInt(deviceCategory));

        if (action == CrudAction.AddOrCreate) {
            addPowerMeter(name, description, portId, roomId, deviceCategoryParsed, uniqueId);
        } else {
            editPowerMeter(name, description, portId, roomId, deviceCategoryParsed, uniqueId);
        }
        onInvalidateCache("/CONFIG/POWERMETERS.JSON");
        onInvalidateCache("/CONFIG/ALLDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLVALUEDEVICES.JSON");
        onModified();
    }

    private void addPowerMeter(String name, String description, String portId, String roomId, DeviceCategory deviceCategory, String uniqueId) throws Exception {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName deviceName = new DescriptiveName(name, uniqueId, description);
        PowerMeter device = new PowerMeter(deviceName, portId, roomId, deviceCategory);
        getPowerMeters().add(device);
        onInvalidateCache("/POWERINPUTPORTS.JSON");
        onModified();
    }

    private void editPowerMeter(String name, String description, String portId, String roomId, DeviceCategory deviceCategory, String uniqueId) throws Exception {
        PowerMeter device = getPowerMeters().find(uniqueId);
        if (!device.getPortId().equals(portId)) {
            onInvalidateCache("/POWERINPUTPORTS.JSON");
        }
        device.getName().setName(name);
        device.getName().setDescription(description);
        device.setPortId(portId);
        device.setRoomId(roomId);
        device.setDeviceCategory(deviceCategory);
    }

    @Override
    public void addDevicesCollectors(java.util.ArrayList<CollectorCollection<? extends IDevice>> devicesCollectors) {
        devicesCollectors.add(getOnOffDevices());
        devicesCollectors.add(getImpulseSwitches());
        devicesCollectors.add(getIntensityDevices());
        devicesCollectors.add(getPowerMeters());
    }

    @Override
    public void addConditionsCollectors(java.util.ArrayList<CollectorCollection<? extends ICondition>> conditionCollectors) {
        conditionCollectors.add(getNfcConditions());
    }

    @Override
    public ArrayList<CollectorCollection<? extends INamedObject>> buildAllCollections() {
        ArrayList<CollectorCollection<? extends INamedObject>> result = new ArrayList<>();
        result.add(getOnOffDevices());
        result.add(getImpulseSwitches());
        result.add(getNfcConditions());
        result.add(getIntensityDevices());
        result.add(getPowerMeters());

        return result;
    }
}