package com.geekhome.coremodule.httpserver;

public interface IResponse
{
    void process(IHttpListenerResponse response) throws Exception;
    boolean isCacheable();
}