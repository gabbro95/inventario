package com.gabbro95.inventario.dao;

import com.gabbro95.inventario.model.Oggetto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OggettoDAO extends BaseDAO {
	
	
    public List<Oggetto> trovaPerContenitoreId(int contenitoreId) { // <-- MODIFICA: Nome del metodo corretto
         return execute(conn -> {
            List<Oggetto> oggetti = new ArrayList<>();
            String sql = "SELECT * FROM oggetto WHERE contenitore_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, contenitoreId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Oggetto o = new Oggetto();
                        o.setId(rs.getInt("id"));
                        o.setNome(rs.getString("nome"));
                        o.setNumero(rs.getInt("numero"));
                        o.setDataInserimento(rs.getDate("data_inserimento").toLocalDate());
                        o.setContenitoreId(rs.getInt("contenitore_id"));
                        o.setSogliaMinima(rs.getInt("soglia_minima"));
                        oggetti.add(o);
                    }
                }
            }
            return oggetti;
        });
    }

    public Oggetto getOggettoById(int oggettoId) {
        return execute(conn -> {
            String sql = "SELECT * FROM oggetto WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, oggettoId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Oggetto o = new Oggetto();
                        o.setId(rs.getInt("id"));
                        o.setNome(rs.getString("nome"));
                        o.setNumero(rs.getInt("numero"));
                        o.setDataInserimento(rs.getDate("data_inserimento").toLocalDate());
                        o.setContenitoreId(rs.getInt("contenitore_id"));
                        o.setSogliaMinima(rs.getInt("soglia_minima"));
                        return o;
                    }
                }
            }
            return null;
        });
    }
    
    public void creaOggetto(Oggetto oggetto) {
        execute(conn -> {
            String sql = "INSERT INTO oggetto (nome, numero, data_inserimento, contenitore_id, soglia_minima) VALUES (?, ?, CURDATE(), ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, oggetto.getNome());
                stmt.setInt(2, oggetto.getNumero());
                stmt.setInt(3, oggetto.getContenitoreId());
                stmt.setInt(4, oggetto.getSogliaMinima()); // <-- Salva la soglia
                stmt.executeUpdate();
            }
            return null;
        });
    }

    public void updateOggetto(Oggetto oggetto) {
        execute(conn -> {
            String sql = "UPDATE oggetto SET nome = ?, numero = ?, soglia_minima = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, oggetto.getNome());
                stmt.setInt(2, oggetto.getNumero());
                stmt.setInt(3, oggetto.getSogliaMinima());
                stmt.setInt(4, oggetto.getId());
                stmt.executeUpdate();
            }
            return null;
        });
    }
    
    public void deleteOggetto(int oggettoId) {
        execute(conn -> {
            // NOTA IMPORTANTE: Questa query funziona correttamente se hai impostato
            // la chiave esterna nella tabella 'chiave_valore' con "ON DELETE CASCADE".
            // In questo modo, quando un oggetto viene eliminato, il database elimina
            // automaticamente tutti i dettagli (chiavi-valore) a lui associati.
            // Se non l'hai fatto, dovresti prima eliminare le chiavi-valore e poi l'oggetto
            // all'interno di una transazione.
            String sql = "DELETE FROM oggetto WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, oggettoId);
                stmt.executeUpdate();
            }
            return null;
        });
    }
}