package com.farmroute.app.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DiagnosisDao {

    @Insert
    long insert(Diagnosis diagnosis);

    @Query("SELECT * FROM diagnoses ORDER BY timestamp DESC")
    LiveData<List<Diagnosis>> observeAll();

    @Query("SELECT * FROM diagnoses WHERE syncedToServer = 0")
    List<Diagnosis> getUnsynced();

    @Query("UPDATE diagnoses SET syncedToServer = 1 WHERE id = :id")
    void markSynced(long id);

    @Query("SELECT * FROM diagnoses WHERE fieldId = :fieldId ORDER BY timestamp DESC")
    LiveData<List<Diagnosis>> observeForField(long fieldId);

    @Query("SELECT COUNT(*) FROM diagnoses WHERE fieldId = :fieldId AND timestamp > :since")
    int countRecentForField(long fieldId, long since);
}
