package com.example.demo.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum DatabaseEnum {
    CLICKHOUSE("CLICKHOUSE"), MYSQL("MYSQL"), BOTH("BOTH");

    private final String text;

    DatabaseEnum(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public String getText(){return this.text;}

    @JsonCreator
    public static DatabaseEnum fromText(String text){
        for(DatabaseEnum d : DatabaseEnum.values()){
            if(d.getText().equals(text)){
                return d;
            }
        }
        throw new IllegalArgumentException();
    }
}
