package com.geekhome.hardwaremanagermodule;

import com.geekhome.common.IHardwareManagerAdapterFactory;
import com.geekhome.http.Resource;
import com.geekhome.hardwaremanager.IHardwareManager;
import com.geekhome.hardwaremanagermodule.httpserver.HardwareManagerDebugPostRequestsDispatcher;
import com.geekhome.http.IRequestsDispatcher;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.httpserver.modules.*;
import com.geekhome.hardwaremanagermodule.httpserver.HardwareManagerJsonRequestsDispatcher;
import com.geekhome.httpserver.modules.Module;

import java.util.ArrayList;

public class HardwareManagerModule extends Module {
    private boolean _debugMode;
    private ILocalizationProvider _localizationProvider;
    private IHardwareManager _hardwareManager;

    public HardwareManagerModule(boolean debugMode, ILocalizationProvider localizationProvider,
                                 IHardwareManager hardwareManager) {
        _debugMode = debugMode;
        _localizationProvider = localizationProvider;
        _hardwareManager = hardwareManager;
    }

    @Override
    public ArrayList<IUnit> createUnits() {
        ArrayList<IUnit> units = new ArrayList<>();
        units.add(new Unit(UnitCategory.Diagnostics, _localizationProvider.getValue("C:DigitalInputPorts"), "inputplug", "/diagnostics/digitalinputports.htm"));
        units.add(new Unit(UnitCategory.Diagnostics, _localizationProvider.getValue("C:DigitalOutputPorts"), "outputplug", "/diagnostics/digitaloutputports.htm"));
        units.add(new Unit(UnitCategory.Diagnostics, _localizationProvider.getValue("C:TemperaturePorts"),"temperatureplug", "/diagnostics/temperatureports.htm"));
        units.add(new Unit(UnitCategory.Diagnostics, _localizationProvider.getValue("C:TogglePorts"),"toggleplug", "/diagnostics/toggleports.htm"));
//        units.add(new Unit(UnitCategory.Diagnostics, _localizationProvider.getValue("C:AnalogInputPorts"), "inputplug", "/diagnostics/analoginputports.htm"));
        units.add(new Unit(UnitCategory.Diagnostics, _localizationProvider.getValue("C:AnalogOutputPorts"), "outputplug", "/diagnostics/analogoutputports.htm"));
        units.add(new Unit(UnitCategory.Diagnostics, _localizationProvider.getValue("C:HumidityPorts"),"humidityplug", "/diagnostics/humidityports.htm"));
        units.add(new Unit(UnitCategory.Diagnostics, _localizationProvider.getValue("C:LuminosityPorts"),"luminosityplug", "/diagnostics/luminosityports.htm"));
        units.add(new Unit(UnitCategory.Automatic, _localizationProvider.getValue("HM:ControlTable"), "table", "/automatic/controltable.htm"));

        return units;
    }

    @Override
    public ArrayList<IRequestsDispatcher> createDispatchers() {
        ArrayList<IRequestsDispatcher> dispatchers = new ArrayList<>();
        dispatchers.add(new HardwareManagerJsonRequestsDispatcher(_hardwareManager));
        if (_debugMode) {
            dispatchers.add(new HardwareManagerDebugPostRequestsDispatcher(_hardwareManager));
        }
        return dispatchers;
    }

    @Override
    public IConfigurationValidator createConfigurationValidator() {
        return new HardwareManagerConfigurationValidator(_localizationProvider, _hardwareManager);
    }

    @Override
    public void addSerialAdaptersFactory(ArrayList<IHardwareManagerAdapterFactory> factories) {
        if (_debugMode) {
            IHardwareManagerAdapterFactory factory = new MockedAdapterFactory(_localizationProvider);
            factories.add(factory);
        }
    }

    @Override
    public Resource[] getResources() {
        return new Resource[] {
                new Resource("HM:ControlTable", "Control table", "Tabela kontroli"),
                new Resource("HM:PortOvermapped", "Devices: '%s' are using the same port '%s'. The automation of those device may not work correctly. Make sure it's intended!", "Urządzenia: '%s' używają tego samego portu '%s'. Automatyka tych urządzeń może kolidować ze sobą. Upewnij się, że jest to zamierzone działanie."),
                new Resource("HM:Toggling", "Toggling", "Przełączanie"),
                new Resource("HM:Inputs", "Inputs", "Inputs"),
                new Resource("HM:Outputs", "Outputs", "Outputs"),
                new Resource("HM:Temperatures", "Temperatures", "Temperatury"),
                new Resource("HM:Humidity", "Humidity", "Wilgotność"),
                new Resource("HM:Luminosity", "Luminosity", "Iluminacja"),
        };
    }

    @Override
    public String getTextResourcesPrefix() {
        return "HM";
    }

    @Override
    public String[] listConsolidatedJavascripts() {
        if (_debugMode) {
            return new String[]
                    {
                            "JS\\HARDWAREMANAGER.JS",
                            "JS\\HARDWAREMANAGERDEBUG.JS",
                    };
        }

        return new String[]
            {
                "JS\\HARDWAREMANAGER.JS",
            };
    }

    @Override
    public String[] listConsolidatedStyleSheets() {
        return new String[]
            {
                "CSS\\CUSTOMHARDWAREMANAGER.CSS",
            };
    }
}