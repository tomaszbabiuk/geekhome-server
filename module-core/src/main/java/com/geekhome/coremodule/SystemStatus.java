package com.geekhome.coremodule;

import com.geekhome.common.Persistable;
import com.geekhome.common.json.JSONAwareBase;

public class SystemStatus extends JSONAwareBase {
    private DateTimeInfo _date;
    private int _operationMode;
    private String _userName;
    private boolean _isLicenceAccepted;

    @Persistable(name="Date")
    public DateTimeInfo getDate() {
        return _date;
    }

    @Persistable(name="OperationMode")
    public int getOperationMode() {
        return _operationMode;
    }

    @Persistable(name="UserName")
    public String getUserName() {
        return _userName;
    }

    @Persistable(name="IsLicenseAccepted")
    public boolean isLicenseAccepted() {
        return _isLicenceAccepted;
    }

    public SystemStatus(String userName, int operationMode, boolean isLicenceAccepted) {
        _userName = userName;
        _operationMode = operationMode;
        _date = new DateTimeInfo();
        _isLicenceAccepted = isLicenceAccepted;
    }
}