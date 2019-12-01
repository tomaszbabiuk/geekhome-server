package com.geekhome.shellymodule;

import com.geekhome.common.SynchronizedOutputPort;

public class ShellyOutputPort<T> extends SynchronizedOutputPort<T> {

    private String _writeTopic;
    private String _readTopic;
    private boolean _valueChanged;

    public ShellyOutputPort(String id, T initialValue, String readTopic, String writeTopic) {
        super(id, initialValue);
        _readTopic = readTopic;
        _writeTopic = writeTopic;
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
    }

    public void resetLatch() {
        _valueChanged = false;
    }

    public boolean didChangeValue() {
        return _valueChanged;
    }
}
