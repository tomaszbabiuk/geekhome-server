package com.geekhome.greemodule;

import com.geekhome.common.*;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.hardwaremanager.*;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.httpserver.OperationMode;
import org.openhab.binding.greeair.internal.GreeDeviceFinder;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
import java.net.ConnectException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

class GreeAdapter extends NamedObject implements IHardwareManagerAdapter {

    private ILocalizationProvider _localizationProvider;
    private static ILogger _logger = LoggingService.getLogger();
    private boolean _isOperational;


    GreeAdapter(final ILocalizationProvider localizationProvider) {
        super(new DescriptiveName("Gree Adapter", "GREE"));
        _localizationProvider = localizationProvider;
    }

    @Override
    public String discover(final InputPortsCollection<Boolean> digitalInputPorts,
                           final OutputPortsCollection<Boolean> digitalOutputPorts,
                           final InputPortsCollection<Integer> analogInputPorts,
                           final OutputPortsCollection<Integer> analogOutputPorts,
                           final InputPortsCollection<Double> temperaturePorts,
                           final TogglePortsCollection togglePorts,
                           final InputPortsCollection<Double> humidityPorts,
                           final InputPortsCollection<Double> luminosityPorts) throws DiscoveryException {
        _logger.info("Starting Gree discovery");

        try {
            InetAddress broadcastAddress = InetAddress.getByAddress(new byte[]{(byte) 192, (byte) 168, (byte) 1, (byte) 255});
            DatagramSocket clientSocket = new DatagramSocket();
            clientSocket.setSoTimeout(60000);

            GreeDeviceFinder finder = new GreeDeviceFinder(broadcastAddress);
            finder.Scan(clientSocket);

            Object devices = finder.GetDevices();
            Object dupa = devices;
        } catch (Exception ex) {
            return ex.toString();
        }

        return "GREE";
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
