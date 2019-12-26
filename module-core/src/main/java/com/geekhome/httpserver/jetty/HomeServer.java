package com.geekhome.httpserver.jetty;

import com.geekhome.common.configuration.JSONArrayList;
import com.geekhome.common.automation.SystemInfo;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;
import com.geekhome.http.jetty.*;
import com.geekhome.common.configuration.MasterConfiguration;
import com.geekhome.coremodule.PhotosDispatcher;
import com.geekhome.common.hardwaremanager.IHardwareManager;
import com.geekhome.http.IHttpServer;
import com.geekhome.http.ILocalizationProvider;
import com.geekhome.http.IRequestsDispatcher;
import com.geekhome.http.IResponseCache;
import com.geekhome.httpserver.*;
import com.geekhome.common.IInvalidateCacheListener;
import com.geekhome.httpserver.modules.IModule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class HomeServer {
    private final ILogger _logger = LoggingService.getLogger();
    private static ResponseCache _cache;
    private IHttpServer _httpServer;

    private static void removeFromCache(String what) {
        if (what.equals("all")) {
            _cache.clear();
        } else {
            _cache.remove(what);
        }
    }

    private IHttpServer buildHttpServer(int port, SystemInfo systemInfo, IResponseCache cache, ILocalizationProvider localizationProvider,
                                        ArrayList<IModule> modules) throws Exception {
        ArrayList<IRequestsDispatcher> requestsDispatchers = new ArrayList<>();
        ArrayList<String> scriptsToConsolidate = new ArrayList<>();
        ArrayList<String> stylesheetsToConsolidate = new ArrayList<>();
        ArrayList<ICrudPostHandler> crudPostHandlers = new ArrayList<>();

        for(IModule module : modules) {
            crudPostHandlers.addAll(module.createCrudPostHandlers());
            ArrayList<IRequestsDispatcher> moduleDispatchers = module.createDispatchers();
            if (moduleDispatchers != null) {
                requestsDispatchers.addAll(moduleDispatchers);
            }

            Collections.addAll(scriptsToConsolidate, module.listConsolidatedJavascripts());
            Collections.addAll(stylesheetsToConsolidate, module.listConsolidatedStyleSheets());
        }

        EmbeddedResourcesUContentProvider contentProvider = new EmbeddedResourcesUContentProvider();
        requestsDispatchers.add(0, new UpageRequestsDispatcher(localizationProvider, contentProvider));
        requestsDispatchers.add(0, new JavascriptsConsolidationRequestsDispatcher(scriptsToConsolidate, contentProvider, localizationProvider));
        requestsDispatchers.add(0, new StyleSheetsConsolidationRequestsDispatcher(stylesheetsToConsolidate, contentProvider, localizationProvider));
        requestsDispatchers.add(0, new AuthorizationRequestsDispatcher(systemInfo, localizationProvider, contentProvider));
        requestsDispatchers.add(0, new PhotosDispatcher());
        requestsDispatchers.add(new CrudPostRequestsDispatcher(crudPostHandlers, "CONFIG/"));
        requestsDispatchers.add(new CrudPostRequestsDispatcher(crudPostHandlers, "SETTINGS/"));
        requestsDispatchers.add(new ErrorRequestsDispatcher(404, localizationProvider, contentProvider));
        JettyWebHandler handler = new JettyWebHandler(requestsDispatchers, cache, localizationProvider, contentProvider);
        return new JettyHttpServer(port, handler);
    }

    public HomeServer(int port, MasterConfiguration masterConfiguration, IHardwareManager hardwareManager,
                      JSONArrayList<IModule> modules, SystemInfo systemInfo, ILocalizationProvider localizationProvider) throws Exception {
        _cache = new ResponseCache();
        IInvalidateCacheListener clearCacheOnModificationListener = new IInvalidateCacheListener() {
            @Override
            public void invalidate(String what) {
                removeFromCache(what);
            }
        };

        masterConfiguration.setMasterInvalidateCacheListener(clearCacheOnModificationListener);
        hardwareManager.setInvalidateCacheListener(clearCacheOnModificationListener);
        _httpServer = buildHttpServer(port, systemInfo, _cache, localizationProvider, modules);
    }


    public void start() throws Exception {
        startHttpServerInDifferentThread();
    }

    private void startHttpServerInDifferentThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    _httpServer.start();
                } catch (IOException e) {
                    _logger.error("Home http server error", e);
                }
            }
        }).start();

    }
}