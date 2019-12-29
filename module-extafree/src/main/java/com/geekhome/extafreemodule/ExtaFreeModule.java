package com.geekhome.extafreemodule;

import com.geekhome.common.CrudAction;
import com.geekhome.common.automation.IAutomationModule;
import com.geekhome.common.configuration.Collector;
import com.geekhome.common.configuration.IConfigurationValidator;
import com.geekhome.common.configuration.MasterConfiguration;
import com.geekhome.common.hardwaremanager.IHardwareManagerAdapterFactory;
import com.geekhome.common.localization.Resource;
import com.geekhome.coremodule.httpserver.CrudPostHandler;
import com.geekhome.extafreemodule.automation.ExtaFreeAutomationModule;
import com.geekhome.extafreemodule.httpserver.ExtaFreeJsonHardwareRequestsDispatcher;
import com.geekhome.extafreemodule.httpserver.ExtaFreeJsonRequestsDispatcher;
import com.geekhome.common.hardwaremanager.IHardwareManager;
import com.geekhome.common.INameValueSet;
import com.geekhome.coremodule.httpserver.IRequestsDispatcher;
import com.geekhome.coremodule.httpserver.ICrudPostHandler;
import com.geekhome.common.localization.ILocalizationProvider;
import com.geekhome.coremodule.jetty.RedirectionResponse;
import com.geekhome.coremodule.modules.*;
import com.geekhome.coremodule.modules.Module;

import java.util.ArrayList;

public class ExtaFreeModule extends Module {
    private final ILocalizationProvider _localizationProvider;
    private final ExtaFreeConfiguration _extaFreeConfiguration;
    private IHardwareManager _hardwareManager;
    private MasterConfiguration _masterConfiguration;

    public ExtaFreeModule(ILocalizationProvider localizationProvider, IHardwareManager hardwareManager,
                          MasterConfiguration masterConfiguration) {
        _hardwareManager = hardwareManager;
        _localizationProvider = localizationProvider;
        _masterConfiguration = masterConfiguration;
        _extaFreeConfiguration = new ExtaFreeConfiguration(masterConfiguration.getPool());
    }

    @Override
    public Collector createCollector() {
        return _extaFreeConfiguration;
    }

    @Override
    public ArrayList<IUnit> createUnits() {
        ArrayList<IUnit> units = new ArrayList<>();
        units.add(new Unit(UnitCategory.Diagnostics, _localizationProvider.getValue("EXF:ExtaFreePairing"),
                "arrows", "/diagnostics/extafreepairing.htm"));
        units.add(new Unit(UnitCategory.ConfigurationDevices, _localizationProvider.getValue("EXF:ExtaFreeBlinds"), "extafreeblinds", "/config/extafreeblinds.htm"));
        return units;
    }

    @Override
    public ArrayList<IRequestsDispatcher> createDispatchers() {
        ArrayList<IRequestsDispatcher> dispatchers = new ArrayList<>();
        dispatchers.add(new ExtaFreeJsonHardwareRequestsDispatcher(_hardwareManager));
        dispatchers.add(new ExtaFreeJsonRequestsDispatcher(_extaFreeConfiguration));
        return dispatchers;
    }

    @Override
    public IAutomationModule createAutomationModule() {
        return new ExtaFreeAutomationModule(_extaFreeConfiguration, _hardwareManager, _localizationProvider);
    }

    @Override
    public ArrayList<ICrudPostHandler> createCrudPostHandlers() {
        ArrayList<ICrudPostHandler> handlers = new ArrayList<>();

        CrudPostHandler blindsHandler = new CrudPostHandler(_masterConfiguration, "EXTAFREEBLIND",
                new CrudPostHandler.ICrudModificationFunction() {
                    @Override
                    public void execute(CrudAction action, INameValueSet values) throws Exception {
                        _extaFreeConfiguration.modifyExtaFreeBlind(action, values);
                    }
                }, new RedirectionResponse("/config/extafreeblinds.htm"));
        handlers.add(blindsHandler);

        return handlers;
    }


    @Override
    public String[] listConsolidatedJavascripts() {
        return new String[]
                {
                        "JS\\EXTAFREE.JS",
                };
    }

    @Override
    public String[] listConsolidatedStyleSheets() {
        return new String[]
                {
                        "CSS\\EXTAFREENAVIGATION.CSS",
                };
    }

    @Override
    public void addSerialAdaptersFactory(ArrayList<IHardwareManagerAdapterFactory> factories) {
        IHardwareManagerAdapterFactory factory = new ExtaFreeSerialAdaptersFactory(_localizationProvider);
        factories.add(factory);
    }

    @Override
    public IConfigurationValidator createConfigurationValidator() {
        return new ExtaFreeConfigurationValidator(_localizationProvider, _extaFreeConfiguration);
    }

    @Override
    public Resource[] getResources() {
        return new Resource[] {
                new Resource("EXF:ExtaFreePairing", "'Exta Free' pairing", "Parowanie 'Exta Free'"),
                new Resource("EXF:ExtaFreePorts", "'Exta Free' ports", "Porty 'Exta Free'"),
                new Resource("EXF:Pair", "Pair", "Paruj"),
                new Resource("EXF:Pairing", "Pairing", "Parowanie"),
                new Resource("EXF:SRPs", "SRP-*", "SRP-*"),
                new Resource("EXF:ROPs", "ROP-*", "ROP-*"),
                new Resource("EXF:ExtaFreeBlind", "Exta-Free Blind", "Roleta Exta-Free"),
                new Resource("EXF:ExtaFreeBlinds", "Exta-Free Blinds", "Rolety Exta-Free"),
                new Resource("EXF:AddExtaFreeBlind", "Add Exta-Free blind", "Dodaj roletę Exta-Free"),
                new Resource("EXF:ExtaFreeBlindDetails", "Exta-Free blind details", "Szczegóły rolety Exta-Free"),
                new Resource("EXF:FullOpening", "Full opening",  "Pełne otwarcie"),
                new Resource("EXF:FullClosing", "Full closing",  "Pełne zamknięcie"),
                new Resource("EXF:SlightlyUp", "Slightly up",  "Lekko w górę"),
                new Resource("EXF:SlightlyDown", "Slightly down",  "Lekko w dół"),
                new Resource("EXF:BlindUpPort", "Blind up port",  "Port podnoszenia rolety"),
                new Resource("EXF:BlindDownPort", "Blind down port",  "Port opuszczania rolety"),
                new Resource("EXF:BlindPortsAreSame", "Exta-Free blind '%s' is using duplicated ports. Make sure that blind up port and blind down port are different!",
                        "Rolata Exta-Free '%s' używa zduplikowanych portów. Upewnij się, że porty podnoszenia oraz opuszczania rolety są różne!"),

        };
    }

    @Override
    public String getTextResourcesPrefix() {
        return "EXF";
    }
}