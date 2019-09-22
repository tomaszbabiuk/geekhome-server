package com.geekhome.onewiremodule;

import com.geekhome.common.DescriptiveName;
import com.geekhome.common.DiscoveryException;
import com.geekhome.common.RefreshState;
import com.geekhome.common.SerialAdapterBase;
import com.geekhome.common.SynchronizedInputPort;
import com.geekhome.common.json.JSONArrayList;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;
import com.geekhome.coremodule.settings.AutomationSettings;
import com.geekhome.hardwaremanager.IHardwareManager;
import com.geekhome.hardwaremanager.InputPortsCollection;
import com.geekhome.hardwaremanager.OutputPortsCollection;
import com.geekhome.hardwaremanager.PortNotFoundException;
import com.geekhome.hardwaremanager.TogglePortsCollection;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.httpserver.OperationMode;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class OneWireAdapter extends SerialAdapterBase {
    private ILogger _logger = LoggingService.getLogger();
    private final List<AdapterTask> _pipe = Collections.synchronizedList(new ArrayList<AdapterTask>());
    private final ArrayList<SwitchDiscoveryInfo> _switchesAsSensedInputs = new ArrayList<>();
    private final ArrayList<SwitchDiscoveryInfo> _switchesAsInputs = new ArrayList<>();
    private final ArrayList<SwitchDiscoveryInfo> _switchesAsOutputs = new ArrayList<>();
    private final JSONArrayList<SwitchDiscoveryInfo> _allSwitches = new JSONArrayList<>();
    private final JSONArrayList<IdentityDiscoveryInfo> _identifiers = new JSONArrayList<>();
    private IHardwareManager _hardwareManager;
    private AutomationSettings _automationSettings;
    private RefreshProcess _refreshProcess;
    private RefreshState _state;

    JSONArrayList<SwitchDiscoveryInfo> getAllSwitches() {
        return _allSwitches;
    }

    OneWireAdapter(IHardwareManager hardwareManager, String port, AutomationSettings automationSettings, ILocalizationProvider localizationProvider) {
        super(new DescriptiveName("1-wire adapter " + port, "1wire" + port), port, localizationProvider);
        _hardwareManager = hardwareManager;
        _automationSettings = automationSettings;
    }

    @Override
    public void discover(InputPortsCollection<Boolean> digitalInputPorts, OutputPortsCollection<Boolean> digitalOutputPorts,
                         InputPortsCollection<Integer> analogInputPorts, OutputPortsCollection<Integer> analogOutputPorts,
                         InputPortsCollection<Double> temperaturePorts, TogglePortsCollection togglePorts,
                         InputPortsCollection<Double> humidityPorts, InputPortsCollection<Double> luminosityPorts) throws DiscoveryException {
        _switchesAsSensedInputs.clear();
        _switchesAsOutputs.clear();
        _identifiers.clear();
        DiscoveryProcess discoveryProcess = new DiscoveryProcess(getSerialPortName(), _automationSettings);

        boolean hasThermometers = false;
        boolean hasSwitches = false;

        try {
            for (final DiscoveryInfo di : discoveryProcess.call()) {
                if (di instanceof TemperatureDiscoveryInfo) {
                    hasThermometers = true;
                    TemperatureDiscoveryInfo temperatureDiscoveryInfo = (TemperatureDiscoveryInfo) di;
                    addTemperaturePort(temperaturePorts, temperatureDiscoveryInfo);
                } else if (di instanceof SwitchDiscoveryInfo) {
                    hasSwitches = true;
                    SwitchDiscoveryInfo switchDiscoveryInfo = (SwitchDiscoveryInfo) di;
                    _allSwitches.add(switchDiscoveryInfo);
                    for (int channel = 0; channel < switchDiscoveryInfo.getChannelsCount(); channel++) {
                        addInputPort(digitalInputPorts,
                                switchDiscoveryInfo.extractPortId(channel, SwitchDiscoveryInfo.Direction.Input));
                        addOutputPort(digitalOutputPorts, switchDiscoveryInfo, channel,
                                switchDiscoveryInfo.extractPortId(channel, SwitchDiscoveryInfo.Direction.Output));
                    }
                } else if (di instanceof IdentityDiscoveryInfo) {
                    _identifiers.add((IdentityDiscoveryInfo) di);
                }
            }
            markAsOperational();
        } catch (Exception e) {
            markAsNonOperational(e);
            throw new DiscoveryException(e.getMessage(), e);
        }
        _logger.info("Devices discovered!");
        if (hasSwitches && !hasThermometers) {
            _pipe.add(new AdapterTask(TaskType.Continue, null));
        }
    }

    private void addOutputPort(OutputPortsCollection<Boolean> digitalOutputPorts, SwitchDiscoveryInfo di, int channel, String id) {
        SwitchContainerWrapper switchContainer = di.getContainer();
        OneWireOutputPort outputPort = new OneWireOutputPort(id, switchContainer, channel);
        digitalOutputPorts.add(outputPort);
    }

    private void addInputPort(InputPortsCollection<Boolean> digitalInputPorts, String id) {
        SynchronizedInputPort<Boolean> inputPort = new SynchronizedInputPort<>(id);
        inputPort.setValue(false);
        digitalInputPorts.add(inputPort);
    }

    private void addTemperaturePort(InputPortsCollection<Double> temperaturePorts, TemperatureDiscoveryInfo di) {
        SynchronizedInputPort<Double> tempPort = new SynchronizedInputPort<>(di.getAddress());
        tempPort.setValue(di.getInitialTemperature());
        temperaturePorts.add(tempPort);
        _pipe.add(new AdapterTask(TaskType.RefreshTemperature, di));
    }

    @Override
    public void refresh() {
        if (isOperational()) {
            _state = RefreshState.Refreshing;
            ArrayList<AdapterTask> tasks = new ArrayList<>();
            if (_switchesAsSensedInputs.size() > 0 || _switchesAsOutputs.size() > 0) {
                for (DiscoveryInfo di : _switchesAsOutputs) {
                    tasks.add(new AdapterTask(TaskType.WriteSwitchValue, di));
                }

                for (DiscoveryInfo di : _switchesAsSensedInputs) {
                    tasks.add(new AdapterTask(TaskType.ReadSensedSwitchValue, di));
                }

                for (DiscoveryInfo di : _switchesAsInputs) {
                    tasks.add(new AdapterTask(TaskType.ReadSwitchValue, di));
                }

                for (DiscoveryInfo di : _identifiers) {
                    tasks.add(new AdapterTask(TaskType.ValidateIdentity, di));
                }
            }
            tasks.add(new AdapterTask(TaskType.RefreshLoopFinished, null));
            _pipe.addAll(_pipe.size() > 0 ? 0 : 0, tasks);
        } else {
            _state = RefreshState.RefreshSuccess;
        }
    }

    private void assignSwitches(OperationMode operationMode) throws Exception {
        _switchesAsInputs.clear();
        _switchesAsSensedInputs.clear();
        _switchesAsOutputs.clear();
        if (operationMode == OperationMode.Diagnostics) {
            _switchesAsInputs.addAll(_allSwitches);
            _switchesAsOutputs.addAll(_allSwitches);
        } else {
            for (SwitchDiscoveryInfo di : _allSwitches) {
                SwitchRole switchRole = di.calculateSwitchRole(_hardwareManager);
                if (switchRole == SwitchRole.RelayBoard) {
                    _switchesAsOutputs.add(di);
                }

                if (switchRole == SwitchRole.AlarmSensing) {
                    _switchesAsSensedInputs.add(di);
                }

                if (switchRole == SwitchRole.BothConfliting) {
                    String errorMessage = String.format("Switch %s has conflicting role of relay board and alarm sensing board at the same time", di.getAddress());
                    throw new Exception(errorMessage);
                }
            }
        }
    }

    @Override
    public RefreshState getRefreshState() {
        return _state;
    }

    @Override
    public void resetLatches() {
        //in 1-wire latches are controlled on the chip in most cases
    }

    @Override
    public void invalidate(OperationMode operationMode) throws Exception {
        assignSwitches(operationMode);
    }

    @Override
    public void start() {
        _logger.info("Starting 1-wire sensing");
        _refreshProcess = new RefreshProcess(getSerialPortName(), _pipe, new IAdapterRefreshedListener() {
            @Override
            public void refreshed(boolean hasRefreshErrors) {
                _state = hasRefreshErrors ? RefreshState.RefreshError : RefreshState.RefreshSuccess;
            }
        });

        _refreshProcess.setTemperatureRefreshedListener(new RefreshProcess.ITemperatureRefreshedListener() {
            @Override
            public void execute(DiscoveryInfo discoveryInfo, double value) {
                SynchronizedInputPort<Double> temperaturePort;
                try {
                    temperaturePort = (SynchronizedInputPort<Double>) _hardwareManager.findTemperaturePort(discoveryInfo.getAddress());
                    temperaturePort.setValue(value);
                } catch (PortNotFoundException ex) {
                    _logger.error("Cannot refresh temperature value. Making OneWireAdapter non-operational", ex);
                    markAsNonOperational(ex);
                }
            }
        });

        _refreshProcess.setInputSwitchRefreshedListener(new RefreshProcess.IInputSwitchRefreshedListener() {
            @Override
            public void execute(DiscoveryInfo discoveryInfo, int channel, boolean level, boolean isSensed) {
                SwitchDiscoveryInfo sdi = (SwitchDiscoveryInfo)discoveryInfo;
                String portId = sdi.extractPortId(channel, SwitchDiscoveryInfo.Direction.Input);
                SynchronizedInputPort<Boolean> inputPort;
                try {
                    inputPort = (SynchronizedInputPort<Boolean>) _hardwareManager.findDigitalInputPort(portId);
                    boolean prevValue = inputPort.getValue();
                    if (isSensed) {
                        inputPort.setValue(!prevValue);
                    } else {
                        inputPort.setValue(!level);
                    }
                    if (prevValue != inputPort.getValue()) {
                        _logger.debug(String.format("Input of: %s-%d has changed, value %b [%b] %s", discoveryInfo.getAddress(), channel, level, prevValue, isSensed ? "SENSED" : ""));
                    }
                } catch (PortNotFoundException ex) {
                    _logger.error("Cannot refresh switch value. Making OneWireAdapter non-operational.", ex);
                    markAsNonOperational(ex);
                }
            }
        });

        Thread refreshThread = new Thread(_refreshProcess);
        refreshThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable ex) {
                _logger.error("Error while refreshing 1-wire device, making adapter non-operational.", ex);
                markAsNonOperational(ex);
            }
        });

        refreshThread.start();
    }

    @Override
    public void stop() {
        _pipe.clear();
        if (_refreshProcess != null) {
            ThreadsSynchronizer refreshSynchronizer = new ThreadsSynchronizer();
            _refreshProcess.setSynchronizer(refreshSynchronizer);
            _pipe.add(new AdapterTask(TaskType.Break, null));
            refreshSynchronizer.doWait();
        }
    }

    @Override
    public String toString() {
        return "1-wire adapter on " + getSerialPortName();
    }

    public void enterPincode() throws NoSuchAlgorithmException {
//        MessageDigest md = MessageDigest.getInstance("SHA-256");
//        md.update(new byte[] { (byte)234, 34, 54});
//        md.digest();
//        return new String(md.digest());
    }
}