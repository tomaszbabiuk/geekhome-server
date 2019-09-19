package com.geekhome.http.jetty;

import com.geekhome.http.IHttpListenerRequest;
import com.geekhome.http.jetty.RequestsDispatcherBase;

public abstract class JsonRequestsDispatcherBase extends RequestsDispatcherBase {

    @Override
    public boolean isRequestSupported(IHttpListenerRequest request) {
        return (isSpecificTypeOfFileRequest(request, ".JSON"));
    }
}

