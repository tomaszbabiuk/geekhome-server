package com.geekhome.http;

public interface IResponse
{
    void process(IHttpListenerResponse response) throws Exception;
    boolean isCacheable();
}