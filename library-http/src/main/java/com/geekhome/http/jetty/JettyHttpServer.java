package com.geekhome.http.jetty;

import com.geekhome.http.IHttpServer;
import com.geekhome.http.IRequestsDispatcher;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class JettyHttpServer implements IHttpServer {
    private final int _port;
    private final AbstractHandler _handler;
    private Server _server;


    public JettyHttpServer(int port, AbstractHandler handler) {
        _port = port;
        _handler = handler;
    }

    public void stop() throws Exception {
        if (_server != null) {
            _server.stop();
        }
    }

    public void start() throws IOException {
        QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setMinThreads(10);
        threadPool.setMaxThreads(1000);

        _server = new Server(threadPool);

        //SSL
        ServerConnector connector = new ServerConnector(_server);
        connector.setPort(_port);
//        HttpConfiguration https = new HttpConfiguration();
//        https.addCustomizer(new SecureRequestCustomizer());
//        SslContextFactory sslContextFactory = new SslContextFactory();
//        sslContextFactory.setKeyStorePath(getKeystorePath());
//        sslContextFactory.setKeyStorePassword("geekhome");
//        sslContextFactory.setKeyManagerPassword("geekhome");
//        ServerConnector sslConnector = new ServerConnector(_server,
//        new SslConnectionFactory(sslContextFactory, "http/1.1"),
//        new HttpConnectionFactory(https));
//        sslConnector.setPort(443);

        //AUTHORIZATION
        LoginService loginService = new HashLoginService("geekHOME", getRealmPath());
        _server.addBean(loginService);
        _server.addBean(new CustomErrorHandler());

        ConstraintSecurityHandler security = new ConstraintSecurityHandler();
        _server.setHandler(security);

        ConstraintMapping dashboardMapping = createConstraintMapping("/*", new String[] { "user" });
        security.setConstraintMappings(new ConstraintMapping[] { dashboardMapping });

        security.setAuthenticator(new BasicAuthenticator());
        security.setLoginService(loginService);

        security.setHandler(_handler);

        _server.setConnectors(new Connector[] {
                connector,
        });

        try {
            _server.start();
            _server.join();
        }
        catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    private String getRealmPath() throws IOException {
        String currentDir = new File(".").getCanonicalPath();
        return currentDir + File.separator + "geekhomerealm.properties";
    }

//    private String getKeystorePath() throws IOException {
//        String currentDir = new File(".").getCanonicalPath();
//        return currentDir + File.separator + "geekhomeserver.jks";
//    }

    private ConstraintMapping createConstraintMapping(String mappingPathSpec, String[] userRoles) {
        Constraint constraint = new Constraint();
        constraint.setAuthenticate( true );
        constraint.setRoles(userRoles);

        ConstraintMapping mapping = new ConstraintMapping();
        mapping.setPathSpec(mappingPathSpec);
        mapping.setConstraint(constraint);
        return mapping;
    }
}
