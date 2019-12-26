package com.geekhome.coremodule.httpserver;

import com.geekhome.common.configuration.IDevice;
import com.geekhome.common.configuration.MasterConfiguration;
import com.geekhome.common.settings.AutomationSettings;
import com.geekhome.http.IHttpListenerRequest;
import com.geekhome.http.IResponse;
import com.geekhome.http.jetty.JsonRequestsDispatcherBase;
import com.geekhome.common.configuration.CollectorCollection;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.Arrays;
import java.util.List;

public class AutomationSettingsJsonRequestsDispatcher extends JsonRequestsDispatcherBase {
    private AutomationSettings _settings;
    private MasterConfiguration _masterConfiguration;

    public AutomationSettingsJsonRequestsDispatcher(AutomationSettings settings, MasterConfiguration masterConfiguration) {
        _settings = settings;
        _masterConfiguration = masterConfiguration;
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) throws Exception {
        String originalStringUppercased = request.getUrl().getUrlNoQuery().toUpperCase();
        if (originalStringUppercased.equals("/CHECKAUTOMATIONSETTING.JSON")) {
            final String id = request.getUrl().getQueryString().getValues().getValue("id");
            final String value = _settings.readSetting(id);
            JSONAware json = new JSONAware() {
                @Override
                public String toJSONString() {
                    return JSONValue.toJSONString(value);
                }
            };
            return new JsonResponse(JsonResponse.createJSONResult(json), false);
        }

        if (originalStringUppercased.equals("/ISFAVORITE.JSON")) {
            final String id = request.getUrl().getQueryString().getValues().getValue("id");
            String favoritesKey = String.format("FAVORITES.%s", request.getUserName());
            String favoritesValue = _settings.readSetting(favoritesKey);
            List<String> favorites = Arrays.asList(favoritesValue.split(","));
            boolean isFavorite = favorites.contains(id);
            return new JsonResponse(JsonResponse.createJSONResult(isFavorite), false);
        }

        if (originalStringUppercased.equals("/FAVORITEDEVICES.JSON")) {
            String favoritesKey = String.format("FAVORITES.%s", request.getUserName());
            String favoritesValue = _settings.readSetting(favoritesKey, "");
            CollectorCollection<IDevice> favoriteDevices = new CollectorCollection<>();
            if (!favoritesValue.equals("")) {
                for(String deviceId : favoritesValue.split(",")) {
                    IDevice device = _masterConfiguration.trySearchDevice(deviceId);
                    if (device != null) {
                        favoriteDevices.add(device);
                    } else {
                        //TODO:remove broken device from favorites list
                    }
                }
            }
            JSONObject json = JsonResponse.createJSONResult(favoriteDevices);
            return new JsonResponse(json, false);
        }

        return null;
    }
}