package com.example.demo.services;

import com.example.demo.database.ClickHouse;
import com.example.demo.web.dto.BenchmarkDto;
import com.example.demo.web.dto.TableEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class ClickhouseService implements DatabaseService {
    private final ClickHouse ch;

    public ClickhouseService() throws SQLException {
        ch = new ClickHouse();
    }

    public ResponseEntity initializeDatabase() throws SQLException {
        ch.createDb();
        ch.createClickHouseTables();
        return new ResponseEntity(HttpStatus.OK);
    }

    public BenchmarkDto loadTableData(TableEnum tableName) throws SQLException {
        switch (tableName) {
            case TAGS:
                return ch.loadCsvDataIntoTable("C:/Users/stanl/Desktop/IPS_lab1_gr1-master/tags.csv", "demo.tags");
            case LINKS:
                return ch.loadCsvDataIntoTable("C:/Users/stanl/Desktop/IPS_lab1_gr1-master/links.csv", "demo.links");
            case MOVIES:
                return ch.loadCsvDataIntoTable("C:/Users/stanl/Desktop/IPS_lab1_gr1-master/movies.csv", "demo.movies");
            case RATINGS:
                return ch.loadCsvDataIntoTable("C:/Users/stanl/Desktop/IPS_lab1_gr1-master/ratings.csv", "demo.ratings");
            case GENOME_TAGS:
                return ch.loadCsvDataIntoTable("C:/Users/stanl/Desktop/IPS_lab1_gr1-master/genome-tags.csv", "demo.genome_tags");
            default:
                return ch.loadCsvDataIntoTable("C:/Users/stanl/Desktop/IPS_lab1_gr1-master/genome-scores.csv", "demo.genome_scores");
        }
    }

    public BenchmarkDto executeQuery(String sql) throws SQLException {
        return ch.executeQuery(sql);
    }

    public BenchmarkDto selectAllDataFromTable(TableEnum tableName) throws SQLException {
        return ch.selectAllDataFromTable(tableName);
    }

    public void loadAllTablesData() throws SQLException {
        ch.loadCsvDataIntoTable("C:/Users/stanl/Desktop/IPS_lab1_gr1-master/movies.csv", "demo.movies");
        ch.loadCsvDataIntoTable("C:/Users/stanl/Desktop/IPS_lab1_gr1-master/tags.csv", "demo.tags");
        ch.loadCsvDataIntoTable("C:/Users/stanl/Desktop/IPS_lab1_gr1-master/ratings.csv", "demo.ratings");
        ch.loadCsvDataIntoTable("C:/Users/stanl/Desktop/IPS_lab1_gr1-master/links.csv", "demo.links");
        ch.loadCsvDataIntoTable("C:/Users/stanl/Desktop/IPS_lab1_gr1-master/genome-tags.csv", "demo.genome_tags");
        ch.loadCsvDataIntoTable("C:/Users/stanl/Desktop/IPS_lab1_gr1-master/genome-scores.csv", "demo.genome_scores");
    }

    public ResponseEntity truncateTable(TableEnum tableName) throws SQLException {
        return ch.truncateTable(tableName);
    }

    public BenchmarkDto truncateSingleTable(TableEnum tableName) throws SQLException {
        return ch.truncateSingleTable(tableName);
    }

    public List<String> getTables() throws SQLException {
        return ch.getTables();
    }
}
