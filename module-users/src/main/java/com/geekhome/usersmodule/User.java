package com.geekhome.usersmodule;

import com.geekhome.common.DescriptiveName;
import com.geekhome.common.NamedObject;

public class User extends NamedObject {
    private String _password;

    public void setPassword(String value) {
        _password = value;
    }

    public String getPassword() {
        return _password;
    }

    public User(DescriptiveName name, String password) {
        super(name);
        setPassword(password);
    }
}
