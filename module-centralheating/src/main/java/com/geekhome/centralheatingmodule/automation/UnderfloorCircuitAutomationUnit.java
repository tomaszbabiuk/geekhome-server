package com.geekhome.centralheatingmodule.automation;

import com.geekhome.centralheatingmodule.UnderfloorCircuit;
import com.geekhome.common.KeyValue;
import com.geekhome.common.automation.EvaluationResult;
import com.geekhome.common.automation.MasterAutomation;
import com.geekhome.common.hardwaremanager.IOutputPort;
import com.geekhome.http.ILocalizationProvider;

public class UnderfloorCircuitAutomationUnit extends HeatingCircuitAutomationUnit<UnderfloorCircuit> {
    private final IThermometerAutomationUnit _floorThermometerUnit;
    private boolean _floorToHot;
    private boolean _floorToCold;

    public UnderfloorCircuitAutomationUnit(IOutputPort<Boolean> outputPort, UnderfloorCircuit underfloorCircuit, MasterAutomation masterAutomation, ILocalizationProvider localizationProvider) throws Exception {
        super(outputPort, underfloorCircuit, masterAutomation, localizationProvider);
        _floorThermometerUnit = (IThermometerAutomationUnit) masterAutomation.findDeviceUnit(underfloorCircuit.getFloorThermometerId());
    }


    @Override
    public EvaluationResult buildEvaluationResult() {
        EvaluationResult circuitEvaluationResult = super.buildEvaluationResult();
        KeyValue floorTemperature = new KeyValue(getLocalizationProvider().getValue("CH:FloorTemperature"),
                String.format("%.2f°C", _floorThermometerUnit.getValue()));
        circuitEvaluationResult.getDescriptions().add(floorTemperature);

        if (_floorToHot) {
            KeyValue floorToHighDescription = new KeyValue(getLocalizationProvider().getValue("C:Warning"),
                    getLocalizationProvider().getValue("CH:FloorTemperatureToHigh"));
            circuitEvaluationResult.getDescriptions().add(floorToHighDescription);
        }

        if (_floorToCold) {
            KeyValue floorToHighDescription = new KeyValue(getLocalizationProvider().getValue("C:Warning"),
                    getLocalizationProvider().getValue("CH:FloorTemperatureToLow"));
            circuitEvaluationResult.getDescriptions().add(floorToHighDescription);
        }

        return circuitEvaluationResult;
    }

    @Override
    protected boolean calculateActivity() {
        _floorToHot = _floorThermometerUnit.getValue() > getDevice().getMaxFloorTemperature();
        _floorToCold = _floorThermometerUnit.getValue() < getDevice().getMinFloorTemperature();
        if (_floorToHot) {
            return false;
        } else {
            boolean floorTempPriority = checkIfAnyBlockPasses("floortemppriority");
            boolean roomRequiresHeating = getAmbientThermometerUnit().getValue() < getTemperatureControllerUnit().getValue();
            boolean floorRequiresHeating = _floorToCold && floorTempPriority;
            return roomRequiresHeating || floorRequiresHeating;
        }
    }


}
