package com.geekhome.coremodule;

import com.geekhome.common.DescriptiveName;
import com.geekhome.common.NamedObject;

public class ConditionBase extends NamedObject implements ICondition {
    public ConditionBase(DescriptiveName name) {
        super(name);
    }

    @Override
    public String[] getDevicesIds() {
        return new String[0];
    }
}
