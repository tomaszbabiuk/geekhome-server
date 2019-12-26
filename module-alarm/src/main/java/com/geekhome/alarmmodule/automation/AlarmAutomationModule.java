package com.geekhome.alarmmodule.automation;

import com.geekhome.alarmmodule.*;
import com.geekhome.common.automation.*;
import com.geekhome.common.hardwaremanager.IHardwareManager;
import com.geekhome.common.hardwaremanager.IInputPort;
import com.geekhome.common.hardwaremanager.IOutputPort;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.common.configuration.CollectorCollection;
import com.geekhome.common.automation.IAutomationModule;
import com.geekhome.synchronizationmodule.business.SmartEvent;

public class AlarmAutomationModule implements IAutomationModule {
    private MasterAutomation _masterAutomation;
    private AlarmConfiguration _alarmConfiguration;
    private IHardwareManager _hardwareManager;
    private ILocalizationProvider _localizationProvider;

    public AlarmAutomationModule(MasterAutomation masterAutomation, AlarmConfiguration alarmConfiguration, IHardwareManager hardwareManager, ILocalizationProvider localizationProvider) {
        _masterAutomation = masterAutomation;
        _alarmConfiguration = alarmConfiguration;
        _hardwareManager = hardwareManager;
        _localizationProvider = localizationProvider;
    }

    @Override
    public void addIndependentConditionAutomationUnits(CollectorCollection<IEvaluableAutomationUnit> conditionsList) throws Exception {
        for (PresenceCondition condition : _alarmConfiguration.getPresenceConditions().values()) {
            PresenceConditionAutomationUnit unit = new PresenceConditionAutomationUnit(condition, _masterAutomation);
            conditionsList.add(unit);
        }
    }

    @Override
    public void addDependentConditionAutomationUnits(CollectorCollection<IEvaluableAutomationUnit> conditionsList) throws Exception {
    }

    @Override
    public void addDeviceAutomationUnits(CollectorCollection<IDeviceAutomationUnit> devicesList) throws Exception {
        for (SinglePortAlarmSensor alarmDevice : _alarmConfiguration.getAlarmSensors().values()) {
            IInputPort<Boolean> inputPort = _hardwareManager.findDigitalInputPort(alarmDevice.getPortId());
            AlarmSensorAutomationUnit unit = new AlarmSensorAutomationUnit<>(inputPort, alarmDevice, _localizationProvider);
            devicesList.add(unit);
        }

        for (SinglePortAlarmSensor alarmDevice : _alarmConfiguration.getMovementDetectors().values()) {
            IInputPort<Boolean> inputPort = _hardwareManager.findDigitalInputPort(alarmDevice.getPortId());
            AlarmSensorAutomationUnit unit = new AlarmSensorAutomationUnit<>(inputPort, alarmDevice, _localizationProvider);
            devicesList.add(unit);
        }

        for (MagneticDetector magneticDetector : _alarmConfiguration.getMagneticDetectors().values()) {
            IInputPort<Boolean> inputPort = _hardwareManager.findDigitalInputPort(magneticDetector.getPortId());
            MagneticDetectorAutomationUnit unit = (magneticDetector.getDisarmingMovementDetectorId() == null || magneticDetector.getDisarmingMovementDetectorId().equals("")) ?
                    new MagneticDetectorAutomationUnit<>(inputPort, magneticDetector, _localizationProvider) :
                    new DisarmingMagneticDetectorAutomationUnit(inputPort, magneticDetector, _localizationProvider, _masterAutomation);

            devicesList.add(unit);
        }

        for (CodeLock codeLock : _alarmConfiguration.getCodeLocks().values()) {
            IInputPort<Boolean> armingPort = _hardwareManager.findDigitalInputPort(codeLock.getArmingPortId());
            IOutputPort<Boolean> statusPort = _hardwareManager.findDigitalOutputPort(codeLock.getStatusPortId());
            CodeLockAutomationUnit unit = new CodeLockAutomationUnit(armingPort, statusPort, codeLock, _localizationProvider);
            devicesList.add(unit);
        }

        for (Gate gate : _alarmConfiguration.getGates().values()) {
            IOutputPort<Boolean> outputPort = _hardwareManager.findDigitalOutputPort(gate.getGateControlPortId());
            IInputPort<Boolean> inputPort = _hardwareManager.findDigitalInputPort(gate.getMagneticDetectorPortId());
            GateAutomationUnit unit = new GateAutomationUnit(inputPort, outputPort, gate, _localizationProvider);
            devicesList.add(unit);
        }

        for (AlarmZone alarmZone : _alarmConfiguration.getAlarmZones().values()) {
            AlarmZoneAutomationUnit unit = new AlarmZoneAutomationUnit(alarmZone, _localizationProvider, _masterAutomation);
            devicesList.add(unit);
        }

        for (Signalizator signalizator : _alarmConfiguration.getSignalizators().values()) {
            IOutputPort<Boolean> outputPort = _hardwareManager.findDigitalOutputPort(signalizator.getPortId());
            SignalizatorAutomationUnit unit = new SignalizatorAutomationUnit(outputPort, signalizator, _localizationProvider,
                    _masterAutomation, _alarmConfiguration);
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
    public void initialized() {
    }

    @Override
    public void reportSmartEvent(SmartEvent event, String value, String code) throws Exception {
    }
}