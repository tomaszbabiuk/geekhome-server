package com.geekhome.coremodule.httpserver;

import com.geekhome.common.configuration.MasterConfiguration;
import com.geekhome.http.IHttpListenerRequest;
import com.geekhome.http.IResponse;
import com.geekhome.http.QueryString;
import com.geekhome.http.jetty.RedirectionResponse;
import com.geekhome.httpserver.PostRequestsDispatcherBase;

public class BackupPostRequestsDispatcher extends PostRequestsDispatcherBase {
    private MasterConfiguration _masterConfiguration;

    public BackupPostRequestsDispatcher(MasterConfiguration masterConfiguration) {
        _masterConfiguration = masterConfiguration;
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) throws Exception {
        String originalStringUppercased =
                request.getUrl().getUrlNoQuery().substring(1, request.getUrl().getUrlNoQuery().length() - 5).toUpperCase();
        QueryString queryString = new QueryString("?" + new String(request.getPostData(), "UTF-8"));

        if (originalStringUppercased.equals("CONFIG/RESTOREBACKUP")) {
            String uniqueId = queryString.getValues().getValue("uniqueid");

            _masterConfiguration.restore(uniqueId);
            return new RedirectionResponse("/config/backups.htm");
        }

        return null;
    }
}
