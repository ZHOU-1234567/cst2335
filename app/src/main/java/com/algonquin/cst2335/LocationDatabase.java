package com.algonquin.cst2335;

import androidx.room.Database;
import androidx.room.RoomDatabase;
/**
 * Database class for managing Location entities using Room Persistence Library.
 */
@Database(entities = {Location.class}, version = 1)
public abstract class LocationDatabase extends RoomDatabase {
    /**
     * Retrieves the Data Access Object (DAO) interface for Location entities.
     *
     * @return The LocationDAO object.
     */
    public abstract LocationDAO lDAO();
}
