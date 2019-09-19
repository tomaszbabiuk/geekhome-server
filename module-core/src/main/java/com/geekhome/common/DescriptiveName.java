package com.geekhome.common;

import org.json.simple.JSONAware;

public class DescriptiveName implements JSONAware {
    private String name;
    private String description;
    private String uniqueId;

    public DescriptiveName(String name, String uid, String description) {
        this.name = name;
        this.uniqueId = uid;
        this.description = description;
    }

    public DescriptiveName(String name, String uid, boolean descriptionEqualsName) {
        this(name, uid, descriptionEqualsName ? name : "");
    }

    public DescriptiveName(String name, String uid) {
        this(name, uid, "");
    }

    @Override
    public String toJSONString() {
        return PersistableReflector.reflectToJson(this);
    }

    @Persistable(name = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Persistable(name = "Description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Persistable(name = "UniqueId")
    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public String toString() {
        if (getDescription() != null && !getDescription().equals("")) {
            return String.format("%s (%s)", getName(), getDescription());
        }

        return getName();
    }

    @Override
    public boolean equals(Object obj) {
        return ((DescriptiveName)obj).getUniqueId().equals(getUniqueId());
    }
}
