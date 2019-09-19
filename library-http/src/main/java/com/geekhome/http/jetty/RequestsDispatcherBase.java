package com.geekhome.http.jetty;

import com.geekhome.http.IHttpListenerRequest;
import com.geekhome.http.IRequestsDispatcher;
import com.geekhome.http.IResponse;

public abstract class RequestsDispatcherBase implements IRequestsDispatcher {
    protected static boolean isSpecificTypeOfFileRequest(IHttpListenerRequest request, String dotExtensionUpperCased) {
        String urlUpperCased = request.getUrl().getOriginalString().toUpperCase();
        int questionMarkIndex = request.getUrl().getOriginalString().indexOf('?');
        if (questionMarkIndex > 0) {
            urlUpperCased = urlUpperCased.substring(1, questionMarkIndex);
        }

        return (urlUpperCased.lastIndexOf(dotExtensionUpperCased) == urlUpperCased.length() - dotExtensionUpperCased.length()) && urlUpperCased.lastIndexOf(dotExtensionUpperCased) > -1;
    }

    public abstract IResponse dispatch(IHttpListenerRequest request) throws Exception;

    public abstract boolean isRequestSupported(IHttpListenerRequest request);
}
