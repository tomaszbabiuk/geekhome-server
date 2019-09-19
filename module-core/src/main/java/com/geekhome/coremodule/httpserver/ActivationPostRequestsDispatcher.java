package com.geekhome.coremodule.httpserver;

import com.geekhome.common.ILicenseManager;
import com.geekhome.http.IHttpListenerRequest;
import com.geekhome.http.IResponse;
import com.geekhome.http.QueryString;
import com.geekhome.http.jetty.RedirectionResponse;
import com.geekhome.httpserver.PostRequestsDispatcherBase;

public class ActivationPostRequestsDispatcher extends PostRequestsDispatcherBase {
    private ILicenseManager _licenseManager;


    public ActivationPostRequestsDispatcher(ILicenseManager licenseManager) {
        _licenseManager = licenseManager;
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) throws Exception{
        String originalStringUppercased =
                request.getUrl().getUrlNoQuery().substring(1, request.getUrl().getUrlNoQuery().length() - 5).toUpperCase();
        QueryString queryString = new QueryString("?" + new String(request.getPostData(), "UTF-8"));

        if (originalStringUppercased.equals("ACTIVATION/ACTIVATEADAPTER")) {
            String id = queryString.getValues().getValue("id");
            String code = queryString.getValues().getValue("code");
            if (_licenseManager.enterLicense(id, code) ) {
                String successUrl = queryString.getValues().getValue("successurl");
                return new RedirectionResponse(successUrl);
            }

            String failureUrl = queryString.getValues().getValue("failureurl");
            return new RedirectionResponse(failureUrl);
        }

        return null;
    }
}

