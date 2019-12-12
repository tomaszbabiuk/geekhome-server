package com.geekhome.hardwaremanager;

public interface IShadowTogglePort extends IShadowPort, ITogglePort {
    void setTarget(ITogglePort target);
}
