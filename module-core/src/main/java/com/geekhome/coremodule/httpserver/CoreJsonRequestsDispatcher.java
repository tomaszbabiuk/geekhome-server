package com.geekhome.coremodule.httpserver;

import com.geekhome.common.INamedObject;
import com.geekhome.common.configuration.IBlocksTarget;
import com.geekhome.common.configuration.IDevice;
import com.geekhome.common.configuration.JSONArrayList;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.coremodule.*;
import com.geekhome.http.IHttpListenerRequest;
import com.geekhome.http.IResponse;
import com.geekhome.http.jetty.JsonRequestsDispatcherBase;
import com.geekhome.httpserver.ConfigInfo;
import com.geekhome.httpserver.DiagnosticInfo;
import com.geekhome.httpserver.JsonResponse;
import com.geekhome.common.configuration.CollectorCollection;
import com.geekhome.httpserver.modules.Dependency;
import com.geekhome.common.configuration.ObjectNotFoundException;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import java.io.IOException;

public class CoreJsonRequestsDispatcher extends JsonRequestsDispatcherBase {
    private final DiagnosticInfo _diagnosticInfo;
    private final ConfigInfo _configInfo;
    private final DashboardAlertService _dashboardAlertService;
    private MasterConfiguration _masterConfiguration;
    private IImagesLister _photosLister;

    public CoreJsonRequestsDispatcher(MasterConfiguration masterConfiguration, DashboardAlertService dashboardAlertService) {
        _masterConfiguration = masterConfiguration;
        _dashboardAlertService = dashboardAlertService;
        _photosLister = new FileImagesLister();
        _diagnosticInfo = new DiagnosticInfo(dashboardAlertService);
        _configInfo = new ConfigInfo(_masterConfiguration);
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) throws ObjectNotFoundException, IOException {
        String originalStringUppercased = request.getUrl().getUrlNoQuery().toUpperCase();
        if (originalStringUppercased.equals("/PHOTOS.JSON")) {
            return new JsonResponse(_photosLister.listImages(), false);
        }

        if (originalStringUppercased.equals("/CONFIG/FLOORS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_masterConfiguration.getFloors());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/MODES.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_masterConfiguration.getModes());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/ALERTS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_masterConfiguration.getAlerts());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/GEOFENCES.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_masterConfiguration.getGeofences());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/KEYSWITCHES.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_masterConfiguration.getKeySwitches());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/READVALUECOMMANDS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_masterConfiguration.getReadValueCommands());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/CHANGESTATECOMMANDS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_masterConfiguration.getChangeStateCommands());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/BLOCKS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_masterConfiguration.getBlocks());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/MULTISTATECONDITIONS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_masterConfiguration.getMultistateConditions());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/VALUECONDITIONS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_masterConfiguration.getValueConditions());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/DELTACONDITIONS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_masterConfiguration.getDeltaConditions());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/GROUPCONDITIONS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_masterConfiguration.getGroupConditions());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/TIMECONDITIONS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_masterConfiguration.getTimeConditions());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/DATECONDITIONS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_masterConfiguration.getDateConditions());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/GEOFENCECONDITIONS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_masterConfiguration.getGeofenceConditions());
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/ALLCONDITIONS.JSON")) {
            CollectorCollection<INamedObject> conditions = new CollectorCollection<>();
            for (CollectorCollection collector : _masterConfiguration.getConditionsCollectors()) {
                for (Object condition : collector.values()) {
                    conditions.add((INamedObject) condition);
                }

            }
            JSONObject json = JsonResponse.createJSONResult(conditions);
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/ALLDEVICES.JSON")) {
            CollectorCollection<INamedObject> devices = new CollectorCollection<>();
            for (CollectorCollection collection : _masterConfiguration.getDevicesCollectors()) {
                for (Object device : collection.values()) {
                    devices.add((INamedObject) device);
                }
            }
            JSONObject json = JsonResponse.createJSONResult(devices);
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/ALLMULTISTATEDEVICES.JSON")) {
            CollectorCollection<INamedObject> devices = new CollectorCollection<>();
            for (CollectorCollection collection : _masterConfiguration.getDevicesCollectors()) {
                for (Object device : collection.values()) {
                    if (device instanceof MultistateDevice) {
                        devices.add((INamedObject) device);
                    }
                }
            }
            JSONObject json = JsonResponse.createJSONResult(devices);
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/ALLVALUEDEVICES.JSON")) {
            CollectorCollection<INamedObject> devices = new CollectorCollection<>();
            for (CollectorCollection collection : _masterConfiguration.getDevicesCollectors()) {
                for (Object device : collection.values()) {
                    if (device instanceof IValueDevice) {
                        devices.add((INamedObject) device);
                    }
                }
            }
            JSONObject json = JsonResponse.createJSONResult(devices);
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/ALLBLOCKSTARGETDEVICES.JSON")) {
            CollectorCollection<INamedObject> devices = new CollectorCollection<>();
            for (CollectorCollection collection : _masterConfiguration.getDevicesCollectors()) {
                for (Object device : collection.values()) {
                    if (device instanceof IBlocksTarget) {
                        devices.add((INamedObject) device);
                    }
                }
            }
            JSONObject json = JsonResponse.createJSONResult(devices);
            return new JsonResponse(json, true);
        }

        if (originalStringUppercased.equals("/CONFIG/FINDDEVICE.JSON")) {
            String id = request.getUrl().getQueryString().getValues().getValue("id");
            JSONAware deviceFound = _masterConfiguration.searchDevice(id);
            return new JsonResponse(deviceFound, false);
        }

        if (originalStringUppercased.equals("/CONFIG/ALLDEVICESINROOM.JSON")) {
            String id = request.getUrl().getQueryString().getValues().getValue("id");
            CollectorCollection<IDevice> devicesInRoom = _masterConfiguration.searchDevicesInRoom(id, null);
            JSONObject json = JsonResponse.createJSONResult(devicesInRoom);
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/CONFIG/ALLONOFFDEVICESINROOM.JSON")) {
            String id = request.getUrl().getQueryString().getValues().getValue("id");
            CollectorCollection<IDevice> onOffDevicesInRoom = _masterConfiguration.searchDevicesInRoom(id, new MatchDeviceDelegate() {
                @Override
                public boolean execute(IDevice device) {
                    return device instanceof OnOffDeviceBase;
                }
            });
            JSONObject json = JsonResponse.createJSONResult(onOffDevicesInRoom);
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/CONFIG/CANADDDEVICES.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(MasterConfigurationValidation.canAddDevices(_masterConfiguration));
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/CONFIG/CANADDCHANGESTATECOMMANDS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(MasterConfigurationValidation.canAddChangeStateCommands(_masterConfiguration));
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/CONFIG/CANADDGROUPCONDITIONS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(MasterConfigurationValidation.canAddGroupConditions(_masterConfiguration));
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/CONFIG/CANADDMULTISTATECONDITIONS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(MasterConfigurationValidation.canAddMultistateConditions(_masterConfiguration));
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/CONFIG/CANADDVALUECONDITIONS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(MasterConfigurationValidation.canAddValueConditions(_masterConfiguration));
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/CONFIG/CANADDDELTACONDITIONS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(MasterConfigurationValidation.canAddDeltaConditions(_masterConfiguration));
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/CONFIG/CANADDGEOFENCECONDITIONS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(MasterConfigurationValidation.canAddGeofenceConditions(_masterConfiguration));
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/DEPENDENCYCHECK.JSON")) {
            JSONArrayList<Dependency> dependencies = _masterConfiguration.buildDependencies(request.getUrl().getQueryString().getValues().getValue("id"));
            return new JsonResponse(JsonResponse.createJSONResult(dependencies), false);
        }

        if (originalStringUppercased.equals("/CONFIGURATIONCHECK.JSON")) {
            return new JsonResponse(JsonResponse.createJSONResult(_masterConfiguration.validateConfiguration()), false);
        }

        if (originalStringUppercased.equals("/DIAGNOSTICINFO.JSON")) {
            return new JsonResponse(_diagnosticInfo, false);
        }

        if (originalStringUppercased.equals("/CONFIGINFO.JSON")) {
            return new JsonResponse(_configInfo, false);
        }

        if (originalStringUppercased.equals("/LASTSERVERERROR.JSON")) {
            @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
            String errorMessage = LoggingService.getLastServerError() == null ? "" : LoggingService.getLastServerError().getMessage();
            JSONObject json = JsonResponse.createJSONResult(errorMessage);
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/DIAGNOSTICS/ACTIVITIES.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(LoggingService.getLogger().getActivities());
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/DIAGNOSTICS/ERRORS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(LoggingService.getLogger().getErrors());
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/DIAGNOSTICS/ALERTS.JSON")) {
            JSONObject json = JsonResponse.createJSONResult(_dashboardAlertService.getAlerts());
            return new JsonResponse(json, false);
        }

        if (originalStringUppercased.equals("/DIAGNOSTICS/CLEARACTIVITIES.JSON")) {
            LoggingService.getLogger().clearActivities();
            return new JsonResponse(JsonResponse.createJSONResult(true), false);
        }

        return null;
    }
}