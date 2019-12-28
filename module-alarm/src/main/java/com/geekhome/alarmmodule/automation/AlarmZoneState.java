package com.geekhome.alarmmodule.automation;

import com.geekhome.common.localization.ILocalizationProvider;

public enum AlarmZoneState {
    Disarmed(0),
    Leaving(1),
    Armed(2),
    Prealarm(3),
    Alarm(4);

    private int _index;

    private AlarmZoneState(int index) {
        _index = index;
    }

    @Override
    public String toString() {
        return String.valueOf(_index);
    }

    public int toInt() {
        return _index;
    }

    public String translate(ILocalizationProvider localizationProvider) {
        switch (_index) {
            case 0:
                return localizationProvider.getValue("ALARM:AlarmZoneState0.Disarmed");
            case 1:
                return localizationProvider.getValue("ALARM:AlarmZoneState1.Leaving");
            case 2:
                return localizationProvider.getValue("ALARM:AlarmZoneState2.Armed");
            case 3:
                return localizationProvider.getValue("ALARM:AlarmZoneState3.Prealarm");
            default:
                return localizationProvider.getValue("ALARM:AlarmZoneState4.Alarm");
        }
    }

}
