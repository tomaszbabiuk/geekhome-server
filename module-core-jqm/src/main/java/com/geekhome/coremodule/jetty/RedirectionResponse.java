package com.geekhome.coremodule.jetty;

import com.geekhome.coremodule.httpserver.IHttpListenerResponse;
import com.geekhome.coremodule.httpserver.ResponseBase;

import java.io.IOException;

public class RedirectionResponse extends ResponseBase {
    private String _targetUrl;

    public RedirectionResponse(String targetUrl) {
        _targetUrl = targetUrl;
    }

    @Override
    public void process(IHttpListenerResponse response) throws IOException {
        response.setStatusCode(302);
        response.addHeader("Location", _targetUrl);
        response.flush();
    }
}