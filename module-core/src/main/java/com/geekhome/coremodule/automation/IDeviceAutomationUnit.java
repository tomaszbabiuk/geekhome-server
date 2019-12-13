package com.geekhome.coremodule.automation;

import com.geekhome.common.INamedObject;
import com.geekhome.coremodule.IDevice;

public interface IDeviceAutomationUnit<R> extends INamedObject, IBlocksTargetAutomationUnit, ICalculableAutomationUnit {
    R getValue();
    IDevice getDevice();
    EvaluationResult buildEvaluationResult();
}

