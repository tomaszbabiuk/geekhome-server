package com.geekhome.mqttmodule;

import com.geekhome.common.logging.ILogger;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.mqttmodule.tasmotaapi.*;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Inet4Address;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SonoffHijacker {

    private final OkHttpClient _httpClient;
    private Gson _gson;
    private static ILogger _logger = LoggingService.getLogger();


    public SonoffHijacker(GsonProvider gsonProvider) {
        _gson = gsonProvider.provide();
        _httpClient = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .build();
    }

    public PowerStatus readPowerStatus(Inet4Address address) throws IOException {
        String commandUrl = "http://" + address.getHostAddress()+ "/cm?cmnd=Status%2011";
        PowerStatusResponse statusResponse = callAPI(commandUrl, PowerStatusResponse.class);
        return statusResponse.getPowerStatus();
    }

    public SensorStatus readSensorStatus(Inet4Address address) throws IOException {
        String commandUrl = "http://" + address.getHostAddress() + "/cm?cmnd=Status%2010";
        SensorStatusResponse statusResponse = callAPI(commandUrl, SensorStatusResponse.class);
        return statusResponse.getStatus();
    }

    private <T> T callAPI(String url, Class<T> clazz) throws IOException {
        Response response = executeRequestWithRetry(url);
        String bodyJson;
        if (response.body() != null) {
            bodyJson = response.body().string();
            response.close();
            return _gson.fromJson(bodyJson, clazz);
        }

        throw new IOException("Invalid tasmota api reponse.");
    }

    public void hijackMqtt(Inet4Address address, String mqttHost, String mqttTopic) throws IOException {
        String commandUrl = "http://" + address.getHostAddress() + "/cm?cmnd=Backlog%20MqttHost%20" + mqttHost +
                ";FullTopic%20" + mqttTopic +
                ";SetOption15%20" + "0";
        executeRequest(commandUrl);
    }

    private Response executeRequestWithRetry(String nodeUrl) throws IOException {
        try {
            return executeRequest(nodeUrl);
        } catch (ConnectException cex) {
            trySleepForAMoment();
            try {
                return executeRequest(nodeUrl);
            } catch (ConnectException cex2) {
                trySleepForAMoment();
                return executeRequest(nodeUrl);
            }
        }
    }

    private void trySleepForAMoment() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ignored) {
        }
    }

    private Response executeRequest(String nodeUrl) throws IOException {
        _logger.info("Executing request: " + nodeUrl);
        Request request = new Request.Builder().url(nodeUrl).build();
        Response response = _httpClient.newCall(request).execute();
        if (response.code() != 200) {
            throw new IOException("Protocol problem while hijacking tasmota/sonoff device");
        }

        return response;
    }
}
