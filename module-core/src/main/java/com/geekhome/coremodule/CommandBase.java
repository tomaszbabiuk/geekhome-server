package com.geekhome.coremodule;

import com.geekhome.common.DescriptiveName;
import com.geekhome.common.NamedObject;
import com.geekhome.common.Persistable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandBase extends NamedObject {
    public CommandBase(DescriptiveName name) {
        super(name);
    }

    public boolean matches(String subject) {
        return subject!= null && !subject.isEmpty() && getName().getName().toLowerCase().equals(subject.toLowerCase());
    }
}
