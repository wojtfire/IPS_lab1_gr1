package com.example.demo.web.dto;

import lombok.Getter;

@Getter
public class DatabaseDataDto {
    private DatabaseEnum databaseName;
    private TableEnum tableName;
}
