package com.terragrouplabs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import jakarta.annotation.PostConstruct;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@Profile("prod")
public class DatabaseConfig {
    
    @Value("${spring.datasource.url:#{null}}")
    private String dbUrl;
    
    @Value("${CLEARDB_DATABASE_URL:#{null}}")
    private String clearDbUrl;
    
    @PostConstruct
    public void logDatabaseUrl() {
        System.out.println("Database URL from property: " + dbUrl);
        System.out.println("ClearDB URL from env: " + clearDbUrl);
    }
    
    @Bean
    public DataSource dataSource() throws URISyntaxException {
        HikariConfig config = new HikariConfig();
        
        // Herokuの環境変数CLEARDB_DATABASE_URLがある場合はそちらを優先
        if (clearDbUrl != null && !clearDbUrl.isEmpty()) {
            URI dbUri = new URI(clearDbUrl);
            
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:mysql://" + dbUri.getHost() + dbUri.getPath() + "?reconnect=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Tokyo";
            
            config.setJdbcUrl(dbUrl);
            config.setUsername(username);
            config.setPassword(password);
        } 
        // spring.datasource.urlがある場合はそちらを使用
        else if (dbUrl != null && !dbUrl.isEmpty()) {
            config.setJdbcUrl(dbUrl);
        } 
        // どちらもない場合はエラー
        else {
            throw new RuntimeException("Database connection URL not found in environment variables");
        }
        
        // その他のHikariCP設定
        config.setMaximumPoolSize(5); // Herokuの無料プランでは接続数に制限があるため小さな値に設定
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        
        return new HikariDataSource(config);
    }
}
