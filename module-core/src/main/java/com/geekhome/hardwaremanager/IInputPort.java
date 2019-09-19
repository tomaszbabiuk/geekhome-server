package com.geekhome.hardwaremanager;

public interface IInputPort<T> extends IPort {
    T read();
}
