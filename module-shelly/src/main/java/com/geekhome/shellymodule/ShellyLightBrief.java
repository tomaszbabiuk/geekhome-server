package com.geekhome.shellymodule;

import com.google.gson.annotations.SerializedName;

public class ShellyLightBrief {
    @SerializedName("ison")
    private boolean _isOn;

    public boolean isOn() {
        return _isOn;
    }
}
