package com.geekhome.shellymodule;

import com.geekhome.common.IConnectible;
import com.geekhome.common.SynchronizedInputPort;

public abstract class ShellyInputPort<T> extends SynchronizedInputPort<T> implements IConnectible, IShellyPort {
    private String _readTopic;
    private boolean _connected;

    public ShellyInputPort(String id, T initialValue, String readTopic) {
        super(id, initialValue);
        _readTopic = readTopic;
    }

    public String getReadTopic() {
        return _readTopic;
    }

    @Override
    public boolean isConnected() {
        return _connected;
    }

    public void markDisconnected() {
        _connected = false;
    }

    @Override
    public synchronized void setValue(T value) {
        super.setValue(value);
        _connected = true;
    }
}
