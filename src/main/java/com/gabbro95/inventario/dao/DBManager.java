package com.gabbro95.inventario.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.net.URI; // Import aggiunto
import java.net.URISyntaxException; // Import aggiunto

public class DBManager {
    private static final Logger logger = LoggerFactory.getLogger(DBManager.class);
    private static HikariDataSource dataSource;

    static {
        initDataSource();
    }

    private static void initDataSource() {
        try {
            // Caricamento esplicito del driver MySQL (anche se con JDBC 4+ spesso non è necessario)
            // È buona pratica per sicurezza in ambienti come Heroku.
            Class.forName("com.mysql.cj.jdbc.Driver");

            HikariConfig config = new HikariConfig();
            
            // *** INIZIO MODIFICA CRITICA PER HEROKU ***
            String herokuDbUrl = System.getenv("JAWSDB_URL"); // Per JawsDB MySQL
            // String herokuDbUrl = System.getenv("CLEARDB_DATABASE_URL"); // Se usi ClearDB MySQL
            // String herokuDbUrl = System.getenv("DATABASE_URL"); // Se usi un add-on PostgreSQL (tipo Heroku Postgres)
            
            if (herokuDbUrl != null && !herokuDbUrl.isEmpty()) {
                // Heroku DB URL trovato, usiamo quello
                try {
                    URI dbUri = new URI(herokuDbUrl);
                    String username = dbUri.getUserInfo() != null ? dbUri.getUserInfo().split(":")[0] : null;
                    String password = dbUri.getUserInfo() != null ? dbUri.getUserInfo().split(":")[1] : null;
                    String host = dbUri.getHost();
                    int port = dbUri.getPort();
                    String dbName = dbUri.getPath().substring(1); // Rimuove lo slash iniziale
                    String query = dbUri.getQuery(); // Per parametri come useSSL, serverTimezone

                    // Costruiamo l'URL JDBC per MySQL
                    StringBuilder jdbcUrlBuilder = new StringBuilder("jdbc:mysql://")
                                                        .append(host);
                    if (port != -1) { // Se la porta è specificata
                        jdbcUrlBuilder.append(":").append(port);
                    }
                    jdbcUrlBuilder.append("/").append(dbName);

                    if (query != null && !query.isEmpty()) {
                        // Aggiungiamo i parametri di query esistenti
                        jdbcUrlBuilder.append("?").append(query);
                    } else {
                        // Aggiungiamo parametri standard se non ci sono già per evitare problemi
                        jdbcUrlBuilder.append("?useSSL=true&requireSSL=true&serverTimezone=UTC"); 
                    }
                    // Aggiungiamo useSSL e requireSSL se non ci sono, cruciale per Heroku
                    if (!jdbcUrlBuilder.toString().contains("useSSL")) {
                        jdbcUrlBuilder.append("&useSSL=true");
                    }
                    if (!jdbcUrlBuilder.toString().contains("requireSSL")) {
                        jdbcUrlBuilder.append("&requireSSL=true");
                    }
                    if (!jdbcUrlBuilder.toString().contains("serverTimezone")) {
                        jdbcUrlBuilder.append("&serverTimezone=UTC");
                    }


                    config.setJdbcUrl(jdbcUrlBuilder.toString());
                    config.setUsername(username);
                    config.setPassword(password);

                    logger.info("Configurazione HikariCP per Heroku con URL: {}", config.getJdbcUrl());

                } catch (URISyntaxException e) {
                    logger.error("Errore nel parsing dell'URL del database di Heroku: {}", herokuDbUrl, e);
                    throw new RuntimeException("Errore configurazione database Heroku", e);
                }
            } else {
                // Heroku DB URL non trovato, usiamo configurazione locale di fallback
                logger.warn("Variabile d'ambiente JAWSDB_URL (o equivalente) non trovata. Utilizzo di credenziali DB di fallback per lo sviluppo locale.");
                config.setJdbcUrl("jdbc:mysql://localhost:3306/inventario_db?useSSL=false&serverTimezone=UTC"); // Cambia con il tuo DB locale
                config.setUsername("root"); // Cambia con il tuo utente DB locale
                config.setPassword("password"); // Cambia con la tua password DB locale
            }
            // *** FINE MODIFICA CRITICA PER HEROKU ***

            // Impostazioni raccomandate per HikariCP (rimangono invariate e sono buone)
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
        try (Connection conn = getConnection()) {
            return function.apply(conn);
        } catch (SQLException e) {
            logger.error("Errore SQL durante l'esecuzione dell'operazione DB.", e);
            throw new RuntimeException("Errore durante l'operazione sul DB", e);
        }
    }

    @FunctionalInterface
    public interface ConnectionFunction<T> {
        T apply(Connection conn) throws SQLException;
    }

    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            logger.info("Chiusura di HikariCP DataSource...");
            dataSource.close();
            logger.info("HikariCP DataSource chiuso.");
        }
    }
}