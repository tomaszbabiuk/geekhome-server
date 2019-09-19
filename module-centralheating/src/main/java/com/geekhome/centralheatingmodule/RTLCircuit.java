package com.geekhome.centralheatingmodule;

import com.geekhome.common.DescriptiveName;
import com.geekhome.common.Persistable;
import com.geekhome.common.json.JSONArrayList;
import com.geekhome.coremodule.InactiveState;
import com.geekhome.http.ILocalizationProvider;

public class RTLCircuit extends Radiator {
    private Double _minReturnTemperature;
    private Double _maxReturnTemperature;
    private String _returnThermometerId;
    
    @Persistable(name="MinReturnTemperature")
    public Double getMinReturnTemperature() {
        return _minReturnTemperature;
    }

    public void setMinReturnTemperature(Double value) {
        _minReturnTemperature = value;
    }

    @Persistable(name="MaxReturnTemperature")
    public Double getMaxReturnTemperature() {
        return _maxReturnTemperature;
    }

    public void setMaxReturnTemperature(Double value) {
        _maxReturnTemperature = value;
    }

    @Persistable(name="ReturnThermometerId")
    public String getReturnThermometerId() {
        return _returnThermometerId;
    }

    public void setReturnThermometerId(String value) {
        _returnThermometerId = value;
    }


    public RTLCircuit(DescriptiveName name, String portId, String roomId, InactiveState inactiveState,
                          String activationTime, String ambientThermometerId, String returnThermometerId,
                          String temperatureControllerId, Double minReturnTemperature, Double maxReturnTemperature) {
        super(name, portId, roomId, inactiveState, activationTime, ambientThermometerId, temperatureControllerId, "rtl");
        setReturnThermometerId(returnThermometerId);
        setMinReturnTemperature(minReturnTemperature);
        setMaxReturnTemperature(maxReturnTemperature);
    }

    @Override
    public JSONArrayList<DescriptiveName> buildBlockCategories(ILocalizationProvider localizationProvider) {
        JSONArrayList<DescriptiveName> categories = super.buildBlockCategories(localizationProvider);
        categories.add(new DescriptiveName(localizationProvider.getValue("CH:BlocksEnablingReturnTemperaturePriority"), "returntemppriority"));
        return categories;
    }
}
