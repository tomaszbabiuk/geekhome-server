package com.geekhome.httpserver.modules;

import com.geekhome.common.DescriptiveName;
import com.geekhome.common.NamedObject;
import com.geekhome.common.Persistable;

public class BackupInfo extends NamedObject {
    private final String _fromDate;
    private int _version;
    private boolean _current;

    @Persistable(name="Current")
    public boolean isCurrent() {
        return _current;
    }

    @Persistable(name="Version")
    public int getVersion() {
        return _version;
    }

    @Persistable(name = "FromDate")
    public String getFromDate() {
        return _fromDate;
    }

    public BackupInfo(DescriptiveName name, int version, String fromDate, boolean current) {
        super(name);
        _version = version;
        _fromDate = fromDate;
        _current = current;
    }
}
