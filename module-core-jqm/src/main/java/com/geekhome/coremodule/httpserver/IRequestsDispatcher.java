package com.geekhome.coremodule.httpserver;

public interface IRequestsDispatcher {
    IResponse dispatch(IHttpListenerRequest request) throws Exception;
    boolean isRequestSupported(IHttpListenerRequest request);
}
