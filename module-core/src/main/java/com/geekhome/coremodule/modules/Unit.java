package com.geekhome.coremodule.modules;

import org.json.simple.JSONObject;

public class Unit implements IUnit {
    private UnitCategory _category;
    private String _iconName;
    private String _name;
    private String _targetUrl;

    public UnitCategory getCategory() {
        return _category;
    }

    private void setCategory(UnitCategory value) {
        _category = value;
    }

    public String getIconName() {
        return _iconName;
    }

    private void setIconName(String value) {
        _iconName = value;
    }

    public String getName() {
        return _name;
    }

    private void setName(String value) {
        _name = value;
    }

    public String getTargetUrl() {
        return _targetUrl;
    }

    private void setTargetUrl(String value) {
        _targetUrl = value;
    }

    public Unit(UnitCategory category, String name, String iconName, String targetUrl) {
        setCategory(category);
        setName(name);
        setIconName(iconName);
        setTargetUrl(targetUrl);
    }

    @Override
    public String toString() {
        JSONObject json = new JSONObject();
        json.put("Name", getName());
        json.put("IconName", getIconName());
        json.put("Category", getCategory());
        json.put("TargetUrl", getTargetUrl());
        return json.toJSONString();
    }
}
