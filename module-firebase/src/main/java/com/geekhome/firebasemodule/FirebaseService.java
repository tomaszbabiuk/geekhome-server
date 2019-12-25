package com.geekhome.firebasemodule;

import com.geekhome.common.*;
import com.geekhome.coremodule.commands.*;
import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.utils.Sleeper;
import com.geekhome.coremodule.settings.AutomationSettings;
import com.geekhome.firebasemodule.sse.FirebaseSse;
import com.geekhome.firebasemodule.sse.FirebaseSseListener;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.firebasemodule.httpserver.FirebaseSettings;
import com.geekhome.synchronizationmodule.business.ReceivedSynchronizationRequest;
import com.geekhome.synchronizationmodule.business.SynchronizationContentType;
import com.geekhome.synchronizationmodule.business.SynchronizationRequest;
import com.geekhome.synchronizationmodule.business.SynchronizedError;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FirebaseService extends MonitorableBase implements FirebaseSseListener {

    private ILogger _logger = LoggingService.getLogger();
    private Synchronizer _synchronizer;
    private ILocalizationProvider _localizationProvider;
    private AutomationSettings _automationSettings;
    private boolean _closed;
    private Gson _gson = new Gson();
    private FirebaseSse _sse;

    FirebaseService(AutomationSettings automationSettings, Synchronizer synchronizer,
                    ILocalizationProvider localizationProvider) {
        super(new DescriptiveName(localizationProvider.getValue("FIRE:FirebaseControl"), "fire"), false, localizationProvider.getValue("C:Initialization"));
        _automationSettings = automationSettings;
        _synchronizer = synchronizer;
        _localizationProvider = localizationProvider;
    }

    private Thread createWatchingThread() {
        Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);

        Runnable watchingWorker = () -> {
            //noinspection InfiniteLoopStatement
            while (true) {
                _closed = false;

                try {
                    FirebaseSettings settings = new FirebaseSettings(_automationSettings);
                    _sse = new FirebaseSse(settings);
                    _sse.attachListener(settings.getPath() + "/sync", this);

                    while (!_closed) {
                        Sleeper.trySleep(1_000);
                    }

                } catch (Exception e) {
                    updateStatus(false, _localizationProvider.getValue("C:ConnectionError"));
                    _logger.error("A problem connecting to Firebase server, waiting 30s", e);
                } finally {
                    Sleeper.trySleep(30_000);
                }
            }
        };

        return new Thread(watchingWorker);
    }

    @Override
    public void start() {
        FirebaseSettings settings = new FirebaseSettings(_automationSettings);
        if (settings.valid()) {
            Thread watchingThread = createWatchingThread();
            watchingThread.start();
        } else {
            updateStatus(false, _localizationProvider.getValue("C:ConfigurationError"));
        }
    }

    @Override
    public void onOpen() {
        _logger.info("SSE open");
        updateStatus(true, _localizationProvider.getValue("C:Operational"));
    }

    @Override
    public void onClosed() {
        _logger.info("Firebase SSE stream has been closed");
        updateStatus(false, _localizationProvider.getValue("C:ConnectionError"));
        _closed = true;
    }

    @Override
    public void onMessage(String eventName, String dataString) throws Exception {
        _logger.info("SSE Event: " + eventName + ", data: " + dataString);

        FirebaseSettings settings = new FirebaseSettings(_automationSettings);
        String from = settings.getPath();

        if (!dataString.equals("null") && eventName.equals("put")) {
            JsonObject jsonObject = _gson.fromJson(dataString, JsonObject.class);
            String path = jsonObject.get("path").getAsString();
            JsonElement jsonDataElement = jsonObject.get("data");

            if (!path.equals("/")) {
                processSingleEvent(from, path, jsonDataElement);
            }
        }
    }

    private void processSingleEvent(String from, String path, JsonElement jsonDataElement) {
        String pathCleaned = path.replaceAll("/", "");
        long timestamp = Long.parseLong(pathCleaned);
        if (!jsonDataElement.isJsonNull()) {
            String innerJson = jsonDataElement.toString();
            processDataEvent(from, timestamp, innerJson);
        }
    }

    private void processDataEvent(String from, long timestamp, String innerJson) {
        SynchronizationRequest syncRequest = _gson.fromJson(innerJson, SynchronizationRequest.class);
        ReceivedSynchronizationRequest receivedRequest = new ReceivedSynchronizationRequest(
                from, timestamp, syncRequest);
        processSynchronizationCommand(receivedRequest);
    }

    private void processSynchronizationCommand(ReceivedSynchronizationRequest command) {
        try {
            Object result = _synchronizer.processSynchronizationCommand(command);
            if (result != null) {
                putSyncResultToFirebase(command.getId(), command.getType().getContentType(), result);
            } else {
                throw new Exception(_localizationProvider.getValue("C:CommandNotFound"));
            }
        } catch (Exception ex) {
            _logger.warning("Error processing sync command", ex);
            Object error = new SynchronizedError(ex);
            try {
                putSyncResultToFirebase(command.getId(), SynchronizationContentType.Error, error);
            } catch (Exception e) {
                _logger.error("Critical error processing sync command", e);
                updateStatus(false, _localizationProvider.getValue("C:ConnectionError"));
            }
        }
    }

    private void putSyncResultToFirebase(Long timestamp, SynchronizationContentType contentType, Object result) throws Exception {
        FirebaseSettings settings = new FirebaseSettings(_automationSettings);
        FirebaseSse sse = new FirebaseSse(settings);
        String path = "/" + settings.getPath() + "/out/" + timestamp + "/" + contentType.toString();

        String json = _gson.toJson(result);
        sse.put(path, json, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                _logger.error("Error putting sync result to Firebase", e);
                updateStatus(false, _localizationProvider.getValue("C:ConnectionError"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                _logger.info("Synchronization command processed.");
                response.close();
            }
        });
    }

    @Override
    public void onComment(String trim) {
        _logger.info("SSE comment");
    }
}