package com.example.demo.services;

import com.example.demo.web.dto.BenchmarkDto;
import com.example.demo.web.dto.TableEnum;
import org.springframework.http.ResponseEntity;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseService {
    ResponseEntity initializeDatabase() throws SQLException;
    void loadAllTablesData() throws SQLException;
    BenchmarkDto loadTableData(TableEnum tableName)  throws SQLException;
    ResponseEntity truncateTable(TableEnum tableName) throws SQLException;
    BenchmarkDto truncateSingleTable(TableEnum tableName) throws SQLException;
    List<String> getTables() throws SQLException;
    BenchmarkDto selectAllDataFromTable(TableEnum tableName) throws SQLException;
    BenchmarkDto executeQuery(String sql) throws SQLException;
}
