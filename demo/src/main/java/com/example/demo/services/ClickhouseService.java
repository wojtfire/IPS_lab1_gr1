package com.example.demo.services;

import com.example.demo.database.ClickHouse;

import java.sql.SQLException;

public class ClickhouseService implements DatabaseService {
    private final ClickHouse ch;

    public ClickhouseService() throws SQLException {
        ch = new ClickHouse();
    }

    public void initializeDatabase() throws SQLException {
        ch.createDb();
        ch.createClickHouseTables();
    }

    public void loadAllTablesData() throws SQLException {
        ch.loadCsvDataIntoTable("/home/wojt/Pobrane/27m-database/movies.csv", "demo.movies");
        ch.loadCsvDataIntoTable("/home/wojt/Pobrane/27m-database/tags.csv", "demo.tags");
        ch.loadCsvDataIntoTable("/home/wojt/Pobrane/27m-database/ratings.csv", "demo.ratings");
        ch.loadCsvDataIntoTable("/home/wojt/Pobrane/27m-database/links.csv", "demo.links");
        ch.loadCsvDataIntoTable("/home/wojt/Pobrane/27m-database/genome-tags.csv", "demo.genome_tags");
        ch.loadCsvDataIntoTable("/home/wojt/Pobrane/27m-database/genome-scores.csv", "demo.genome_scores");
    }

    public void truncateTable(String tableName) throws SQLException {
        ch.truncateTable(tableName);
        System.out.println("Table " + tableName + " truncated");
    }
}
