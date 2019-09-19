package com.geekhome.ventilationmodule.automation;

public enum Gear {
    Manual(0),
    First(1),
    Second(2),
    Third(3);

    private final int _index;

    Gear(int index) {
        _index = index;
    }

    public int toInt() {
        return _index;
    }
}
