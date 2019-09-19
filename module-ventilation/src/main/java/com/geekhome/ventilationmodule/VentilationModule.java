package com.geekhome.ventilationmodule;

import com.geekhome.common.CrudAction;
import com.geekhome.coremodule.MasterConfiguration;
import com.geekhome.http.Resource;
import com.geekhome.coremodule.httpserver.CrudPostHandler;
import com.geekhome.hardwaremanager.IHardwareManager;
import com.geekhome.http.INameValueSet;
import com.geekhome.http.IRequestsDispatcher;
import com.geekhome.httpserver.ICrudPostHandler;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.http.jetty.RedirectionResponse;
import com.geekhome.httpserver.modules.*;
import com.geekhome.httpserver.modules.Module;
import com.geekhome.ventilationmodule.automation.VentilationAutomationModule;
import com.geekhome.ventilationmodule.httpserver.VentilationJsonRequestsDispatcher;

import java.util.ArrayList;

public class VentilationModule extends Module {
    private MasterConfiguration _masterConfiguration;
    private ILocalizationProvider _localizationProvider;
    private VentilationConfiguration _ventilationConfiguration;
    private IHardwareManager _hardwareManager;

    public VentilationModule( ILocalizationProvider localizationProvider, MasterConfiguration masterConfiguration,
                             IHardwareManager hardwareManager) throws Exception {
        _masterConfiguration = masterConfiguration;
        _hardwareManager = hardwareManager;
        _localizationProvider = localizationProvider;
        _ventilationConfiguration = new VentilationConfiguration(masterConfiguration.getPool());
    }

    @Override
    public ArrayList<IUnit> createUnits() {
        ArrayList<IUnit> units = new ArrayList<>();
        units.add(new Unit(UnitCategory.ConfigurationDevices, _localizationProvider.getValue("VENT:Recuperators"), "weatherizedhome", "/config/recuperators.htm"));
        return units;
    }

    @Override
    public Resource[] getResources() {
        return new Resource[] {
                new Resource("VENT:3StepSwitch", "3-step switch", "Przełącznik 3-stopniowy"),
                new Resource("VENT:AddRecuperator", "Add recuperator", "Dodaj rekuperator"),
                new Resource("VENT:AutomaticControlPort", "Automatic control's port", "Port automatycznej kontroli"),
                new Resource("VENT:BlocksEnablingSecondGear", "Blocks enabling second gear", "Bloki włączające II bieg"),
                new Resource("VENT:BlocksEnablingThirdGear", "Blocks enabling third gear", "Bloki włączające III bieg"),
                new Resource("VENT:FirstGear", "1st Gear", "I bieg"),
                new Resource("VENT:Recuperator", "Recuperator", "Rekuperator"),
                new Resource("VENT:RecuperatorDetails", "Recuperator details", "Szczegóły rekuperators"),
                new Resource("VENT:RecuperatorPortsAreSame", "Recuperator '%s' is using duplicated ports. Make sure automation control's port, second gear's port and third gear's port are different!", "Rekuperator '%s' używa zduplikowanych portów. Upewnij się, że porty: automatycznej kontroli, port włączyjący drugi bieg oraz port włączający trzeci bieg są różne!"),
                new Resource("VENT:Recuperators", "Recuperators", "Rekuperatory"),
                new Resource("VENT:SecondGear", "2nd Gear", "II bieg"),
                new Resource("VENT:SecondGearPort", "2nd gear's port", "Port II biegu"),
                new Resource("VENT:ThirdGear", "3rd Gear", "III bieg"),
                new Resource("VENT:ThirdGearPort", "3rd gear's port", "Port III biegu"),
        };
    }

    @Override
    public String getTextResourcesPrefix() {
        return "VENT";
    }

    @Override
    public ArrayList<IRequestsDispatcher> createDispatchers() {
        ArrayList<IRequestsDispatcher> dispatchers = new ArrayList<>();
        dispatchers.add(new VentilationJsonRequestsDispatcher(_ventilationConfiguration));
        return dispatchers;
    }

    @Override
    public Collector createCollector() {
        return _ventilationConfiguration;
    }

    @Override
    public IAutomationModule createAutomationModule() {
        return new VentilationAutomationModule(_ventilationConfiguration, _hardwareManager, _localizationProvider);
    }

    @Override
    public String[] listConsolidatedJavascripts() {
        return new String[]
            {
                "JS\\VENTILATIONCONFIG.JS",
            };
    }

    @Override
    public String[] listConsolidatedStyleSheets() {
        return new String[]
            {
                "CSS\\CUSTOMVENTILATION.CSS",
            };
    }

    @Override
    public ArrayList<ICrudPostHandler> createCrudPostHandlers() {
        ArrayList<ICrudPostHandler> handlers = new ArrayList<>();

        CrudPostHandler recuperatorsHandler = new CrudPostHandler(_masterConfiguration, "RECUPERATOR",
                new CrudPostHandler.ICrudModificationFunction() {
                    @Override
                    public void execute(CrudAction action, INameValueSet values) throws Exception {
                        _ventilationConfiguration.modifyRecuperator(action, values);
                    }
                }, new RedirectionResponse("/config/Recuperators.htm"));
        handlers.add(recuperatorsHandler);

        return handlers;
    }

    @Override
    public VentilationConfigurationValidator createConfigurationValidator() {
        return new VentilationConfigurationValidator(_localizationProvider, _ventilationConfiguration);
    }
}
