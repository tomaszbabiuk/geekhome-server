package com.geekhome.firebasemodule.httpserver;

import com.geekhome.common.settings.AutomationSettings;

public class FirebaseSettings {
    private final String _host;
    private final String _path;

    public String getHost() {
        return _host;
    }

    public String getPath() {
        return _path;
    }


    public FirebaseSettings(AutomationSettings settings) {
        _host = settings.readSetting("Firebase.Host");
        _path = settings.readSetting("Firebase.Path");
    }

    public boolean valid() {
        boolean hostDefined =  _host != null && !_host.isEmpty();
        boolean pathDefined = _path != null && !_path.isEmpty();
        return hostDefined && pathDefined;
    }
}
