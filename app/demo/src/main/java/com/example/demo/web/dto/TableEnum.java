package com.example.demo.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TableEnum {
    TAGS("TAGS"), RATINGS("RATINGS"), MOVIES("MOVIES"), LINKS("LINKS"), GENOME_SCORES("GENOME_SCORES"), GENOME_TAGS("GENOME_TAGS");

    private final String text;

    TableEnum(final String text) {
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
