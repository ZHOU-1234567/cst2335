package com.algonquin.cst2335;

import androidx.room.Database;
import androidx.room.RoomDatabase;
/**
 * Room database for storing term definitions.
 * This database holds the TermDefinition entities and provides access to the data
 * through the TermDefinitionDAO. Defined as an abstract class extending RoomDatabase.
 *
 * @version 1 The version number of the database.
 */
@Database(entities = {TermDefinition.class}, version = 1)
public abstract class TermDefinitionDatabase extends RoomDatabase {
    /**
     * Provides the DAO (Data Access Object) for accessing the TermDefinition table.
     *
     * @return The DAO object for the TermDefinition entity.
     */
    public abstract TermDefinitionDAO dDAO();
}