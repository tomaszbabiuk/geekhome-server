package com.geekhome.hardwaremanagermodule;

import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.configuration.JSONArrayList;
import com.geekhome.common.hardwaremanager.IHardwareManager;
import com.geekhome.common.hardwaremanager.IOutputPort;
import com.geekhome.common.hardwaremanager.IPort;
import com.geekhome.common.hardwaremanager.ITogglePort;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.common.configuration.IConfigurationValidator;

class HardwareManagerConfigurationValidator implements IConfigurationValidator {
    private ILocalizationProvider _localizationProvider;
    private IHardwareManager _hardwareManger;

    HardwareManagerConfigurationValidator(ILocalizationProvider localizationProvider, IHardwareManager hardwareManger) {
        _localizationProvider = localizationProvider;
        _hardwareManger = hardwareManger;
    }

    @Override
    public void addValidations(JSONArrayList<String> validations) {
        for (IOutputPort<Boolean> outputPort : _hardwareManger.getDigitalOutputPorts().values()) {
            validateIfPortIsOvermapped(validations, outputPort);
        }

        for (ITogglePort togglePort : _hardwareManger.getTogglePorts().values()) {
            validateIfPortIsOvermapped(validations, togglePort);
        }
    }

    private void validateIfPortIsOvermapped(JSONArrayList<String> validations, IPort port) {
        if (port.getMappedTo().size() > 1) {
            StringBuilder deviceNamesBuilder = new StringBuilder();
            for (DescriptiveName deviceName : port.getMappedTo()) {
                if (deviceNamesBuilder.length() > 0) {
                    deviceNamesBuilder.append(", ");
                }
                deviceNamesBuilder.append(deviceName.getName());
            }
            String validationMessage = String.format(_localizationProvider.getValue("HM:PortOvermapped"), deviceNamesBuilder.toString(), port.getId());
            validations.add(validationMessage);
        }
    }
}
