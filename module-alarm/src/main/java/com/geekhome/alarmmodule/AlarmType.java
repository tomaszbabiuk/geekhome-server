package com.geekhome.alarmmodule;

import com.geekhome.common.localization.ILocalizationProvider;

public enum AlarmType {
    Burglary(0),
    Silent(1),
    Fire(2),
    Flood(3),
    Medical(4),
    Prealarm(5),
    Sabotage(6),
    SecurityCall(7),
    Other(100);

    private int _index;

    AlarmType(int index) {
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
                return localizationProvider.getValue("ALARM:AlarmType0.Burglary");
            case 1:
                return localizationProvider.getValue("ALARM:AlarmType1.Silent");
            case 2:
                return localizationProvider.getValue("ALARM:AlarmType2.Fire");
            case 3:
                return localizationProvider.getValue("ALARM:AlarmType3.Flood");
            case 4:
                return localizationProvider.getValue("ALARM:AlarmType4.Medical");
            case 5:
                return localizationProvider.getValue("ALARM:AlarmType5.Prealarm");
            case 6:
                return localizationProvider.getValue("ALARM:AlarmType6.Sabotage");
            case 7:
                return localizationProvider.getValue("ALARM:AlarmType7.SecurityCall");
            default:
                return localizationProvider.getValue("ALARM:AlarmType100.Other");
        }
    }

    public static AlarmType fromInt(int i) {
        for(AlarmType value : values()) {
            if (value.toInt() == i) {
                return value;
            }
        }

        return null;
    }
}
