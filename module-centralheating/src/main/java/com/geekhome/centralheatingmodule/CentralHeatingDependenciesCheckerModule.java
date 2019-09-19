package com.geekhome.centralheatingmodule;

import com.geekhome.coremodule.MasterConfiguration;
import com.geekhome.coremodule.Mode;
import com.geekhome.httpserver.modules.DependenciesCheckerModule;
import com.geekhome.httpserver.modules.Dependency;
import com.geekhome.httpserver.modules.DependencyType;

import java.util.ArrayList;

class CentralHeatingDependenciesCheckerModule extends DependenciesCheckerModule {
    private CentralHeatingConfiguration _centralHeatingConfiguration;

    CentralHeatingDependenciesCheckerModule(MasterConfiguration masterConfiguration, CentralHeatingConfiguration centralHeatingConfiguration) {
        super(masterConfiguration);
        _centralHeatingConfiguration = centralHeatingConfiguration;
    }

    @Override
    public void checkDependency(Object source, ArrayList<Dependency> dependencies, int level) {
        if (source instanceof Thermometer) {
            level++;
            Thermometer thermometer = (Thermometer) source;
            for (AveragingThermometer averagingThermometer : _centralHeatingConfiguration.getAveragingThermometers().values()) {
                for (String thermometerId : averagingThermometer.getThermometersIds().split(",")) {
                    if (thermometerId.equals(thermometer.getName().getUniqueId())) {
                        addDependency(dependencies, DependencyType.Device, averagingThermometer, level);
                    }
                }
            }

            for (Radiator radiator : _centralHeatingConfiguration.getRadiators().values()) {
                if (radiator.getAmbientThermometerId().equals(thermometer.getName().getUniqueId())) {
                    addDependency(dependencies, DependencyType.Device, radiator, level);
                }
            }

            for (UnderfloorCircuit underfloorCircuit : _centralHeatingConfiguration.getUnderfloorCircuits().values()) {
                if (underfloorCircuit.getAmbientThermometerId().equals(thermometer.getName().getUniqueId()) ||
                    underfloorCircuit.getFloorThermometerId().equals(thermometer.getName().getUniqueId())) {
                    addDependency(dependencies, DependencyType.Device, underfloorCircuit, level);
                }
            }

            for (RTLCircuit rtlCircuit : _centralHeatingConfiguration.getRTLCircuits().values()) {
                if (rtlCircuit.getAmbientThermometerId().equals(thermometer.getName().getUniqueId()) ||
                        rtlCircuit.getReturnThermometerId().equals(thermometer.getName().getUniqueId())) {
                    addDependency(dependencies, DependencyType.Device, rtlCircuit, level);
                }
            }

            for (ThermostatCondition thermostatCondition : _centralHeatingConfiguration.getThermostatConditions().values()) {
                if (thermostatCondition.getThermometerId().equals(thermometer.getName().getUniqueId())) {
                    addDependency(dependencies, DependencyType.Condition, thermostatCondition, level);
                }
            }

            for (CirculationPump circulationPump : _centralHeatingConfiguration.getCirculationPumps().values()) {
                if (circulationPump.getThermometerId().equals(thermometer.getName().getUniqueId())) {
                    addDependency(dependencies, DependencyType.Condition, circulationPump, level);
                }
            }
        }

        if (source instanceof TemperatureController) {
            level++;
            TemperatureController temperatureController = (TemperatureController) source;
            for (Radiator radiator : _centralHeatingConfiguration.getRadiators().values()) {
                if (radiator.getTemperatureControllerId().equals(temperatureController.getName().getUniqueId())) {
                    addDependency(dependencies, DependencyType.Device, radiator, level);
                }
            }

            for (UnderfloorCircuit underfloorCircuit : _centralHeatingConfiguration.getUnderfloorCircuits().values()) {
                if (underfloorCircuit.getTemperatureControllerId().equals(temperatureController.getName().getUniqueId())) {
                    addDependency(dependencies, DependencyType.Device, underfloorCircuit, level);
                }
            }

            for (RTLCircuit rtlCircuit : _centralHeatingConfiguration.getRTLCircuits().values()) {
                if (rtlCircuit.getTemperatureControllerId().equals(temperatureController.getName().getUniqueId())) {
                    addDependency(dependencies, DependencyType.Device, rtlCircuit, level);
                }
            }

            for (ThermostatCondition thermostatCondition : _centralHeatingConfiguration.getThermostatConditions().values()) {
                if (thermostatCondition.getTemperatureControllerId().equals(temperatureController.getName().getUniqueId())) {
                    addDependency(dependencies, DependencyType.Condition, thermostatCondition, level);
                }
            }
        }

        if (source instanceof Radiator) {
            level++;
            Radiator radiator = (Radiator) source;
            for (HeatingManifold manifold : _centralHeatingConfiguration.getHeatingManifolds().values()) {
                for (String circuitId : manifold.getCircuitsIds().split(",")) {
                    if (circuitId.equals(radiator.getName().getUniqueId())) {
                        addDependency(dependencies, DependencyType.Device, manifold, level);
                    }
                }
            }
        }

        if (source instanceof Mode) {
            level++;
            Mode mode = (Mode) source;
            for (TemperatureController controller : _centralHeatingConfiguration.getTemperatureControllers().values()) {
                for (String modeId : controller.getModesIds().split(",")) {
                    if (modeId.equals(mode.getName().getUniqueId())) {
                        addDependency(dependencies, DependencyType.Device, controller, level);
                    }
                }
            }
        }
    }
}