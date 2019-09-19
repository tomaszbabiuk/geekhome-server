package com.geekhome.hardwaremanager;

public class PortNotFoundException extends Exception {
    private String _uniqueId;

    public PortNotFoundException(String uniqueId) {
        _uniqueId = uniqueId;
    }

    @Override
    public String getMessage() {
        return "Cannot find port of unique id: " + _uniqueId;
    }
}
