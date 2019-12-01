package com.geekhome.hardwaremanagermodule;

import com.geekhome.common.*;
import com.geekhome.hardwaremanager.InputPortsCollection;
import com.geekhome.hardwaremanager.OutputPortsCollection;
import com.geekhome.hardwaremanager.TogglePortsCollection;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.httpserver.OperationMode;

import java.util.Calendar;

class MockedAdapter extends NamedObject implements IHardwareManagerAdapter {
    private ILocalizationProvider _localizationProvider;

    MockedAdapter(ILocalizationProvider localizationProvider) {
        super(new DescriptiveName("Mocked Adapter", "MOCK"));
        _localizationProvider = localizationProvider;
    }

    @Override
    public void discover(InputPortsCollection<Boolean> digitalInputPorts, OutputPortsCollection<Boolean> digitalOutputPorts,
                         InputPortsCollection<Double> powerInputPorts, OutputPortsCollection<Integer> powerOutputPorts,
                         InputPortsCollection<Double> temperaturePorts, TogglePortsCollection togglePorts,
                         InputPortsCollection<Double> humidityPorts, InputPortsCollection<Double> luminosityPorts) throws DiscoveryException {
        digitalInputPorts.add(new SynchronizedInputPort<>("INPUT0", false));
        digitalInputPorts.add(new SynchronizedInputPort<>("INPUT1", false));
        digitalInputPorts.add(new SynchronizedInputPort<>("INPUT2", false));
        digitalInputPorts.add(new SynchronizedInputPort<>("INPUT3", false));

        digitalOutputPorts.add(new SynchronizedOutputPort<>("OUTPUT0", false));
        digitalOutputPorts.add(new SynchronizedOutputPort<>("OUTPUT1", false));
        digitalOutputPorts.add(new SynchronizedOutputPort<>("OUTPUT2", false));
        digitalOutputPorts.add(new SynchronizedOutputPort<>("OUTPUT3", false));

        temperaturePorts.add(new SynchronizedInputPort<>("TEMP0", 0.0));
        temperaturePorts.add(new SynchronizedInputPort<>("TEMP1", 1.0));
        temperaturePorts.add(new SynchronizedInputPort<>("TEMP2", 2.0));
        temperaturePorts.add(new SynchronizedInputPort<>("TEMP3", 3.0));

        humidityPorts.add(new SynchronizedInputPort<>("HUM0", 0.0));
        humidityPorts.add(new SynchronizedInputPort<>("HUM1", 25.0));
        humidityPorts.add(new SynchronizedInputPort<>("HUM2", 50.0));
        humidityPorts.add(new SynchronizedInputPort<>("HUM3", 100.0));

        luminosityPorts.add(new SynchronizedInputPort<>("LUM0", 0.0));
        luminosityPorts.add(new SynchronizedInputPort<>("LUM1", 500.0));
        luminosityPorts.add(new SynchronizedInputPort<>("LUM2", 1000.0));
        luminosityPorts.add(new SynchronizedInputPort<>("LUM3", 2000.0));

        powerInputPorts.add(new SynchronizedInputPort<>("POWER-IN-0", 0.0));
        powerInputPorts.add(new SynchronizedInputPort<>("POWER-IN-1", 0.0));
        powerInputPorts.add(new SynchronizedInputPort<>("POWER-IN-2", 0.0));
        powerInputPorts.add(new SynchronizedInputPort<>("POWER-IN-3", 0.0));

        togglePorts.add(new SynchronizedTogglePort("ExtaFree-001"));
        togglePorts.add(new SynchronizedTogglePort("ExtaFree-002"));
        togglePorts.add(new SynchronizedTogglePort("ExtaFree-003"));
        togglePorts.add(new SynchronizedTogglePort("ExtaFree-004"));

        powerOutputPorts.add(new SynchronizedOutputPort<>("POWER-OUT-1", 0));
        powerOutputPorts.add(new SynchronizedOutputPort<>("POWER-OUT-2", 0));
        powerOutputPorts.add(new SynchronizedOutputPort<>("POWER-OUT-3", 0));
        powerOutputPorts.add(new SynchronizedOutputPort<>("POWER-OUT-4", 0));
    }

    @Override
    public boolean isOperational() {
        return true;
    }

    @Override
    public void start() {
    }

    @Override
    public String getStatus() {
        return _localizationProvider.getValue("C:NA");
    }

    @Override
    public void reconfigure(OperationMode operationMode) {
    }

    @Override
    public void stop() {
    }

    @Override
    public void refresh(Calendar now) throws Exception {
    }

    @Override
    public RefreshState getRefreshState() {
        return RefreshState.NA;
    }

    @Override
    public void resetLatches() {

    }
}
