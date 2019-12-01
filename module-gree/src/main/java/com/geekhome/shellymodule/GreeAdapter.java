package com.geekhome.shellymodule;

import com.geekhome.common.*;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.hardwaremanager.*;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.httpserver.OperationMode;
import org.openhab.binding.greeair.internal.GreeDevice;
import org.openhab.binding.greeair.internal.GreeDeviceFinder;

import java.net.*;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;

class GreeAdapter extends NamedObject implements IHardwareManagerAdapter {

    private DatagramSocket _clientSocket;
    private HardwareManager _hardwareManager;
    private ILocalizationProvider _localizationProvider;
    private static ILogger _logger = LoggingService.getLogger();
    private boolean _isOperational;
    private HashMap<String, GreeDevice> _greeDevices;
    private long _lastRefresh;
    private boolean _lightRefreshTurn = true;


    GreeAdapter(final HardwareManager hardwareManager,
                final ILocalizationProvider localizationProvider) {
        super(new DescriptiveName("Gree Adapter", "GREE"));
        _hardwareManager = hardwareManager;
        _localizationProvider = localizationProvider;
        _clientSocket = prepareClientSocket();
    }

    private static DatagramSocket prepareClientSocket() {
        DatagramSocket result = null;

        try {
            result = new DatagramSocket();
            result.setSoTimeout(15 * 1000); //15 sec
        } catch (SocketException ex) {
            _logger.error("Cannot create gree adapter", ex);
        }

        return result;
    }

    @Override
    public void discover(final InputPortsCollection<Boolean> digitalInputPorts,
                         final OutputPortsCollection<Boolean> digitalOutputPorts,
                         final InputPortsCollection<Double> powerInputPorts,
                         final OutputPortsCollection<Integer> powerOutputPorts,
                         final InputPortsCollection<Double> temperaturePorts,
                         final TogglePortsCollection togglePorts,
                         final InputPortsCollection<Double> humidityPorts,
                         final InputPortsCollection<Double> luminosityPorts) throws DiscoveryException {

        _logger.info("Starting Gree air conditioners discovery");

        if (_clientSocket == null) {
            _logger.info("Gree discovery stopped - empty client socket!");
            return;
        }

        try {
            InetAddress broadcastAddress = getBroadcastAddress();
            if (broadcastAddress == null) {
                _logger.info("Impossible to get local address, is your network address starting from 192.168.x.x?");
                return;
            }

            GreeDeviceFinder finder = new GreeDeviceFinder(broadcastAddress);
            finder.Scan(_clientSocket);
            _greeDevices = finder.GetDevices();

            for (String greeDeviceId : _greeDevices.keySet()) {
                _logger.info("Gree device found: " + greeDeviceId);
                GreeDevice greeDevice = _greeDevices.get(greeDeviceId);
                greeDevice.BindWithDevice(_clientSocket);
                greeDevice.getDeviceStatus(_clientSocket);

                String lightPortId = greeDeviceIdToLightPortId(greeDeviceId);
                boolean isLightOn = greeDevice.GetDeviceLight() == 1;
                IInputPort<Boolean> lightPort =
                        new ConnectableSynchronizedInputPort<>(lightPortId, isLightOn, true);
                digitalInputPorts.add(lightPort);

                String turboPortId = greeDeviceIdToTurboPortId(greeDeviceId);
                boolean isTurboOn = greeDevice.GetDeviceTurbo() == 1;
                IOutputPort<Boolean> turboPort =
                        new ConnectableSynchronizedOutputPort<>(turboPortId, isTurboOn, true);
                digitalOutputPorts.add(turboPort);

                String temperatureControlPortId = greeDeviceIdToTemperatureControlPortId(greeDeviceId);
                int temperature = greeDevice.GetDeviceTempSet();
                IOutputPort<Integer> temperatureControlPort =
                        new ConnectableSynchronizedOutputPort<>(temperatureControlPortId, temperature, true);
                powerOutputPorts.add(temperatureControlPort);
            }
        } catch (Exception ex) {
            _logger.warning("Error during gree discovery", ex);
        }

        _lastRefresh = Calendar.getInstance().getTimeInMillis();
        _isOperational = true;
    }

    private InetAddress getBroadcastAddress() throws SocketException, UnknownHostException {
        Enumeration e = NetworkInterface.getNetworkInterfaces();
        while (e.hasMoreElements()) {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements()) {
                InetAddress i = (InetAddress) ee.nextElement();
                if (i.getAddress()[0] == (byte) 192 && i.getAddress()[1] == (byte) 168) {
                    byte[] broadcastAddress = {
                            i.getAddress()[0],
                            i.getAddress()[1],
                            i.getAddress()[2],
                            (byte) 255
                    };

                    return InetAddress.getByAddress(broadcastAddress);
                }
            }
        }

        return null;
    }

    private String greeDeviceIdToLightPortId(String greeDeviceId) {
        return greeDeviceIdTo(greeDeviceId, "light");
    }

    private String greeDeviceIdToTemperatureControlPortId(String greeDeviceId) {
        return greeDeviceIdTo(greeDeviceId, "settemp");
    }

    private String greeDeviceIdToTurboPortId(String greeDeviceId) {
        return greeDeviceIdTo(greeDeviceId, "turbo");
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
    public void reconfigure(OperationMode operationMode) {
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

                    if (_lightRefreshTurn) {
                        refreshLight(greeDevice);
                    } else {
                        refreshTurbo(greeDevice);
                        refreshTemperature(greeDevice);
                    }

                    _lightRefreshTurn = !_lightRefreshTurn;
                }

                _lastRefresh = now.getTimeInMillis();
            }
        }
    }

    private void refreshLight(GreeDevice greeDevice) {

        ConnectableSynchronizedOutputPort<Boolean> lightPort =
                (ConnectableSynchronizedOutputPort<Boolean>) _hardwareManager.tryFindDigitalOutputPort(greeDeviceIdToLightPortId(greeDevice.getId()));
        if (lightPort != null) {
            boolean isLightOn = greeDevice.GetDeviceLight() == 1;
            try {
                lightPort.setValue(isLightOn);
                lightPort.setConnected(true);
            } catch (Exception ex) {
                lightPort.setConnected(false);
            }
        }
    }

    private void refreshTurbo(GreeDevice greeDevice) {
        ConnectableSynchronizedOutputPort<Boolean> turboPort =
                (ConnectableSynchronizedOutputPort<Boolean>) _hardwareManager.tryFindDigitalOutputPort(greeDeviceIdToTurboPortId(greeDevice.getId()));
        if (turboPort != null) {
            boolean isTurboOn = greeDevice.GetDeviceTurbo() == 1;
            boolean shouldUpdateTurbo = turboPort.read() != isTurboOn;
            if (shouldUpdateTurbo) {
                try {
                    greeDevice.SetDeviceTurbo(_clientSocket, turboPort.read() ? 1 : 0);
                    turboPort.setConnected(true);
                } catch (Exception ex) {
                    turboPort.setConnected(false);
                }
            }
        }
    }

    private void refreshTemperature(GreeDevice greeDevice) {
        ConnectableSynchronizedOutputPort<Integer> temperatureControlPort =
                (ConnectableSynchronizedOutputPort<Integer>) _hardwareManager.tryFindPowerOutputPort(greeDeviceIdToTemperatureControlPortId(greeDevice.getId()));
        if (temperatureControlPort != null) {
            try {
                greeDevice.getDeviceStatus(_clientSocket);
                boolean isCooling = greeDevice.GetDeviceMode() == 1;
                boolean isHeating = greeDevice.GetDeviceMode() == 4;
                boolean isOn = greeDevice.GetDevicePower() == 1;
                int tempSet = greeDevice.GetDeviceTempSet();

                Integer shouldTempBeSetTo = temperatureControlPort.read();

                //heating
                if (shouldTempBeSetTo > 0) {
                    if (!isHeating) {
                        greeDevice.SetDeviceMode(_clientSocket, 4);
                    }
                    if (!isOn) {
                        greeDevice.SetDevicePower(_clientSocket, 1);
                    }
                    if (tempSet != shouldTempBeSetTo) {
                        greeDevice.SetDeviceTempSet(_clientSocket, shouldTempBeSetTo);
                    }
                }

                //cooling
                if (shouldTempBeSetTo < 0) {
                    if (!isCooling) {
                        greeDevice.SetDeviceMode(_clientSocket, 1);
                    }
                    if (!isOn) {
                        greeDevice.SetDevicePower(_clientSocket, 1);
                    }
                    if (tempSet != -shouldTempBeSetTo) {
                        greeDevice.SetDeviceTempSet(_clientSocket, -shouldTempBeSetTo);
                    }
                }

                //no demand
                if (shouldTempBeSetTo == 0) {
                    if (isOn) {
                        greeDevice.SetDevicePower(_clientSocket, 0);
                    }
                }

                temperatureControlPort.setConnected(true);
            } catch (Exception ex) {
                temperatureControlPort.setConnected(false);
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
