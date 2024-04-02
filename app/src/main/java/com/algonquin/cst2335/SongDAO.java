package com.algonquin.cst2335;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SongDAO {
    @Insert
    long insertSong(Song s);

    @Query("SELECT * FROM Song")
    List<Song> getAllSongs();

    @Query("SELECT * FROM Song WHERE title = :title")
    List<Song> getSongs(String title);

    @Delete
    void deleteSong(Song s);
}
