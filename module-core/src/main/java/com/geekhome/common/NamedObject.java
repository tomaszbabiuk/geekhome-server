package com.geekhome.common;

import com.geekhome.common.json.JSONAwareBase;

public class NamedObject extends JSONAwareBase implements INamedObject {
    private DescriptiveName _name;

    @Persistable(name="Name")
    public DescriptiveName getName() {
        return _name;
    }

    public void setName(DescriptiveName value) {
        _name = value;
    }

    public NamedObject(DescriptiveName name) {
        setName(name);
    }

    public boolean equals(Object obj) {
        return obj instanceof INamedObject && ((INamedObject) obj).getName().getUniqueId().equals(getName().getUniqueId());
    }

    public int hashCode() {
        String uniqueNumber = getName().getUniqueId().substring(1);
        return Integer.parseInt(uniqueNumber);
    }

    public String toString() {
        return getName().toString();
    }
}
