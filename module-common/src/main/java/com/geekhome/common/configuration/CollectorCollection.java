package com.geekhome.common.configuration;

import com.geekhome.common.INamedObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;

import java.util.Hashtable;

public class CollectorCollection<T extends INamedObject> extends Hashtable<String, T> implements JSONAware {
    public void add(T namedObject) {
        put(namedObject.getName().getUniqueId(), namedObject);
    }

    public T find(String uniqueId) throws ObjectNotFoundException {
        return find(uniqueId, true);
    }

    public T find(String uniqueId, boolean throwExceptionIfNotFound) throws ObjectNotFoundException {
        if (containsKey(uniqueId)) {
            return get(uniqueId);
        }

        if (throwExceptionIfNotFound) {
            throw new ObjectNotFoundException(uniqueId);
        }

        return null;
    }

    @Override
    public String toJSONString() {
        JSONArray json = new JSONArray();
        for (T item : values()) {
            json.add(item instanceof JSONAware ? ((JSONAware)item) : item.toString());
        }
        return json.toJSONString();
    }
}
