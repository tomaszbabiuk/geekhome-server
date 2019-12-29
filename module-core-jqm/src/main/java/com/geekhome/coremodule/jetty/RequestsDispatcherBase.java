package com.geekhome.coremodule.jetty;

import com.geekhome.coremodule.httpserver.IHttpListenerRequest;
import com.geekhome.coremodule.httpserver.IRequestsDispatcher;
import com.geekhome.coremodule.httpserver.IResponse;

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
