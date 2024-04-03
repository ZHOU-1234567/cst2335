package com.algonquin.cst2335;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Room database for storing songs.
 * This database holds the Song entities and provides access to the data through the SongDAO.
 * It is defined as an abstract class that extends RoomDatabase.
 *
 * @version 1 Indicates the version of the database.
 */
@Database(entities = {Song.class}, version = 1)
public abstract class SongDatabase extends RoomDatabase {
    /**
     * Provides the DAO (Data Access Object) for accessing the Song table.
     *
     * @return The DAO object for the Song entity.
     */
    public abstract SongDAO sDAO();
}
