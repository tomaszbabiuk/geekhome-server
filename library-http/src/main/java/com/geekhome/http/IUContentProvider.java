package com.geekhome.http;

public interface IUContentProvider {
    public byte[] findInContentProviders(String contentPath) throws ContentNotFoundException;
}
