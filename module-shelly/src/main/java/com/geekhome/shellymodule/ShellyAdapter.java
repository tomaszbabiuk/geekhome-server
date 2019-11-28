package com.geekhome.shellymodule;

import com.geekhome.common.*;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.hardwaremanager.InputPortsCollection;
import com.geekhome.hardwaremanager.OutputPortsCollection;
import com.geekhome.hardwaremanager.TogglePortsCollection;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.httpserver.OperationMode;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;

class ShellyAdapter extends NamedObject implements IHardwareManagerAdapter {

    private static ILogger _logger = LoggingService.getLogger();
    private HardwareManager _hardwareManager;

    ShellyAdapter(final HardwareManager hardwareManager,
                  final ILocalizationProvider localizationProvider) {
        super(new DescriptiveName("Shelly Adapter", "SHELLY"));
        _hardwareManager = hardwareManager;
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

        try {
                // Create a JmDNS instance
                JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());

                // Add a service listener
                jmdns.addServiceListener("_http._tcp.local.", new ServiceListener() {
                    @Override
                    public void serviceAdded(ServiceEvent serviceEvent) {

                    }

                    @Override
                    public void serviceRemoved(ServiceEvent serviceEvent) {

                    }

                    @Override
                    public void serviceResolved(ServiceEvent serviceEvent) {
                        if (serviceEvent.getName().startsWith("shelly")) {
                            InetAddress shellyIP = serviceEvent.getInfo().getInet4Addresses()[0];

                            //TODO:GET /settings/cloud?enabled=1
                            //TODO:GET /settings/cloud?mqtt_enable=1
                            //TODO:GET /settings/cloud?mqtt_server=1

                            /*

                            FormData
                            mqtt_enable=true&mqtt_server=192.168.33.3%3A1882&mqtt_user=&mqtt_reconnect_timeout_max=60&mqtt_reconnect_timeout_min=2&mqtt_clean_session=true&mqtt_keep_alive=60&mqtt_will_topic=shellies%2Fshellyplug-s-376CC2%2Fonline&mqtt_will_message=false&mqtt_max_qos=0&mqtt_retain=false
                            Request URL: http://192.168.1.7/settings
                            Request Method: POST
                             */

                        }
                    }
                });

                // Wait a bit
                Thread.sleep(30000);
        } catch (Exception ex) {
            throw new DiscoveryException("Shelly discovery exception", ex);
        }
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

    @Override
    public void reconfigure(OperationMode operationMode) throws Exception {
    }

    @Override
    public void stop() {
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
        return null;
    }
}
