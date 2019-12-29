package com.geekhome.coremodule.httpserver;

public class ResponseBase implements IResponse {
    private String _content;
    private String _contentType;
    private byte[] _contentBinary;
    private boolean _cacheable;

    public String getContent() {
        return _content;
    }

    protected void setContent(String content) {
        _content = content;
    }

    public String getContentType() {
        return _contentType;
    }

    public void setContentType(String value) {
        _contentType = value;
    }

    public byte[] getContentBinary() {
        return _contentBinary;
    }

    protected void setContentBinary(byte[] value) {
        _contentBinary = value;
    }

    public boolean isCacheable() {
        return _cacheable;
    }

    public void setCacheable(boolean value) {
        _cacheable = value;
    }


    protected ResponseBase() {
        setCacheable(false);
    }

    protected static String matchContentType(String originalString) {
        String originalStringUppercased = originalString.toUpperCase();

        if (originalStringUppercased.endsWith(".JS")) {
            return "text/javascript; charset=utf-8";
        }

        if (originalStringUppercased.endsWith(".CSS")) {
            return "text/css; charset=utf-8";
        }

        if (originalStringUppercased.endsWith(".PNG")) {
            return "image/png";
        }

        if (originalStringUppercased.endsWith(".JPG")) {
            return "image/jpeg";
        }

        if (originalStringUppercased.endsWith(".SVG")) {
            return "image/svg+xml";
        }

        if (originalStringUppercased.endsWith(".GIF")) {
            return "image/gif";
        }

        return "text/html; charset=utf-8";
    }

    public ResponseBase(String content) {
        setContent(content);
    }

    public ResponseBase(byte[] content) {
        setContentBinary(content);
    }

    public void process(IHttpListenerResponse response) throws Exception {
        if (getContentBinary() == null) {
            setContentBinary(getContent().getBytes());
        }

        response.setStatusCode(200);
        response.setContentType(getContentType());
        response.flush();
        response.writeToOutputStream(getContentBinary());
        response.flush();
    }
}