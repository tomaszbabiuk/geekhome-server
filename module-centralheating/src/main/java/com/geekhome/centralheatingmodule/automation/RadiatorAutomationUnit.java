package com.geekhome.centralheatingmodule.automation;

import com.geekhome.centralheatingmodule.Radiator;
import com.geekhome.common.automation.MasterAutomation;
import com.geekhome.common.hardwaremanager.IOutputPort;
import com.geekhome.http.ILocalizationProvider;

public class RadiatorAutomationUnit extends HeatingCircuitAutomationUnit<Radiator> {
    public RadiatorAutomationUnit(IOutputPort<Boolean> outputPort, Radiator radiator, MasterAutomation masterAutomation, ILocalizationProvider localizationProvider) throws Exception {
        super(outputPort, radiator, masterAutomation, localizationProvider);
    }

    @Override
    protected boolean calculateActivity() {
        return getAmbientThermometerUnit().getValue() < getTemperatureControllerUnit().getValue();
    }
}
