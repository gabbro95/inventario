// File: src/com/tuoapp/utils/EmailUtils.java
package com.gabbro95.inventario.utils;

public class EmailUtils {

    /**
     * Normalizza un indirizzo email, rimuovendo i punti dagli username di Gmail.
     *
     * @param email L'email da normalizzare.
     * @return L'email normalizzata.
     */
    public static String normalizeGmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return email;
        }

        // Converte in minuscolo e toglie spazi
        String trimmedEmail = email.toLowerCase().trim();
        
        if (!trimmedEmail.contains("@")) {
            return trimmedEmail; // Non è un'email valida, la restituiamo com'è
        }

        String[] parts = trimmedEmail.split("@", 2);
        String username = parts[0];
        String domain = parts[1];

        // La normalizzazione si applica solo a gmail.com e googlemail.com
        if ("gmail.com".equals(domain) || "googlemail.com".equals(domain)) {
            // Rimuove tutti i punti dall'username
            username = username.replace(".", "");
            
            // Gestione opzionale degli alias con '+'
            // Esempio: "nomeutente+test@gmail.com" diventa "nomeutente@gmail.com"
            if (username.contains("+")) {
                username = username.substring(0, username.indexOf('+'));
            }
        }

        return username + "@" + domain;
    }
}