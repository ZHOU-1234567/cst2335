package com.algonquin.cst2335;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
/**
 * Entity class representing a song. This class is used for storing information about songs
 * in the database.
 */
@Entity
public class Song implements Serializable {
    @ColumnInfo(name = "title")
    protected String title;

    @ColumnInfo(name = "duration")
    protected int duration;

    @ColumnInfo(name = "album_title")
    protected String album_title;

    @ColumnInfo(name = "album_cover")
    protected String album_cover;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;
    /**
     * Gets the title of the song.
     *
     * @return The title of the song.
     */
    public String getTitle() {
        return title;
    }
    /**
     * Gets the duration of the song.
     *
     * @return The duration of the song in seconds.
     */
    public int getDuration() {
        return duration;
    }
    /**
     * Gets the title of the album the song is from.
     *
     * @return The album title.
     */
    public String getAlbum_title() {
        return album_title;
    }
    /**
     * Gets the URL of the album cover.
     *
     * @return The URL of the album cover image.
     */
    public String getAlbum_cover() {
        return album_cover;
    }
    /**
        * Default constructor for Song.
     */
    public Song() {

    }
    /**
     * Gets the title of the song.
     *
     * @return The title of the song.
     */
    public Song(String title, int duration, String album_title, String album_cover) {
        this.title = title;
        this.duration = duration;
        this.album_title = album_title;
        this.album_cover = album_cover;
    }
}
