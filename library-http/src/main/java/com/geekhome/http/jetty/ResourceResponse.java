package com.geekhome.http.jetty;

import com.geekhome.http.IHttpListenerResponse;
import com.geekhome.http.ResponseBase;

import java.io.IOException;

public class ResourceResponse extends ResponseBase {
    private String _originalString;
    private byte[] _content;

    public ResourceResponse(String originalString, byte[] content) {
        _originalString = originalString;
        _content = content;
    }

    @Override
    public void process(IHttpListenerResponse response) throws IOException {
        response.setStatusCode(200);
        response.setContentType(matchContentType(_originalString));
        response.addHeader("Cache-Control", "max-age=290304000, public");
        response.flush();
        response.writeToOutputStream(_content);
        response.flush();
    }
}
