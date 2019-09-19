package com.geekhome.coremodule.httpserver;

import com.geekhome.common.CrudAction;
import com.geekhome.coremodule.MasterConfiguration;
import com.geekhome.http.INameValueSet;
import com.geekhome.http.QueryString;
import com.geekhome.http.ResponseBase;
import com.geekhome.httpserver.ICrudPostHandler;
import com.geekhome.http.jetty.RedirectionResponse;

public class CrudPostHandler implements ICrudPostHandler {
    public interface ICrudModificationFunction {
        void execute(CrudAction action, INameValueSet values) throws Exception;
    }

    private MasterConfiguration _masterConfiguration;
    private String _keyword;
    private ICrudModificationFunction _modifyFunction;
    private ResponseBase _redirectionRequest;

    @Override
    public String getKeyword() {
        return _keyword;
    }

    public CrudPostHandler(MasterConfiguration masterConfiguration, String keyword, ICrudModificationFunction modifyFunction, ResponseBase redirectionRequest) {
        _masterConfiguration = masterConfiguration;
        _keyword = keyword;
        _modifyFunction = modifyFunction;
        _redirectionRequest = redirectionRequest;
    }

    @Override
    public ResponseBase returnRequest(QueryString queryString) throws Exception {
        if (_redirectionRequest == null) {
            String returnUrl = queryString.getValues().getValue("returnurl");
            if (!returnUrl.equals("")) {
                return new RedirectionResponse(returnUrl);
            }

            throw new Exception(
                    "No redirection request specified. No 'returnurl' in querystring. No redirect possible.");
        }
        return _redirectionRequest;
    }

    public void doAdd(QueryString queryString) throws Exception {
        _modifyFunction.execute(CrudAction.AddOrCreate, queryString.getValues());
    }

    public void doEdit(QueryString queryString) throws Exception {
        _modifyFunction.execute(CrudAction.Edit, queryString.getValues());
    }

    public void doModify(QueryString queryString) throws Exception {
        if (queryString.getValues().getValue("uniqueid").equals("")) {
            doAdd(queryString);
        } else {
            doEdit(queryString);
        }
    }

    public void doRemove(QueryString queryString) throws Exception {
        _masterConfiguration.masterRemoveObjectWithItsDependencies(queryString.getValues().getValue("param"));
    }
}
