package com.geekhome.lightsmodule;

import com.geekhome.coremodule.MasterConfiguration;
import com.geekhome.httpserver.modules.DependenciesCheckerModule;
import com.geekhome.httpserver.modules.Dependency;
import com.geekhome.httpserver.modules.DependencyType;

import java.util.ArrayList;

public class LightsDependenciesCheckerModule extends DependenciesCheckerModule {
    private LightsConfiguration _automationConfiguration;

    public LightsDependenciesCheckerModule(MasterConfiguration masterConfiguration, LightsConfiguration automationConfiguration) {
        super(masterConfiguration);
        _automationConfiguration = automationConfiguration;
    }

    @Override
    public void checkDependency(Object source, ArrayList<Dependency> dependencies, int level) {
    }
}

