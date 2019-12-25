package com.geekhome.ventilationmodule;

import com.geekhome.common.*;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.configuration.IDevice;
import com.geekhome.http.INameValueSet;
import com.geekhome.common.configuration.Collector;
import com.geekhome.common.configuration.CollectorCollection;
import java.util.ArrayList;

public class VentilationConfiguration extends Collector {
    private CollectorCollection<Recuperator> _recuperators;

    @ConfigurationSaver(sectionName = "Recuperator", hasChildren = false)
    public CollectorCollection<Recuperator> getRecuperators() {
        return _recuperators;
    }


    public VentilationConfiguration(IdPool pool) {
        super(pool);
        _recuperators = new CollectorCollection<>();
    }

    @Override
    public void clear() {
        getRecuperators().clear();
    }

    @Override
    public String getPoolPrefix() {
        return "vent";
    }

    @ConfigurationLoader(sectionName = "Recuperator", parentId = "")
    public void modifyRecuperator(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String automaticControlPortId = values.getValue("automaticcontrolportid");
        String secondGearPortId = values.getValue("secondgearportid");
        String thirdGearPortId = values.getValue("thirdgearportid");
        String roomId = values.getValue("roomid");
        if (action == CrudAction.AddOrCreate) {
            addRecuperator(name, description, automaticControlPortId, secondGearPortId, thirdGearPortId, roomId, uniqueId);
        } else {
            editRecuperator(name, description, automaticControlPortId, secondGearPortId, thirdGearPortId, roomId, uniqueId);
        }
        onInvalidateCache("/CONFIG/RECUPERATORS.JSON");
        onInvalidateCache("/CONFIG/ALLDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLMULTISTATEDEVICES.JSON");
        onInvalidateCache("/CONFIG/ALLBLOCKSTARGETDEVICES.JSON");
        onModified();
    }

    private void addRecuperator(String name, String description, String automaticControlPortId, String secondGearPortId,
                                String thirdGearPortId, String roomId, String uniqueId) throws Exception {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName deviceName = new DescriptiveName(name, uniqueId, description);
        Recuperator device = new Recuperator(deviceName, automaticControlPortId, secondGearPortId, thirdGearPortId, roomId);
        getRecuperators().add(device);
        onInvalidateCache("/DIGITALOUTPUTPORTS.JSON");
    }

    private void editRecuperator(String name, String description, String automaticControlPortId, String secondGearPortId,
                                 String thirdGearPortId, String roomId, String uniqueId) throws Exception {
        Recuperator device = getRecuperators().find(uniqueId);
        if (!device.getAutomaticControlPortId().equals(automaticControlPortId) || !device.getThirdGearPortId().equals(thirdGearPortId) ||
                !device.getSecondGearPortId().equals(secondGearPortId)) {
            onInvalidateCache("/DIGITALOUTPUTPORTS.JSON");
        }
        device.getName().setName(name);
        device.getName().setDescription(description);
        device.setAutomaticControlPortId(automaticControlPortId);
        device.setThirdGearPortId(thirdGearPortId);
        device.setSecondGearPortId(secondGearPortId);
        device.setRoomId(roomId);
    }


    @Override
    public void addDevicesCollectors(java.util.ArrayList<CollectorCollection<? extends IDevice>> devicesCollectors) {
        devicesCollectors.add(getRecuperators());
    }

    @Override
    public ArrayList<CollectorCollection<? extends INamedObject>> buildAllCollections() {
        ArrayList<CollectorCollection<? extends INamedObject>> result = new ArrayList<>();
        result.add(getRecuperators());
        return result;
    }
}