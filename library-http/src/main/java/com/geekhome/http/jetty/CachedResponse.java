package com.geekhome.http.jetty;

import com.geekhome.http.IHttpListenerResponse;
import com.geekhome.http.INameValueSet;
import com.geekhome.http.IResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CachedResponse implements IResponse {
    private String _contentType;
    private int _statusCode;
    private INameValueSet _headers;
    private ByteArrayOutputStream _stream;

    public boolean isCacheable() {
        return true;
    }

    public CachedResponse(String contentType, int statusCode, INameValueSet headers, ByteArrayOutputStream stream) {
        _contentType = contentType;
        _statusCode = statusCode;
        _headers = headers;
        _stream = stream;
    }

    public void process(IHttpListenerResponse response) throws IOException {
        response.setContentType(_contentType);
        response.setStatusCode(_statusCode);
        for (String key : _headers.getKeys()) {
            response.addHeader(key, _headers.getValue(key));
        }
        response.flush();
        response.writeToOutputStream(_stream.toByteArray());
        response.flush();
    }
}
