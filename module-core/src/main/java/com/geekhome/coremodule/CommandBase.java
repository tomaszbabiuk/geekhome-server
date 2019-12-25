package com.geekhome.coremodule;

import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.NamedObject;

public class CommandBase extends NamedObject {
    public CommandBase(DescriptiveName name) {
        super(name);
    }

    public boolean matches(String subject) {
        return subject!= null && !subject.isEmpty() && getName().getName().toLowerCase().equals(subject.toLowerCase());
    }
}
