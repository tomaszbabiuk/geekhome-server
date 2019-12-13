package com.geekhome.centralheatingmodule.automation;

import com.geekhome.common.logging.ILogger;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.math.AverageCollection;
import com.geekhome.common.math.AverageDouble;
import com.geekhome.coremodule.IDevice;
import com.geekhome.coremodule.automation.DeviceAutomationUnit;

import java.util.Calendar;

public abstract class ThermometerAutomationUnitBase<D extends IDevice> extends DeviceAutomationUnit<Double, D> {

    private ILogger _logger = LoggingService.getLogger();
    private Double _prevValue;
    private AverageCollection<AverageDouble> _prevValues;

    ThermometerAutomationUnitBase(D device) {
        super(device);
        _prevValue = Double.MIN_VALUE;
        _prevValues = new AverageCollection<>(AverageDouble.class);
    }

    @Override
    public void calculateInternal(Calendar now) throws Exception {
        double newValue = getValue();
        if (newValue != _prevValue) {
            AverageDouble aDouble = _prevValues.findOrCreate(now);
            aDouble.include(newValue);
            _logger.valueChange(getDevice().getName().getUniqueId(), newValue);
        }
        _prevValue = newValue;
    }
}
