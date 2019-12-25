package com.geekhome.httpserver;

import com.geekhome.common.OperationMode;
import com.geekhome.http.IHttpListenerRequest;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.http.IResponse;
import com.geekhome.http.IUContentProvider;
import com.geekhome.http.jetty.RequestsDispatcherBase;
import com.geekhome.http.jetty.UPagesResponse;

public class AuthorizationRequestsDispatcher extends RequestsDispatcherBase {
    private SystemInfo _systemInfo;
    private ILocalizationProvider _localizationProvider;
    private IUContentProvider _uContentProvidersSearcher;

    public AuthorizationRequestsDispatcher(SystemInfo systemInfo, ILocalizationProvider localizationProvider, IUContentProvider uContentProvidersSearcher) {
        _systemInfo = systemInfo;
        _localizationProvider = localizationProvider;
        _uContentProvidersSearcher = uContentProvidersSearcher;
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) {
        String originalStringUppercased = request.getUrl().getOriginalString().toUpperCase();

        if (originalStringUppercased.indexOf("/CORE") == 0) {
            return null;
        }

        if ((originalStringUppercased.indexOf("/CONFIG") == 0 && _systemInfo.getOperationMode() != OperationMode.Configuration) ||
            (originalStringUppercased.indexOf("/DIAGNOSTICS") == 0 && _systemInfo.getOperationMode() != OperationMode.Diagnostics) ||
            (originalStringUppercased.indexOf("/AUTOMATIC") == 0 && _systemInfo.getOperationMode() != OperationMode.Automatic)) {
            return new UPagesResponse(request.getUrl().getOriginalString(), "ERROR403.HTM", _localizationProvider, _uContentProvidersSearcher, 403);
        }

        return null;
    }

    @Override
    public boolean isRequestSupported(IHttpListenerRequest request) {
        return isSpecificTypeOfFileRequest(request, ".HTM");
    }
}
