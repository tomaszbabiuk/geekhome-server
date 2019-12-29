package com.geekhome.coremodule.modules;

public enum UnitCategory
{
    Manual(0),
    Automatic(1),
    AutomaticCalculations(2),
    Diagnostics(3),
    Configuration(4),
    ConfigurationDevices(5),
    ConfigurationConditions(6),
    ConfigurationSettings(7),
    ConfigurationCommands(8);

    private int _index;

    private UnitCategory(int index) {
        _index = index;
    }

    @Override
    public String toString() {
        return String.valueOf(_index);
    }
}