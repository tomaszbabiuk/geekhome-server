package com.geekhome.http.jetty;

import com.geekhome.http.IHttpListenerResponse;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpServletResponseWrapper implements IHttpListenerResponse {
    private final ServletOutputStream _stream;
    private final HttpServletResponse _wrapped;

    public HttpServletResponseWrapper(HttpServletResponse wrapped) throws IOException {
        _wrapped = wrapped;
        _stream = _wrapped.getOutputStream();
    }

    @Override
    public int getStatusCode() {
        return _wrapped.getStatus();
    }

    @Override
    public void setStatusCode(int value) {
        _wrapped.setStatus(value);
    }

    @Override
    public void setContentType(String value) {
        _wrapped.setContentType(value);
    }

    @Override
    public void writeToOutputStream(byte[] contentBinary) throws IOException {
        if (contentBinary != null) {
            writeToOutputStream(contentBinary, 0, contentBinary.length);
        }
    }

    @Override
    public void writeToOutputStream(byte[] contentBinary, int offset, int count) throws IOException {
        _stream.write(contentBinary,offset, count);
    }

    @Override
    public void writeToOutputStream(String str) throws IOException {
        writeToOutputStream(str.getBytes());
    }

    @Override
    public void addHeader(String name, String value) {
        _wrapped.addHeader(name, value);
    }

    @Override
    public void flush() throws IOException {
        _wrapped.flushBuffer();
    }
}