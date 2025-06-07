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

public class Main {
    public static void main(String[] args) throws Exception {
        // Usa la porta fornita da Heroku, o la 8080 di default
        String port = System.getenv("PORT");
        if (port == null || port.isEmpty()) {
            port = "8080";
        }

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.parseInt(port));
        tomcat.getConnector(); // Necessario per inizializzare il connettore

        // Il contesto è "/inventario" come definito dal tuo URL su Heroku
        // La baseDir è dove si trovano le tue risorse web (JSP, CSS, JS)
        // Per Heroku, il percorso corretto è la directory di lavoro corrente dove il JAR è estratto.
        // `.` indica la directory corrente del processo, dove il buildpack Heroku posiziona il tuo webapp.
        String webappDir = new File("src/main/webapp").getAbsolutePath();
        Context context = tomcat.addWebapp("/inventario", webappDir); //
        System.out.println("Configuring webapp with basedir: " + webappDir + " at context /inventario"); //

        // *** Mappatura Programmatica di TUTTI i Servlet ***

        // CarrelloServlet
        Tomcat.addServlet(context, "CarrelloServlet", new CarrelloServlet()); //
        context.addServletMappingDecoded("/carrello", "CarrelloServlet"); //

        // ChiaveValoreServlet
        Tomcat.addServlet(context, "ChiaveValoreServlet", new ChiaveValoreServlet()); //
        context.addServletMappingDecoded("/chiave-valore", "ChiaveValoreServlet"); //

        // ContenitoreServlet
        Tomcat.addServlet(context, "ContenitoreServlet", new ContenitoreServlet()); //
        context.addServletMappingDecoded("/contenitore", "ContenitoreServlet"); //

        // DashboardServlet
        Tomcat.addServlet(context, "DashboardServlet", new DashboardServlet()); //
        context.addServletMappingDecoded("/dashboard", "DashboardServlet"); //

        // GestisciChiaveValoreServlet
        Tomcat.addServlet(context, "GestisciChiaveValoreServlet", new GestisciChiaveValoreServlet()); //
        context.addServletMappingDecoded("/gestisci-dettaglio", "GestisciChiaveValoreServlet"); //

        // LoginServlet
        Tomcat.addServlet(context, "LoginServlet", new LoginServlet()); //
        context.addServletMappingDecoded("/login", "LoginServlet"); //

        // LogoutServlet
        Tomcat.addServlet(context, "LogoutServlet", new LogoutServlet()); //
        context.addServletMappingDecoded("/logout", "LogoutServlet"); //

        // ModificaChiaveValoreServlet
        Tomcat.addServlet(context, "ModificaChiaveValoreServlet", new ModificaChiaveValoreServlet()); //
        context.addServletMappingDecoded("/modifica-dettaglio", "ModificaChiaveValoreServlet"); //

        // ModificaOggettoServlet
        Tomcat.addServlet(context, "ModificaOggettoServlet", new ModificaOggettoServlet()); //
        context.addServletMappingDecoded("/modifica-oggetto", "ModificaOggettoServlet"); //

        // NuovoOggettoServlet
        Tomcat.addServlet(context, "NuovoOggettoServlet", new NuovoOggettoServlet()); //
        context.addServletMappingDecoded("/nuovo-oggetto", "NuovoOggettoServlet"); //

        // OAuth2CallbackServlet
        Tomcat.addServlet(context, "OAuth2CallbackServlet", new OAuth2CallbackServlet()); //
        context.addServletMappingDecoded("/oauth2callback", "OAuth2CallbackServlet"); //

        // OAuth2LoginRedirectServlet (QUESTO ERA IL PROBLEMA PRINCIPALE)
        Tomcat.addServlet(context, "OAuth2LoginRedirectServlet", new OAuth2LoginRedirectServlet()); //
        context.addServletMappingDecoded("/oauth2login", "OAuth2LoginRedirectServlet"); //

        // OggettoServlet
        Tomcat.addServlet(context, "OggettoServlet", new OggettoServlet()); //
        context.addServletMappingDecoded("/oggetti", "OggettoServlet"); //

        // ProfiloServlet
        Tomcat.addServlet(context, "ProfiloServlet", new ProfiloServlet()); //
        context.addServletMappingDecoded("/profilo", "ProfiloServlet"); //

        // IMPORTANTE: Mappa la root del contesto ("/") alla tua DashboardServlet o a una HomeServlet
        // Se la tua applicazione deve rispondere anche a "/inventario" (senza niente dopo),
        // devi mappare la root del contesto a un servlet.
        // Ad esempio, la DashboardServlet è un buon candidato per la pagina principale dopo il login.
        // Se hai una HomeServlet che vuoi che gestisca l'URL "/inventario", mappala qui.
        // In questo caso, suppongo che l'URL principale dopo il login sia "/dashboard".
        // Se vai su /inventario, la tua index.jsp probabilmente reindirizza a /login o /dashboard.
        // Per assicurarti che anche l'URL base funzioni correttamente:
        // Potresti anche voler mappare un servlet alla root del contesto "/"
        // Tomcat.addServlet(context, "DefaultHome", new DashboardServlet()); // Puoi usare Dashboard o una HomeServlet
        // context.addServletMappingDecoded("/", "DefaultHome"); // Esempio: mappa "/" a DashboardServlet

        // Se hai un filtro (es. per l'autenticazione), lo registreresti qui in modo simile:
        // FilterDef filterDef = new FilterDef();
        // filterDef.setFilterName("AuthenticationFilter");
        // filterDef.setFilterClass("com.gabbro95.inventario.filter.AuthenticationFilter");
        // context.addFilterDef(filterDef);
        // FilterMap filterMap = new FilterMap();
        // filterMap.setFilterName("AuthenticationFilter");
        // filterMap.addURLPattern("/*"); // Applica a tutti gli URL
        // context.addFilterMap(filterMap);


        tomcat.start(); //
        tomcat.getServer().await(); //
    }
}