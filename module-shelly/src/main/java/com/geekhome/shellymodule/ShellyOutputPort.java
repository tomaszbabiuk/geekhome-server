package com.geekhome.shellymodule;

import com.geekhome.common.IConnectable;
import com.geekhome.common.SynchronizedOutputPort;

public abstract class ShellyOutputPort<T> extends SynchronizedOutputPort<T> implements IConnectable, IShellyOutputPort {

    private String _writeTopic;
    private String _readTopic;
    private boolean _valueChanged;
    private boolean _connected;

    public ShellyOutputPort(String id, T initialValue, String readTopic, String writeTopic) {
        super(id, initialValue);
        _readTopic = readTopic;
        _writeTopic = writeTopic;
        _connected = true;
    }

    public String getReadTopic() {
        return _readTopic;
    }

    public String getWriteTopic() {
        return _writeTopic;
    }

    @Override
    public void write(T value) throws Exception {
        T currentValue = read();
        if (value != currentValue) {
            super.write(value);
            _valueChanged = true;
        }

        _connected = true;
    }

    @Override
    public void resetLatch() {
        _valueChanged = false;
    }

    @Override
    public boolean didChangeValue() {
        return _valueChanged;
    }

    @Override
    public boolean isConnected() {
        return _connected;
    }

    public void markDisconnected() {
        _connected = false;
    }

}
