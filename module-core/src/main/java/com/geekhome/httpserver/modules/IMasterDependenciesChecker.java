package com.geekhome.httpserver.modules;

import java.util.ArrayList;

public interface IMasterDependenciesChecker {
    void checkDependencyInAllDependenciesCheckers(Object obj, ArrayList<Dependency> dependencies, int level);
}