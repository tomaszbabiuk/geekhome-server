package com.geekhome.shellymodule;

import com.geekhome.common.*;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.utils.Sleeper;
import com.geekhome.hardwaremanager.InputPortsCollection;
import com.geekhome.hardwaremanager.OutputPortsCollection;
import com.geekhome.hardwaremanager.TogglePortsCollection;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.httpserver.OperationMode;
import com.geekhome.moquettemodule.MqttBroker;
import com.geekhome.moquettemodule.MqttListener;
import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.net.*;
import java.util.Calendar;
import java.util.Enumeration;

class ShellyAdapter extends NamedObject implements IHardwareManagerAdapter, MqttListener {

    private static ILogger _logger = LoggingService.getLogger();
    private final InetAddress _brokerIP;
    private final OkHttpClient _okClient;
    private final Gson _gson;
    private HardwareManager _hardwareManager;
    private ILocalizationProvider _localizationProvider;
    private MqttBroker _mqttBroker;

    ShellyAdapter(final HardwareManager hardwareManager,
                  final ILocalizationProvider localizationProvider,
                  final MqttBroker mqttBroker) {
        super(new DescriptiveName("Shelly Adapter", "SHELLY"));
        _hardwareManager = hardwareManager;
        _localizationProvider = localizationProvider;
        _mqttBroker = mqttBroker;
        _brokerIP = getLocalNetworkIP();
        _okClient = createAnonymousClient();
        _gson = new Gson();
        _mqttBroker = mqttBroker;

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
                            response.body().close();
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
            Sleeper.trySleep(10);
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
    public void start() {
        _mqttBroker.addMqttListener(this);
    }

    @Override
    public void stop() {
        _mqttBroker.removeMqttListener(this);
    }

    @Override
    public boolean isOperational() {
        return true;
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

    @Override
    public void onPublish(String topicName, String msgAsString) {
        _logger.info("MQTT message: " + topicName + ", content: " + msgAsString);
    }
}

/*
{
   "device":{
      "type":"SHPLG-S",
      "mac":"CC50E3376CC2",
      "hostname":"shellyplug-s-376CC2",
      "num_outputs":1,
      "num_meters":1
   },
   "wifi_ap":{
      "enabled":false,
      "ssid":"shellyplug-s-376CC2",
      "key":""
   },
   "wifi_sta":{
      "enabled":true,
      "ssid":"lte",
      "ipv4_method":"dhcp",
      "ip":null,
      "gw":null,
      "mask":null,
      "dns":null
   },
   "wifi_sta1":{
      "enabled":false,
      "ssid":null,
      "ipv4_method":"dhcp",
      "ip":null,
      "gw":null,
      "mask":null,
      "dns":null
   },
   "mqtt":{
      "enable":true,
      "server":"192.168.1.7:1883",
      "user":"",
      "reconnect_timeout_max":60.000000,
      "reconnect_timeout_min":2.000000,
      "clean_session":true,
      "keep_alive":60,
      "will_topic":"shellies/shellyplug-s-376CC2/online",
      "will_message":"false",
      "max_qos":0,
      "retain":false,
      "update_period":30
   },
   "sntp":{
      "server":"time.google.com"
   },
   "login":{
      "enabled":false,
      "unprotected":false,
      "username":"admin",
      "password":"admin"
   },
   "pin_code":"{tkW0N",
   "coiot_execute_enable":false,
   "name":"",
   "fw":"20190821-095311/v1.5.2@4148d2b7",
   "build_info":{
      "build_id":"20190821-095311/v1.5.2@4148d2b7",
      "build_timestamp":"2019-08-21T09:53:11Z",
      "build_version":"1.0"
   },
   "cloud":{
      "enabled":false,
      "connected":false
   },
   "timezone":"Europe/Warsaw",
   "lat":52.406399,
   "lng":16.925200,
   "tzautodetect":true,
   "time":"09:19",
   "hwinfo":{
      "hw_revision":"prod-190325-test",
      "batch_id":1
   },
   "max_power":2500,
   "led_status_disable":true,
   "led_power_disable":false,
   "relays":[
      {
         "ison":true,
         "has_timer":false,
         "overpower":false,
         "default_state":"on",
         "auto_on":0.00,
         "auto_off":0.00,
         "btn_on_url":null,
         "out_on_url":null,
         "out_off_url":null,
         "schedule":false,
         "schedule_rules":[

         ],
         "max_power":2500
      }
   ],
   "meters":[
      {
         "power":0.00,
         "is_valid":true,
         "timestamp":1575191987,
         "counters":[
            0.000,
            0.000,
            0.000
         ],
         "total":0
      }
   ]
}
 */
