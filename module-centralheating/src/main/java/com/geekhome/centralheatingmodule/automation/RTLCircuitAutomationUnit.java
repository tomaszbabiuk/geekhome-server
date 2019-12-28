package com.geekhome.centralheatingmodule.automation;

import com.geekhome.centralheatingmodule.RTLCircuit;
import com.geekhome.common.KeyValue;
import com.geekhome.common.automation.EvaluationResult;
import com.geekhome.common.automation.MasterAutomation;
import com.geekhome.common.hardwaremanager.IOutputPort;
import com.geekhome.common.localization.ILocalizationProvider;

public class RTLCircuitAutomationUnit extends HeatingCircuitAutomationUnit<RTLCircuit> {
    private final IThermometerAutomationUnit _returnThermometerUnit;
    private boolean _returnToHot;
    private boolean _returnToCold;

    public RTLCircuitAutomationUnit(IOutputPort<Boolean> outputPort, RTLCircuit rtlCircuit, MasterAutomation masterAutomation, ILocalizationProvider localizationProvider) throws Exception {
        super(outputPort, rtlCircuit, masterAutomation, localizationProvider);
        _returnThermometerUnit = (IThermometerAutomationUnit) masterAutomation.findDeviceUnit(rtlCircuit.getReturnThermometerId());
    }

    @Override
    public EvaluationResult buildEvaluationResult() {
        EvaluationResult circuitEvaluationResult = super.buildEvaluationResult();
        KeyValue returnTemperature = new KeyValue(getLocalizationProvider().getValue("CH:ReturnTemperature"),
                String.format("%.2f°C", _returnThermometerUnit.getValue()));
        circuitEvaluationResult.getDescriptions().add(returnTemperature);

        if (_returnToHot) {
            KeyValue returnToHighDescription = new KeyValue(getLocalizationProvider().getValue("C:Warning"),
                    getLocalizationProvider().getValue("CH:ReturnTemperatureToHigh"));
            circuitEvaluationResult.getDescriptions().add(returnToHighDescription);
        }

        if (_returnToCold) {
            KeyValue returnToHighDescription = new KeyValue(getLocalizationProvider().getValue("C:Warning"),
                    getLocalizationProvider().getValue("CH:ReturnTemperatureToLow"));
            circuitEvaluationResult.getDescriptions().add(returnToHighDescription);
        }

        return circuitEvaluationResult;
    }

    @Override
    protected boolean calculateActivity() {
        _returnToHot = _returnThermometerUnit.getValue() > getDevice().getMaxReturnTemperature();
        _returnToCold = _returnThermometerUnit.getValue() < getDevice().getMinReturnTemperature();
        if (_returnToHot) {
            return false;
        } else {
            boolean returnTempPriority = checkIfAnyBlockPasses("returntemppriority");
            boolean roomRequiresHeating = getAmbientThermometerUnit().getValue() < getTemperatureControllerUnit().getValue();
            boolean returnRequiresHeating = _returnToCold && returnTempPriority;
            return roomRequiresHeating || returnRequiresHeating;
        }
    }
}
