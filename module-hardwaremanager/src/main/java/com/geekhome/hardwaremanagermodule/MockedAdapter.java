package com.geekhome.hardwaremanagermodule;

import com.geekhome.common.*;
import com.geekhome.hardwaremanager.InputPortsCollection;
import com.geekhome.hardwaremanager.OutputPortsCollection;
import com.geekhome.hardwaremanager.TogglePortsCollection;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.httpserver.OperationMode;

class MockedAdapter extends NamedObject implements IHardwareManagerAdapter {
    private ILocalizationProvider _localizationProvider;

    MockedAdapter(ILocalizationProvider localizationProvider) {
        super(new DescriptiveName("Mocked Adapter", "MOCK"));
        _localizationProvider = localizationProvider;
    }

    @Override
    public String discover(InputPortsCollection<Boolean> digitalInputPorts, OutputPortsCollection<Boolean> digitalOutputPorts,
                         InputPortsCollection<Integer> analogInputPorts, OutputPortsCollection<Integer> analogOutputPorts,
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

        analogInputPorts.add(new SynchronizedInputPort<>("ANALOG-IN-0", 0));
        analogInputPorts.add(new SynchronizedInputPort<>("ANALOG-IN-1", 0));
        analogInputPorts.add(new SynchronizedInputPort<>("ANALOG-IN-2", 0));
        analogInputPorts.add(new SynchronizedInputPort<>("ANALOG-IN-3", 0));

        togglePorts.add(new SynchronizedTogglePort("ExtaFree-001"));
        togglePorts.add(new SynchronizedTogglePort("ExtaFree-002"));
        togglePorts.add(new SynchronizedTogglePort("ExtaFree-003"));
        togglePorts.add(new SynchronizedTogglePort("ExtaFree-004"));

        analogOutputPorts.add(new SynchronizedOutputPort<>("ANALOG-OUT-1"));
        analogOutputPorts.add(new SynchronizedOutputPort<>("ANALOG-OUT-2"));
        analogOutputPorts.add(new SynchronizedOutputPort<>("ANALOG-OUT-3"));
        analogOutputPorts.add(new SynchronizedOutputPort<>("ANALOG-OUT-4"));

        return "MOCK";
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
    public void invalidate(OperationMode operationMode) {
    }

    @Override
    public void stop() {
    }

    @Override
    public void refresh() throws Exception {
    }

    @Override
    public RefreshState getRefreshState() {
        return RefreshState.NA;
    }

    @Override
    public void resetLatches() {

    }
}
