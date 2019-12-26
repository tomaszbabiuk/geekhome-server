package com.geekhome.coremodule.httpserver;

import com.geekhome.common.configuration.MasterConfiguration;
import com.geekhome.http.IHttpListenerRequest;
import com.geekhome.http.IResponse;
import com.geekhome.http.QueryString;
import com.geekhome.httpserver.PostRequestsDispatcherBase;
import com.geekhome.http.jetty.RedirectionResponse;

public class MasterConfigurationPostRequestsDispatcher extends PostRequestsDispatcherBase {
    private MasterConfiguration _masterConfiguration;

    public MasterConfigurationPostRequestsDispatcher(MasterConfiguration masterConfiguration) {
        _masterConfiguration = masterConfiguration;
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) throws Exception {
        String urlUpperCased = request.getUrl().getUrlNoQuery().substring(1, request.getUrl().getUrlNoQuery().length() - 5).toUpperCase();
        if (urlUpperCased.equals("CONFIG/SAVE")) {
            QueryString queryString = new QueryString("?" + new String(request.getPostData(), "UTF-8"));
            String comment = queryString.getValues().getValue("comment");
            String returnUrl = queryString.getValues().getValue("returnurl");
            _masterConfiguration.saveAll(comment);
            return new RedirectionResponse(returnUrl);
        }

        if (urlUpperCased.equals("CONFIG/DISCARD")) {
            QueryString queryString = new QueryString("?" + new String(request.getPostData(), "UTF-8"));
            String returnUrl = queryString.getValues().getValue("returnurl");
            _masterConfiguration.rollbackAll();
            return new RedirectionResponse(returnUrl);
        }

        return null;
    }
}
