package com.geekhome.http.jetty;

import com.geekhome.http.HttpListenerRequestBase;
import org.eclipse.jetty.server.Request;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;

class HttpServletRequestWrapper extends HttpListenerRequestBase {
    private final HttpServletRequest _wrapped;
    private byte[] _postDataCached;

    public String getUserName() {
        return _wrapped.getUserPrincipal().getName();
    }

    @Override
    public Part getPart(String partName) throws IOException, ServletException {
        return _wrapped.getPart(partName);
    }

    @Override
    public String getHttpMethod() {
        return _wrapped.getMethod();
    }

    @Override
    protected String getRequestOriginalString() {
        return  ((Request) _wrapped).getOriginalURI();
    }

    @Override
    public byte[] getPostData() {
        try {
            if (_postDataCached == null) {
                ServletInputStream postDataStream = _wrapped.getInputStream();
                int count = postDataStream.available();
                _postDataCached = new byte[count];
                postDataStream.read(_postDataCached, 0, count);
            }
            return _postDataCached;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new byte[0];
    }

    HttpServletRequestWrapper(HttpServletRequest wrapped) {
        _wrapped = wrapped;
    }
}
