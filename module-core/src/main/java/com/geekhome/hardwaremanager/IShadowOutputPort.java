package com.geekhome.hardwaremanager;

public interface IShadowOutputPort<T> extends IShadowPort, IOutputPort<T> {
    void setTarget(IOutputPort<T> target);
}
