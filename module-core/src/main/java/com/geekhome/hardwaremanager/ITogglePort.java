package com.geekhome.hardwaremanager;

public interface ITogglePort extends IPort {
    void toggleOn() throws Exception;
    void toggleOff() throws Exception;
}
