package com.algonquin.cst2335;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
/**
 * Represents a geographical location with latitude and longitude .
 */
@Entity
public class Location {
    /** The latitude coordinate of the location. */
    @ColumnInfo(name = "latitude")
    protected String latitude;
    /** The longitude coordinate of the location. */
    @ColumnInfo(name = "longitude")
    protected String longitude;
    /** The unique identifier for the location. */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;
    /**
     * Get the latitude coordinate of the location.
     *
     * @return The latitude coordinate.
     */
    public String getLatitude() {
        return latitude;
    }
    /**
     * Get the longitude coordinate of the location.
     *
     * @return The longitude coordinate.
     */
    public String getLongitude() {
        return longitude;
    }
    /**
     * Constructs a new Location object with default latitude and longitude coordinates.
     */
    public Location() {

    }
    /**
     * Constructs a new Location object with the specified latitude and longitude coordinates.
     *
     * @param lat The latitude coordinate.
     * @param lng The longitude coordinate.
     */
    public Location(String lat, String lng) {
        latitude = lat;
        longitude = lng;
    }
}
