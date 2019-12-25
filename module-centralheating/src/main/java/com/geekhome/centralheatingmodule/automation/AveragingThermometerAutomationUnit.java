package com.geekhome.centralheatingmodule.automation;

import com.geekhome.centralheatingmodule.AveragingThermometer;
import com.geekhome.coremodule.automation.EvaluationResult;
import com.geekhome.coremodule.automation.MasterAutomation;
import com.geekhome.common.hardwaremanager.IPort;

import java.util.ArrayList;

public class AveragingThermometerAutomationUnit extends ThermometerAutomationUnitBase<AveragingThermometer>
        implements IThermometerAutomationUnit {

    private ArrayList<IThermometerAutomationUnit> _thermometersUnits;

    public AveragingThermometerAutomationUnit(AveragingThermometer thermometer, MasterAutomation masterAutomation) throws Exception {
        super(thermometer);

        _thermometersUnits = new ArrayList<>();
        if (!thermometer.getThermometersIds().equals("")) {
            for (String thermometerId : thermometer.getThermometersIds().split(",")) {
                IThermometerAutomationUnit unit = (IThermometerAutomationUnit) masterAutomation.findDeviceUnit(thermometerId);
                _thermometersUnits.add(unit);
            }
        }
    }

    @Override
    public EvaluationResult buildEvaluationResult() {
        String interfaceValue = String.format("%.2f°C", getValue());
        return new EvaluationResult(getValue(), interfaceValue, false, isConnected());
    }

    @Override
    public IPort[] getUsedPorts() {
        return new IPort[0];
    }

    @Override
    public Double getValue() {
        return calculateAverageTemperatures();
    }

    private Double calculateAverageTemperatures() {
        double sum = 0;
        for (IThermometerAutomationUnit thermometerUnit : _thermometersUnits) {
            sum += thermometerUnit.getValue();
        }

        if (_thermometersUnits.size() > 0) {
            return sum / _thermometersUnits.size();
        }

        return 0.0;
    }
}