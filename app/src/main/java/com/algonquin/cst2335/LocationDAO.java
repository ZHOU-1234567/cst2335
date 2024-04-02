package com.algonquin.cst2335;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
/**
 * Data Access Object (DAO) interface for accessing Location entities in the database.
 */
@Dao
public interface LocationDAO {
    /**
     * Inserts a new Location into the database.
     *
     * @param l The Location object to insert.
     * @return The ID of the inserted Location entity.
     */
    @Insert
    long insertLocation(Location l);
    /**
     * Retrieves all Location from the database.
     *
     * @return A list containing all Location entities.
     */
    @Query("SELECT * FROM Location")
    List<Location> getAllLocations();
    /**
     * Deletes a Location from the database.
     *
     * @param l The Location object to delete.
     */
    @Delete
    void deleteLocation(Location l);
}
