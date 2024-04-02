package com.algonquin.cst2335;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Location {
    @ColumnInfo(name = "latitude")
    protected String latitude;

    @ColumnInfo(name = "longitude")
    protected String longitude;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public Location() {

    }

    public Location(String lat, String lng) {
        latitude = lat;
        longitude = lng;
    }
}
