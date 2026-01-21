package com.wearhouse.bankproject.config;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.wearhouse.bankproject.repository.historical",
        entityManagerFactoryRef = "historicalEntityManagerFactory",
        transactionManagerRef = "historicalTransactionManager"
)
public class HistoricalDbConfig {

    @Bean(name = "historicalDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.historical")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "historicalEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("historicalDataSource") DataSource dataSource) {

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");

        return builder
                .dataSource(dataSource)
                .packages("com.wearhouse.bankproject.model.historical")
                .persistenceUnit("historical")
                .properties(properties)
                .build();
    }

    @Bean(name = "historicalTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("historicalEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
