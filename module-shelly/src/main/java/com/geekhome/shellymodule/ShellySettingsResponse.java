package com.geekhome.shellymodule;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ShellySettingsResponse {

    @SerializedName("device")
    private ShellyDeviceBrief _device;

    @SerializedName("relays")
    private ArrayList<ShellyRelayBrief> _relays;

    public ShellyDeviceBrief getDevice() {
        return _device;
    }

    public ArrayList<ShellyRelayBrief> getRelays() {
        return _relays;
    }
}
