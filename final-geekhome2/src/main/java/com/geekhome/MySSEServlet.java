package com.geekhome;

import org.eclipse.jetty.servlets.EventSource;
import org.eclipse.jetty.servlets.EventSourceServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

@WebServlet(urlPatterns = "/talk", initParams = { @WebInitParam(name = "heartBeatPeriod", value = "5") }, asyncSupported = true)
public class MySSEServlet extends EventSourceServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected EventSource newEventSource(final HttpServletRequest req) {
        return new EventSource() {

            @Override
            public void onOpen(final Emitter emitter) throws IOException {
                emitter.data("new server event " + new Date().toString());
                while (true) {
                    System.out.println("propagating event..");
                    try {
                        Thread.sleep(5000);
                        emitter.data("new server event "
                                + new Date().toString());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onClose() {
                System.out.println("closed");
            }
        };
    }
}
