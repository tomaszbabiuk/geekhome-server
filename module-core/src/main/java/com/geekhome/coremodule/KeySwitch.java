package com.geekhome.coremodule;

import com.geekhome.common.*;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.configuration.Persistable;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.httpserver.modules.CollectorCollection;

public class KeySwitch extends PortRoomMultistateDevice {
    @Override
    public String getGroupName(ILocalizationProvider localizationProvider) {
        return localizationProvider.getValue("C:KeySwitches");
    }

    public KeySwitch(DescriptiveName name, String portId, String roomId, DeviceCategory deviceCategory) {
        super(name, "trigger", deviceCategory, portId, roomId);
    }

    @Override
    protected ControlType calculateControlType() {
        return (getPortId() != null && getPortId().equals("-")) ? ControlType.Code : ControlType.ReadValue;
    }

    @Override
    public CollectorCollection<State> buildStates(ILocalizationProvider localizationProvider) {
        CollectorCollection<State> states = new CollectorCollection<>();
        states.add(new State(new DescriptiveName(localizationProvider.getValue("C:On"), "on",
                localizationProvider.getValue("C:SwitchOn")), StateType.NonSignaledAction, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("C:Off"), "off",
                localizationProvider.getValue("C:SwitchOff")), StateType.SignaledAction, false));
        return states;
    }

    @Override
    @Persistable(name="HasNonReadonlyStates")
    public boolean hasNonReadonlyStates() {
        return true;
    }
}
