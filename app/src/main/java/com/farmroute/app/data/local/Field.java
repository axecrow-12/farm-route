package com.farmroute.app.data.local;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * A registered field, stored as a circular geofence (centre + radius).
 * Diagnoses are associated with the field whose geofence contains them.
 */
@Entity(tableName = "fields")
public class Field {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String name = "";

    public double latitude;
    public double longitude;

    /** Geofence radius in metres. */
    public float radiusMeters;

    public long createdAt;
}
