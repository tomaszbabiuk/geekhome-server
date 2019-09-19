package com.geekhome.httpserver.modules;

public interface IAutomationHook {
    void beforeAutomationLoop();
    void onAutomationLoop();
}