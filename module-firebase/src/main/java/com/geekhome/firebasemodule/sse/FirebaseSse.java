package com.geekhome.firebasemodule.sse;

import com.geekhome.firebasemodule.httpserver.FirebaseSettings;
import okhttp3.*;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class FirebaseSse {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final String _firebaseUrl;
    private OkHttpClient _inboundClient;
    private OkHttpClient _outboundClient;
    private FirebaseSseEventsDispatcher _sse;

    private static String extractFirebaseUrl(FirebaseSettings settings) throws Exception {
        if (!settings.valid()) {
            throw new Exception("Invalid firebase settings!");
        } else {
            return "https://" + settings.getHost() + "/";
        }
    }

    public FirebaseSse(FirebaseSettings settings) throws Exception {
        this(extractFirebaseUrl(settings));
    }

    public FirebaseSse(String firebaseUrl) {
        _firebaseUrl = firebaseUrl;

        _inboundClient = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .build();

        _outboundClient = new OkHttpClient.Builder()
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .build();
    }

    public void attachListener(String path, FirebaseSseListener listener) {
        String nodeUrl = _firebaseUrl + path + ".json";
        Request request = new Request.Builder().url(nodeUrl).build();
        _sse = new FirebaseSseEventsDispatcher(request, listener);
        _sse.setTimeout(60, TimeUnit.SECONDS);
        _sse.connect(_inboundClient);
    }

    public void put(String path, String json, Callback callback) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(_firebaseUrl + path + ".json")
                .put(body)
                .build();

        _outboundClient.newCall(request).enqueue(callback);
    }

    public void post(String path, String json, Callback callback) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(_firebaseUrl + path + ".json")
                .post(body)
                .build();

        _outboundClient.newCall(request).enqueue(callback);
    }

    public void delete(String path, Callback callback) {
        Request request = new Request.Builder()
                .url(_firebaseUrl + path + ".json")
                .delete()
                .build();

        _outboundClient.newCall(request).enqueue(callback);
    }

    public void detachListener() {
        _sse.close();
    }



}
