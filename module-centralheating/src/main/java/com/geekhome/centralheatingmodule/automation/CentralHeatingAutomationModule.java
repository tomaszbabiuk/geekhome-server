package com.geekhome.centralheatingmodule.automation;

import com.geekhome.centralheatingmodule.*;
import com.geekhome.common.configuration.Multicontroller;
import com.geekhome.common.automation.*;
import com.geekhome.common.settings.AutomationSettings;
import com.geekhome.common.hardwaremanager.IHardwareManager;
import com.geekhome.common.hardwaremanager.IInputPort;
import com.geekhome.common.hardwaremanager.IOutputPort;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.common.configuration.CollectorCollection;
import com.geekhome.common.automation.IAutomationModule;
import com.geekhome.synchronizationmodule.business.SmartEvent;

import java.util.ArrayList;

public class CentralHeatingAutomationModule implements IAutomationModule {
    private MasterAutomation _masterAutomation;
    private IHardwareManager _hardwareManager;
    private AutomationSettings _settings;
    private CentralHeatingConfiguration _centralHeatingConfiguration;
    private ILocalizationProvider _localizationProvider;

    public CentralHeatingAutomationModule(MasterAutomation masterAutomation, IHardwareManager hardwareManager, AutomationSettings settings,
                                          CentralHeatingConfiguration centralHeatingConfiguration, ILocalizationProvider localizationProvider) {
        _masterAutomation = masterAutomation;
        _hardwareManager = hardwareManager;
        _settings = settings;
        _centralHeatingConfiguration = centralHeatingConfiguration;
        _localizationProvider = localizationProvider;
    }

    @Override
    public void addIndependentConditionAutomationUnits(CollectorCollection<IEvaluableAutomationUnit> conditionsList) throws Exception {
        for (ThermostatCondition condition : _centralHeatingConfiguration.getThermostatConditions().values()) {
            ThermostatConditionAutomationUnit unit = new ThermostatConditionAutomationUnit(condition, _masterAutomation);
            conditionsList.add(unit);
        }

        conditionsList.put("alwayson", new AlwaysOnAutomationUnit());
    }

    @Override
    public void addDependentConditionAutomationUnits(CollectorCollection<IEvaluableAutomationUnit> conditionsList) throws Exception {
    }

    @Override
    public void addDeviceAutomationUnits(CollectorCollection<IDeviceAutomationUnit> devicesList) throws Exception {
        for (Thermometer thermometer : _centralHeatingConfiguration.getThermometers().values()) {
            IInputPort<Double> temperaturePort = _hardwareManager.findTemperaturePort(thermometer.getPortId());
            ThermometerAutomationUnit unit = new ThermometerAutomationUnit(thermometer, temperaturePort);
            devicesList.add(unit);
        }

        for (Hygrometer hygrometer : _centralHeatingConfiguration.getHygrometers().values()) {
            IInputPort<Double> humidityPort = _hardwareManager.findHumidityPort(hygrometer.getPortId());
            HygrometerAutomationUnit unit = new HygrometerAutomationUnit(hygrometer, humidityPort);
            devicesList.add(unit);
        }

        for (AveragingThermometer averagingThermometer : _centralHeatingConfiguration.getAveragingThermometers().values()) {
            AveragingThermometerAutomationUnit unit = new AveragingThermometerAutomationUnit(averagingThermometer, _masterAutomation);
            devicesList.add(unit);
        }

        for (Comfortmeter comfortmeter : _centralHeatingConfiguration.getComfortmeters().values()) {
            IInputPort<Double> temperaturePort = _hardwareManager.findTemperaturePort(comfortmeter.getTemperaturePortId());
            IInputPort<Double> humidityPort = _hardwareManager.findHumidityPort(comfortmeter.getHumidityPortId());
            ComfortmeterAutomationUnit unit = new ComfortmeterAutomationUnit(comfortmeter, temperaturePort, humidityPort, _localizationProvider);
            devicesList.add(unit);
        }

        for (TemperatureController temperatureController : _centralHeatingConfiguration.getTemperatureControllers().values()) {
            TemperatureMulticontrollerAutomationUnit unit = new TemperatureMulticontrollerAutomationUnit(temperatureController, _settings);
            devicesList.add(unit);
        }

        for (Radiator radiator : _centralHeatingConfiguration.getRadiators().values()) {
            IOutputPort<Boolean> outputPort = _hardwareManager.findDigitalOutputPort(radiator.getPortId());
            RadiatorAutomationUnit unit = new RadiatorAutomationUnit(outputPort, radiator, _masterAutomation, _localizationProvider);
            devicesList.add(unit);
        }

        for (UnderfloorCircuit underfloorCircuit : _centralHeatingConfiguration.getUnderfloorCircuits().values()) {
            IOutputPort<Boolean> outputPort = _hardwareManager.findDigitalOutputPort(underfloorCircuit.getPortId());
            UnderfloorCircuitAutomationUnit unit = new UnderfloorCircuitAutomationUnit(outputPort, underfloorCircuit, _masterAutomation, _localizationProvider);
            devicesList.add(unit);
        }

        for (RTLCircuit rtlCircuit : _centralHeatingConfiguration.getRTLCircuits().values()) {
            IOutputPort<Boolean> outputPort = _hardwareManager.findDigitalOutputPort(rtlCircuit.getPortId());
            RTLCircuitAutomationUnit unit = new RTLCircuitAutomationUnit(outputPort, rtlCircuit, _masterAutomation, _localizationProvider);
            devicesList.add(unit);
        }

        for (HeatingManifold manifold : _centralHeatingConfiguration.getHeatingManifolds().values()) {
            IOutputPort<Boolean> pumpOrFurnacePort = _hardwareManager.tryFindDigitalOutputPort(manifold.getPumpOrFurnacePortId());
            IOutputPort<Boolean> actuatorsTransformerPort = _hardwareManager.tryFindDigitalOutputPort(manifold.getActuatorsTransformerPortId());
            HeatingManifoldAutomationUnit unit = new HeatingManifoldAutomationUnit(pumpOrFurnacePort, actuatorsTransformerPort, manifold, _masterAutomation, _localizationProvider);
            devicesList.add(unit);
        }

        for (AirConditioner airConditioner : _centralHeatingConfiguration.getAirConditioners().values()) {
            IOutputPort<Integer> temperatureControlPort = _hardwareManager.tryFindPowerOutputPort(airConditioner.getTemperatureControlPortId());
            AirConditionerAutomationUnit unit = new AirConditionerAutomationUnit(temperatureControlPort, airConditioner, _masterAutomation, _localizationProvider);
            devicesList.add(unit);
        }

        for (CirculationPump pump : _centralHeatingConfiguration.getCirculationPumps().values()) {
            IOutputPort<Boolean> outputPort = _hardwareManager.findDigitalOutputPort(pump.getPortId());
            CirculationPumpAutomationUnit unit = new CirculationPumpAutomationUnit(outputPort, pump, _masterAutomation, _localizationProvider);
            devicesList.add(unit);
        }
    }

    @Override
    public void addModeAutomationUnits(CollectorCollection<ModeAutomationUnit> modesList) {
    }

    @Override
    public void addAlertAutomationUnits(CollectorCollection<AlertAutomationUnit> alertsList) {
    }

    @Override
    public void addBlocksAutomationUnits(CollectorCollection<BlockAutomationUnit> blocksList) {
    }

    @Override
    public void initialized() throws Exception {
        initializeTemperatureControllersAutomationUnits();
    }

    private void initializeTemperatureControllersAutomationUnits() throws Exception {
        for (TemperatureController controller : _centralHeatingConfiguration.getTemperatureControllers().values()) {
            addModeUnitsToTemperatureControllerAutomationUnit(controller);
        }
    }

    private void addModeUnitsToTemperatureControllerAutomationUnit(Multicontroller multicontroller) throws Exception {
        TemperatureMulticontrollerAutomationUnit controllerUnit =
                (TemperatureMulticontrollerAutomationUnit) _masterAutomation.findDeviceUnit(multicontroller.getName().getUniqueId());

        ArrayList<ModeAutomationUnit> modesAutomationUnits = new ArrayList<>();
        if (!multicontroller.getModesIds().equals("")) {
            for (String modeId : multicontroller.getModesIds().split(",")) {
                ModeAutomationUnit modeUnit = _masterAutomation.findModeUnit(modeId);
                MasterAutomation.addByPriority(modesAutomationUnits, modeUnit);
            }
        }

        controllerUnit.setModesUnits(modesAutomationUnits);
    }

    @Override
    public void reportSmartEvent(SmartEvent event, String value, String code) throws Exception {
    }
}
