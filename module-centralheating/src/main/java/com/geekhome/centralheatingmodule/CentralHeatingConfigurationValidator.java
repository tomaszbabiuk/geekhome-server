package com.geekhome.centralheatingmodule;

import com.geekhome.common.PortBase;
import com.geekhome.common.json.JSONArrayList;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.httpserver.modules.IConfigurationValidator;

import java.util.Arrays;
import java.util.List;

class CentralHeatingConfigurationValidator implements IConfigurationValidator {
    private CentralHeatingConfiguration _centralHeatingConfiguration;
    private ILocalizationProvider _localizationProvider;

    CentralHeatingConfigurationValidator(CentralHeatingConfiguration centralHeatingConfiguration, ILocalizationProvider localizationProvider) {
        _centralHeatingConfiguration = centralHeatingConfiguration;
        _localizationProvider = localizationProvider;
    }
    @Override
    public void addValidations(JSONArrayList<String> validations) {
        for (Radiator radiator : _centralHeatingConfiguration.buildAllCircuits().values()) {
            boolean radiatorAssigned = false;
            for (HeatingManifold manifold : _centralHeatingConfiguration.getHeatingManifolds().values()) {
                List<String> manifoldCircuitsIds = Arrays.asList(manifold.getCircuitsIds().split(","));
                if (manifoldCircuitsIds.contains(radiator.getName().getUniqueId())) {
                    radiatorAssigned = true;
                    break;
                }
            }

            if (!radiatorAssigned) {
                String validationMessage = String.format(_localizationProvider.getValue("CH:CircuitNotAssignedToManifold"), radiator.getName());
                validations.add(validationMessage);
            }
        }

        for (HeatingManifold manifold : _centralHeatingConfiguration.getHeatingManifolds().values()) {
            if (!PortBase.EMPTY.equals(manifold.getPumpOrFurnacePortId()) && manifold.getPumpOrFurnacePortId().equals(manifold.getActuatorsTransformerPortId())) {
                String validationMessage = String.format(_localizationProvider.getValue("CH:HeatingManifoldPortsAreSame"), manifold.toString());
                validations.add(validationMessage);
            }

        }
    }
}
