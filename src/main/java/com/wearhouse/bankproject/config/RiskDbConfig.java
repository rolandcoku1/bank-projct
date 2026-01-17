package com.wearhouse.bankproject.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.wearhouse.bankproject.repository.risk",
        entityManagerFactoryRef = "riskEntityManagerFactory",
        transactionManagerRef = "riskTransactionManager"
)
public class RiskDbConfig {

    @Bean(name = "riskDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.risk")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * Configures risk entity manager factory with dialect and schema
     */
    @Bean(name = "riskEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("riskDataSource") DataSource dataSource) {

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");

        return builder
                .dataSource(dataSource)
                .packages("com.wearhouse.bankproject.model.risk")
                .persistenceUnit("risk")
                .properties(properties)
                .build();
    }

    @Bean(name = "riskTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("riskEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}