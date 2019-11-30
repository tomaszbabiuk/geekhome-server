package com.geekhome.shellymodule;

import com.geekhome.common.*;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.hardwaremanager.InputPortsCollection;
import com.geekhome.hardwaremanager.OutputPortsCollection;
import com.geekhome.hardwaremanager.TogglePortsCollection;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.httpserver.OperationMode;
import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.net.*;
import java.util.Calendar;
import java.util.Enumeration;

class ShellyAdapter extends NamedObject implements IHardwareManagerAdapter {

    private static ILogger _logger = LoggingService.getLogger();
    private final InetAddress _brokerIP;
    private final OkHttpClient _okClient;
    private final Gson _gson;
    private HardwareManager _hardwareManager;

    ShellyAdapter(final HardwareManager hardwareManager,
                  final ILocalizationProvider localizationProvider) {
        super(new DescriptiveName("Shelly Adapter", "SHELLY"));
        _hardwareManager = hardwareManager;
        _brokerIP = getLocalNetworkIP();
        _okClient = createAnonymousClient();
        _gson = new Gson();
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

        _logger.info("Starting mDNS discovery");

        try {

            Callback shellyCheckCallback = new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            ShellyResponse shellyResponse = _gson.fromJson(response.body().string(), ShellyResponse.class);
                            if (!shellyResponse.getType().isEmpty()) {
                                _logger.info("Shelly FOUND: " + response.request().url());
                                hijackShelly(InetAddress.getByName(response.request().url().host()));
                            }
                        } catch (Exception ex) {
                            //ignored - not a shelly device
                        }
                    }
                }
            };

            for (int i = 0; i < 256; i++) {
                InetAddress ipToCheck = InetAddress.getByAddress( new byte[]{
                        _brokerIP.getAddress()[0],
                        _brokerIP.getAddress()[1],
                        _brokerIP.getAddress()[2],
                        (byte) i
                });

                _logger.info("Checking shelly: " + ipToCheck.getHostAddress());
                checkIfItsShelly(ipToCheck, shellyCheckCallback);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private void hijackShelly(InetAddress shellyIP) throws IOException {
        disableCloud(shellyIP);
        enableMQTT(shellyIP);
    }

    private void checkIfItsShelly(InetAddress possibleShellyIP, Callback callback) {
        Request request = new Request.Builder()
                .url("http://" + possibleShellyIP + "/shelly")
                .build();

            _okClient.newCall(request).enqueue(callback);
    }

    private void enableMQTT(InetAddress shellyIP) throws IOException {
        Request request = new Request.Builder()
                .url("http://" + shellyIP + "/settings/mqtt?mqtt_enable=1&mqtt_server="+ _brokerIP.getHostAddress() +"%3A1883")
                .build();

        Response response = _okClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
    }

    private void disableCloud(InetAddress shellyIP) throws IOException {
        Request request = new Request.Builder()
                .url("http://" + shellyIP + "/settings/cloud?enabled=0")
                .build();

        Response response = _okClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
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

    private InetAddress getLocalNetworkIP() {
        try {
            for (final Enumeration<NetworkInterface> interfaces =
                 NetworkInterface.getNetworkInterfaces();
                 interfaces.hasMoreElements(); ) {
                final NetworkInterface cur = interfaces.nextElement();

                if (cur.isLoopback()) {
                    continue;
                }

                for (final InterfaceAddress addr : cur.getInterfaceAddresses()) {
                    final InetAddress inet_addr = addr.getAddress();

                    if (!(inet_addr instanceof Inet4Address)) {
                        continue;
                    }

                    return inet_addr;
                }
            }
        } catch (Exception ex) {
            _logger.error("Unknown error getting IP address in LAN", ex);
        }

        return null;
    }

    private static OkHttpClient createAnonymousClient() {
        return new OkHttpClient.Builder().build();
    }
}
