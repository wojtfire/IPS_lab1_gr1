package com.example.demo.database;

import com.example.demo.util.Timer;
import ru.yandex.clickhouse.ClickHouseConnection;
import ru.yandex.clickhouse.ClickHouseDriver;
import ru.yandex.clickhouse.ClickHouseStatement;
import ru.yandex.clickhouse.domain.ClickHouseFormat;
import ru.yandex.clickhouse.settings.ClickHouseProperties;

import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ClickHouse implements AutoCloseable {
    private String DB_URL = "jdbc:clickhouse://localhost:8123";
    private ClickHouseDriver driver;
    private ClickHouseProperties props;
    private ClickHouseConnection conn = null;
    private Timer timer;

    public ClickHouse() throws SQLException {
        timer = Timer.getTimer();
        try {
            props = new ClickHouseProperties();
            driver = new ClickHouseDriver();
            conn = driver.connect(DB_URL, props);
            System.out.println("Clickhouse connection established");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void createDb() throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeQuery("CREATE DATABASE IF NOT EXISTS demo");
        DB_URL += "/demo";
        conn = driver.connect(DB_URL, props);
        System.out.println("Clickhouse database demo connected");
    }

    public void createClickHouseTables() throws SQLException {
        Map<Statement, String> listOfStatements = new HashMap<Statement, String>();
        Statement table1 = conn.createStatement();
        Statement table2 = conn.createStatement();
        Statement table3 = conn.createStatement();
        Statement table4 = conn.createStatement();
        Statement table5 = conn.createStatement();
        Statement table6 = conn.createStatement();
        listOfStatements.put(table1, "CREATE TABLE IF NOT EXISTS demo.tags (userId Int64, movieId Int64, tag String, `timestamp` Int64) ENGINE = Log;");
        listOfStatements.put(table2, "CREATE TABLE IF NOT EXISTS demo.ratings (userId Int64, movieId Int64, rating Float32, `timestamp` Int64) ENGINE = Log;");
        listOfStatements.put(table3, "CREATE TABLE IF NOT EXISTS demo.movies (userId Int64, movieId String, genres String) ENGINE = Log;");
        listOfStatements.put(table4, "CREATE TABLE IF NOT EXISTS demo.links (movieId Int64, imdbId String, tmbdId String) ENGINE = Log;");
        listOfStatements.put(table5, "CREATE TABLE IF NOT EXISTS demo.genome_scores (movieId Int64, tagI Int64, relevance String) ENGINE = Log; ");
        listOfStatements.put(table6, "CREATE TABLE IF NOT EXISTS demo.genome_tags (tagId Int64, tag String) ENGINE = Log; ");
        for(Map.Entry<Statement, String> statement: listOfStatements.entrySet()) {
            try(ResultSet rs = statement.getKey().executeQuery(statement.getValue())){};
        }
    }

    public void loadCsvDataIntoTable(String filePath, String tableName) throws SQLException {
        String message = "Clickhouse load of " + tableName + " took: ";
        ClickHouseStatement sth = conn.createStatement();
        timer.start();
        sth
                .write()
                .table(tableName)
                .data(new File(filePath), ClickHouseFormat.TSV)
                .send();
        timer.stop(message);
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
