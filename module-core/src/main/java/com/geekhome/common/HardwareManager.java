package com.geekhome.common;

import com.geekhome.common.json.JSONArrayList;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.utils.Sleeper;
import com.geekhome.hardwaremanager.*;
import com.geekhome.httpserver.OperationMode;
import com.geekhome.httpserver.SystemInfo;
import com.geekhome.httpserver.modules.IInvalidateCacheListener;
import com.geekhome.httpserver.modules.IModule;

import java.util.ArrayList;
import java.util.Calendar;


public class HardwareManager implements IHardwareManager {

    private ILogger _logger = LoggingService.getLogger();
    private InputPortsCollection<Boolean> _digitalInputPorts;
    private OutputPortsCollection<Boolean> _digitalOutputPorts;
    private InputPortsCollection<Integer> _powerInputPorts;
    private OutputPortsCollection<Integer> _powerOutputPorts;
    private InputPortsCollection<Double> _temperaturePorts;
    private InputPortsCollection<Double> _humidityPorts;
    private InputPortsCollection<Double> _luminosityPorts;
    private TogglePortsCollection _togglePorts;
    private IInvalidateCacheListener _invalidateCacheListener;
    private ArrayList<IHardwareManagerAdapter> _adapters = new ArrayList<>();
    private boolean _discoveryPending;

    public ArrayList<IHardwareManagerAdapter> getAdapters() {
        return _adapters;
    }

    public HardwareManager() {
        _digitalInputPorts = new InputPortsCollection<>();
        _digitalOutputPorts = new OutputPortsCollection<>();
        _powerInputPorts = new InputPortsCollection<>();
        _powerOutputPorts = new OutputPortsCollection<>();
        _temperaturePorts = new InputPortsCollection<>();
        _humidityPorts = new InputPortsCollection<>();
        _luminosityPorts = new InputPortsCollection<>();
        _togglePorts = new TogglePortsCollection();
    }

    protected void clear() {
        _digitalInputPorts.clear();
        _digitalOutputPorts.clear();
        _powerInputPorts.clear();
        _powerOutputPorts.clear();
        _temperaturePorts.clear();
        _humidityPorts.clear();
        _luminosityPorts.clear();
        _togglePorts.clear();
    }

    @Override
    public OutputPortsCollection<Boolean> getDigitalOutputPorts() {
        return _digitalOutputPorts;
    }

    @Override
    public InputPortsCollection<Boolean> getDigitalInputPorts() {
        return _digitalInputPorts;
    }

    @Override
    public OutputPortsCollection<Integer> getPowerOutputPorts() {
        return _powerOutputPorts;
    }

    @Override
    public InputPortsCollection<Integer> getPowerInputPorts() {
        return _powerInputPorts;
    }

    @Override
    public InputPortsCollection<Double> getTemperaturePorts() {
        return _temperaturePorts;
    }

    @Override
    public InputPortsCollection<Double> getHumidityPorts() {
        return _humidityPorts;
    }

    @Override
    public InputPortsCollection<Double> getLuminosityPorts() {
        return _luminosityPorts;
    }

    @Override
    public TogglePortsCollection getTogglePorts() {
        return _togglePorts;
    }

    @Override
    public IOutputPort<Boolean> tryFindDigitalOutputPort(String id) {
        return getDigitalOutputPorts().tryFind(id);
    }

    @Override
    public IOutputPort<Boolean> findDigitalOutputPort(String id) throws PortNotFoundException {
        return getDigitalOutputPorts().find(id);
    }

    @Override
    public IInputPort<Boolean> findDigitalInputPort(String id) throws PortNotFoundException {
        return getDigitalInputPorts().find(id);
    }

    @Override
    public IInputPort<Boolean> tryFindDigitalInputPort(String id) {
        return getDigitalInputPorts().tryFind(id);
    }

    @Override
    public IOutputPort<Integer> findPowerOutputPort(String id) throws PortNotFoundException {
        return getPowerOutputPorts().find(id);
    }

    @Override
    public IOutputPort<Integer> tryFindPowerOutputPort(String id) {
        return getPowerOutputPorts().tryFind(id);
    }

    @Override
    public IInputPort<Integer> findPowerInputPort(String id) throws PortNotFoundException {
        return getPowerInputPorts().find(id);
    }

    @Override
    public IInputPort<Integer> tryFindPowerInputPort(String id) {
        return getPowerInputPorts().tryFind(id);
    }

    @Override
    public IInputPort<Double> findTemperaturePort(String id) throws PortNotFoundException {
        return getTemperaturePorts().find(id);
    }

    @Override
    public IInputPort<Double> tryFindTemperaturePort(String id) {
        return getTemperaturePorts().tryFind(id);
    }

    @Override
    public IInputPort<Double> findHumidityPort(String id) throws PortNotFoundException {
        return getHumidityPorts().find(id);
    }

    @Override
    public IInputPort<Double> tryFindHumidityPort(String id) {
        return getHumidityPorts().tryFind(id);
    }

    @Override
    public IInputPort<Double> findLuminosityPort(String id) throws PortNotFoundException {
        return getLuminosityPorts().find(id);
    }

    @Override
    public IInputPort<Double> tryFindLuminosityPort(String id) {
        return getLuminosityPorts().tryFind(id);
    }

    @Override
    public ITogglePort findTogglePort(String id) throws PortNotFoundException {
        return getTogglePorts().find(id);
    }

    @Override
    public ITogglePort tryFindTogglePort(String id) {
        return getTogglePorts().tryFind(id);
    }

    @Override
    public void setPortMapper(IPortMapper portMapper) {
        initializePortMapper(portMapper);
    }

    private void initializePortMapper(IPortMapper portMapper) {
        for (IInputPort<Boolean> port : getDigitalInputPorts().values()) {
            port.initialize(portMapper);
        }
        for (IOutputPort<Boolean> port : getDigitalOutputPorts().values()) {
            port.initialize(portMapper);
        }
        for (IInputPort<Integer> port : getPowerInputPorts().values()) {
            port.initialize(portMapper);
        }
        for (IOutputPort<Integer> port : getPowerOutputPorts().values()) {
            port.initialize(portMapper);
        }
        for (IInputPort<Double> port : getTemperaturePorts().values()) {
            port.initialize(portMapper);
        }
        for (IInputPort<Double> port : getHumidityPorts().values()) {
            port.initialize(portMapper);
        }
        for (IInputPort<Double> port : getLuminosityPorts().values()) {
            port.initialize(portMapper);
        }
        for (ITogglePort port : getTogglePorts().values()) {
            port.initialize(portMapper);
        }
    }

    @Override
    public void refreshAndWait(Calendar now) throws Exception {
        int refreshTrial = 0;
        do {
            if (!_discoveryPending && _adapters != null) {
                for (IHardwareManagerAdapter adapter : _adapters) {
                    adapter.refresh(now);
                }

                waitTillAllAdaptersAreRefreshed();

                for (IHardwareManagerAdapter adapter : _adapters) {
                    adapter.resetLatches();
                    if (!adapter.isOperational()) {
                        throw new Exception("Adapter " + adapter.getName() +" non-operational");
                    }
                }
            }
            refreshTrial ++;
            if (refreshTrial == 4) {
                throw new Exception("Has refresh errors despite retrying!");
            }
        } while (hasRefreshErrors());
    }

    private boolean hasRefreshErrors() {
        for (IHardwareManagerAdapter adapter : _adapters) {
            if (adapter.getRefreshState() == RefreshState.RefreshError) {
                _logger.info(String.format("Adapter refresh error found for adapter %s", adapter.toString()));
                return true;
            }
        }

        return false;
    }

    private void waitTillAllAdaptersAreRefreshed() {
        boolean refreshPending = true;
        while (refreshPending) {
            refreshPending = false;
            for (IHardwareManagerAdapter adapter : _adapters) {
                if (adapter.getRefreshState() == RefreshState.NA) {
                    continue;
                }
                if (adapter.getRefreshState() == RefreshState.Refreshing) {
                    refreshPending = true;
                    break;
                }
            }
            Sleeper.trySleep(50);
        }
    }

    public void setAdapters(ArrayList<IHardwareManagerAdapter> value) {
        _adapters = value;
    }

    @Override
    public void discover() {
        _discoveryPending = true;

        clear();
        onInvalidateCache("/DIGITALINPUTPORTS.JSON");
        onInvalidateCache("/DIGITALOUTPUTPORTS.JSON");
        onInvalidateCache("/POWERINPUTPORTS.JSON");
        onInvalidateCache("/POWEROUTPUTPORTS.JSON");
        onInvalidateCache("/TEMPERATUREPORTS.JSON");
        onInvalidateCache("/HUMIDITYPORTS.JSON");
        onInvalidateCache("/LUMINOSITYPORTS.JSON");

        if (_adapters != null) {
            for (IHardwareManagerAdapter adapter : _adapters) {
                try {
                    adapter.discover(
                            getDigitalInputPorts(), getDigitalOutputPorts(),
                            getPowerInputPorts(), getPowerOutputPorts(),
                            getTemperaturePorts(), getTogglePorts(),
                            getHumidityPorts(), getLuminosityPorts());

                } catch (DiscoveryException e) {
                    _logger.error("Discovery exception", e);
                }
            }
        }

        _discoveryPending = false;
    }

    public void invalidateAdapters(OperationMode operationMode) throws Exception {
        if (_adapters != null) {
            for (IHardwareManagerAdapter adapter : _adapters) {
                adapter.invalidate(operationMode);
            }
        }
    }

    private void onInvalidateCache(String what) {
        if (_invalidateCacheListener != null) {
            _invalidateCacheListener.invalidate(what);
        }
    }

    @Override
    public void setInvalidateCacheListener(IInvalidateCacheListener listener) {
        _invalidateCacheListener = listener;
    }

    public void initialize(JSONArrayList<IModule> modules, SystemInfo systemInfo) throws Exception {
        ArrayList<IHardwareManagerAdapterFactory> adapterFactories = extractSerialAdaptersFactories(modules);
        ArrayList<IHardwareManagerAdapter> adapters = createAdapters(adapterFactories);
        setAdapters(adapters);

        for (IHardwareManagerAdapter adapter : adapters) {
            systemInfo.registerMonitorable(adapter);
        }
    }

    private ArrayList<IHardwareManagerAdapter> createAdapters(ArrayList<IHardwareManagerAdapterFactory> adapterFactories) throws Exception {
        _logger.info("Creating adapters.");
        ArrayList<IHardwareManagerAdapter> result = new ArrayList<>();

        for(IHardwareManagerAdapterFactory factory : adapterFactories) {
            ArrayList<? extends IHardwareManagerAdapter> adapters = factory.createAdapters();
            for (IHardwareManagerAdapter adapter : adapters) {
                _logger.info("Adding serial adapter " + adapter.toString());
                result.add(adapter);
            }
        }

        return result;
    }

    private ArrayList<IHardwareManagerAdapterFactory> extractSerialAdaptersFactories(JSONArrayList<IModule> modules) {
        ArrayList<IHardwareManagerAdapterFactory> factories = new ArrayList<>();
        for (IModule module : modules) {
            module.addSerialAdaptersFactory(factories);
        }
        return factories;
    }
}
