package com.gabbro95.inventario.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class DBManager {
    private static final Logger logger = LoggerFactory.getLogger(DBManager.class);
    private static HikariDataSource dataSource;

    static {
        try {
            // Caricamento esplicito del driver MySQL (anche se con JDBC 4+ spesso non è necessario)
            Class.forName("com.mysql.cj.jdbc.Driver");

            HikariConfig config = new HikariConfig();
            // Recupera le credenziali dalle variabili d'ambiente
            // Assicurati che queste variabili siano impostate nel tuo ambiente di deploy
            String dbUrl = System.getenv("DB_URL");
            String dbUser = System.getenv("DB_USER");
            String dbPassword = System.getenv("DB_PASSWORD");

            if (dbUrl == null || dbUser == null || dbPassword == null) {
                logger.error("Variabili d'ambiente DB_URL, DB_USER, o DB_PASSWORD non impostate!");
                // Potresti voler lanciare un'eccezione qui o gestire diversamente
                // Per ora, proviamo con valori di default SE l'ambiente non li fornisce (NON RACCOMANDATO PER PRODUZIONE)
                // Questo è solo un fallback per lo sviluppo locale se le env var non sono settate.
                // In produzione, le env var DEVONO essere impostate.
                dbUrl = dbUrl == null ? "jdbc:mysql://localhost:3306/inventario_db?useSSL=false&serverTimezone=UTC" : dbUrl;
                dbUser = dbUser == null ? "root" : dbUser; // Cambia con il tuo utente DB locale
                dbPassword = dbPassword == null ? "password" : dbPassword; // Cambia con la tua password DB locale
                logger.warn("Utilizzo di credenziali DB di fallback per lo sviluppo. Assicurarsi che le variabili d'ambiente siano impostate in produzione.");
            }
            
            logger.info("Configurazione HikariCP con URL: {}", dbUrl);

            config.setJdbcUrl(dbUrl);
            config.setUsername(dbUser);
            config.setPassword(dbPassword);

            // Impostazioni raccomandate per HikariCP
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            config.addDataSourceProperty("useServerPrepStmts", "true");
            config.addDataSourceProperty("useLocalSessionState", "true");
            config.addDataSourceProperty("rewriteBatchedStatements", "true");
            config.addDataSourceProperty("cacheResultSetMetadata", "true");
            config.addDataSourceProperty("cacheServerConfiguration", "true");
            config.addDataSourceProperty("elideSetAutoCommits", "true");
            config.addDataSourceProperty("maintainTimeStats", "false");

            config.setMinimumIdle(5); // Numero minimo di connessioni idle
            config.setMaximumPoolSize(20); // Numero massimo di connessioni attive
            config.setConnectionTimeout(30000); // Timeout per ottenere una connessione (ms)
            config.setIdleTimeout(600000); // Timeout per connessioni idle (ms)
            config.setMaxLifetime(1800000); // Massimo tempo di vita di una connessione (ms)

            dataSource = new HikariDataSource(config);
            logger.info("HikariCP DataSource inizializzato con successo.");

        } catch (ClassNotFoundException e) {
            logger.error("Driver MySQL JDBC non trovato.", e);
            throw new RuntimeException("Driver MySQL non trovato", e);
        } catch (Exception e) {
            logger.error("Errore durante l'inizializzazione di HikariCP DataSource.", e);
            throw new RuntimeException("Errore inizializzazione DataSource", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            logger.error("HikariCP DataSource non è stato inizializzato correttamente.");
            throw new SQLException("DataSource non inizializzato.");
        }
        logger.debug("Richiesta una connessione dal pool.");
        Connection conn = dataSource.getConnection();
        logger.debug("Connessione ottenuta: {}", conn);
        return conn;
    }

    public static <T> T withConnection(ConnectionFunction<T> function) {
        // Il try-with-resources sulla connessione è ancora una buona pratica
        // anche se HikariCP gestisce il ritorno della connessione al pool al posto della chiusura fisica.
        try (Connection conn = getConnection()) {
            return function.apply(conn);
        } catch (SQLException e) {
            logger.error("Errore SQL durante l'esecuzione dell'operazione DB.", e);
            // Potresti voler propagare un'eccezione personalizzata più specifica del dominio
            throw new RuntimeException("Errore durante l'operazione sul DB", e);
        }
    }

    @FunctionalInterface
    public interface ConnectionFunction<T> {
        T apply(Connection conn) throws SQLException;
    }

    // Metodo per chiudere il DataSource quando l'applicazione si ferma
    // Dovrebbe essere chiamato da un ServletContextListener#contextDestroyed
    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            logger.info("Chiusura di HikariCP DataSource...");
            dataSource.close();
            logger.info("HikariCP DataSource chiuso.");
        }
    }
}
