package com.upc.cedorehab.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.url:}")
    private String springDatasourceUrl;

    @Value("${spring.datasource.username:}")
    private String springDatasourceUsername;

    @Value("${spring.datasource.password:}")
    private String springDatasourcePassword;

    @Bean
    @Primary
    public DataSource dataSource() {
        String databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl == null || databaseUrl.isBlank()) {
            databaseUrl = System.getenv("SPRING_DATASOURCE_URL");
        }
        if (databaseUrl == null || databaseUrl.isBlank()) {
            databaseUrl = springDatasourceUrl;
        }

        HikariConfig config = new HikariConfig();

        if (databaseUrl != null && (databaseUrl.startsWith("postgres://") || databaseUrl.startsWith("postgresql://"))) {
            try {
                URI dbUri = new URI(databaseUrl);
                String userInfo = dbUri.getUserInfo();
                String username = "";
                String password = "";
                if (userInfo != null) {
                    int colonIdx = userInfo.indexOf(':');
                    if (colonIdx != -1) {
                        username = URLDecoder.decode(userInfo.substring(0, colonIdx), StandardCharsets.UTF_8);
                        password = URLDecoder.decode(userInfo.substring(colonIdx + 1), StandardCharsets.UTF_8);
                    } else {
                        username = URLDecoder.decode(userInfo, StandardCharsets.UTF_8);
                    }
                }
                int port = dbUri.getPort() > 0 ? dbUri.getPort() : 5432;
                String host = dbUri.getHost();
                String path = dbUri.getPath();
                String query = dbUri.getQuery();
                String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + path + (query != null && !query.isBlank() ? "?" + query : "");

                config.setJdbcUrl(jdbcUrl);
                if (!username.isBlank()) config.setUsername(username);
                if (!password.isBlank()) config.setPassword(password);
                config.setDriverClassName("org.postgresql.Driver");
                System.out.println("✅ Conectando a PostgreSQL remoto: " + host + ":" + port + path);
                return new HikariDataSource(config);
            } catch (Exception e) {
                System.err.println("⚠️ Error al parsear DATABASE_URL, intentando formato directo: " + e.getMessage());
            }
        }

        // Si es una URL JDBC explícita pero no localhost
        if (databaseUrl != null && databaseUrl.startsWith("jdbc:") && !databaseUrl.contains("localhost")) {
            config.setJdbcUrl(databaseUrl);
            if (springDatasourceUsername != null && !springDatasourceUsername.isBlank()) {
                config.setUsername(springDatasourceUsername);
            }
            if (springDatasourcePassword != null && !springDatasourcePassword.isBlank()) {
                config.setPassword(springDatasourcePassword);
            }
            config.setDriverClassName("org.postgresql.Driver");
            return new HikariDataSource(config);
        }

        // Si es localhost o no está configurado, verificar si PostgreSQL local está activo
        if (isLocalPostgresActive()) {
            System.out.println("✅ PostgreSQL local detectado en localhost:5432");
            config.setJdbcUrl(databaseUrl != null && !databaseUrl.isBlank() ? databaseUrl : "jdbc:postgresql://localhost:5432/db_cedo_rehab");
            config.setUsername(springDatasourceUsername != null && !springDatasourceUsername.isBlank() ? springDatasourceUsername : "postgres");
            config.setPassword(springDatasourcePassword != null && !springDatasourcePassword.isBlank() ? springDatasourcePassword : "20092002doge");
            config.setDriverClassName("org.postgresql.Driver");
            return new HikariDataSource(config);
        }

        // Fallback resiliente para despliegues en la nube sin base de datos externa vinculada
        System.out.println("⚠️ PostgreSQL no disponible en localhost:5432 ni en DATABASE_URL.");
        System.out.println("🚀 Inicializando base de datos embebida persistente H2 (Modo PostgreSQL)...");
        try {
            new File("./data").mkdirs();
        } catch (Exception ignored) {}
        config.setJdbcUrl("jdbc:h2:file:./data/cedodb;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH;AUTO_SERVER=TRUE");
        config.setDriverClassName("org.h2.Driver");
        config.setUsername("sa");
        config.setPassword("");
        return new HikariDataSource(config);
    }

    private boolean isLocalPostgresActive() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("127.0.0.1", 5432), 1000);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
