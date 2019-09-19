package com.geekhome.mqttmodule.tasmotaapi;

public enum TemperatureUnit {
    C("C"),
    K("K"),
    Unknown("");

    private String _code;

    TemperatureUnit(String code) {
        _code = code;
    }

    public String getCode() {
        return _code;
    }

    public static TemperatureUnit fromString(String i) {
        for(TemperatureUnit value : values()) {
            if (value.getCode().equals(i)) {
                return value;
            }
        }

        return null;
    }
}
