package com.geekhome.onewiremodule;

import com.dalsemi.onewire.OneWireException;
import com.dalsemi.onewire.container.OneWireContainer28;
import com.dalsemi.onewire.container.TemperatureContainer;
import com.geekhome.common.Persistable;

public class TemperatureDiscoveryInfo extends DiscoveryInfo<TemperatureContainerWrapper> {
    private final Double _initialTemperature;

    @Persistable(name="InitialTemperature")
    public double getInitialTemperature() {
        return _initialTemperature;
    }

    public TemperatureDiscoveryInfo(OneWireContainer28 container) throws OneWireException {
        super(new TemperatureContainerWrapper(container));
        _initialTemperature = getContainer().readTemperature();
    }
}