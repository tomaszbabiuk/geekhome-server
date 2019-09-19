package com.geekhome;

import com.geekhome.http.Resource;
import com.geekhome.httpserver.modules.Module;

public class HeatingAdaptationModule extends Module {
    @Override
    public Resource[] getResources() {
        return new Resource[] {
            new Resource("THIS:AppEdition", "geekHOME \"Central Heating edition\"", "geekHOME \"Centralne ogrzewanie\""),
        };
    }

    @Override
    public String getTextResourcesPrefix() {
        return "THIS";
    }
}
