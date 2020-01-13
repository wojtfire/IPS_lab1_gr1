package com.example.demo.services;

import com.example.demo.database.MySQL;

import java.sql.SQLException;

public class MysqlService implements DatabaseService {
    private final MySQL mysql;

    public MysqlService() throws SQLException {
        mysql = new MySQL();
    }

    public void initializeDatabase() throws SQLException {
        mysql.createDb();
        mysql.createMySqlTables();
    }

    public void loadAllTablesData() throws SQLException {
        mysql.loadCsvDataIntoTable("27m-movie-ratings/movies.csv", "demo.movies");
        mysql.loadCsvDataIntoTable("27m-movie-ratings/tags.csv", "demo.tags");
        mysql.loadCsvDataIntoTable("27m-movie-ratings/ratings.csv", "demo.rating");
        mysql.loadCsvDataIntoTable("27m-movie-ratings/links.csv", "demo.link");
        mysql.loadCsvDataIntoTable("27m-movie-ratings/genome-tags.csv", "demo.genome_tags");
        mysql.loadCsvDataIntoTable("27m-movie-ratings/genome-scores.csv", "demo.genome_scores");
    }

    public void truncateTable(String tableName) throws SQLException {
        mysql.truncateTable(tableName);
        System.out.println("Table " + tableName + " truncated");
    }
}
