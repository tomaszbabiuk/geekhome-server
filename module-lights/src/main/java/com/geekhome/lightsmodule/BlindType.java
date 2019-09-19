package com.geekhome.lightsmodule;

public enum BlindType {
    Wired(0),
    Wireless(1);

    private int _index;

    private BlindType(int index) {
        _index = index;
    }

    @Override
    public String toString() {
        return String.valueOf(_index);
    }

    public int toInt() {
        return _index;
    }

    public static BlindType fromInt(int i) {
        for(BlindType value : values()) {
            if (value.toInt() == i) {
                return value;
            }
        }

        return null;
    }
}
