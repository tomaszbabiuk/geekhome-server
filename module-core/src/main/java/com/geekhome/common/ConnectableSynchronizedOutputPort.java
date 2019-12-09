package com.geekhome.common;

public class ConnectableSynchronizedOutputPort<T> extends SynchronizedOutputPort<T> implements IConnectable {

    private boolean _connected;

    public ConnectableSynchronizedOutputPort(String id, T initialValue, boolean connected) {
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
