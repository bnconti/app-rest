package ar.edu.unnoba.pdyc.apprest;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatasourceConfig {
    @Bean
    public DataSource datasource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5432/music_rest")
                .username("postgres")
                .password("postgres")
                .build();
    }
}
