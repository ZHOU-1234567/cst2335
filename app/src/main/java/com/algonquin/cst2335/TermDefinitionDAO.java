package com.algonquin.cst2335;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
/**
 * Data Access Object (DAO) for the TermDefinition entity.
 * This interface defines methods for accessing the database to perform operations
 * related to term definitions, such as inserting, querying, and deleting definitions.
 */
@Dao
public interface TermDefinitionDAO {
    /**
     * Inserts a new term definition into the database.
     *
     * @param t The TermDefinition object to be inserted.
     * @return The row ID of the newly inserted term definition.
     */
    @Insert
    long insertDefinition(TermDefinition t);
    /**
     * Retrieves all distinct terms from the database.
     *
     * @return A list of all distinct terms.
     */
    @Query("SELECT DISTINCT term FROM TermDefinition")
    List<String> getAllTerms();
    /**
     * Retrieves definitions for a specific term from the database.
     *
     * @param term The term for which definitions are requested.
     * @return A list of TermDefinition objects associated with the specified term.
     */
    @Query("SELECT * FROM TermDefinition WHERE term = :term")
    List<TermDefinition> getDefinitions(String term);
    /**
     * Deletes a term definition from the database.
     *
     * @param t The TermDefinition object to be deleted.
     */
    @Delete
    void deleteDefinition(TermDefinition t);
}