package com.geekhome.coremodule.jetty;


import com.geekhome.common.localization.ILocalizationProvider;
import com.geekhome.coremodule.httpserver.IHttpListenerRequest;
import com.geekhome.coremodule.httpserver.IResponse;
import com.geekhome.coremodule.httpserver.IUContentProvider;

import java.util.ArrayList;

public class JavascriptsConsolidationRequestsDispatcher extends RequestsDispatcherBase {
    private ArrayList<String> _toConsolidate;
    private IUContentProvider _searcher;
    private ILocalizationProvider _localizationProvider;

    public JavascriptsConsolidationRequestsDispatcher(ArrayList<String> toConsolidate, IUContentProvider searcher, ILocalizationProvider localizationProvider) {
        _toConsolidate = toConsolidate;
        _searcher = searcher;
        _localizationProvider = localizationProvider;
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) {
        return new ConsolidationResponse(_toConsolidate, "text/javascript; charset=UTF-8", _searcher, _localizationProvider);
    }

    @Override
    public boolean isRequestSupported(IHttpListenerRequest request) {
        return isSpecificTypeOfFileRequest(request, "CONSOLIDATED.MIN.JS");
    }
}
