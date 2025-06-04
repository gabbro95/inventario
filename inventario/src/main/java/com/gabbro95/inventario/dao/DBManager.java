package com.gabbro95.inventario.dao;

import java.sql.*;

public class DBManager {
    private static final String URL = System.getenv("DB_URL");
    private static final String USER = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static <T> T withConnection(ConnectionFunction<T> function) {
        try (Connection conn = getConnection()) {
            return function.apply(conn);
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante la connessione al DB", e);
        }
    }

    @FunctionalInterface
    public interface ConnectionFunction<T> {
        T apply(Connection conn) throws SQLException;
    }
}
