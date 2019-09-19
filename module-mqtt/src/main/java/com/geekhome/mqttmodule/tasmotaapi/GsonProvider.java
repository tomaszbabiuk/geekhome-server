package com.geekhome.mqttmodule.tasmotaapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonProvider {

    public Gson provide() {
        return new GsonBuilder()
                .registerTypeAdapter(Power.class, new PowerTypeAdapter())
                .registerTypeAdapter(TemperatureUnit.class, new TemperatureUnitTypeAdapter())
                .create();
    }
}
