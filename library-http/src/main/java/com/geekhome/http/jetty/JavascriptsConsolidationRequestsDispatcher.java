package com.geekhome.http.jetty;


import com.geekhome.http.IHttpListenerRequest;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.http.IResponse;
import com.geekhome.http.IUContentProvider;
import com.geekhome.http.jetty.RequestsDispatcherBase;

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
