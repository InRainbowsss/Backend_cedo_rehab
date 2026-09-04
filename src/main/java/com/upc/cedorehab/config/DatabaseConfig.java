package com.upc.cedorehab.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;

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
                String username = (userInfo != null && userInfo.contains(":")) ? userInfo.split(":")[0] : "";
                String password = (userInfo != null && userInfo.contains(":")) ? userInfo.split(":")[1] : "";
                int port = dbUri.getPort() > 0 ? dbUri.getPort() : 5432;
                String host = dbUri.getHost();
                String path = dbUri.getPath();
                String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + path;

                config.setJdbcUrl(jdbcUrl);
                config.setUsername(username);
                config.setPassword(password);
            } catch (Exception e) {
                config.setJdbcUrl("jdbc:" + databaseUrl);
                if (springDatasourceUsername != null && !springDatasourceUsername.isBlank()) {
                    config.setUsername(springDatasourceUsername);
                }
                if (springDatasourcePassword != null && !springDatasourcePassword.isBlank()) {
                    config.setPassword(springDatasourcePassword);
                }
            }
        } else {
            config.setJdbcUrl(databaseUrl != null && !databaseUrl.isBlank() ? databaseUrl : "jdbc:postgresql://localhost:5432/db_cedo_rehab");
            config.setUsername(springDatasourceUsername != null && !springDatasourceUsername.isBlank() ? springDatasourceUsername : "postgres");
            config.setPassword(springDatasourcePassword != null && !springDatasourcePassword.isBlank() ? springDatasourcePassword : "20092002doge");
        }

        config.setDriverClassName("org.postgresql.Driver");
        return new HikariDataSource(config);
    }
}
