package com.geekhome.httpserver.modules;

import com.geekhome.common.configuration.JSONArrayList;

public interface IConfigurationValidator {
    void addValidations(JSONArrayList<String> validations);
}
