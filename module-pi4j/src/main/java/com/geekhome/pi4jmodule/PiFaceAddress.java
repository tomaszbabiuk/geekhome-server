package com.geekhome.pi4jmodule;

public enum PiFaceAddress {
    JUMPERS_00((byte)0b01000000, "00"),
    JUMPERS_01((byte)0b01000100, "01"),
    JUMPERS_10((byte)0b01000010, "10"),
    JUMPERS_11((byte)0b01000110, "11");

    private byte _spiAddress;
    private String _code;

    PiFaceAddress(byte spiAddress, String code) {
        _spiAddress = spiAddress;
        _code = code;
    }

    public byte getSpiAddress() {
        return _spiAddress;
    }

    public String getCode() {
        return _code;
    }
}
