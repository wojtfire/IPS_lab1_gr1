package com.example.demo.web.dto;

import lombok.Data;

import java.util.List;

@Data
public class DatabaseTablesDto {
    private List<String> mysqlTables;
    private List<String> clickhouseTables;
}
