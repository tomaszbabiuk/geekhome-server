package com.geekhome.httpserver;

import com.geekhome.common.configuration.Persistable;
import com.geekhome.common.configuration.JSONArrayList;
import com.geekhome.common.configuration.JSONAwareBase;
import com.geekhome.coremodule.MasterConfiguration;

public class ConfigInfo extends JSONAwareBase {
    private final MasterConfiguration _masterConfiguration;

    public ConfigInfo(MasterConfiguration masterConfiguration) {
        _masterConfiguration = masterConfiguration;
    }

    @Persistable(name="IsConfigurationModified")
    public boolean isConfigurationModified() {
        return _masterConfiguration.isAnyCollectionerModified();
    }

    @Persistable(name="Validations")
    public JSONArrayList<String> getValidations() {
        return _masterConfiguration.getVallidations();
    }
}