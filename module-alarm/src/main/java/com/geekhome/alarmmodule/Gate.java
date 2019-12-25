package com.geekhome.alarmmodule;

import com.geekhome.common.*;
import com.geekhome.common.configuration.*;
import com.geekhome.http.ILocalizationProvider;

public class Gate extends AlarmSensor implements IBlocksTarget, IPortsDrivenDevice {
    private String _code;
    private String _magneticDetectorPortId;
    private String _gateControlPortId;

    @Persistable(name="DeviceCategory")
    public DeviceCategory getCategory() { return DeviceCategory.Locks; }

    @Persistable(name="Code")
    public String getCode() {
        return _code;
    }

    public void setCode(String value) {
        _code = value;
    }

    @Persistable(name = "GateControlPortId")
    public String getGateControlPortId() {
        return _gateControlPortId;
    }

    public void setGateControlPortId(String value) {
        _gateControlPortId = value;
    }

    @Persistable(name = "MagneticDetectorPortId")
    public String getMagneticDetectorPortId() {
        return _magneticDetectorPortId;
    }

    public void setMagneticDetectorPortId(String value) {
        _magneticDetectorPortId = value;
    }

    public Gate(DescriptiveName name, String magneticDetectorPortId, String gateControlPortId, String roomId,
                InactiveState inactivityState, String delayTime, String pinCode, String iconName) {
        super(name, roomId, inactivityState, delayTime, iconName);
        setMagneticDetectorPortId(magneticDetectorPortId);
        setGateControlPortId(gateControlPortId);
        setCode(pinCode);
    }

    @Override
    protected ControlType calculateControlType() {
        return ControlType.Code;
    }

    @Override
    public CollectorCollection<State> buildStates(ILocalizationProvider localizationProvider) {
        CollectorCollection<State> states = new CollectorCollection<>();
        states.add(new State(new DescriptiveName(localizationProvider.getValue("ALARM:MagneticDetectorState0Open.Disarmed"), "open-disarmed"), StateType.ReadOnly, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("ALARM:MagneticDetectorState0Closed.Disarmed"), "closed-disarmed"), StateType.ReadOnly, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("ALARM:MagneticDetectorState1Open.Watching"),"open-watching"), StateType.ReadOnly, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("ALARM:MagneticDetectorState1Closed.Watching"),"closed-watching"), StateType.ReadOnly, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("ALARM:MagneticDetectorState2Open.Prealarm"),"open-prealarm"), StateType.ReadOnly, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("ALARM:MagneticDetectorState2Closed.Prealarm"),"closed-prealarm"), StateType.ReadOnly, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("ALARM:MagneticDetectorState3Open.Alarm"),"open-alarm"), StateType.ReadOnly, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("ALARM:MagneticDetectorState3Closed.Alarm"),"closed-alarm"), StateType.ReadOnly, false));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("ALARM:GateOpen"), "*doopen",
                localizationProvider.getValue("C:DoOpen")), StateType.NonSignaledAction, true));
        states.add(new State(new DescriptiveName(localizationProvider.getValue("ALARM:GateClosed"), "*doclose",
                localizationProvider.getValue("C:DoClose")), StateType.SignaledAction, false));
        return states;
    }

    @Override
    @Persistable(name="HasNonReadonlyStates")
    public boolean hasNonReadonlyStates() {
        return true;
    }

    @Override
    public JSONArrayList<DescriptiveName> buildBlockCategories(ILocalizationProvider localizationProvider) {
        JSONArrayList<DescriptiveName> categories = new JSONArrayList<>();
        categories.add(new DescriptiveName(localizationProvider.getValue("ALARM:BlocksClosingGate"), "*doclose"));
        categories.add(new DescriptiveName(localizationProvider.getValue("ALARM:BlocksOpeningGate"), "*doopen"));
        return categories;
    }

    @Override
    public String getPortsIds() {
        return getGateControlPortId() + "," + getMagneticDetectorPortId();
    }

    @Override
    public String getGroupName(ILocalizationProvider localizationProvider) {
        return localizationProvider.getValue("ALARM:Gates");
    }
}