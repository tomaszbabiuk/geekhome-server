package com.geekhome.mqttmodule.tasmotaapi;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class TemperatureUnitTypeAdapter extends TypeAdapter<TemperatureUnit> {

    @Override
    public void write(JsonWriter writer, TemperatureUnit value) throws IOException {
        if (value == null) {
            writer.value(0);
        } else {
            writer.value(value.getCode());
        }
    }

    public TemperatureUnit read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return TemperatureUnit.Unknown;
        }

        String nextStr = reader.nextString();
        return TemperatureUnit.fromString(nextStr);
    }
}
