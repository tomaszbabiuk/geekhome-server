package com.geekhome.common;

public class ConnectableSynchronizedInputPort<T> extends SynchronizedInputPort<T> implements IConnectable {

    private boolean _connected;

    public ConnectableSynchronizedInputPort(String id, T initialValue, boolean connected) {
        super(id, initialValue);
        _connected = connected;
    }

    @Override
    public boolean isConnected() {
        return _connected;
    }

    public void markDisconnected() {
        _connected = false;
    }
}
