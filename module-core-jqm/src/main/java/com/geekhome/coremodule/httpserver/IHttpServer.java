package com.geekhome.coremodule.httpserver;

import java.io.IOException;

public interface IHttpServer
{
    void start() throws IOException;
    void stop() throws Exception;
}
