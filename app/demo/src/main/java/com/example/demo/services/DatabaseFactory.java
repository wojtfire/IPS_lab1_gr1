package com.example.demo.services;

import com.example.demo.web.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DatabaseFactory {
    @Autowired
    private ClickhouseService clickhouseService;

    @Autowired
    private MysqlService mysqlService;

    private List<DatabaseService> services;

    @PostConstruct
    public void setServices() {
        services = new ArrayList<>(Arrays.asList(mysqlService, clickhouseService));
    }

    public ResponseEntity initializeDatabase(String databaseName) {
        getServiceBasedOnEnumString(databaseName).stream().forEach(service -> {
            try {
                service.initializeDatabase();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return new ResponseEntity(HttpStatus.OK);
    }

    public BenchmarkDto loadTableData(DatabaseDataDto dto) throws SQLException {
        return getServiceBasedOnDatabaseName(dto).loadTableData(dto.getTableName());
    }

    public void loadAllTablesData() {
        services.stream().forEach(service -> {
            try {
                service.loadAllTablesData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public ResponseEntity truncateTable(TruncateDto truncateDto) {
        truncateDto.getDatabaseDataList().stream().forEach(data -> {
            try {
                getServiceBasedOnDatabaseName(data).truncateTable(data.getTableName());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return new ResponseEntity(HttpStatus.OK);
    }

    public DatabaseTablesDto getTables(String database) {
        DatabaseTablesDto dto = new DatabaseTablesDto();
        getServiceBasedOnEnumString(database).stream().forEach(service -> {
            try {
                if(service instanceof MysqlService) {
                    dto.setMysqlTables(service.getTables());
                } else {
                    dto.setClickhouseTables(service.getTables());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return dto;
    }

    private List<DatabaseService> getServiceBasedOnEnumString(String databaseName) {
        switch(databaseName) {
            case "MYSQL":
                return new ArrayList<>(Arrays.asList(mysqlService));
            case "CLICKHOUSE":
                return new ArrayList<>(Arrays.asList(clickhouseService));
            default:
                return new ArrayList<>(Arrays.asList(mysqlService, clickhouseService));
        }
    }

    private DatabaseService getServiceBasedOnDatabaseName(DatabaseDataDto databaseData) {
        return databaseData.getDatabaseName() == DatabaseEnum.CLICKHOUSE ? clickhouseService : mysqlService;
    }
}
