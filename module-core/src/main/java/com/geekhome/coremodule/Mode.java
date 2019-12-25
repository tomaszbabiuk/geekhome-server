package com.geekhome.coremodule;

import com.geekhome.common.*;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.configuration.IBlocksTarget;
import com.geekhome.common.configuration.Persistable;
import com.geekhome.common.configuration.JSONArrayList;
import com.geekhome.http.ILocalizationProvider;

public class Mode extends NamedObject implements IPrioritized, IBlocksTarget {
    private int _priority;

    @Persistable(name="Priority")
    public int getPriority() {
        return _priority;
    }

    public void setPriority(int value) {
        _priority = value;
    }

    public Mode(DescriptiveName name, int priority) {
        super(name);
        setPriority(priority);
    }

    @Override
    public JSONArrayList<DescriptiveName> buildBlockCategories(ILocalizationProvider localizationProvider) {
        return null;
    }

    public boolean equals(Object obj) {
        boolean equalsToNamedObject = obj instanceof INamedObject && ((INamedObject) obj).getName().getUniqueId().equals(getName().getUniqueId());
        boolean equalsToUniqueId = obj instanceof String && obj.equals(getName().getUniqueId());

        return equalsToNamedObject || equalsToUniqueId;
    }
}
