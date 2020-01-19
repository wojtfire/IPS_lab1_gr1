package com.example.demo.web.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class DatabaseDataDto {
    private DatabaseEnum databaseName;
    private TableEnum tableName;
    private String query;
}
