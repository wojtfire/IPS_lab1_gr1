package com.example.demo.services;

import ch.qos.logback.classic.db.names.TableName;
import com.example.demo.database.MySQL;
import com.example.demo.web.dto.BenchmarkDto;
import com.example.demo.web.dto.DatabaseTablesDto;
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
        mysql.loadCsvDataIntoTable("27m-movie-ratings/movies.csv", "demo.movies");
        mysql.loadCsvDataIntoTable("27m-movie-ratings/tags.csv", "demo.tags");
        mysql.loadCsvDataIntoTable("27m-movie-ratings/ratings.csv", "demo.ratings");
        mysql.loadCsvDataIntoTable("27m-movie-ratings/links.csv", "demo.links");
        mysql.loadCsvDataIntoTable("27m-movie-ratings/genome-tags.csv", "demo.genome_tags");
        mysql.loadCsvDataIntoTable("27m-movie-ratings/genome-scores.csv", "demo.genome_scores");
    }

    public BenchmarkDto loadTableData(TableEnum tableName) throws SQLException {
        switch (tableName) {
            case TAGS:
                return mysql.loadCsvDataIntoTable("27m-movie-ratings/tags.csv", "demo.tags");
            case LINKS:
                return mysql.loadCsvDataIntoTable("27m-movie-ratings/links.csv", "demo.links");
            case MOVIES:
                return mysql.loadCsvDataIntoTable("27m-movie-ratings/movies.csv", "demo.movies");
            case RATINGS:
                return mysql.loadCsvDataIntoTable("27m-movie-ratings/ratings.csv", "demo.ratings");
            case GENOME_TAGS:
                return mysql.loadCsvDataIntoTable("27m-movie-ratings/genome-tags.csv", "demo.genome_tags");
            default:
                return mysql.loadCsvDataIntoTable("27m-movie-ratings/genome-scores.csv", "demo.genome_scores");
        }
    }

    public void truncateTable(TableEnum tableName) throws SQLException {
        mysql.truncateTable(tableName);
        System.out.println("Table " + tableName + " truncated");
    }

    public List<String> getTables() throws SQLException {
        return mysql.getTables();
    }
}
