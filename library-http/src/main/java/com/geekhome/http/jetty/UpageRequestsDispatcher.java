package com.geekhome.http.jetty;

import com.geekhome.http.*;
import com.geekhome.http.jetty.RequestsDispatcherBase;
import com.geekhome.http.jetty.UPagesResponse;

public class UpageRequestsDispatcher extends RequestsDispatcherBase {
    private ILocalizationProvider _resourceProvider;
    private IUContentProvider _uContentProvidersSearcher;

    public UpageRequestsDispatcher(ILocalizationProvider resourceProvider, IUContentProvider uContentProvidersSearcher) {
        _resourceProvider = resourceProvider;
        _uContentProvidersSearcher = uContentProvidersSearcher;
    }

    private static boolean isUPagesRequest(IHttpListenerRequest request) {
        return (isSpecificTypeOfFileRequest(request, "RESOURCES.JS")) ||
                (isSpecificTypeOfFileRequest(request, ".RES.JS")) ||
                (isSpecificTypeOfFileRequest(request, ".HTM")) ||
                (request.getUrl().getOriginalString().equals("/"));
    }

    @Override
    public boolean isRequestSupported(IHttpListenerRequest request) {
        return isSpecificTypeOfFileRequest(request, ".JS") ||
                isSpecificTypeOfFileRequest(request, ".CSS") ||
                isSpecificTypeOfFileRequest(request, ".GIF") ||
                isSpecificTypeOfFileRequest(request, ".HTM") ||
                isSpecificTypeOfFileRequest(request, ".ICO") ||
                isSpecificTypeOfFileRequest(request, ".JPG") ||
                isSpecificTypeOfFileRequest(request, ".PNG") ||
                isSpecificTypeOfFileRequest(request, ".MAP") ||
                isSpecificTypeOfFileRequest(request, ".SVG");
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) {
        String contentPath = request.getUrl().buildContentPath();

        if (isUPagesRequest(request)) {
            return new UPagesResponse(request.getUrl().getOriginalString(), contentPath, _resourceProvider,
                    _uContentProvidersSearcher);
        }

        try {
            byte[] content = _uContentProvidersSearcher.findInContentProviders(contentPath);
            return new ResourceResponse(request.getUrl().getOriginalString(), content);
        } catch (ContentNotFoundException e) {
            return null;
        }
    }
}
