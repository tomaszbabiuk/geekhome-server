package com.geekhome.extafreemodule;

import com.geekhome.common.*;
import com.geekhome.coremodule.IDevice;
import com.geekhome.http.INameValueSet;
import com.geekhome.httpserver.modules.Collector;
import com.geekhome.httpserver.modules.CollectorCollection;

import java.util.ArrayList;
import java.util.Hashtable;

public class ExtaFreeConfiguration extends Collector {
    private CollectorCollection<ExtaFreeBlind> _blinds;

    @ConfigurationSaver(sectionName = "ExtaFreeBlind", hasChildren = false)
    public CollectorCollection<ExtaFreeBlind> getExtaFreeBlinds() {
        return _blinds;
    }

    public ExtaFreeConfiguration(IdPool pool) {
        super(pool);
        _blinds = new CollectorCollection<>();
    }

    @Override
    public void clear() {
        getExtaFreeBlinds().clear();
    }

    @Override
    public String getPoolPrefix() {
        return "exf";
    }

    @ConfigurationLoader(sectionName = "ExtaFreeBlind", parentId = "")
    public void modifyExtaFreeBlind(CrudAction action, INameValueSet values) throws Exception {
        String name = values.getValue("name");
        String description = values.getValue("description");
        String uniqueId = values.getValue("uniqueid");
        String upPortId = values.getValue("upportid");
        String downPortId = values.getValue("downportid");
        String roomId = values.getValue("roomid");
        if (action == CrudAction.AddOrCreate) {
            addBlind(name, description, upPortId, downPortId, roomId, uniqueId);
        } else {
            editBlind(name, description, upPortId, downPortId, roomId, uniqueId);
        }
        onInvalidateCache("/CONFIG/EXTAFREEBLINDS.JSON");
        onInvalidateCache("/CONFIG/ALLDEVICES.JSON");
        onModified();
    }

    private void addBlind(String name, String description, String upPortId, String downPortId, String roomId, String uniqueId) throws Exception {
        uniqueId = poolUniqueIdIfEmpty(uniqueId);
        DescriptiveName deviceName = new DescriptiveName(name, uniqueId, description);
        ExtaFreeBlind device = new ExtaFreeBlind(deviceName, roomId, upPortId, downPortId);
        getExtaFreeBlinds().add(device);
        onInvalidateCache("/TOGGLEPORTS.JSON");
    }

    private void editBlind(String name, String description, String upPortId, String downPortId, String roomId, String uniqueId) throws Exception {
        ExtaFreeBlind device = getExtaFreeBlinds().find(uniqueId);
        if (!device.getUpPortId().equals(upPortId) || (!device.getDownPortId().equals(downPortId))) {
            onInvalidateCache("/TOGGLEPORTS.JSON");
        }
        device.getName().setName(name);
        device.getName().setDescription(description);
        device.setUpPortId(upPortId);
        device.setDownPortId(downPortId);
        device.setRoomId(roomId);
    }

    @Override
    public void addDevicesCollectors(ArrayList<CollectorCollection<? extends IDevice>> devicesCollectors) {
        devicesCollectors.add(getExtaFreeBlinds());
    }

    @Override
    public ArrayList<CollectorCollection<? extends INamedObject>> buildAllCollections() {
        ArrayList<CollectorCollection<? extends INamedObject>> result = new ArrayList<>();
        result.add(getExtaFreeBlinds());

        return result;
    }
}

