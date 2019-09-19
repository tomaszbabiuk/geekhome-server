package com.geekhome.common.commands;

import com.geekhome.common.INamedObject;
import com.geekhome.coremodule.Alert;

public interface IAlertService extends INamedObject {
    boolean isMandatory();
    void raiseAlert(Alert alert) throws Exception;
    void clearAlert(Alert alert) throws Exception;
}
