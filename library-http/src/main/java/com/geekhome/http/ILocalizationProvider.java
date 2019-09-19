package com.geekhome.http;

public interface ILocalizationProvider {
    String getValue(String key);
    void load(Resource[] resources);
}
