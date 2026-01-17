package com.wearhouse.bankproject.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Service
public class DatabaseCheckService {

    private final DataSource operationalDataSource;
    private final DataSource historicalDataSource;
    private final DataSource riskDataSource;

    public DatabaseCheckService(
            @Qualifier("operationalDataSource") DataSource operationalDataSource,
            @Qualifier("historicalDataSource") DataSource historicalDataSource,
            @Qualifier("riskDataSource") DataSource riskDataSource) {
        this.operationalDataSource = operationalDataSource;
        this.historicalDataSource = historicalDataSource;
        this.riskDataSource = riskDataSource;
    }

    public String checkOperational() {
        return checkConnection(operationalDataSource, "Operational");
    }

    public String checkHistorical() {
        return checkConnection(historicalDataSource, "Historical");
    }

    public String checkRisk() {
        return checkConnection(riskDataSource, "Risk");
    }

    private String checkConnection(DataSource dataSource, String name) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeQuery("SELECT 1");
            return "The connection with the " + name + " database is correct.";
        } catch (Exception e) {
            return "The connection with the " + name + " database failed: " + e.getMessage();
        }
    }
}
