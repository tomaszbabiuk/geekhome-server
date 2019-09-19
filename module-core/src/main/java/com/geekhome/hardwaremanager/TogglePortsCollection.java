package com.geekhome.hardwaremanager;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;

import java.util.Hashtable;

public class TogglePortsCollection extends Hashtable<String, ITogglePort> implements JSONAware {
    public void add(ITogglePort port) {
        put(port.getId(), port);
    }

    public ITogglePort find(String uniqueId) throws PortNotFoundException {
        if (containsKey(uniqueId)) {
            return get(uniqueId);
        }

        throw new PortNotFoundException(uniqueId);
    }

    public ITogglePort tryFind(String uniqueId) {
        if (containsKey(uniqueId)) {
            return get(uniqueId);
        }

        return null;
    }

    @Override
    public String toJSONString() {
        JSONArray jsons = new JSONArray();
        for(ITogglePort port : values()) {
            jsons.add(port);
        }

        return jsons.toJSONString();
    }
}

