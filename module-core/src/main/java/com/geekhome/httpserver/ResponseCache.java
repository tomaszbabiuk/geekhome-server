package com.geekhome.httpserver;

import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;
import com.geekhome.http.IResponse;
import com.geekhome.http.IResponseCache;

import java.util.Hashtable;

public class ResponseCache implements IResponseCache {
    private static ILogger Logger = LoggingService.getLogger();

    private Hashtable<String, IResponse> _cache;

    public ResponseCache() {
        _cache = new Hashtable<>();
    }

    public IResponse get(String key) {
        if (_cache.containsKey(key)) {
            Logger.debug("Retrieving " + key + " from cache.");
            return _cache.get(key);
        }

        return null;
    }

    public void put(String key, IResponse content) {
        Logger.debug("Putting " + key + " into response cache.");
        _cache.put(key, content);
    }

    public void remove(String key) {
        if (_cache.containsKey(key)) {
            _cache.remove(key);
        }
    }

    public void clear() {
        _cache.clear();
    }
}
