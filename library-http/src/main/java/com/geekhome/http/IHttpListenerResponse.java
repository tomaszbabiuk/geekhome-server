package com.geekhome.http;

import java.io.IOException;

public interface IHttpListenerResponse {
    int getStatusCode();
    void setStatusCode(int value);
    void setContentType(String value);
    void writeToOutputStream(byte[] contentBinary) throws IOException;
    void writeToOutputStream(byte[] contentBinary, int offset, int count) throws IOException;
    void writeToOutputStream(String str) throws IOException;
    void addHeader(String name, String value);
    void flush() throws IOException;
}
