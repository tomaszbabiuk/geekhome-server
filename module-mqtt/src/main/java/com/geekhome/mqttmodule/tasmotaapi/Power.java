package com.geekhome.mqttmodule.tasmotaapi;

public enum Power {
    On("ON", true),
    Off("OFF", false);

    private String _code;
    private boolean _asBoolean;

    Power(String code, boolean state) {
        _code = code;
        _asBoolean = state;
    }

    public String getCode() {
        return _code;
    }

    public boolean asBoolean() {
        return _asBoolean;
    }

    public static Power fromString(String i) {
        for(Power value : values()) {
            if (value.getCode().equals(i)) {
                return value;
            }
        }

        return null;
    }
}
