package com.geekhome.usersmodule;

import com.geekhome.common.DescriptiveName;
import com.geekhome.http.INameValueSet;
import com.geekhome.http.QueryString;
import com.geekhome.http.ResponseBase;
import com.geekhome.httpserver.ICrudPostHandler;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.http.jetty.RedirectionResponse;
import com.geekhome.httpserver.modules.CollectorCollection;

public class UsersCrudHandler implements ICrudPostHandler {
    private ILocalizationProvider _localizationProvider;
    private JettyRealmPersister _usersPersister;

    public UsersCrudHandler(JettyRealmPersister usersPersister, ILocalizationProvider localizationProvider) {
        _usersPersister = usersPersister;
        _localizationProvider = localizationProvider;
    }

    @Override
    public String getKeyword() {
        return "USER";
    }

    @Override
    public void doAdd(QueryString queryString) throws Exception {
        doModify(queryString);
    }

    @Override
    public void doEdit(QueryString queryString) throws Exception {
        doModify(queryString);
    }

    @Override
    public void doRemove(QueryString queryString) throws Exception {
        String param = queryString.getValues().getValue("param");
        CollectorCollection<User> users = _usersPersister.load();
        users.remove(param);
        _usersPersister.save(users);
    }

    @Override
    public void doModify(QueryString queryString) throws Exception {
        CollectorCollection<User> users = _usersPersister.load();

        INameValueSet values = queryString.getValues();
        String name = values.getValue("name");
        String uniqueId = values.getValue("uniqueid");
        String password = values.getValue("password");

        if (!uniqueId.equals("")) {
            //modify
            User user = users.find(uniqueId);
            if (user != null) {
                user.setName(new DescriptiveName(name, name));
                user.setPassword(password);
            }
        } else {
            if (users.containsKey(name)) {
                throw new Exception(_localizationProvider.getValue("U:UserAlreadyExists"));
            }

            User newUser = new User(new DescriptiveName(name, name), password);
            users.add(newUser);
        }

        _usersPersister.save(users);
    }

    @Override
    public ResponseBase returnRequest(QueryString queryString) throws Exception {
        return new RedirectionResponse("/config/users.htm");
    }
}