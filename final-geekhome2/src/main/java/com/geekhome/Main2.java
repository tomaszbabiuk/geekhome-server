package com.geekhome;


import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.PathResource;

import java.nio.file.Paths;

public class Main2 {
    public static void main(String[] args) {
        Server server = new Server(80);

        ResourceHandler rh0 = new ResourceHandler();
        rh0.setDirectoriesListed(false);

        ContextHandler context2 = new ContextHandler();
        context2.setContextPath("/");
        context2.setBaseResource(new PathResource(Paths.get("src/main/resources/web")));
        context2.setHandler(rh0);

        ServletContextHandler context0 = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context0.setContextPath("/");
        context0.addServlet(new ServletHolder(new HelloServlet()),"/*");

        ServletContextHandler context1 = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context1.setContextPath("/talk");
        context1.addServlet(new ServletHolder(new MySSEServlet()), "/*");

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] { context2, context1, context0 });

        server.setHandler(contexts);

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
