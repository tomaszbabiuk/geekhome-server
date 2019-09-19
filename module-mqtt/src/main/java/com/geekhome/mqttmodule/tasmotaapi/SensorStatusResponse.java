package com.geekhome.mqttmodule.tasmotaapi;

import com.google.gson.annotations.SerializedName;

public class SensorStatusResponse {

    @SerializedName("StatusSNS")
    private SensorStatus _status;

    public SensorStatus getStatus() {
        return _status;
    }
}
