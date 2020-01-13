package com.example.demo.services;

import java.sql.SQLException;

public interface DatabaseService {
    void initializeDatabase() throws SQLException;
    void loadAllTablesData() throws SQLException;
}
