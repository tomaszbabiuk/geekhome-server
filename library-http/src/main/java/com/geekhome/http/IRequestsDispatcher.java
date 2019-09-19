package com.geekhome.http;

public interface IRequestsDispatcher {
    IResponse dispatch(IHttpListenerRequest request) throws Exception;
    boolean isRequestSupported(IHttpListenerRequest request);
}
