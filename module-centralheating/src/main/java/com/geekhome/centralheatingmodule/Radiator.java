package com.geekhome.centralheatingmodule;

import com.geekhome.common.*;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.configuration.Persistable;
import com.geekhome.common.configuration.JSONArrayList;
import com.geekhome.coremodule.OnOffDeviceBase;
import com.geekhome.coremodule.InactiveState;
import com.geekhome.coremodule.YesNo;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.httpserver.modules.CollectorCollection;

public class Radiator extends OnOffDeviceBase {
    private InactiveState _inactiveState;
    private String _activationTime;
    private String _ambientThermometerId;
    private String _temperatureControllerId;

    @Override
    @Persistable(name="ActuatorPortId")
    public String getPortId() {
        return super.getPortId();
    }

    @Persistable(name="AmbientThermometerId")
    public String getAmbientThermometerId() {
        return _ambientThermometerId;
    }

    public void setAmbientThermometerId(String value) {
        _ambientThermometerId = value;
    }

    @Persistable(name="TemperatureControllerId")
    public String getTemperatureControllerId() {
        return _temperatureControllerId;
    }

    public void setTemperatureControllerId(String value) {
        _temperatureControllerId = value;
    }

    @Persistable(name = "InactiveState")
    public InactiveState getInactiveState() {
        return _inactiveState;
    }

    public void setInactiveState(InactiveState value) {
        _inactiveState = value;
    }

    @Persistable(name="ActivationTime")
    public String getActivationTime() {
        return _activationTime;
    }

    public void setActivationTime(String value) {
        _activationTime = value;
    }

    public Radiator(DescriptiveName name, String portId, String roomId, InactiveState inactiveState,
                    String activationTime, String ambientThermometerId, String temperatureControllerId) {
        this(name, portId, roomId, inactiveState, activationTime, ambientThermometerId, temperatureControllerId, "radiator");
    }

    public Radiator(DescriptiveName name, String portId, String roomId, InactiveState inactiveState,
                               String activationTime, String ambientThermometerId, String temperatureControllerId, String iconName) {
        super(name, portId, roomId, YesNo.No, iconName, DeviceCategory.Heating);
        setInactiveState(inactiveState);
        setActivationTime(activationTime);
        setAmbientThermometerId(ambientThermometerId);
        setTemperatureControllerId(temperatureControllerId);
    }

    @Override
    public CollectorCollection<State> buildStates(ILocalizationProvider localizationProvider) {
        CollectorCollection<State> states = new CollectorCollection<>();
        states.add(new State(new DescriptiveName(localizationProvider.getValue("C:Open"), "open"), StateType.ReadOnly, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("C:Closed"), "closed"), StateType.ReadOnly, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("C:Off"),"off"), StateType.ReadOnly, false));
        return states;
    }

    @Override
    public JSONArrayList<DescriptiveName> buildBlockCategories(ILocalizationProvider localizationProvider) {
        JSONArrayList<DescriptiveName> categories = new JSONArrayList<>();
        categories.add(new DescriptiveName(localizationProvider.getValue("CH:BlocksForcingCircuitToEnable"), "forceon"));
        categories.add(new DescriptiveName(localizationProvider.getValue("CH:BlocksForcingCircuitToDisable"), "forceoff"));
        return categories;
    }

    @Override
    public String getGroupName(ILocalizationProvider localizationProvider) {
        return localizationProvider.getValue("CH:Radiators");
    }
}
