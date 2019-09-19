package com.geekhome.httpserver.modules;

import com.geekhome.common.json.JSONArrayList;

public interface IConfigurationValidator {
    void addValidations(JSONArrayList<String> validations);
}
