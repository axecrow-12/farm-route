package com.farmroute.app.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * A single disease diagnosis produced on the device. Everything needed to make
 * the record useful is captured locally; the syncedToServer flag drives the
 * deferred upload handled by SyncWorker.
 */
@Entity(tableName = "diagnoses")
public class Diagnosis {

    @PrimaryKey(autoGenerate = true)
    public long id;

    /** Local file path or content URI of the captured image. */
    public String imagePath;

    /** Predicted class label, taken from labels.txt. */
    public String predictedLabel;

    /** Model confidence for the predicted label, 0 to 1. */
    public float confidence;

    public double latitude;
    public double longitude;

    /** Field this diagnosis fell inside, or 0 if none matched. */
    public long fieldId;

    public long timestamp;

    /** False until SyncWorker has uploaded this record. */
    public boolean syncedToServer;
}
