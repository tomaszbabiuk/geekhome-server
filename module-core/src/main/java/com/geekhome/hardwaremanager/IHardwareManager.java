package com.geekhome.hardwaremanager;

import com.geekhome.common.DiscoveryException;
import com.geekhome.common.IHardwareManagerAdapter;
import com.geekhome.httpserver.OperationMode;
import com.geekhome.httpserver.modules.IInvalidateCacheListener;

import java.util.ArrayList;

public interface IHardwareManager {
    OutputPortsCollection<Boolean> getDigitalOutputPorts();
    InputPortsCollection<Boolean> getDigitalInputPorts();
    OutputPortsCollection<Integer> getPowerOutputPorts();
    InputPortsCollection<Integer> getPowerInputPorts();
    InputPortsCollection<Double> getTemperaturePorts();
    InputPortsCollection<Double> getHumidityPorts();
    InputPortsCollection<Double> getLuminosityPorts();
    TogglePortsCollection getTogglePorts();

    IOutputPort<Boolean> findDigitalOutputPort(String id) throws PortNotFoundException;
    IOutputPort<Boolean> tryFindDigitalOutputPort(String id);

    IInputPort<Boolean> findDigitalInputPort(String id) throws PortNotFoundException;
    IInputPort<Boolean> tryFindDigitalInputPort(String id);

    IOutputPort<Integer> findPowerOutputPort(String id) throws PortNotFoundException;
    IOutputPort<Integer> tryFindPowerOutputPort(String id);

    IInputPort<Integer> findPowerInputPort(String id) throws PortNotFoundException;
    IInputPort<Integer> tryFindPowerInputPort(String id);

    IInputPort<Double> findTemperaturePort(String id) throws PortNotFoundException;
    IInputPort<Double> tryFindTemperaturePort(String id);

    IInputPort<Double> findHumidityPort(String id) throws PortNotFoundException;
    IInputPort<Double> tryFindHumidityPort(String id);

    IInputPort<Double> findLuminosityPort(String id) throws PortNotFoundException;
    IInputPort<Double> tryFindLuminosityPort(String id);

    ITogglePort findTogglePort(String id) throws PortNotFoundException;
    ITogglePort tryFindTogglePort(String id);

    void setPortMapper(IPortMapper portMapper);

    ArrayList<IHardwareManagerAdapter> getAdapters();
    void setAdapters(ArrayList<IHardwareManagerAdapter> adapters);
    void refreshAndWait() throws Exception;
    void discover() throws DiscoveryException;
    void invalidateAdapters(OperationMode operationMode) throws Exception;
    void setInvalidateCacheListener(IInvalidateCacheListener listener);
}
