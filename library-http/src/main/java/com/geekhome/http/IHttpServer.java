package com.geekhome.http;

import java.io.IOException;

public interface IHttpServer
{
    void start() throws IOException;
    void stop() throws Exception;
}
