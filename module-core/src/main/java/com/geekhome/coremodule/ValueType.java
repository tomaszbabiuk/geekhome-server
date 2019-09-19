package com.geekhome.coremodule;

public enum ValueType {
    Temperature(0),
    Humidity(1),
    Luminosity(2);

    private int _index;

    ValueType(int index) {
        _index = index;
    }

    @Override
    public String toString() {
        return String.valueOf(_index);
    }

    public int toInt() {
        return _index;
    }
}
