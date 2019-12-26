package com.geekhome.usersmodule.httpserver;

import com.geekhome.http.IHttpListenerRequest;
import com.geekhome.http.IResponse;
import com.geekhome.http.jetty.JsonRequestsDispatcherBase;
import com.geekhome.coremodule.httpserver.JsonResponse;
import com.geekhome.common.configuration.CollectorCollection;
import com.geekhome.usersmodule.JettyRealmPersister;
import com.geekhome.usersmodule.User;
import org.json.simple.JSONObject;

public class UsersJsonRequestsDispatcher extends JsonRequestsDispatcherBase {
    private JettyRealmPersister _userPersister;

    public UsersJsonRequestsDispatcher(JettyRealmPersister userPersister) {
        _userPersister = userPersister;
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) throws Exception {
        String originalStringUppercased = request.getUrl().getUrlNoQuery().toUpperCase();
        if (originalStringUppercased.equals("/CONFIG/USERS.JSON")) {
            CollectorCollection<User> users = _userPersister.load();
            JSONObject json = JsonResponse.createJSONResult(users);
            return new JsonResponse(json, false);
        }

        return null;
    }
}