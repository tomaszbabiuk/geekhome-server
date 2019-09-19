package com.geekhome.http;

public abstract class HttpListenerRequestBase implements IHttpListenerRequest {
    private Url _url;

    public Url getUrl() {
        if (_url == null) {
            _url = new Url(getRequestOriginalString());
        }

        return _url;
    }

    public abstract String getHttpMethod();
    protected abstract String getRequestOriginalString();
    public abstract byte[] getPostData();
    public abstract String getUserName();
}
