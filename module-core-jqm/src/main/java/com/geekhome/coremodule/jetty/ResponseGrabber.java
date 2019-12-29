package com.geekhome.coremodule.jetty;

import com.geekhome.coremodule.httpserver.IHttpListenerResponse;
import com.geekhome.common.NameValueSet;

import java.io.ByteArrayOutputStream;

public class ResponseGrabber implements IHttpListenerResponse {
    private NameValueSet _headers;
    private ByteArrayOutputStream _outputStream;
    private String _contentType;
    private int _statusCode;

    public ByteArrayOutputStream getStream() {
        return _outputStream;
    }

    public ResponseGrabber() {
        _outputStream = new ByteArrayOutputStream();
        _headers = new NameValueSet();
    }

    private String getContentType() {
        return _contentType;
    }

    public void setContentType(String value) {
        _contentType = value;
    }

    public int getStatusCode() {
        return _statusCode;
    }

    public void setStatusCode(int value) {
        _statusCode = value;
    }

    public void writeToOutputStream(byte[] contentBinary, int offset, int count) {
        _outputStream.write(contentBinary, offset, count);
    }

    public void writeToOutputStream(String str) {
        writeToOutputStream(str.getBytes());
    }

    public void addHeader(String name, String value) {
        _headers.add(name, value);
    }

    public void flush() {
    }

    public void writeToOutputStream(byte[] contentBinary) {
        _outputStream.write(contentBinary, 0, contentBinary.length);
    }

    public CachedResponse createCachedRequest() {
        return new CachedResponse(getContentType(), getStatusCode(), _headers, _outputStream);
    }
}
