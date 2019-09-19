package com.geekhome.http;

import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.IOException;

public interface IHttpListenerRequest
{
    String getHttpMethod();
    Url getUrl();
    byte[] getPostData();
    String getUserName();
    Part getPart(String file) throws IOException, ServletException;
}