package com.example.demo.services;

import com.example.demo.database.MySQL;
import com.example.demo.web.dto.BenchmarkDto;
import com.example.demo.web.dto.TableEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class MysqlService implements DatabaseService {
    private final MySQL mysql;

    public MysqlService() throws SQLException {
        mysql = new MySQL();
    }

    public ResponseEntity initializeDatabase() throws SQLException {
        mysql.createDb();
        mysql.createMySqlTables();
        return new ResponseEntity(HttpStatus.OK);
    }

    public void loadAllTablesData() throws SQLException {
        mysql.loadCsvDataIntoTable("C:/Users/stanl/Desktop/IPS_lab1_gr1-master/movies.csv", "demo.movies");
        mysql.loadCsvDataIntoTable("C:/Users/stanl/Desktop/IPS_lab1_gr1-master/tags.csv", "demo.tags");
        mysql.loadCsvDataIntoTable("C:/Users/stanl/Desktop/IPS_lab1_gr1-master/ratings.csv", "demo.ratings");
        mysql.loadCsvDataIntoTable("C:/Users/stanl/Desktop/IPS_lab1_gr1-master/links.csv", "demo.links");
        mysql.loadCsvDataIntoTable("C:/Users/stanl/Desktop/IPS_lab1_gr1-master/genome-tags.csv", "demo.genome_tags");
        mysql.loadCsvDataIntoTable("C:/Users/stanl/Desktop/IPS_lab1_gr1-master/genome-scores.csv", "demo.genome_scores");
    }

    public BenchmarkDto loadTableData(TableEnum tableName) throws SQLException {
        switch (tableName) {
            case TAGS:
                return mysql.loadCsvDataIntoTable("C:/Users/stanl/Desktop/IPS_lab1_gr1-master/tags.csv", "demo.tags");
            case LINKS:
                return mysql.loadCsvDataIntoTable("C:/Users/stanl/Desktop/IPS_lab1_gr1-master/links.csv", "demo.links");
            case MOVIES:
                return mysql.loadCsvDataIntoTable("C:/Users/stanl/Desktop/IPS_lab1_gr1-master/movies.csv", "demo.movies");
            case RATINGS:
                return mysql.loadCsvDataIntoTable("C:/Users/stanl/Desktop/IPS_lab1_gr1-master/ratings.csv", "demo.ratings");
            case GENOME_TAGS:
                return mysql.loadCsvDataIntoTable("C:/Users/stanl/Desktop/IPS_lab1_gr1-master/genome-tags.csv", "demo.genome_tags");
            default:
                return mysql.loadCsvDataIntoTable("C:/Users/stanl/Desktop/IPS_lab1_gr1-master/genome-scores.csv", "demo.genome_scores");
        }
    }

    public BenchmarkDto executeQuery(String sql) throws SQLException {
        return mysql.executeQuery(sql);
    }

    public BenchmarkDto selectAllDataFromTable(TableEnum tableName) throws SQLException {
        return mysql.selectAllDataFromTable(tableName);
    }

    public ResponseEntity truncateTable(TableEnum tableName) throws SQLException {
        return mysql.truncateTable(tableName);
    }

    public BenchmarkDto truncateSingleTable(TableEnum tableName) throws SQLException {
        return mysql.truncateSingleTable(tableName);
    }

    public List<String> getTables() throws SQLException {
        return mysql.getTables();
    }
}
