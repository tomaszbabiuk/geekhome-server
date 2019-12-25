package com.geekhome.automationmodule;

import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.configuration.Persistable;
import com.geekhome.coremodule.ConditionBase;

public class NfcCondition extends ConditionBase {
    private String _tag;
    private String _duration;

    @Persistable(name="Duration")
    public String getDuration() {
        return _duration;
    }

    public void setDuration(String value) {
        _duration = value;
    }

    @Persistable(name="Tag")
    public String getTag() {
        return _tag;
    }

    public void setTag(String value) {
        _tag = value;
    }

    public NfcCondition(DescriptiveName name, String tag, String duration) {
        super(name);
        setDuration(duration);
        setTag(tag);
    }
}
