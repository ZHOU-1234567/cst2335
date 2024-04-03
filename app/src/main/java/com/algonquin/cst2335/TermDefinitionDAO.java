package com.algonquin.cst2335;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TermDefinitionDAO {
    @Insert
    long insertDefinition(TermDefinition t);

    @Query("SELECT DISTINCT term FROM TermDefinition")
    List<String> getAllTerms();

    @Query("SELECT * FROM TermDefinition WHERE term = :term")
    List<TermDefinition> getDefinitions(String term);

    @Delete
    void deleteDefinition(TermDefinition t);
}