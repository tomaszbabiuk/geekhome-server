package com.geekhome.coremodule.httpserver;

public interface IResponseCache {
    IResponse get(String key);
    void put(String key, IResponse content);
    void remove(String key);
    void clear();
}
