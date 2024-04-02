package com.algonquin.cst2335;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LocationDAO {
    @Insert
    long insertLocation(Location l);

    @Query("SELECT * FROM Location")
    List<Location> getAllLocations();

    @Delete
    void deleteLocation(Location l);
}
