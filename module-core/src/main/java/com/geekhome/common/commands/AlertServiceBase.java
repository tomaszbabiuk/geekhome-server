package com.geekhome.common.commands;

import com.geekhome.common.DescriptiveName;
import com.geekhome.common.NamedObject;
import com.geekhome.common.Persistable;

public abstract class AlertServiceBase extends NamedObject implements IAlertService {
    private boolean _isMandatory;

    public AlertServiceBase(DescriptiveName name, boolean isMandatory) {
        super(name);
        _isMandatory = isMandatory;
    }

    @Override
    @Persistable(name = "IsMandatory")
    public boolean isMandatory() {
        return _isMandatory;
    }
}
