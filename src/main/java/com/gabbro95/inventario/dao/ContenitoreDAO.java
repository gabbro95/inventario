package com.gabbro95.inventario.dao;

import com.gabbro95.inventario.model.Contenitore;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ContenitoreDAO extends BaseDAO {

    /**
     * Recupera tutti i contenitori di un utente specifico usando il suo ID.
     *
     * @param utenteId L'ID dell'utente di cui recuperare i contenitori.
     * @return Una lista di oggetti Contenitore appartenenti all'utente.
     */
    public List<Contenitore> getContenitoriPerUtente(int utenteId) {
        return execute(conn -> {
            List<Contenitore> contenitori = new ArrayList<>();
            String sql = "SELECT * FROM contenitore WHERE utente_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, utenteId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Contenitore c = new Contenitore();
                        c.setId(rs.getInt("id"));
                        c.setNome(rs.getString("nome"));
                        c.setUtenteId(rs.getInt("utente_id"));
                        contenitori.add(c);
                    }
                }
            }
            return contenitori;
        });
    }

    /**
     * Recupera un contenitore specifico tramite il suo ID, verificando che appartenga all'utente corrente.
     * Questo garantisce che un utente possa accedere solo ai propri contenitori.
     *
     * @param contenitoreId L'ID del contenitore da recuperare.
     * @param utenteId L'ID dell'utente proprietario del contenitore.
     * @return L'oggetto Contenitore se trovato e di proprietà dell'utente, altrimenti null.
     */
    public Contenitore getContenitoreById(int contenitoreId, int utenteId) {
        return execute(conn -> {
            String sql = "SELECT * FROM contenitore WHERE id = ? AND utente_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, contenitoreId);
                stmt.setInt(2, utenteId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Contenitore c = new Contenitore();
                        c.setId(rs.getInt("id"));
                        c.setNome(rs.getString("nome"));
                        c.setUtenteId(rs.getInt("utente_id"));
                        return c;
                    }
                }
            }
            return null;
        });
    }

    /**
     * Crea un nuovo contenitore associandolo a un utente tramite il suo ID.
     *
     * @param nome Il nome del nuovo contenitore.
     * @param utenteId L'ID dell'utente a cui associare il contenitore.
     */
    public void creaContenitore(String nome, int utenteId) {
        execute(conn -> {
            String sql = "INSERT INTO contenitore (nome, utente_id) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nome);
                stmt.setInt(2, utenteId);
                stmt.executeUpdate();
            }
            return null;
        });
    }

    /**
     * Elimina un contenitore, verificando la proprietà tramite l'ID dell'utente.
     *
     * @param contenitoreId L'ID del contenitore da eliminare.
     * @param utenteId L'ID dell'utente proprietario del contenitore.
     */
    public void eliminaContenitore(int contenitoreId, int utenteId) {
        execute(conn -> {
            String sql = "DELETE FROM contenitore WHERE id = ? AND utente_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, contenitoreId);
                stmt.setInt(2, utenteId);
                stmt.executeUpdate();
            }
            return null;
        });
    }

    /**
     * Aggiorna il nome di un contenitore, verificando la proprietà tramite l'ID dell'utente.
     *
     * @param contenitoreId L'ID del contenitore da aggiornare.
     * @param nuovoNome Il nuovo nome da assegnare al contenitore.
     * @param utenteId L'ID dell'utente proprietario del contenitore.
     */
    public void updateContenitore(int contenitoreId, String nuovoNome, int utenteId) {
        execute(conn -> {
            String sql = "UPDATE contenitore SET nome = ? WHERE id = ? AND utente_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nuovoNome);
                stmt.setInt(2, contenitoreId);
                stmt.setInt(3, utenteId);
                stmt.executeUpdate();
            }
            return null;
        });
    }
}