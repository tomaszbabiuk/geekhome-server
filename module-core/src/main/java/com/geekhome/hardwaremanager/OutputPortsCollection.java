package com.geekhome.hardwaremanager;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;

import java.util.Hashtable;

public class OutputPortsCollection<T> extends Hashtable<String, IOutputPort<T>> implements JSONAware {
    public void add(IOutputPort<T> port) {
        put(port.getId(), port);
    }

    public IOutputPort<T> tryFind(String uniqueId) {
        if (containsKey(uniqueId)) {
            return get(uniqueId);
        }

        return null;
    }

    public IOutputPort<T> find(String uniqueId) throws PortNotFoundException {
        if (containsKey(uniqueId)) {
            return get(uniqueId);
        }

        throw new PortNotFoundException(uniqueId);
    }

    @Override
    public String toJSONString() {
        JSONArray jsons = new JSONArray();
        for(IOutputPort<T> port : values()) {
            jsons.add(port);
        }

        return jsons.toJSONString();
    }
}

