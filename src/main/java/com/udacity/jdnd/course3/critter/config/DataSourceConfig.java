package com.udacity.jdnd.course3.critter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    private static final String URL = "jdbc:mysql://localhost:3306/critter";

    @Bean
    @ConfigurationProperties(prefix = "com.udacity.datasource")
    public DataSource getDataSource() {
        return DataSourceBuilder.create().url(URL).build();
    }
}
