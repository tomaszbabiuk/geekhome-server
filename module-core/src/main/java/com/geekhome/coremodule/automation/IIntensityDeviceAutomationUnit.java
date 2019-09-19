package com.geekhome.coremodule.automation;

public interface IIntensityDeviceAutomationUnit {
    ControlMode getControlMode();
    void setIntensity(Integer intensity) throws Exception;
}
