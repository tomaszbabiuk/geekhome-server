package com.geekhome.synchronizationmodule.business;

import com.google.gson.annotations.SerializedName;

public class SynchronizationRequest {
    @SerializedName("code")
    private String _code;

    @SerializedName("type")
    private SynchronizationCommandType _type;

    @SerializedName("parameter")
    private String _parameter;

    @SerializedName("value")
    private String _value;

    public SynchronizationRequest(SynchronizationCommandType type, String parameter, String value, String code) {
        _type = type;
        _parameter = parameter;
        _value = value;
        _code = code;
    }

    public SynchronizationRequest(SynchronizationCommandType type) {
        this(type, null, null, null);
    }

    public SynchronizationCommandType getType() {
        return _type;
    }

    public String getParameter() {
        return _parameter;
    }

    public String getCode() {
        return _code;
    }

    public String getValue() {
        return _value;
    }
}
