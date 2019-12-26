package com.geekhome.httpserver;

import com.geekhome.http.IHttpListenerRequest;
import com.geekhome.http.IRequestsDispatcher;
import com.geekhome.http.IResponse;

public abstract class PostRequestsDispatcherBase implements IRequestsDispatcher {

    @Override
    public abstract IResponse dispatch(IHttpListenerRequest request) throws Exception;

    @Override
    public boolean isRequestSupported(IHttpListenerRequest request)
    {
        return (request.getHttpMethod().equals("POST"));
    }
}
