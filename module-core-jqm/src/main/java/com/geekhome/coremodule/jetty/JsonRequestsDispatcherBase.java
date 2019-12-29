package com.geekhome.coremodule.jetty;

import com.geekhome.coremodule.httpserver.IHttpListenerRequest;

public abstract class JsonRequestsDispatcherBase extends RequestsDispatcherBase {

    @Override
    public boolean isRequestSupported(IHttpListenerRequest request) {
        return (isSpecificTypeOfFileRequest(request, ".JSON"));
    }
}

