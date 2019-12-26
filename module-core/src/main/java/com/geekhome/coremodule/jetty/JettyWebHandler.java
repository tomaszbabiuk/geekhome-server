package com.geekhome.coremodule.jetty;

import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;
import com.geekhome.http.*;
import com.geekhome.http.jetty.*;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

class JettyWebHandler extends AbstractHandler {
    private final ILogger _logger = LoggingService.getLogger();
    private final WebProcessor _webProcessor;

    JettyWebHandler(ArrayList<IRequestsDispatcher> dispatchers, IResponseCache cache, ILocalizationProvider localizationProvider, IUContentProvider contentProvider) {
        _webProcessor = new WebProcessor(dispatchers, cache, localizationProvider, contentProvider, new WebProcessor.LoggingCallback() {
            @Override
            public void info(String info) {
                _logger.info(info);
            }

            @Override
            public void error(String info, Throwable throwable) {
                _logger.error(info, throwable);
                LoggingService.setLastServerError(throwable);
            }
        }, "/dashboard.htm");
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        _webProcessor.process(baseRequest, response);
    }
}