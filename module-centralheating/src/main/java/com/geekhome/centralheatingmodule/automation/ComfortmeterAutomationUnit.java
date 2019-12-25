package com.geekhome.centralheatingmodule.automation;

import com.geekhome.centralheatingmodule.Comfortmeter;
import com.geekhome.common.KeyValue;
import com.geekhome.common.configuration.JSONArrayList;
import com.geekhome.coremodule.automation.DeviceAutomationUnit;
import com.geekhome.coremodule.automation.EvaluationResult;
import com.geekhome.common.hardwaremanager.IInputPort;
import com.geekhome.common.hardwaremanager.IPort;
import com.geekhome.http.ILocalizationProvider;

import java.util.Calendar;

class ComfortmeterAutomationUnit extends DeviceAutomationUnit<Double, Comfortmeter> implements IThermometerAutomationUnit {
    private final ILocalizationProvider _localizationProvider;
    private IInputPort<Double> _temperaturePort;
    private IInputPort<Double> _humidityPort;

    ComfortmeterAutomationUnit(Comfortmeter comfortmeter, IInputPort<Double> temperaturePort,
                               IInputPort<Double> humidityPort, ILocalizationProvider localizationProvider) {
        super(comfortmeter);
        _temperaturePort = temperaturePort;
        _humidityPort = humidityPort;
        _localizationProvider = localizationProvider;
    }


    @Override
    public Double getValue() {
        double temp = _temperaturePort.read();
        double hum = _humidityPort.read();
        double wind = 0;
        return 37 - ((37 - temp) / (0.68 - 0.0014 * hum + (1 / (1.76 + 1.4 * Math.pow(wind, 0.75))))) - 0.29 * temp * (1 - (hum / 100));
    }

    @Override
    public EvaluationResult buildEvaluationResult() {
        String comfortmeterValue = String.format("%.2f°C", getValue());
        String temperatureValue = String.format("%.2f°C", _temperaturePort.read());
        String humidityValue = String.format("%s%%", _humidityPort.read());
        JSONArrayList<KeyValue> descriptions = new JSONArrayList<>();
        descriptions.add(new KeyValue(_localizationProvider.getValue("CH:Temperature"), temperatureValue));
        descriptions.add(new KeyValue(_localizationProvider.getValue("CH:Humidity"), humidityValue));

        return new EvaluationResult(getValue(), comfortmeterValue, false, isConnected(), descriptions);
    }

    @Override
    public IPort[] getUsedPorts() {
        return new IPort[] { _humidityPort, _temperaturePort };
    }

    @Override
    protected void calculateInternal(Calendar now) throws Exception {
    }
}