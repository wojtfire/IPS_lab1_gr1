package com.example.demo.services;

import com.example.demo.web.dto.BenchmarkDto;
import com.example.demo.web.dto.DatabaseTablesDto;
import com.example.demo.web.dto.TableEnum;
import org.springframework.http.ResponseEntity;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseService {
    ResponseEntity initializeDatabase() throws SQLException;
    void loadAllTablesData() throws SQLException;
    BenchmarkDto loadTableData(TableEnum tableName)  throws SQLException;
    void truncateTable(TableEnum tableName) throws SQLException;
    List<String> getTables() throws SQLException;
}
