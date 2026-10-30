
package com.bankofspain.bankofspain;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DatabaseConnectionTest {

    @Bean
    ApplicationRunner verifyDatabase(JdbcTemplate jdbcTemplate) {
        return args -> {
            String database = jdbcTemplate.queryForObject(
                    "SELECT DATABASE()", String.class
            );

            String version = jdbcTemplate.queryForObject(
                    "SELECT VERSION()", String.class
            );

            System.out.println("--------------------------------");
            System.out.println("BANK OF SPAIN DATABASE CONNECTED!");
            System.out.println("Database: " + database);
            System.out.println("MySQL Version: " + version);
            System.out.println("--------------------------------");
        };
    }
}

