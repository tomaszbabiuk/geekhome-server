package com.geekhome.coremodule.httpserver;


import com.geekhome.http.QueryString;
import com.geekhome.http.ResponseBase;

public interface ICrudPostHandler
{
    String getKeyword();
    void doAdd(QueryString queryString) throws Exception;
    void doEdit(QueryString queryString) throws Exception;
    void doRemove(QueryString queryString) throws Exception;
    void doModify(QueryString queryString) throws Exception;
    ResponseBase returnRequest(QueryString queryString) throws Exception;
}