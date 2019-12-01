package com.geekhome.common;

import com.geekhome.hardwaremanager.InputPortsCollection;
import com.geekhome.hardwaremanager.OutputPortsCollection;
import com.geekhome.hardwaremanager.TogglePortsCollection;
import com.geekhome.httpserver.OperationMode;

import java.util.Calendar;

public interface IHardwareManagerAdapter extends IMonitorable, INamedObject {
    void discover(InputPortsCollection<Boolean> digitalInputPorts, OutputPortsCollection<Boolean> digitalOutputPorts,
                  InputPortsCollection<Double> powerInputPorts, OutputPortsCollection<Integer> powerOutputPorts,
                  InputPortsCollection<Double> temperaturePorts, TogglePortsCollection togglePorts,
                  InputPortsCollection<Double> humidityPorts, InputPortsCollection<Double> luminosityPorts) throws DiscoveryException;
    void refresh(Calendar now) throws Exception;
    RefreshState getRefreshState();
    void resetLatches();
    void reconfigure(OperationMode operationMode) throws Exception;
    void stop();
}
