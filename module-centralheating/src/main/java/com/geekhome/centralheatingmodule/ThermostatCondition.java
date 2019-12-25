package com.geekhome.centralheatingmodule;

import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.EqualityOperator;
import com.geekhome.common.configuration.Persistable;
import com.geekhome.coremodule.ConditionBase;

public class ThermostatCondition extends ConditionBase {
    private String _temperatureControllerId;
    private String _thermometerId;
    private EqualityOperator _operator;

    @Persistable(name = "TemperatureControllerId")
    public String getTemperatureControllerId() {
        return _temperatureControllerId;
    }

    public void setTemperatureControllerId(String value) {
        _temperatureControllerId = value;
    }

    @Persistable(name = "ThermometerId")
    public String getThermometerId() {
        return _thermometerId;
    }

    public void setThermometerId(String value) {
        _thermometerId = value;
    }

    @Persistable(name = "Operator")
    public EqualityOperator getOperator() {
        return _operator;
    }

    public void setOperator(EqualityOperator value) {
        _operator = value;
    }

    @Override
    public String[] getDevicesIds() {
        String[] result = new String[2];
        result[0] = getThermometerId();
        result[1] = getTemperatureControllerId();
        return result;
    }

    public ThermostatCondition(DescriptiveName name, EqualityOperator equalityOperator, String thermometerId, String temperatureControllerId) {
        super(name);
        setOperator(equalityOperator);
        setThermometerId(thermometerId);
        setTemperatureControllerId(temperatureControllerId);
    }
}
