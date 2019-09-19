package com.geekhome.coremodule.automation;

import com.geekhome.coremodule.IDevice;

public abstract class DeviceAutomationUnit<R,D extends IDevice> extends BlocksTargetAutomationUnit implements IDeviceAutomationUnit<R> {
    private D _device;

    public D getDevice() {
        return _device;
    }

    public abstract R getValue();

    public EvaluationResult buildEvaluationResult() {
        boolean isSignaled = getValue() instanceof Boolean ? (Boolean)getValue() : false;
        return new EvaluationResult(getValue(), getValue().toString(), isSignaled);
    }

    protected DeviceAutomationUnit(D device) {
        super(device.getName());
        _device = device;
    }
}
