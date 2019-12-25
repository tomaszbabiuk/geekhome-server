package com.geekhome.hardwaremanagermodule;

import com.geekhome.common.*;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.hardwaremanager.IHardwareManagerAdapter;
import com.geekhome.common.hardwaremanager.InputPortsCollection;
import com.geekhome.common.hardwaremanager.OutputPortsCollection;
import com.geekhome.common.hardwaremanager.TogglePortsCollection;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.common.OperationMode;

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
        digitalInputPorts.add(new SynchronizedInputPort<>("INPUT0", false, 0));
        digitalInputPorts.add(new SynchronizedInputPort<>("INPUT1", false, 0));
        digitalInputPorts.add(new SynchronizedInputPort<>("INPUT2", false, 0));
        digitalInputPorts.add(new SynchronizedInputPort<>("INPUT3", false, 0));

        digitalOutputPorts.add(new SynchronizedOutputPort<>("OUTPUT0", false, 0));
        digitalOutputPorts.add(new SynchronizedOutputPort<>("OUTPUT1", false, 0));
        digitalOutputPorts.add(new SynchronizedOutputPort<>("OUTPUT2", false, 0));
        digitalOutputPorts.add(new SynchronizedOutputPort<>("OUTPUT3", false, 0));

        temperaturePorts.add(new SynchronizedInputPort<>("TEMP0", 0.0, 0));
        temperaturePorts.add(new SynchronizedInputPort<>("TEMP1", 1.0, 0));
        temperaturePorts.add(new SynchronizedInputPort<>("TEMP2", 2.0, 0));
        temperaturePorts.add(new SynchronizedInputPort<>("TEMP3", 3.0, 0));

        humidityPorts.add(new SynchronizedInputPort<>("HUM0", 0.0, 0));
        humidityPorts.add(new SynchronizedInputPort<>("HUM1", 25.0, 0));
        humidityPorts.add(new SynchronizedInputPort<>("HUM2", 50.0, 0));
        humidityPorts.add(new SynchronizedInputPort<>("HUM3", 100.0, 0));

        luminosityPorts.add(new SynchronizedInputPort<>("LUM0", 0.0, 0));
        luminosityPorts.add(new SynchronizedInputPort<>("LUM1", 500.0, 0));
        luminosityPorts.add(new SynchronizedInputPort<>("LUM2", 1000.0, 0));
        luminosityPorts.add(new SynchronizedInputPort<>("LUM3", 2000.0, 0));

        powerInputPorts.add(new SynchronizedInputPort<>("POWER-IN-0", 0.0, 0));
        powerInputPorts.add(new SynchronizedInputPort<>("POWER-IN-1", 0.0, 0));
        powerInputPorts.add(new SynchronizedInputPort<>("POWER-IN-2", 0.0, 0));
        powerInputPorts.add(new SynchronizedInputPort<>("POWER-IN-3", 0.0, 0));

        togglePorts.add(new SynchronizedTogglePort("ExtaFree-001", 0));
        togglePorts.add(new SynchronizedTogglePort("ExtaFree-002", 0));
        togglePorts.add(new SynchronizedTogglePort("ExtaFree-003", 0));
        togglePorts.add(new SynchronizedTogglePort("ExtaFree-004", 0));

        powerOutputPorts.add(new SynchronizedOutputPort<>("POWER-OUT-1", 0, 0));
        powerOutputPorts.add(new SynchronizedOutputPort<>("POWER-OUT-2", 0, 0));
        powerOutputPorts.add(new SynchronizedOutputPort<>("POWER-OUT-3", 0, 0));
        powerOutputPorts.add(new SynchronizedOutputPort<>("POWER-OUT-4", 0, 0));
    }

    @Override
    public boolean canBeRediscovered() {
        return false;
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
