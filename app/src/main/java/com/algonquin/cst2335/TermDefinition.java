package com.algonquin.cst2335;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TermDefinition {
    @ColumnInfo(name = "term")
    protected String term;

    @ColumnInfo(name = "definition")
    protected String definition;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    public String getTerm() {
        return term;
    }

    public String getDefinition() {
        return definition;
    }

    public TermDefinition() {

    }

    public TermDefinition(String t, String d) {
        term = t;
        definition = d;
    }
}