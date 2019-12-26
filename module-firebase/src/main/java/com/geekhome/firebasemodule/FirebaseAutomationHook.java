package com.geekhome.firebasemodule;

import com.geekhome.coremodule.commands.Synchronizer;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.automation.AlertAutomationUnit;
import com.geekhome.common.automation.EvaluationResult;
import com.geekhome.common.automation.IDeviceAutomationUnit;
import com.geekhome.common.automation.MasterAutomation;
import com.geekhome.common.settings.AutomationSettings;
import com.geekhome.firebasemodule.httpserver.FirebaseSettings;
import com.geekhome.firebasemodule.sse.FirebaseSse;
import com.geekhome.common.automation.IAutomationHook;
import com.geekhome.synchronizationmodule.business.*;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class FirebaseAutomationHook implements IAutomationHook {

    private ILogger _logger = LoggingService.getLogger();
    private final FirebaseSettings _firebaseSettings;
    private Synchronizer _synchronizer;
    private FirebaseSse _sse;
    private MasterAutomation _masterAutomation;
    private Gson _gson = new Gson();
    private Hashtable<String,Integer> _deviceEvaluationHashes = new Hashtable<>();
    private Hashtable<String,Boolean> _alertEvaluationHashes = new Hashtable<>();

    FirebaseAutomationHook(MasterAutomation masterAutomation,
                           AutomationSettings automationSettings,
                           Synchronizer synchronizer) {
        _masterAutomation = masterAutomation;
        _firebaseSettings = new FirebaseSettings(automationSettings);
        _synchronizer = synchronizer;
        try {
            _sse = new FirebaseSse(_firebaseSettings);
        } catch (Exception ex) {
            _logger.warning("Cannot sync device evaluations with firebase: check configuration!");
        }
    }


    @Override
    public void beforeAutomationLoop() {
        if (_sse != null) {
            sendAllDevicesUpdate();
            sendAllFloorsUpdate();
            sendAllGeofencesUpdate();
            sendAllEvaluationsUpdate();
            sendAllAlertsUpdate();
        }
    }

    private void sendAllDevicesUpdate() {
        Map<String, SynchronizedDevice> devices = _synchronizer.synchronizeDevices();
        String devicesPath = "/" + _firebaseSettings.getPath() + "/devices";
        putToFirebase(devicesPath, _gson.toJson(devices));
    }

    private void sendAllFloorsUpdate() {
        Map<String, SynchronizedFloor> floors = _synchronizer.synchronizeFloors();
        String floorsPath = "/" + _firebaseSettings.getPath() + "/floors";
        putToFirebase(floorsPath, _gson.toJson(floors));
    }

    private void sendAllGeofencesUpdate() {
        Map<String, SynchronizedGeofence> geofences = _synchronizer.synchronizeGeofences();
        String geofencesPath = "/" + _firebaseSettings.getPath() + "/geofences";
        putToFirebase(geofencesPath, _gson.toJson(geofences));
    }

    private void sendAllEvaluationsUpdate() {
        try {
            Map<String, SynchronizedEvaluationResult> evaluations = _synchronizer.synchronizeAllEvaluations();
            String evaluationsPath = "/" + _firebaseSettings.getPath() + "/evaluations";
            putToFirebase(evaluationsPath, _gson.toJson(evaluations));
        } catch (Exception ex) {
            _logger.error("Unhandled exception while sending evaluations to firebase", ex);
        }
    }

    private void sendAllAlertsUpdate() {
        SynchronizedSystemInfo system = _synchronizer.synchronizeSystemInfo();
        String systemPath = "/" + _firebaseSettings.getPath() + "/system";
        putToFirebase(systemPath, _gson.toJson(system));
    }

    private void putToFirebase(String path, String json) {
        _sse.put(path, json, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                _logger.error("Error putting device evaluation to Firebase", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                _logger.info("Device evaluation has been sent to Firebase.");
                response.close();
            }
        });
    }

    @Override
    public void onAutomationLoop() {
        if (_sse != null) {
            for (IDeviceAutomationUnit unit : _masterAutomation.getDevices().values()) {
                String deviceId = unit.getDevice().getName().getUniqueId();
                EvaluationResult result = unit.buildEvaluationResult();
                int resultHash = result.hashCode();
                if (_deviceEvaluationHashes.containsKey(deviceId)) {
                    int previousResultHash = _deviceEvaluationHashes.get(deviceId);
                    if (resultHash != previousResultHash) {
                        _deviceEvaluationHashes.replace(deviceId, resultHash);
                        sendEvaluationUpdate(deviceId);
                    }
                } else {
                    _deviceEvaluationHashes.put(deviceId, resultHash);
                }
            }

            boolean sendSystemUpdate = false;
            for (AlertAutomationUnit unit : _masterAutomation.getAlerts().values()) {
                String alertId = unit.getName().getUniqueId();
                boolean pass = unit.isPassed();

                if (_alertEvaluationHashes.containsKey(alertId)) {
                    boolean previousPass = _alertEvaluationHashes.get(alertId);
                    if (pass != previousPass) {
                        sendSystemUpdate = true;
                    }
                } else {
                    _alertEvaluationHashes.put(alertId, pass);
                }
            }

            if (sendSystemUpdate) {
                sendAlertUpdate();
            }
        }
    }

    private void sendAlertUpdate() {
        sendAllAlertsUpdate();
    }

    private void sendEvaluationUpdate(String deviceId) {
        try {
            SynchronizedEvaluationResult evaluationResult = _synchronizer.synchronizeEvaluation(deviceId);
            String path = "/" + _firebaseSettings.getPath() + "/evaluations/" + deviceId;
            putToFirebase(path, _gson.toJson(evaluationResult));
        } catch (Exception e) {
            _logger.error("Unhandled exception while sending evaluation to Firebase", e);
        }
    }
}
