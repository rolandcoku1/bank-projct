package com.wearhouse.bankproject.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.wearhouse.bankproject.operational.repository",
        entityManagerFactoryRef = "operationalEntityManagerFactory",
        transactionManagerRef = "operationalTransactionManager"
)
public class OperationalDbConfig {

    @Primary
    @Bean(name = "operationalDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.operational")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * Configures primary entity manager factory for operational database
     */
    @Primary
    @Bean(name = "operationalEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("operationalDataSource") DataSource dataSource) {

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "none");

        return builder
                .dataSource(dataSource)
                .packages("com.wearhouse.bankproject.operational.entity")
                .persistenceUnit("operational")
                .properties(properties)
                .build();
    }

    @Primary
    @Bean(name = "operationalTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("operationalEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
