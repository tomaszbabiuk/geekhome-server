package com.geekhome.coremodule.modules;

import com.geekhome.common.configuration.JSONArrayList;

public class NavigationTree {
    private JSONArrayList<IModule> _modules;

    public JSONArrayList<IModule> getModules() {
        return _modules;
    }

    public void setModules(JSONArrayList<IModule> modules) {
        _modules = modules;
    }
}
