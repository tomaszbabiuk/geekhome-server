package com.geekhome.coremodule.httpserver;

import com.geekhome.common.OperationMode;
import com.geekhome.common.automation.SystemInfo;
import com.geekhome.common.localization.ILocalizationProvider;
import com.geekhome.coremodule.jetty.RequestsDispatcherBase;
import com.geekhome.coremodule.jetty.UPagesResponse;

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
