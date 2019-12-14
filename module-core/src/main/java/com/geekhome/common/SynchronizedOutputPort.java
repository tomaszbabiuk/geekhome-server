package com.geekhome.common;

import com.geekhome.hardwaremanager.IOutputPort;

public class SynchronizedOutputPort<T> extends SynchronizedInputPort<T> implements IOutputPort<T> {

    public SynchronizedOutputPort(String id, T initialValue, long connectionLostInterval) {
        super(id, initialValue, connectionLostInterval);
    }

    @Override
    public void write(T value) throws Exception {
        setValue(value, WhoChangeValue.System);
    }
}
