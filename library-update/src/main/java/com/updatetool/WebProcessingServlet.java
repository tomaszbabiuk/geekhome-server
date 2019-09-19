package com.updatetool;

import com.geekhome.http.ILocalizationProvider;
import com.geekhome.http.IRequestsDispatcher;
import com.geekhome.http.IUContentProvider;
import com.geekhome.http.jetty.WebProcessor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class WebProcessingServlet extends HttpServlet {
    private final WebProcessor _webProcessor;

    public WebProcessingServlet(ArrayList<IRequestsDispatcher> dispatchers, ILocalizationProvider localizationProvider, IUContentProvider contentProvider) {
        _webProcessor = new WebProcessor(dispatchers, null, localizationProvider, contentProvider, new WebProcessor.LoggingCallback() {
            @Override
            public void info(String info) {
                System.out.println(info);
            }

            @Override
            public void error(String info, Throwable throwable) {
                System.out.println(info);
                throwable.printStackTrace();
            }
        }, "/index.htm");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        _webProcessor.process(req, resp);
    }
}