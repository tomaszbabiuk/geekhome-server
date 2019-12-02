package com.geekhome.shellymodule;

import com.google.gson.annotations.SerializedName;

/*
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
 */
public class ShellyMqttBrief {
    @SerializedName("enable")
    private boolean _enable;

    @SerializedName("server")
    private String _server;

    public boolean isEnable() {
        return _enable;
    }

    public String getServer() {
        return _server;
    }
}
