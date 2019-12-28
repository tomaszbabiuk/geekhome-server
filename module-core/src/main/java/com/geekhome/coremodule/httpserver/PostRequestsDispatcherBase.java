package com.geekhome.coremodule.httpserver;

public abstract class PostRequestsDispatcherBase implements IRequestsDispatcher {

    @Override
    public abstract IResponse dispatch(IHttpListenerRequest request) throws Exception;

    @Override
    public boolean isRequestSupported(IHttpListenerRequest request)
    {
        return (request.getHttpMethod().equals("POST"));
    }
}
