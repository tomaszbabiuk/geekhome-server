package com.geekhome.shellymodule;

import com.google.gson.annotations.SerializedName;

public class ShellyResponse {

    private String type;
    private String mac;
    private boolean auth;

    @SerializedName("num_outputs")
    private int numOutputs;

    @SerializedName("num_meters")
    private int numMeters;

    public String getType() {
        return type;
    }

    public String getMac() {
        return mac;
    }

    public boolean isAuth() {
        return auth;
    }

    public int getNumOutputs() {
        return numOutputs;
    }

    public int getNumMeters() {
        return numMeters;
    }
}
