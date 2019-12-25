package com.geekhome.onewiremodule;

import com.geekhome.common.configuration.Persistable;
import com.geekhome.common.configuration.JSONAwareBase;

public class DiscoveryInfo<C extends IOneWireContainer> extends JSONAwareBase {
    private String _name;
    private String _address;
    private C _container;

    @Persistable(name="Name")
    public String getName() {
        return _name;
    }

    public void setName(String value) {
        _name = value.toUpperCase();
    }

    @Persistable(name="Address")
    public String getAddress() {
        return _address;
    }

    public void setAddress(String value) {
        _address = value;
    }

    public C getContainer() {
        return _container;
    }

    protected DiscoveryInfo(C container) {
        _container = container;
        setName(container.getNameLowercase());
        setAddress(container.getAddressAsString());
    }

    @Override
    public String toString() {
        return String.format("1-wire device %s [%s]", getName(), getAddress());
    }
}