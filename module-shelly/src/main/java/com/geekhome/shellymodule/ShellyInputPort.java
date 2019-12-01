package com.geekhome.shellymodule;

import com.geekhome.common.SynchronizedInputPort;

public class ShellyInputPort<T> extends SynchronizedInputPort<T> {
    private String _readTopic;

    public ShellyInputPort(String id, T initialValue, String readTopic) {
        super(id, initialValue);
        _readTopic = readTopic;
    }

    public String getReadTopic() {
        return _readTopic;
    }
}
