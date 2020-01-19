package com.example.demo.database;

import com.example.demo.util.Timer;
import com.example.demo.web.dto.BenchmarkDto;
import com.example.demo.web.dto.DatabaseDataDto;
import com.example.demo.web.dto.TableEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.clickhouse.ClickHouseConnection;
import ru.yandex.clickhouse.ClickHouseDriver;
import ru.yandex.clickhouse.ClickHouseStatement;
import ru.yandex.clickhouse.domain.ClickHouseFormat;
import ru.yandex.clickhouse.response.ClickHouseResultSet;
import ru.yandex.clickhouse.settings.ClickHouseProperties;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public ResponseEntity createDb() throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeQuery("CREATE DATABASE IF NOT EXISTS demo");
        setNewUrl();
        System.out.println("Clickhouse database demo connected");
        return new ResponseEntity(HttpStatus.OK);
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
        listOfStatements.put(table3, "CREATE TABLE IF NOT EXISTS demo.movies (movieId Int64, title String, genres String) ENGINE = Log;");
        listOfStatements.put(table4, "CREATE TABLE IF NOT EXISTS demo.links (movieId Int64, imdbId String, tmbdId String) ENGINE = Log;");
        listOfStatements.put(table5, "CREATE TABLE IF NOT EXISTS demo.genome_scores (movieId Int64, tagI Int64, relevance String) ENGINE = Log; ");
        listOfStatements.put(table6, "CREATE TABLE IF NOT EXISTS demo.genome_tags (tagId Int64, tag String) ENGINE = Log; ");
        for(Map.Entry<Statement, String> statement: listOfStatements.entrySet()) {
            try(ResultSet rs = statement.getKey().executeQuery(statement.getValue())){};
        }
    }

    public BenchmarkDto loadCsvDataIntoTable(String filePath, String tableName) throws SQLException {
        String message = "Clickhouse load of " + tableName + " took: ";
        ClickHouseStatement stmt = conn.createStatement();
        timer.start();
        stmt.write().table(tableName).data(new File(filePath), ClickHouseFormat.TSV).send();
        BenchmarkDto dto = timer.stop(message);
        return dto;
    }

    public void truncateTable(TableEnum tableName) throws SQLException {
        String sql = "TRUNCATE TABLE demo." + tableName.getText().toLowerCase();
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
    }

    @Override
    public void close() throws Exception {
        if (conn != null) {
            conn.close();
        }
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

    private void setNewUrl() throws SQLException {
        if(!DB_URL.endsWith("/demo")) {
            DB_URL += "/demo";
            conn = driver.connect(DB_URL, props);
            System.out.println("Clickhouse connection established");
        }
    }

    public BenchmarkDto selectAllDataFromTable(TableEnum tableName) throws SQLException {
        String sql = "SELECT * FROM demo." + tableName.getText().toLowerCase();
        Statement stmt = conn.createStatement();
        timer.start();
        ResultSet rs = stmt.executeQuery(sql);
        return timer.stop(sql + " took ");
    }

    public BenchmarkDto executeQuery(String sql) throws SQLException {
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        timer.start();
        ResultSet rs = stmt.executeQuery(sql);
        BenchmarkDto dto = timer.stop(sql + " took ");
        rs.last();
        dto.setRows(rs.getRow());
        return dto;
    }

    private List<String> getColumnTypes(ResultSet rs) throws SQLException {
        int columnCount = rs.getMetaData().getColumnCount();
        List<String> columnTypes = new ArrayList<>();
        for(int i=1; i<=columnCount; i++) {
            columnTypes.add(rs.getMetaData().getColumnTypeName(i));
        }
        return columnTypes;
    }
}
