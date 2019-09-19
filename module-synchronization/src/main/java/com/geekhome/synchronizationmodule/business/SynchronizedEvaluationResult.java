package com.geekhome.synchronizationmodule.business;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Hashtable;

public class SynchronizedEvaluationResult {
    @SerializedName("value")
    private String _value;

    @SerializedName("interfaceValue")
    private String _interfaceValue;

    @SerializedName("isSignaled")
    private boolean _isSignaled;

    @SerializedName("deviceId")
    private String _deviceId;

    @SerializedName("controlMode")
    private int _controlMode;

    @SerializedName("descriptions")
    private ArrayList<SynchronizedDescription> _descriptions;

    @SerializedName("modeValues")
    private Hashtable<String, Double> _modeValues;

    public SynchronizedEvaluationResult() {
    }

    public String getValue() {
        return _value;
    }

    public void setValue(String value) {
        _value = value;
    }

    public boolean isSignaled() {
        return _isSignaled;
    }

    public void setSignaled(boolean isSignaled) {
        _isSignaled = isSignaled;
    }

    public String getDeviceId() {
        return _deviceId;
    }

    public void setDeviceId(String deviceId) {
        _deviceId = deviceId;
    }

    public int getControlMode() {
        return _controlMode;
    }

    public void setControlMode(int controlMode) {
        _controlMode = controlMode;
    }

    public ArrayList<SynchronizedDescription> getDescriptions() {
        return _descriptions;
    }

    public void setDescriptions(ArrayList<SynchronizedDescription> descriptions) {
        _descriptions = descriptions;
    }

    public String getInterfaceValue() {
        return _interfaceValue;
    }

    public void setInterfaceValue(String interfaceValue) {
        _interfaceValue = interfaceValue;
    }

    public Hashtable<String, Double> getModeValues() {
        return _modeValues;
    }

    public void setModeValues(Hashtable<String, Double> modeValues) {
        _modeValues = modeValues;
    }
}