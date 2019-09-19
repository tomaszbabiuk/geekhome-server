package com.geekhome.http.jetty;


import com.geekhome.http.IHttpListenerRequest;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.http.IResponse;
import com.geekhome.http.IUContentProvider;

public class ErrorRequestsDispatcher extends RequestsDispatcherBase {
    private int _errorCode;
    private ILocalizationProvider _localizationProvider;
    private IUContentProvider _uContentProvidersSearcher;

    public ErrorRequestsDispatcher(int errorCode, ILocalizationProvider localizationProvider, IUContentProvider uContentProvidersSearcher) {
        _errorCode = errorCode;
        _localizationProvider = localizationProvider;
        _uContentProvidersSearcher = uContentProvidersSearcher;
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) {
        String contentPath = "ERROR" + _errorCode + ".HTM";
        return new UPagesResponse(request.getUrl().getOriginalString(), contentPath, _localizationProvider, _uContentProvidersSearcher, _errorCode);
    }

    @Override
    public boolean isRequestSupported(IHttpListenerRequest request) {
        return true;
    }
}
