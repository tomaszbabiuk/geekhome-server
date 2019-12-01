package com.geekhome.shellymodule;

import com.geekhome.common.SynchronizedInputPort;

public class ShellyDigitalInputPort extends ShellyInputPort<Boolean> {
    public ShellyDigitalInputPort(String id, Boolean initialValue, String readTopic) {
        super(id, initialValue, readTopic);
    }
}
