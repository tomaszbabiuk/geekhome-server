package com.geekhome.coremodule;

import com.geekhome.common.DescriptiveName;
import com.geekhome.common.NamedObject;
import com.geekhome.common.Persistable;

public class ActivationStatus extends NamedObject {
    private String _activationId;
    private String _activationCode;

    @Persistable(name="ActivationId")
    public String getActivationId() {
        return _activationId;
    }

    @Persistable(name="ActivationCode")
    public String getActivationCode() {
        return _activationCode;
    }

    public ActivationStatus(DescriptiveName name, String activationId, String activationCode) {
        super(name);
        _activationId = activationId;
        _activationCode = activationCode;
    }
}