package com.geekhome.coremodule.httpserver;

import com.geekhome.common.settings.AutomationSettings;
import com.geekhome.http.IHttpListenerRequest;
import com.geekhome.http.IResponse;
import com.geekhome.http.QueryString;

import java.util.ArrayList;
import java.util.Arrays;

public class AutomationSettingsPostRequestsDispatcher extends PostRequestsDispatcherBase {
    private AutomationSettings _settings;

    public AutomationSettingsPostRequestsDispatcher(AutomationSettings settings) {
        _settings = settings;
    }

    @Override
    public IResponse dispatch(IHttpListenerRequest request) throws Exception{
        String originalStringUppercased =
                request.getUrl().getUrlNoQuery().substring(1, request.getUrl().getUrlNoQuery().length() - 5).toUpperCase();
        QueryString queryString = new QueryString("?" + new String(request.getPostData(), "UTF-8"));

        if (originalStringUppercased.equals("CHANGEAUTOMATIONSETTING")) {
            String id = queryString.getValues().getValue("id");
            String value = queryString.getValues().getValue("value");
            _settings.changeSetting(id, value);
            return new JsonResponse(JsonResponse.createJSONResult(true), false);
        }

        if (originalStringUppercased.equals("TOGGLEFAVORITE")) {
            String id = queryString.getValues().getValue("id");
            String favoritesKey = String.format("FAVORITES.%s",request.getUserName());
            String favoritesValue = _settings.readSetting(favoritesKey, "");
            ArrayList<String> favorites = new ArrayList<>(Arrays.asList(favoritesValue.split(",")));
            boolean isFavorite = favorites.contains(id);
            if (isFavorite) {
                favorites.remove(id);
                favoritesValue = ("," + favoritesValue).replace("," + id, "");
                if (favoritesValue.startsWith(",")) {
                    favoritesValue = favoritesValue.substring(1);
                }
            } else {
                if (favoritesValue.equals("")) {
                    favoritesValue += id;
                } else {
                    favoritesValue += "," + id;
                }
            }
            _settings.changeSetting(favoritesKey, favoritesValue);
            return new JsonResponse(JsonResponse.createJSONResult(true), false);
        }

        return null;
    }
}

