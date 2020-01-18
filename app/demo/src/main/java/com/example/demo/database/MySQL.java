package com.example.demo.database;

import com.example.demo.util.Timer;
import com.example.demo.web.dto.BenchmarkDto;
import com.example.demo.web.dto.DatabaseTablesDto;
import com.example.demo.web.dto.TableEnum;
import com.google.common.collect.Table;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.xml.ws.Response;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySQL implements AutoCloseable {
    private String DB_URL = "jdbc:mysql://localhost:3307";
    private Connection conn;
    private Timer timer;

    public MySQL() throws SQLException {
        timer = Timer.getTimer();
        try {
            conn = DriverManager.getConnection(DB_URL, "root", "root");
            System.out.println("MySQL connection established");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public ResponseEntity createDb() throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE DATABASE IF NOT EXISTS demo");
        setNewUrl();
        return new ResponseEntity(HttpStatus.OK);
    }

    public BenchmarkDto loadCsvDataIntoTable(String filePath, String tableName) throws SQLException {
        String message = "Mysql load of " + tableName + " table took: ";
        String sql = "LOAD DATA INFILE '" + filePath + "' INTO TABLE " + tableName + " FIELDS TERMINATED BY '\\t' ESCAPED BY '\\b' ENCLOSED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 ROWS";
        Statement stmt = conn.createStatement();
        timer.start();
        ResultSet rs = stmt.executeQuery(sql);
        BenchmarkDto dto = timer.stop(message);
        return dto;
    }

    public ResponseEntity createTable(TableEnum tableName) throws SQLException {
        String sql;
        switch (tableName) {
            case TAGS:
                sql = "CREATE TABLE IF NOT EXISTS demo.tags (userId BIGINT NULL, movieId BIGINT NULL, tag VARCHAR(255) NULL, timestamp BIGINT NULL) DEFAULT CHARSET=utf8;";
                break;
            case LINKS:
                sql = "CREATE TABLE IF NOT EXISTS demo.links (movieId BIGINT NULL, imdbId BIGINT NULL, tmbdId BIGINT NULL) DEFAULT CHARSET=utf8;";
                break;
            case MOVIES:
                sql = "CREATE TABLE IF NOT EXISTS demo.movies (movieId BIGINT NULL, title VARCHAR(255), genres VARCHAR(255) NULL) DEFAULT CHARSET=utf8;";
                break;
            case RATINGS:
                sql = "CREATE TABLE IF NOT EXISTS demo.ratings (userId BIGINT NULL, movieId BIGINT NULL, rating FLOAT NULL, timestamp BIGINT NULL) DEFAULT CHARSET=utf8;";
                break;
            case GENOME_TAGS:
                sql = "CREATE TABLE IF NOT EXISTS demo.genome_tags (tagId BIGINT NULL, tag VARCHAR(255) NULL) DEFAULT CHARSET=utf8;";
                break;
            default:
                sql = "CREATE TABLE IF NOT EXISTS demo.genome_scores (movieId BIGINT NULL, tagId BIGINT NULL, relevance VARCHAR(255) NULL) DEFAULT CHARSET=utf8;";
                break;
        }
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
        return new ResponseEntity(HttpStatus.OK);
    }

    public void createMySqlTables() throws SQLException {
        Map<Statement, String> listOfStatements = new HashMap<Statement, String>();
        Statement table1 = conn.createStatement();
        Statement table2 = conn.createStatement();
        Statement table3 = conn.createStatement();
        Statement table4 = conn.createStatement();
        Statement table5 = conn.createStatement();
        Statement table6 = conn.createStatement();
        listOfStatements.put(table1, "CREATE TABLE IF NOT EXISTS demo.tags (userId BIGINT NULL, movieId BIGINT NULL, tag VARCHAR(255) NULL, timestamp BIGINT NULL) DEFAULT CHARSET=utf8;");
        listOfStatements.put(table2, "CREATE TABLE IF NOT EXISTS demo.ratings (userId BIGINT NULL, movieId BIGINT NULL, rating FLOAT NULL, timestamp BIGINT NULL) DEFAULT CHARSET=utf8;");
        listOfStatements.put(table3, "CREATE TABLE IF NOT EXISTS demo.movies (movieId BIGINT NULL, title VARCHAR(255), genres VARCHAR(255) NULL) DEFAULT CHARSET=utf8;");
        listOfStatements.put(table4, "CREATE TABLE IF NOT EXISTS demo.links (movieId BIGINT NULL, imdbId BIGINT NULL, tmbdId BIGINT NULL) DEFAULT CHARSET=utf8;");
        listOfStatements.put(table5, "CREATE TABLE IF NOT EXISTS demo.genome_scores (movieId BIGINT NULL, tagId BIGINT NULL, relevance VARCHAR(255) NULL) DEFAULT CHARSET=utf8;");
        listOfStatements.put(table6, "CREATE TABLE IF NOT EXISTS demo.genome_tags (tagId BIGINT NULL, tag VARCHAR(255) NULL) DEFAULT CHARSET=utf8;");
        for(Map.Entry<Statement, String> statement: listOfStatements.entrySet()) {
            try{
                statement.getKey().execute(statement.getValue());
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }

    public void truncateTable(TableEnum tableName) throws SQLException {
        String sql = "TRUNCATE TABLE demo." + tableName.getText();
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
    }

    public List<String> getTables() throws SQLException {
        List<String> tables = new ArrayList<>();
        String sql = "SHOW TABLES FROM demo";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()) {
            tables.add(rs.getString(1));
        }
        return tables;
    }

    @Override
    public void close() throws Exception {
        if (conn != null) {
            conn.close();
        }
    }

    private void setNewUrl() throws SQLException {
        if(!DB_URL.endsWith("/demo")) {
            DB_URL += "/demo";
            conn = DriverManager.getConnection(DB_URL, "root", "root");
            System.out.println("MySQL database demo connected");
        }
    }
}
