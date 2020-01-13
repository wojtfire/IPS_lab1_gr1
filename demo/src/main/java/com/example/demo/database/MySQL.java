package com.example.demo.database;

import com.example.demo.util.Timer;

import java.sql.*;
import java.util.HashMap;
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

    public void createDb() throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE DATABASE IF NOT EXISTS demo");
        DB_URL += "/demo";
        conn = DriverManager.getConnection(DB_URL, "root", "root");
        System.out.println("MySQL database demo connected");
    }

    public void loadCsvDataIntoTable(String filePath, String tableName) throws SQLException {
        String message = "Mysql load of " + tableName + " table took: ";
        String sql = "LOAD DATA INFILE '" + filePath + "' INTO TABLE " + tableName + " FIELDS TERMINATED BY '\\t' ESCAPED BY '\\b' ENCLOSED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 ROWS";
        Statement stmt = conn.createStatement();
        timer.start();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs != null) {
            timer.stop(message);
        }
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
        listOfStatements.put(table2, "CREATE TABLE IF NOT EXISTS demo.rating (userId BIGINT NULL, movieId BIGINT NULL, rating FLOAT NULL, timestamp BIGINT NULL) DEFAULT CHARSET=utf8;");
        listOfStatements.put(table3, "CREATE TABLE IF NOT EXISTS demo.movies (movieId BIGINT NULL, title VARCHAR(255), genres VARCHAR(255) NULL) DEFAULT CHARSET=utf8;");
        listOfStatements.put(table4, "CREATE TABLE IF NOT EXISTS demo.link (movieId BIGINT NULL, imdbId BIGINT NULL, tmbdId BIGINT NULL) DEFAULT CHARSET=utf8;");
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

    public void truncateTable(String tableName) throws SQLException {
        String sql = "TRUNCATE TABLE demo." + tableName;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs != null) {
            rs = stmt.getResultSet();
            System.out.println(rs);
        }
    }

    @Override
    public void close() throws Exception {
        if (conn != null) {
            conn.close();
        }
    }
}
