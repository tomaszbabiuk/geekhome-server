package com.geekhome.extafreemodule;

public enum PairingType {
    ROP("rop"),
    SRP("srp");
    private final String _type;

    PairingType(String type) {
        _type=type;
    }

    public String getType() {
        return _type;
    }
}
