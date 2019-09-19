package com.geekhome.httpserver.modules;

public interface IUnit {
    UnitCategory getCategory();
    String getIconName();
    String getName();
    String getTargetUrl();
}
