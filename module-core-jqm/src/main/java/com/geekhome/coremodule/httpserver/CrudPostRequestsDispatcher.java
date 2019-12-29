package com.geekhome.coremodule.httpserver;

import java.util.ArrayList;

public class CrudPostRequestsDispatcher extends PostRequestsDispatcherBase {
    private ArrayList<ICrudPostHandler> _crudPostHandlers;
    private String _prefix;

    public CrudPostRequestsDispatcher(ArrayList<ICrudPostHandler> crudPostHandlers, String prefix) {
        _crudPostHandlers = crudPostHandlers;
        _prefix = prefix;
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) throws Exception {
        String urlUpperCased = request.getUrl().getUrlNoQuery().substring(1, request.getUrl().getUrlNoQuery().length() - 5).toUpperCase();
        QueryString queryString = new QueryString("?" + new String(request.getPostData(), "UTF-8"));

        if ((urlUpperCased.indexOf(_prefix + "ADD") == 0) || (urlUpperCased.indexOf(_prefix + "REMOVE") == 0) || (urlUpperCased.indexOf(_prefix + "EDIT") == 0) || (urlUpperCased.indexOf(_prefix + "MODIFY") == 0)) {
            ICrudPostHandler handler = null;
            if (urlUpperCased.indexOf(_prefix + "MODIFY") == 0) {
                String keyword = urlUpperCased.substring(_prefix.length() + 6);
                handler = findHandler(keyword);
                if (handler != null) {
                    handler.doModify(queryString);
                }
            }
            if (urlUpperCased.indexOf(_prefix + "ADD") == 0) {
                String keyword = urlUpperCased.substring(_prefix.length() + 3);
                handler = findHandler(keyword);
                if (handler != null) {
                    handler.doAdd(queryString);
                }
            } else if (urlUpperCased.indexOf(_prefix + "REMOVE") == 0) {
                String keyword = urlUpperCased.substring(_prefix.length() + 6);
                handler = findHandler(keyword);
                if (handler != null) {
                    handler.doRemove(queryString);
                    return new ResponseBase("OK");
                } else {
                    throw new Exception("Cannot find handler for request: " + urlUpperCased);
                }
            } else if (urlUpperCased.indexOf(_prefix + "EDIT") == 0) {
                String keyword = urlUpperCased.substring(_prefix.length() + 4);
                handler = findHandler(keyword);
                if (handler != null) {
                    handler.doEdit(queryString);
                }
            }

            if (handler != null) {
                return handler.returnRequest(queryString);
            }
        }

        return null;
    }

    private ICrudPostHandler findHandler(String keyword) {
        for(ICrudPostHandler crudPostHandler : _crudPostHandlers)
        {
            if (crudPostHandler.getKeyword().equals(keyword)) {
                return crudPostHandler;
            }
        }

        return null;
    }
}
