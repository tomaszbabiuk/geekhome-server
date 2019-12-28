package com.geekhome.coremodule.jetty;

import com.geekhome.common.localization.ILocalizationProvider;
import com.geekhome.coremodule.httpserver.ContentNotFoundException;
import com.geekhome.coremodule.httpserver.IHttpListenerRequest;
import com.geekhome.coremodule.httpserver.IResponse;
import com.geekhome.coremodule.httpserver.IUContentProvider;

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
