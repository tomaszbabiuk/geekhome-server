package com.geekhome.httpserver.modules;

public interface ISettingChangedListener {
    void execute(String name, String value);
}
