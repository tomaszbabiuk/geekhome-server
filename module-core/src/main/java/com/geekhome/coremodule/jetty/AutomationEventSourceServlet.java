package com.geekhome.coremodule.jetty;

import com.geekhome.coremodule.AutomationEventSource;
import org.eclipse.jetty.servlets.EventSource;
import org.eclipse.jetty.servlets.EventSourceServlet;

import javax.servlet.http.HttpServletRequest;

public class AutomationEventSourceServlet extends EventSourceServlet {
    @Override
    protected EventSource newEventSource(HttpServletRequest httpServletRequest) {
        return new AutomationEventSource();
    }
}
