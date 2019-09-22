package com.geekhome.centralheatingmodule;

import com.geekhome.coremodule.MasterConfiguration;
import com.geekhome.coremodule.MasterConfigurationValidation;

public class CentralHeatingConfigurationValidation extends MasterConfigurationValidation {
    public static boolean canAddAveragingThermometers(MasterConfiguration masterConfiguration, CentralHeatingConfiguration centralHeatingConfiguration) {
        return isAnyRoomDefined(masterConfiguration) && thereAreAtLeast2ThermometersDefined(centralHeatingConfiguration);
    }

    public static boolean canAddThermostatConditions(CentralHeatingConfiguration centralHeatingConfiguration) {
        return isAnyThermometerDefined(centralHeatingConfiguration) && isAnyTemperatureControllerDefined(centralHeatingConfiguration);
    }

    public static boolean canAddCirculationPumps(CentralHeatingConfiguration centralHeatingConfiguration) {
        return isAnyThermometerDefined(centralHeatingConfiguration);
    }

    public static boolean canAddCircuits(CentralHeatingConfiguration centralHeatingConfiguration) {
        return isAnyThermometerDefined(centralHeatingConfiguration);
    }

    public static boolean canAddHeatingManifolds(CentralHeatingConfiguration centralHeatingConfiguration) {
        return isAnyRadiatorDefined(centralHeatingConfiguration) || isAnyUnderfloorCircuitDefined(centralHeatingConfiguration) ||
               isAnyRtlCircuitDefined(centralHeatingConfiguration);
    }

    public static boolean canAddAirConditioners(CentralHeatingConfiguration centralHeatingConfiguration) {
        return isAnyTemperatureControllerDefined(centralHeatingConfiguration);
    }

    private static boolean isAnyRtlCircuitDefined(CentralHeatingConfiguration centralHeatingConfiguration) {
        return centralHeatingConfiguration.getRTLCircuits().size() > 0;
    }

    private static boolean isAnyUnderfloorCircuitDefined(CentralHeatingConfiguration centralHeatingConfiguration) {
        return centralHeatingConfiguration.getUnderfloorCircuits().size() > 0;
    }

    private static boolean isAnyRadiatorDefined(CentralHeatingConfiguration centralHeatingConfiguration) {
        return centralHeatingConfiguration.getRadiators().size() > 0;
    }

    private static boolean isAnyTemperatureControllerDefined(CentralHeatingConfiguration centralHeatingConfiguration) {
        return centralHeatingConfiguration.getTemperatureControllers().size() > 0;
    }

    private static boolean isAnyThermometerDefined(CentralHeatingConfiguration centralHeatingConfiguration) {
        return centralHeatingConfiguration.getThermometers().size() > 0;
    }

    private static boolean thereAreAtLeast2ThermometersDefined(CentralHeatingConfiguration centralHeatingConfiguration) {
        return centralHeatingConfiguration.getThermometers().size() > 1;
    }
}
