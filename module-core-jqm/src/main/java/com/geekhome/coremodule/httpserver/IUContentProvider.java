package com.geekhome.coremodule.httpserver;

public interface IUContentProvider {
    public byte[] findInContentProviders(String contentPath) throws ContentNotFoundException;
}
