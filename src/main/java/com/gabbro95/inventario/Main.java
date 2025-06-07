package com.gabbro95.inventario;

import com.gabbro95.inventario.servlet.CarrelloServlet;
import com.gabbro95.inventario.servlet.ChiaveValoreServlet;
import com.gabbro95.inventario.servlet.ContenitoreServlet;
import com.gabbro95.inventario.servlet.DashboardServlet;
import com.gabbro95.inventario.servlet.GestisciChiaveValoreServlet;
import com.gabbro95.inventario.servlet.LoginServlet;
import com.gabbro95.inventario.servlet.LogoutServlet;
import com.gabbro95.inventario.servlet.ModificaChiaveValoreServlet;
import com.gabbro95.inventario.servlet.ModificaOggettoServlet;
import com.gabbro95.inventario.servlet.NuovaChiaveValoreServlet;
import com.gabbro95.inventario.servlet.NuovoOggettoServlet; // Aggiunto import
import com.gabbro95.inventario.servlet.OAuth2CallbackServlet;
import com.gabbro95.inventario.servlet.OAuth2LoginRedirectServlet;
import com.gabbro95.inventario.servlet.OggettoServlet;
import com.gabbro95.inventario.servlet.ProfiloServlet;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import java.io.File;
import java.nio.file.Files; // <--- AGGIUNGI QUESTO IMPORT

public class Main {
    public static void main(String[] args) throws Exception {
        // Usa la porta fornita da Heroku, o la 8080 di default
        String port = System.getenv("PORT");
        if (port == null || port.isEmpty()) {
            port = "8080";
        }
        System.out.println("DEBUG: Heroku PORT environment variable: " + port); // <--- AGGIUNGI QUESTO LOG

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.parseInt(port));

        // AGGIUNGI QUESTO LISTENER PER PIÙ LOG SULL'AVVIO DI TOMCAT
        tomcat.getServer().addLifecycleListener(new org.apache.catalina.startup.VersionLoggerListener()); // <--- AGGIUNGI QUESTA RIGA

        tomcat.getConnector();

        String webappDir = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "webapp";

        // AGGIUNGI QUESTI LOG DI DEBUG PER VERIFICARE LA DIRECTORY WEBAPP
        File webappFile = new File(webappDir);
        System.out.println("DEBUG: Calculated webappDir: " + webappDir); // <--- AGGIUNGI QUESTO LOG
        System.out.println("DEBUG: Does webappDir exist? " + webappFile.exists()); // <--- AGGIUNGI QUESTO LOG
        System.out.println("DEBUG: Is webappDir a directory? " + webappFile.isDirectory()); // <--- AGGIUNGI QUESTO LOG

        // --- CAMBIA DA addWebapp A addContext QUI SOTTO ---
        Context context = tomcat.addContext("/inventario", webappDir); // <--- CAMBIA QUESTA RIGA

        System.out.println("Configuring webapp with basedir: " + webappDir + " at context /inventario");

        // *** Mappatura Programmatica di TUTTI i Servlet ***
        // ... (tutte le mappature dei servlet rimangono invariate, come le hai ora) ...
        // Le ho rimosse qui per brevità, ma nel tuo file devono esserci tutte quelle che hai già.

        // CarrelloServlet
        Tomcat.addServlet(context, "CarrelloServlet", new CarrelloServlet());
        context.addServletMappingDecoded("/carrello", "CarrelloServlet");

        // ChiaveValoreServlet
        Tomcat.addServlet(context, "ChiaveValoreServlet", new ChiaveValoreServlet());
        context.addServletMappingDecoded("/chiave-valore", "ChiaveValoreServlet");

        Tomcat.addServlet(context, "ContenitoreServlet", new ContenitoreServlet());
        context.addServletMappingDecoded("/contenitore", "ContenitoreServlet");

        Tomcat.addServlet(context, "DashboardServlet", new DashboardServlet());
        context.addServletMappingDecoded("/dashboard", "DashboardServlet");

        Tomcat.addServlet(context, "GestisciChiaveValoreServlet", new GestisciChiaveValoreServlet());
        context.addServletMappingDecoded("/gestisci-dettaglio", "GestisciChiaveValoreServlet");

        Tomcat.addServlet(context, "LoginServlet", new LoginServlet());
        context.addServletMappingDecoded("/login", "LoginServlet");

        Tomcat.addServlet(context, "LogoutServlet", new LogoutServlet());
        context.addServletMappingDecoded("/logout", "LogoutServlet");

        Tomcat.addServlet(context, "ModificaChiaveValoreServlet", new ModificaChiaveValoreServlet());
        context.addServletMappingDecoded("/modifica-dettaglio", "ModificaChiaveValoreServlet");

        Tomcat.addServlet(context, "ModificaOggettoServlet", new ModificaOggettoServlet());
        context.addServletMappingDecoded("/modifica-oggetto", "ModificaOggettoServlet");

        Tomcat.addServlet(context, "NuovaChiaveValoreServlet", new NuovaChiaveValoreServlet());
        context.addServletMappingDecoded("/nuova-chiave-valore", "NuovaChiaveValoreServlet");

        Tomcat.addServlet(context, "NuovoOggettoServlet", new NuovoOggettoServlet());
        context.addServletMappingDecoded("/nuovo-oggetto", "NuovoOggettoServlet");

        Tomcat.addServlet(context, "OAuth2CallbackServlet", new OAuth2CallbackServlet());
        context.addServletMappingDecoded("/oauth2callback", "OAuth2CallbackServlet");

        Tomcat.addServlet(context, "OAuth2LoginRedirectServlet", new OAuth2LoginRedirectServlet());
        context.addServletMappingDecoded("/oauth2login", "OAuth2LoginRedirectServlet");

        Tomcat.addServlet(context, "OggettoServlet", new OggettoServlet());
        context.addServletMappingDecoded("/oggetti", "OggettoServlet");

        Tomcat.addServlet(context, "ProfiloServlet", new ProfiloServlet());
        context.addServletMappingDecoded("/profilo", "ProfiloServlet");

        Tomcat.addServlet(context, "RootServlet", new DashboardServlet());
        context.addServletMappingDecoded("/", "RootServlet");


        System.out.println("DEBUG: Starting Tomcat..."); // <--- AGGIUNGI QUESTO LOG
        tomcat.start();
        System.out.println("DEBUG: Tomcat started."); // <--- AGGIUNGI QUESTO LOG
        tomcat.getServer().await();
        System.out.println("DEBUG: Tomcat shut down."); // <--- AGGIUNGI QUESTO LOG
    }
}