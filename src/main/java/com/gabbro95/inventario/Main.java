// File: src/main/java/com/gabbro95/inventario/Main.java
package com.gabbro95.inventario;

import org.apache.catalina.startup.Tomcat;
import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        // Usa la porta fornita da Heroku, o la 8080 di default
        String port = System.getenv("PORT");
        if (port == null || port.isEmpty()) {
            port = "8080";
        }

        // Trova il percorso della nostra webapp
        String webappDirPath = new File("src/main/webapp/").getAbsolutePath();

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.parseInt(port));
        tomcat.getConnector(); // Necessario per inizializzare il connettore

        // Dice a Tomcat dove trovare i file JSP e le altre risorse web
        tomcat.addWebapp("/inventario", webappDirPath);
        System.out.println("Configuring app with basedir: " + webappDirPath);

        tomcat.start();
        tomcat.getServer().await();
    }
}