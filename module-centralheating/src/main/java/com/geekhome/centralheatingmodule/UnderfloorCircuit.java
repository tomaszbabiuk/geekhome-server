package com.geekhome.centralheatingmodule;

import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.configuration.Persistable;
import com.geekhome.common.configuration.JSONArrayList;
import com.geekhome.common.configuration.InactiveState;
import com.geekhome.common.localization.ILocalizationProvider;

public class UnderfloorCircuit extends Radiator {
    private Double _minFloorTemperature;
    private Double _maxFloorTemperature;
    private String _floorThermometerId;

    @Persistable(name="MinFloorTemperature")
    public Double getMinFloorTemperature() {
        return _minFloorTemperature;
    }

    public void setMinFloorTemperature(Double value) {
        _minFloorTemperature = value;
    }

    @Persistable(name="MaxFloorTemperature")
    public Double getMaxFloorTemperature() {
        return _maxFloorTemperature;
    }

    public void setMaxFloorTemperature(Double value) {
        _maxFloorTemperature = value;
    }

    @Persistable(name="FloorThermometerId")
    public String getFloorThermometerId() {
        return _floorThermometerId;
    }

    public void setFloorThermometerId(String value) {
        _floorThermometerId = value;
    }

    public UnderfloorCircuit(DescriptiveName name, String portId, String roomId, InactiveState inactiveState,
                             String activationTime, String ambientThermometerId, String floorThermometerId,
                             String temperatureControllerId, Double minFloorTemperature, Double maxFloorTemperature) {
        super(name, portId, roomId, inactiveState, activationTime, ambientThermometerId, temperatureControllerId, "heat");
        setFloorThermometerId(floorThermometerId);
        setMinFloorTemperature(minFloorTemperature);
        setMaxFloorTemperature(maxFloorTemperature);
    }

    @Override
    public JSONArrayList<DescriptiveName> buildBlockCategories(ILocalizationProvider localizationProvider) {
        JSONArrayList<DescriptiveName> categories = super.buildBlockCategories(localizationProvider);
        categories.add(new DescriptiveName(localizationProvider.getValue("CH:BlocksEnablingFloorTemperaturePriority"), "floortemppriority"));
        return categories;
    }
}
