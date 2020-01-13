package com.example.demo.services;

import com.example.demo.services.DatabaseService;

import java.sql.SQLException;
import java.util.List;

public class DatabaseFactory {
    public DatabaseFactory(){}

    public void initializeDatabase(List<DatabaseService> services) {
        services.stream().forEach(service -> {
            try {
                service.initializeDatabase();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void loadAllTablesData(List<DatabaseService> services) {
        services.stream().forEach(service -> {
            try {
                service.loadAllTablesData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
