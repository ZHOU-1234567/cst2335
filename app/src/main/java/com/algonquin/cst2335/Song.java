package com.algonquin.cst2335;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

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

    public String getTitle() {
        return title;
    }

    public int getDuration() {
        return duration;
    }

    public String getAlbum_title() {
        return album_title;
    }

    public String getAlbum_cover() {
        return album_cover;
    }

    public Song() {

    }

    public Song(String title, int duration, String album_title, String album_cover) {
        this.title = title;
        this.duration = duration;
        this.album_title = album_title;
        this.album_cover = album_cover;
    }
}
