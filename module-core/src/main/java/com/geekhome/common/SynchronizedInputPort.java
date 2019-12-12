package com.geekhome.common;

import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;
import com.geekhome.hardwaremanager.IInputPort;

import java.util.Calendar;

public class SynchronizedInputPort<T> extends ConnectiblePortBase implements IInputPort<T> {
    private ILogger _logger = LoggingService.getLogger();
    private T _value;

    public SynchronizedInputPort(String id) {
        super(id);
    }

    public SynchronizedInputPort(String id, T initialValue) {
        this(id);
        setValue(initialValue);
    }

    @Override
    public T read() {
        return getValue();
    }

    public synchronized void setValue(T value) {
        if (!value.equals(_value)) {
            _logger.activity(getId(), value, _value);
        }

        _value = value;

        updateLastSeen(Calendar.getInstance().getTimeInMillis());
    }

    public synchronized T getValue() {
        return _value;
    }
}
