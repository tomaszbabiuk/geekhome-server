package com.geekhome.greemodule;

import com.geekhome.common.*;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.hardwaremanager.*;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.httpserver.OperationMode;
import org.openhab.binding.greeair.internal.GreeDevice;
import org.openhab.binding.greeair.internal.GreeDeviceFinder;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Calendar;
import java.util.HashMap;

class GreeAdapter extends NamedObject implements IHardwareManagerAdapter {

    private DatagramSocket _clientSocket;
    private HardwareManager _hardwareManager;
    private ILocalizationProvider _localizationProvider;
    private static ILogger _logger = LoggingService.getLogger();
    private boolean _isOperational;
    private HashMap<String, GreeDevice> _greeDevices;
    private long _lastRefresh;


    GreeAdapter(final HardwareManager hardwareManager,
                final ILocalizationProvider localizationProvider) {
        super(new DescriptiveName("Gree Adapter", "GREE"));
        _hardwareManager = hardwareManager;
        _localizationProvider = localizationProvider;
        try {
            _clientSocket = new DatagramSocket();
            _clientSocket.setSoTimeout(60000);
        } catch (SocketException ex) {
            _logger.error("Cannot create gree adapter", ex);
        }
    }

    @Override
    public void discover(final InputPortsCollection<Boolean> digitalInputPorts,
                           final OutputPortsCollection<Boolean> digitalOutputPorts,
                           final InputPortsCollection<Integer> powerInputPorts,
                           final OutputPortsCollection<Integer> powerOutputPorts,
                           final InputPortsCollection<Double> temperaturePorts,
                           final TogglePortsCollection togglePorts,
                           final InputPortsCollection<Double> humidityPorts,
                           final InputPortsCollection<Double> luminosityPorts) throws DiscoveryException {

        if (_clientSocket == null) {
            return;
        }

        _logger.info("Starting Gree discovery");

        try {
            InetAddress broadcastAddress = InetAddress.getByAddress(new byte[]{(byte) 192, (byte) 168, (byte) 1, (byte) 255});

            GreeDeviceFinder finder = new GreeDeviceFinder(broadcastAddress);
            finder.Scan(_clientSocket);
            _greeDevices = finder.GetDevices();

            for (String greeDeviceId : _greeDevices.keySet()) {
                _logger.info("Gree device found: " + greeDeviceId);
                GreeDevice greeDevice = _greeDevices.get(greeDeviceId);
                greeDevice.BindWithDevice(_clientSocket);
                greeDevice.getDeviceStatus(_clientSocket);

                String greeForceManualPortId = greeDeviceIdToLightPortId(greeDeviceId);
                boolean isLightOn = greeDevice.GetDeviceLight() == 1;
                IInputPort<Boolean> forceManualPort = new SynchronizedInputPort<>(greeForceManualPortId, isLightOn);
                digitalInputPorts.add(forceManualPort);

                String coolingEnablePortId = greeDeviceIdToCoolingEnablePortId(greeDeviceId);
                boolean isCooling = greeDevice.GetDeviceMode() == 1;
                IOutputPort<Boolean> coolingEnablePort = new SynchronizedOutputPort<>(coolingEnablePortId, isCooling);
                digitalOutputPorts.add(coolingEnablePort);

                String heatingEnablePortId = greeDeviceIdToHeatingEnablePortId(greeDeviceId);
                boolean isHeating = greeDevice.GetDevicePower() == 4;
                IOutputPort<Boolean> heatingEnablePort = new SynchronizedOutputPort<>(heatingEnablePortId, isHeating);
                digitalOutputPorts.add(heatingEnablePort);

                String temperatureControlPortId = greeDeviceIdToTemperatureControlPortId(greeDeviceId);
                int temperature = greeDevice.GetDeviceTempSet();
                IOutputPort<Integer> temperatureControlPort = new SynchronizedOutputPort<>(temperatureControlPortId, temperature);
                powerOutputPorts.add(temperatureControlPort);
            }
        } catch (Exception ex) {
            _logger.warning("Error during gree discovery", ex);
        }

        _lastRefresh = Calendar.getInstance().getTimeInMillis();
        _isOperational = true;
    }

    private String greeDeviceIdToLightPortId(String greeDeviceId) {
        return greeDeviceIdTo(greeDeviceId, "light");
    }

    private String greeDeviceIdToHeatingEnablePortId(String greeDeviceId) {
        return greeDeviceIdTo(greeDeviceId, "heat");
    }

    private String greeDeviceIdToCoolingEnablePortId(String greeDeviceId) {
        return greeDeviceIdTo(greeDeviceId, "cool");
    }

    private String greeDeviceIdToTemperatureControlPortId(String greeDeviceId) {
        return greeDeviceIdTo(greeDeviceId, "settemp");
    }

    private String greeDeviceIdTo(String greeDeviceId, String suffix) {
        return "GREE:" + greeDeviceId + ":" + suffix;
    }

    @Override
    public boolean isOperational() {
        return _isOperational;
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
    public void refresh(Calendar now) throws Exception {
        if (_clientSocket == null) {
            return;
        }

        if (_greeDevices != null) {
            if (now.getTimeInMillis() - _lastRefresh > 10000) {
                for (String greeDeviceId : _greeDevices.keySet()) {
                    GreeDevice greeDevice = _greeDevices.get(greeDeviceId);
                    greeDevice.getDeviceStatus(_clientSocket);
                    boolean isLightOn = greeDevice.GetDeviceLight() == 1;
                    boolean isCooling = greeDevice.GetDeviceMode() == 1;
                    boolean isHeating = greeDevice.GetDeviceMode() == 4;
                    boolean isOn = greeDevice.GetDevicePower() == 1;
                    int tempSet = greeDevice.GetDeviceTempSet();

                    //light
                    SynchronizedInputPort<Boolean> lightInput =
                            (SynchronizedInputPort<Boolean>) _hardwareManager.findDigitalInputPort(greeDeviceIdToLightPortId(greeDeviceId));
                    lightInput.setValue(isLightOn);

                    //cooling
                    SynchronizedOutputPort<Boolean> coolingEnablePort =
                            (SynchronizedOutputPort<Boolean>) _hardwareManager.findDigitalOutputPort(greeDeviceIdToCoolingEnablePortId(greeDeviceId));
                    boolean shouldCool = coolingEnablePort.read();
                    if (shouldCool && !isCooling) {
                        greeDevice.SetDeviceMode(_clientSocket, 1);
                    }

                    //heating
                    SynchronizedOutputPort<Boolean> heatingEnablePort =
                            (SynchronizedOutputPort<Boolean>) _hardwareManager.findDigitalOutputPort(greeDeviceIdToHeatingEnablePortId(greeDeviceId));
                    boolean shouldHeat = heatingEnablePort.read();
                    if (shouldHeat && !isHeating) {
                        greeDevice.SetDeviceMode(_clientSocket, 4);
                    }

                    //power
                    boolean shouldBeOn = isCooling || isHeating;
                    if (shouldBeOn && !isOn) {
                        greeDevice.SetDevicePower(_clientSocket, 1);
                    }
                    if (!shouldBeOn && isOn) {
                        greeDevice.SetDevicePower(_clientSocket, 0);
                    }

                    //temperature
                    SynchronizedOutputPort<Integer> temperatureControlPort =
                            (SynchronizedOutputPort<Integer>) _hardwareManager.findPowerOutputPort(greeDeviceIdToTemperatureControlPortId(greeDeviceId));
                    int shouldTempBeSetTo = temperatureControlPort.read();
                    boolean adjustingTempNeeded = shouldTempBeSetTo != tempSet && shouldTempBeSetTo != -1;
                    if (shouldBeOn && adjustingTempNeeded) {
                        greeDevice.SetDeviceTempSet(_clientSocket, shouldTempBeSetTo);
                    }
                }

                _lastRefresh = now.getTimeInMillis();
            }
        }
    }

    @Override
    public RefreshState getRefreshState() {
        return RefreshState.NA;
    }

    @Override
    public void resetLatches() {

    }
}
