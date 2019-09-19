package com.geekhome.mqttmodule.tasmotaapi;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class PowerTypeAdapter extends TypeAdapter<Power> {

    @Override
    public void write(JsonWriter writer, Power value) throws IOException {
        if (value == null) {
            writer.value(0);
        } else {
            writer.value(value.getCode());
        }
    }

    public Power read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return Power.Off;
        }

        String nextInt = reader.nextString();
        return Power.fromString(nextInt);
    }
}
