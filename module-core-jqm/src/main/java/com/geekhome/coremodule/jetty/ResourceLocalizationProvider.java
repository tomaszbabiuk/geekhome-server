package com.geekhome.coremodule.jetty;

import com.geekhome.common.localization.ILocalizationProvider;
import com.geekhome.common.localization.Language;
import com.geekhome.common.localization.Resource;

import java.util.Hashtable;

public class ResourceLocalizationProvider implements ILocalizationProvider {
    private Hashtable<String, Resource> _resources;

    public ResourceLocalizationProvider() {
        _resources = new Hashtable<>();
    }

    @Override
    public void load(Resource[] resources) {
        for (Resource r : resources) {
            _resources.put(r.getKey(), r);
        }
    }

    public String getValue(String key) {
        Resource resource = _resources.get(key);
        return resource != null ? resource.getValue(Language.PL) : key;
    }

}
