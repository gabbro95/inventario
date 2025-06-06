package com.gabbro95.inventario.dao;

import com.gabbro95.inventario.model.Contenitore;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ContenitoreDAO extends BaseDAO {

    /**
     * Recupera tutti i contenitori di un utente specifico usando il suo ID.
     */
    public List<Contenitore> getContenitoriPerUtente(int utenteId) { // <-- MODIFICA: accetta int utenteId
        return execute(conn -> {
            List<Contenitore> contenitori = new ArrayList<>();
            String sql = "SELECT * FROM contenitore WHERE utente_id = ?"; // <-- MODIFICA: usa utente_id
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, utenteId); // <-- MODIFICA: usa setInt
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Contenitore c = new Contenitore();
                        c.setId(rs.getInt("id"));
                        c.setNome(rs.getString("nome"));
                        c.setUtenteId(rs.getInt("utente_id")); // <-- MODIFICA: legge utente_id dal DB
                        contenitori.add(c);
                    }
                }
            }
            return contenitori;
        });
    }

    /**
     * Recupera un contenitore specifico, verificando che appartenga all'utente corretto tramite ID.
     */
    public Contenitore getContenitoreById(int contenitoreId, int utenteId) { // <-- MODIFICA: accetta int utenteId
        return execute(conn -> {
            String sql = "SELECT * FROM contenitore WHERE id = ? AND utente_id = ?"; // <-- MODIFICA: usa utente_id
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, contenitoreId);
                stmt.setInt(2, utenteId); // <-- MODIFICA: usa setInt per la sicurezza
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Contenitore c = new Contenitore();
                        c.setId(rs.getInt("id"));
                        c.setNome(rs.getString("nome"));
                        c.setUtenteId(rs.getInt("utente_id")); // <-- MODIFICA: legge utente_id
                        return c;
                    }
                }
            }
            return null;
        });
    }

    /**
     * Crea un nuovo contenitore associandolo a un utente tramite il suo ID.
     */
    public void creaContenitore(String nome, int utenteId) { // <-- MODIFICA: accetta int utenteId
        execute(conn -> {
            String sql = "INSERT INTO contenitore (nome, utente_id) VALUES (?, ?)"; // <-- MODIFICA: usa utente_id
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nome);
                stmt.setInt(2, utenteId); // <-- MODIFICA: usa setInt
                stmt.executeUpdate();
            }
            return null;
        });
    }

    /**
     * Elimina un contenitore, verificando la proprietà tramite l'ID dell'utente.
     */
    public void eliminaContenitore(int contenitoreId, int utenteId) { // <-- MODIFICA: accetta int utenteId
        execute(conn -> {
            String sql = "DELETE FROM contenitore WHERE id = ? AND utente_id = ?"; // <-- MODIFICA: usa utente_id
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, contenitoreId);
                stmt.setInt(2, utenteId); // <-- MODIFICA: usa setInt
                stmt.executeUpdate();
            }
            return null;
        });
    }

    /**
     * Aggiorna il nome di un contenitore, verificando la proprietà tramite l'ID dell'utente.
     */
    public void updateContenitore(int contenitoreId, String nuovoNome, int utenteId) { // <-- MODIFICA: accetta int utenteId
        execute(conn -> {
            String sql = "UPDATE contenitore SET nome = ? WHERE id = ? AND utente_id = ?"; // <-- MODIFICA: usa utente_id
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nuovoNome);
                stmt.setInt(2, contenitoreId);
                stmt.setInt(3, utenteId); // <-- MODIFICA: usa setInt
                stmt.executeUpdate();
            }
            return null;
        });
    }
}