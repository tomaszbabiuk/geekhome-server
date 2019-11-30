package com.geekhome.moquettemodule;

import com.geekhome.common.HardwareManager;
import com.geekhome.common.IHardwareManagerAdapterFactory;
import com.geekhome.common.IMonitorable;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.http.Resource;
import com.geekhome.httpserver.modules.Module;

import java.util.ArrayList;

public class MoquetteModule extends Module {

    private HardwareManager _hardwareManager;
    private ILocalizationProvider _localizationProvider;

    public MoquetteModule(final HardwareManager hardwareManager,
                          final ILocalizationProvider localizationProvider) {
        _hardwareManager = hardwareManager;
        _localizationProvider = localizationProvider;
    }

    @Override
    public Resource[] getResources() {
        return new Resource[0];
    }

    @Override
    public String getTextResourcesPrefix() {
        return "MQT";
    }

    @Override
    public void addMonitorable(ArrayList<IMonitorable> monitorables) throws Exception {
        monitorables.add(new MoquetteBrokerMonitorable());
    }
}
