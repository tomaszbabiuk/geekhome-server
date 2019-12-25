package com.geekhome.centralheatingmodule;

import com.geekhome.common.*;
import com.geekhome.common.configuration.*;
import com.geekhome.http.ILocalizationProvider;

public class CirculationPump extends OnOffDeviceBase {
    private String _minimumWorkingTime;
    private String _thermometerId;

    @Persistable(name = "MinimumWorkingTime")
    public String getMinimumWorkingTime() {
        return _minimumWorkingTime;
    }

    public void setMinimumWorkingTime(String value) {
        _minimumWorkingTime = value;
    }

    @Persistable(name = "ThermometerId")
    public String getThermometerId() {
        return _thermometerId;
    }

    public void setThermometerId(String value) {
        _thermometerId = value;
    }

   @Override
    @Persistable(name = "TriggeredManually")
    public YesNo getTriggeredManually() {
        return YesNo.Yes;
    }

    @Override
    public CollectorCollection<State> buildStates(ILocalizationProvider localizationProvider) {
        CollectorCollection<State> states = new CollectorCollection<>();
        states.add(new State(new DescriptiveName(localizationProvider.getValue("CH:Pumping"), "pumping"), StateType.Control, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("CH:WaterHeated"),"wh"), StateType.ReadOnly, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("C:SwitchingOff"),"soff"), StateType.ReadOnly, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("C:Off"),"off"), StateType.Control, false));

        return states;
    }

    public CirculationPump(DescriptiveName name, String portId, String roomId, String minimumWorkingTime, String thermometerId) {
        super(name, portId, roomId, YesNo.No, "watertreatmentplant", DeviceCategory.HotWater);
        setMinimumWorkingTime(minimumWorkingTime);
        setThermometerId(thermometerId);
    }

    @Override
    public String getGroupName(ILocalizationProvider localizationProvider) {
        return localizationProvider.getValue("CH:CirculationPumps");
    }
}
