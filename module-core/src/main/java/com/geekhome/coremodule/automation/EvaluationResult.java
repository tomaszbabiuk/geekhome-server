package com.geekhome.coremodule.automation;

import com.geekhome.common.KeyValue;
import com.geekhome.common.Persistable;
import com.geekhome.common.json.JSONArrayList;
import com.geekhome.common.json.JSONAwareBase;

import java.util.Objects;

public class EvaluationResult extends JSONAwareBase {
    private Object _value;
    private String _interfaceValue;
    private JSONArrayList<KeyValue> _descriptions;
    private ControlMode _controlMode;
    private boolean _isSignaled;
    private boolean _isAlternating;
    private String _colorValue;

    @Persistable(name="Value")
    public Object getValue() {
        return _value;
    }

    public void setValue(Object value) {
        _value = value;
    }

    @Persistable(name="IsSignaled")
    public boolean isSignaled() {
        return _isSignaled;
    }

    public void setIsSignaled(boolean value) {
        _isSignaled = value;
    }

    @Persistable(name="IsAlternating")
    public boolean isAlternating() {
        return _isAlternating;
    }

    public void setIsAlternating(boolean value) {
        _isAlternating = value;
    }

    @Persistable(name="InterfaceValue")
    public String getInterfaceValue() {
        return _interfaceValue;
    }

    @Persistable(name="ColorValue")
    public String getColorValue() {
        return _colorValue;
    }

    public void setColorValue(String colorValue) {
        _colorValue = colorValue;
    }

    public void setInterfaceValue(String value) {
        _interfaceValue = value;
    }

    @Persistable(name="Descriptions")
    public JSONArrayList<KeyValue> getDescriptions() {
        return _descriptions;
    }

    public void setControlMode(ControlMode controlMode) {
        _controlMode = controlMode;
    }

    @Persistable(name="ControlMode")
    public ControlMode getControlMode() {
        return _controlMode;
    }

    public void setDescriptions(JSONArrayList<KeyValue> value) {
        _descriptions = value;
    }

    public EvaluationResult(Object value, String interfaceValue, boolean isSignaled, JSONArrayList<KeyValue> descriptions, ControlMode controlMode, boolean isAlternating) {
        setDescriptions(descriptions);
        setValue(value);
        setInterfaceValue(interfaceValue);
        setControlMode(controlMode);
        setIsSignaled(isSignaled);
        setIsAlternating(isAlternating);
    }

    public EvaluationResult(Object value, String interfaceValue, boolean isSignaled, JSONArrayList<KeyValue> descriptions) {
        this(value, interfaceValue, isSignaled, descriptions, ControlMode.Auto, false);
    }

    public EvaluationResult(Object value, String interfaceValue, boolean isSignaled) {
        this(value, interfaceValue, isSignaled, new JSONArrayList<>());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EvaluationResult that = (EvaluationResult) o;
        return _isSignaled == that._isSignaled &&
                _isAlternating == that._isAlternating &&
                Objects.equals(_value, that._value) &&
                Objects.equals(_interfaceValue, that._interfaceValue) &&
                Objects.equals(_descriptions, that._descriptions) &&
                _controlMode == that._controlMode &&
                Objects.equals(_colorValue, that._colorValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_value, _interfaceValue, _descriptions != null ? _descriptions.toJSONString() : 0, _controlMode, _isSignaled, _isAlternating, _colorValue);
    }
}
