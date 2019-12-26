package com.geekhome.coremodule.httpserver;

import com.geekhome.common.configuration.ObjectNotFoundException;
import com.geekhome.http.IHttpListenerRequest;
import com.geekhome.http.IResponse;
import com.geekhome.http.jetty.JsonRequestsDispatcherBase;
import com.geekhome.coremodule.modules.NavigationTree;

public class NavigationTreeJsonRequestsDispatcher extends JsonRequestsDispatcherBase {
    private NavigationTree _navigationTree;

    public NavigationTreeJsonRequestsDispatcher(NavigationTree navigationTree) {
        _navigationTree = navigationTree;
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) throws ObjectNotFoundException {
        String originalStringUppercased = request.getUrl().getUrlNoQuery().toUpperCase();

        if (originalStringUppercased.equals("/NAVIGATIONTREE/MODULES.JSON")) {
            return new JsonResponse(_navigationTree.getModules(), true);
        }

        return null;
    }
}