package com.geekhome.onewiremodule;

enum TaskType {
    RefreshTemperature,
    ReadSwitchValue,
    ReadSensedSwitchValue,
    WriteSwitchValue,
    RefreshLoopFinished,
    ValidateIdentity,
    Continue,
    Break
}
