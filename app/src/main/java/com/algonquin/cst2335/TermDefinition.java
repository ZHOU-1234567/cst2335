package com.algonquin.cst2335;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
/**
 * Entity class representing a term definition.
 * This class is used for storing information about term definitions in the database.
 */
@Entity
public class TermDefinition {
    @ColumnInfo(name = "term")
    protected String term;

    @ColumnInfo(name = "definition")
    protected String definition;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    /**
     * Gets the term.
     *
     * @return The term.
     */
    public String getTerm() {
        return term;
    }

    /**
     * Gets the definition of the term.
     *
     * @return The definition of the term.
     */
    public String getDefinition() {
        return definition;
    }
    /**
     * Default constructor for TermDefinition.
     */
    public TermDefinition() {

    }
    /**
     * Constructor for TermDefinition with parameters.
     *
     * @param t The term.
     * @param d The definition of the term.
     */
    public TermDefinition(String t, String d) {
        term = t;
        definition = d;
    }
}