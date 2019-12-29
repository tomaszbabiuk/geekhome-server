package com.geekhome.pi4jmodule;

import com.geekhome.common.hardwaremanager.IHardwareManagerAdapter;
import com.geekhome.common.hardwaremanager.IHardwareManagerAdapterFactory;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;
import com.geekhome.common.localization.ILocalizationProvider;
import com.pi4j.wiringpi.Spi;

import java.util.ArrayList;

public class Pi4jAdapterFactory implements IHardwareManagerAdapterFactory {
    private final ILocalizationProvider _localizationProvider;
    private ILogger _logger = LoggingService.getLogger();

    public Pi4jAdapterFactory(ILocalizationProvider localizationProvider) {
        _localizationProvider = localizationProvider;
    }

    @Override
    public ArrayList<? extends IHardwareManagerAdapter> createAdapters() {

        ArrayList<IHardwareManagerAdapter> adapters = new ArrayList<>();

        try {
            try {
                PiFaceAdapter adapter1 = new PiFaceAdapter(_localizationProvider, PiFaceAddress.JUMPERS_00, Spi.CHANNEL_0);
                PiFaceAdapter adapter2 = new PiFaceAdapter(_localizationProvider, PiFaceAddress.JUMPERS_01, Spi.CHANNEL_0);
                PiFaceAdapter adapter3 = new PiFaceAdapter(_localizationProvider, PiFaceAddress.JUMPERS_10, Spi.CHANNEL_0);
                PiFaceAdapter adapter4 = new PiFaceAdapter(_localizationProvider, PiFaceAddress.JUMPERS_11, Spi.CHANNEL_0);
                adapters.add(adapter1);
                adapters.add(adapter2);
                adapters.add(adapter3);
                adapters.add(adapter4);
            } catch (Exception e) {
                _logger.error("Error creating PiFace adapter", e);
            }
        } catch (NoClassDefFoundError | ExceptionInInitializerError | UnsatisfiedLinkError error) {
            _logger.warning("Cannot load Pi4J native libraries! Is system running on Raspberry PI?");
        }

        return adapters;
    }
}