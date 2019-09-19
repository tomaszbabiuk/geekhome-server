package com.updatetool;

import com.geekhome.http.ILocalizationProvider;
import com.geekhome.http.IRequestsDispatcher;
import com.geekhome.http.Resource;
import com.geekhome.http.jetty.EmbeddedResourcesUContentProvider;
import com.geekhome.http.jetty.JettyHttpServer;
import com.geekhome.http.jetty.ResourceLocalizationProvider;
import com.geekhome.http.jetty.UpageRequestsDispatcher;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServlet;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        JettyHttpServer server = new JettyHttpServer(8080, createHandler());
        System.out.println("Starting update server on port 8080");
        server.start();
    }

    private static AbstractHandler createHandler() {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        ServletHolder uploadServletHolder = new ServletHolder(createWebServlet());
        uploadServletHolder.getRegistration().setMultipartConfig(new MultipartConfigElement("."));
        context.addServlet(uploadServletHolder, "/*");
        return context;
    }

    private static HttpServlet createWebServlet() {
        ArrayList<IRequestsDispatcher> requestsDispatchers = new ArrayList<>();
        EmbeddedResourcesUContentProvider contentProvider = new EmbeddedResourcesUContentProvider();
        ILocalizationProvider localizationProvider = new ResourceLocalizationProvider();
        localizationProvider.load(new Resource[] {
                new Resource("UT:ServicePage", "GeekHEAT Service Page", "Strona serwisowa systemu geekHEAT"),
                new Resource("UT:Submit", "Submit", "Wgraj"),
                new Resource("UT:InstallIntroduction", "The installation of new geekHEAT version", "Instalacja nowej wersji systemu geekHEAT"),
                new Resource("UT:InstallInstruction", "Browse for installation package and press submit...", "Wskaż plik z paczką instalacyjną, a następnie kliknij przycisk 'Wgraj'..."),
                new Resource("UT:UpdateFailure", "Installation failed. The package is broken or invalid!", "Instalacja nie powiodła się, pakiet jest uszkodzony lub niepoprawny!"),
                new Resource("UT:UpdateSuccess", "Installation success." ,"Instalacja powiodła się."),
                new Resource("UT:FactoryReset", "Factory reset", "Przywracanie wersji fabrycznej"),
                new Resource("UT:FactoryInstruction", "In order to restore factory version click", "Aby przywrócić fabryczną wersję systemu, kliknij"),
                new Resource("UT:Here", "here", "tutaj"),
                new Resource("UT:ResetFailure", "Reset failure", "Błąd przywracania wersji fabrycznej"),
                new Resource("UT:ResetSuccess", "Factory reset completed.", "Przywracanie wersji fabrycznej powiodło się."),
                new Resource("UT:YouCanRestart", "You can restart system now...", "Możesz teraz uruchomić system ponownie..."),
                new Resource("UT:DefaultUserRestored", "Default user has been restored (name:<strong>user</strong>, password: <strong>user</strong>).", "Przywrocono domyślnego użytkownika systemu (nazwa: <strong>user</strong>, hasło: <strong>user</strong>).")
        });

        requestsDispatchers.add(0, new UpageRequestsDispatcher(localizationProvider, contentProvider));
        requestsDispatchers.add(new FileUploadDispatcher("1jmz1bi81jyt1jfu1k8c1iz419q519c31sal1j8x1kff1inm19x419iy1abm1aij1abo19q119xk1b401b3y19x219qd1abe1air1abg19j819xi1iks1kcz1j651sap19bv19q91j1q1k5u1jdg1k151bi61jkj"));
        requestsDispatchers.add(new FactoryResetDispatcher());
        return new WebProcessingServlet(requestsDispatchers, localizationProvider, contentProvider);
    }
}
