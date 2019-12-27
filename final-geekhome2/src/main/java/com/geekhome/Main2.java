package com.geekhome;


import com.geekhome.http.jetty.JettyHttpServer;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.PathResource;

import org.glassfish.jersey.servlet.ServletContainer;

import java.io.IOException;
import java.nio.file.Paths;

public class Main2 {
    public static void main(String[] args) {
//        Server server = new Server(80);

        ResourceHandler rh0 = new ResourceHandler();
        rh0.setDirectoriesListed(false);

        ContextHandler webContext = new ContextHandler();
        webContext.setContextPath("/");
        webContext.setBaseResource(new PathResource(Paths.get("src/main/resources/dist")));
        webContext.setHandler(rh0);

        ServletContextHandler streamContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        streamContext.setContextPath("/stream");
        streamContext.addServlet(new ServletHolder(new MySSEServlet()), "/*");

        ServletContextHandler restContext =
                new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        ServletHolder serHol = restContext.addServlet(ServletContainer.class, "/rest/*");
        serHol.setInitOrder(1);
        serHol.setInitParameter("jersey.config.server.provider.packages",
                "com.geekhome.rest");



        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] { webContext, streamContext, restContext });


        JettyHttpServer server = new JettyHttpServer(80, contexts);
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


//        server.setHandler(contexts);
//
//        try {
//            server.start();
//            server.join();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
