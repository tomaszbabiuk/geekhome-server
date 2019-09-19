package com.geekhome.mqttmodule.tasmotaapi;

import com.google.gson.annotations.SerializedName;

public class PowerStatusResponse {

    @SerializedName("StatusSTS")
    private PowerStatus _powerStatus;

    public PowerStatus getPowerStatus() {
        return _powerStatus;
    }
}
