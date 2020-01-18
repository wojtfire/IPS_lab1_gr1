package com.example.demo.web.controller;

import com.example.demo.services.DatabaseFactory;
import com.example.demo.web.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping({"/api"})
@CrossOrigin
public class Controller {
    @Autowired
    private DatabaseFactory databaseFactory;

    @CrossOrigin
    @PostMapping("/truncate")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity truncateTable(@RequestBody TruncateDto truncateDto) {
        return databaseFactory.truncateTable(truncateDto);
    }

    @CrossOrigin
    @GetMapping("/tables")
    public DatabaseTablesDto getDatabaseTables() {
        return databaseFactory.getTables(DatabaseEnum.BOTH.toString());
    }

    @CrossOrigin
    @PostMapping("/database")
    public ResponseEntity initDb(@RequestBody String database){
        return databaseFactory.initializeDatabase(database);
    }

    @CrossOrigin
    @PostMapping("/load")
    public BenchmarkDto loadTable(@RequestBody DatabaseDataDto dto) throws SQLException { return databaseFactory.loadTableData(dto); }
}
