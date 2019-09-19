package com.geekhome.http;

import java.util.ArrayList;

public interface INameValueSet
{
    String getValue(String key);
    String getValues(String key);
    void add(String key, String value);
    ArrayList<String> getKeys();
}
