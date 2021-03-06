package com.geekhome.lightsmodule;

import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.configuration.Persistable;
import com.geekhome.common.configuration.ConditionBase;

public class TwilightCondition extends ConditionBase {
    private double _latitude;
    private double _longitude;

    @Persistable(name = "Latitude")
    public double getLatitude() {
        return _latitude;
    }

    public void setLatitude(double value) {
        _latitude = value;
    }

    @Persistable(name = "Longitude")
    public double getLongitude() {
        return _longitude;
    }

    public void setLongitude(double value) {
        _longitude = value;
    }

    public TwilightCondition(DescriptiveName name, double longitude, double latitude) {
        super(name);
        {
            setLatitude(latitude);
            setLongitude(longitude);
        }
    }
}