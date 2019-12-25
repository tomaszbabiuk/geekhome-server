package com.geekhome.coremodule;

import com.geekhome.common.*;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.configuration.Persistable;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.httpserver.modules.CollectorCollection;

public abstract class MultistateDevice extends Device {
    public abstract CollectorCollection<State> buildStates(ILocalizationProvider localizationProvider);

    @Persistable(name="HasNonReadonlyStates")
    public abstract boolean hasNonReadonlyStates();

    protected MultistateDevice(DescriptiveName name, String iconName, DeviceCategory deviceCategory) {
        super(name, iconName, deviceCategory);
    }

    @Override
    protected ControlType calculateControlType() {
        return ControlType.Multistate;
    }
}

