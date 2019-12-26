package com.geekhome;

import com.geekhome.http.Resource;
import com.geekhome.coremodule.modules.Module;

public class FullEditionAdaptationModule extends Module {
    @Override
    public Resource[] getResources() {
        return new Resource[] {
            new Resource("THIS:AppEdition", "geekHOME", "geekHOME"),
        };
    }

    @Override
    public String getTextResourcesPrefix() {
        return "THIS";
    }
}
