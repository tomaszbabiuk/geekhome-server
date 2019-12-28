package com.geekhome.centralheatingmodule.automation;

import com.geekhome.centralheatingmodule.CirculationPump;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.configuration.Duration;
import com.geekhome.common.automation.OnOffDeviceAutomationUnit;
import com.geekhome.common.automation.ControlMode;
import com.geekhome.common.automation.MasterAutomation;
import com.geekhome.common.hardwaremanager.IOutputPort;
import com.geekhome.common.localization.ILocalizationProvider;

import java.util.Calendar;

public class CirculationPumpAutomationUnit extends OnOffDeviceAutomationUnit<CirculationPump> {
    private long _oneMinutesMillis;
    private long _minimumWorkingTime;
    private IThermometerAutomationUnit _thermometerAutomationUnit;
    private long _lastTemperatureCheck;
    private long _switchedOffTime;
    private double _lastTemperatureMeasured;
    private ILogger _logger = LoggingService.getLogger();

    public CirculationPumpAutomationUnit(IOutputPort<Boolean> outputPort, CirculationPump pump, MasterAutomation masterAutomation, ILocalizationProvider localizationProvider) throws Exception {
        super(outputPort, pump, localizationProvider);
        _minimumWorkingTime = Duration.parse(pump.getMinimumWorkingTime());
        _thermometerAutomationUnit = (IThermometerAutomationUnit) masterAutomation.findDeviceUnit(pump.getThermometerId());
        _switchedOffTime = Calendar.getInstance().getTimeInMillis();
        _oneMinutesMillis = 90 * 1000;
        changeStateInternal("off", ControlMode.Auto);
    }

    @Override
    public void calculateInternal(Calendar now) throws Exception {
        if (getControlMode() == ControlMode.Auto) {
            long nowMillis = now.getTimeInMillis();
            if (checkIfAnyBlockPasses("on")) {
                if ((nowMillis - _switchedOffTime > _oneMinutesMillis * 3 && !isOn())) {
                    _lastTemperatureCheck = nowMillis;
                    _lastTemperatureMeasured = _thermometerAutomationUnit.getValue();
                    _logger.info("Hot water recirculation - trying to start the pump.");
                    changeStateInternal("pumping", ControlMode.Auto);
                } else {
                    if (nowMillis - _lastTemperatureCheck > _oneMinutesMillis) {
                        _lastTemperatureCheck = nowMillis;
                        double currentTemperature = _thermometerAutomationUnit.getValue();
                        double temperatureDelta = currentTemperature - _lastTemperatureMeasured;
                        if (temperatureDelta > 1) {
                            if (!isOn()) {
                                _logger.info("Hot water recirculation - temperature delta = " + temperatureDelta + "- switching on");
                                setLastSwitchingOnTime(now);
                                changeStateInternal("pumping", ControlMode.Auto);
                            }
                        } else {
                            if (isOn()) {
                                _logger.info("Hot water recirculation - temperature delta = " + temperatureDelta + "< 1 - stopping");
                                _switchedOffTime = nowMillis;
                                changeStateInternal("wh", ControlMode.Auto);
                            }
                        }
                        _lastTemperatureMeasured = currentTemperature;
                    }
                }
            } else {
                if (!getStateId().equals("off")) {
                    changeStateInternal("soff", ControlMode.Auto);
                }
            }
        }

        if (getStateId().equals("soff") && now.getTimeInMillis() > getLastSwitchingOnTime().getTimeInMillis() + _minimumWorkingTime) {
            changeStateInternal("off", ControlMode.Auto);
        }

        if (getStateId().equals("pumping")) {
            changeOutputPortStateIfNeeded(getPort(), true);
        } else if (getStateId().equals("off") || getStateId().equals("wh")) {
            changeOutputPortStateIfNeeded(getPort(), false);
        }
    }

    @Override
    public boolean isSignaled() {
        return getStateId().equals("pumping") || getStateId().equals("wh");
    }
}