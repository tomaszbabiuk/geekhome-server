package com.geekhome.mqttmodule.tasmotaapi;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class PowerStatus {

    @SerializedName("POWER")
    private Power _power;

    @SerializedName("POWER1")
    private Power _power1;

    @SerializedName("POWER2")
    private Power _power2;

    @SerializedName("POWER3")
    private Power _power3;

    @SerializedName("POWER4")
    private Power _power4;

    @SerializedName("PWM")
    private Map<String, Integer> _pwm;

    public Map<String, Integer> getPwm() {
        return _pwm;
    }

    public Power getPower() {
        return _power;
    }

    public Power getPower1() {
        return _power1;
    }

    public Power getPower2() {
        return _power2;
    }

    public Power getPower3() {
        return _power3;
    }

    public Power getPower4() {
        return _power4;
    }
}
