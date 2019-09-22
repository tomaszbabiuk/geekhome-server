package com.geekhome.common;

import com.geekhome.hardwaremanager.InputPortsCollection;
import com.geekhome.hardwaremanager.OutputPortsCollection;
import com.geekhome.hardwaremanager.TogglePortsCollection;
import com.geekhome.httpserver.OperationMode;

public interface IHardwareManagerAdapter extends IMonitorable, INamedObject {
    void discover(InputPortsCollection<Boolean> digitalInputPorts, OutputPortsCollection<Boolean> digitalOutputPorts,
                  InputPortsCollection<Integer> analogInputPorts, OutputPortsCollection<Integer> analogOutputPorts,
                  InputPortsCollection<Double> temperaturePorts, TogglePortsCollection togglePorts,
                  InputPortsCollection<Double> humidityPorts, InputPortsCollection<Double> luminosityPorts) throws DiscoveryException;
    void refresh() throws Exception;
    RefreshState getRefreshState();
    void resetLatches();
    void invalidate(OperationMode operationMode) throws Exception;
    void stop();
}
