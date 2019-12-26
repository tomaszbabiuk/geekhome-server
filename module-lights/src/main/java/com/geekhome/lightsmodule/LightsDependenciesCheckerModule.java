package com.geekhome.lightsmodule;

import com.geekhome.common.configuration.MasterConfiguration;
import com.geekhome.common.configuration.DependenciesCheckerModule;
import com.geekhome.common.configuration.Dependency;

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

