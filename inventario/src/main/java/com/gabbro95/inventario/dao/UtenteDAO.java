package com.gabbro95.inventario.dao;

import com.gabbro95.inventario.model.Utente;

import java.sql.*;
import java.time.LocalDate;

public class UtenteDAO extends BaseDAO {

    public Utente trovaPerEmail(String email) {
        return execute(conn -> {
            String sql = "SELECT * FROM utente WHERE email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Utente u = new Utente();
                        u.setId(rs.getInt("id"));
                        u.setEmail(rs.getString("email"));
                        u.setNome(rs.getString("nome"));
                        u.setImmagineProfilo(rs.getString("immagine_profilo"));
                        u.setDataCreazione(rs.getDate("data_creazione").toLocalDate());
                        return u;
                    }
                }
            }
            return null;
        });
    }

    public void salvaOUAggiorna(Utente utente) {
        Utente esistente = trovaPerEmail(utente.getEmail());
        if (esistente != null) return;

        execute(conn -> {
            String sql = "INSERT INTO utente (email, nome, immagine_profilo, data_creazione) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, utente.getEmail());
                stmt.setString(2, utente.getNome());
                stmt.setString(3, utente.getImmagineProfilo());
                stmt.setDate(4, Date.valueOf(utente.getDataCreazione()));
                stmt.executeUpdate();
            }
            return null;
        });
    }
    
    public void inserisciOaggiornaUtente(Utente utente) {
        execute(conn -> {
            String sql = """
                    INSERT INTO utente (email, nome, immagine_profilo, data_creazione)
                    VALUES (?, ?, ?, ?)
                    ON DUPLICATE KEY UPDATE nome = VALUES(nome), immagine_profilo = VALUES(immagine_profilo)
                    """;
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, utente.getEmail());
                stmt.setString(2, utente.getNome());
                stmt.setString(3, utente.getImmagineProfilo());
                stmt.setDate(4, Date.valueOf(utente.getDataCreazione()));
                stmt.executeUpdate();
            }
            return null;
        });
    }

}
