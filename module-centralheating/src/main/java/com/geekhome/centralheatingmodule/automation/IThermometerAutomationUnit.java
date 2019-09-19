package com.geekhome.centralheatingmodule.automation;

import com.geekhome.common.INamedObject;
import com.geekhome.coremodule.automation.EvaluationResult;

public interface IThermometerAutomationUnit extends INamedObject {
    Double getValue();
}
