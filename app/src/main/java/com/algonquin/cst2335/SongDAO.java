package com.algonquin.cst2335;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Data Access Object (DAO) for the Song entity.
 * This interface defines the methods that access the database to perform various operations
 * related to the Song entity, such as inserting, querying, and deleting songs.
 */
@Dao
public interface SongDAO {
    /**
     * Inserts a new song into the database.
     *
     * @param s The Song object to be inserted.
     * @return The row ID of the newly inserted song.
     */
    @Insert
    long insertSong(Song s);
    /**
     * Retrieves all songs from the database.
     *
     * @return A list of all songs in the database.
     */
    @Query("SELECT * FROM Song")
    List<Song> getAllSongs();
    /**
     * Retrieves songs from the database that match the specified title.
     *
     * @param title The title of the song(s) to be retrieved.
     * @return A list of songs matching the title.
     */
    @Query("SELECT * FROM Song WHERE title = :title")
    List<Song> getSongs(String title);
    /**
     * Deletes a song from the database.
     *
     * @param s The Song object to be deleted.
     */
    @Delete
    void deleteSong(Song s);
}
