package com.farmroute.app.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FieldDao {

    @Insert
    long insert(Field field);

    @Query("SELECT * FROM fields ORDER BY createdAt DESC")
    LiveData<List<Field>> observeAll();

    @Query("SELECT * FROM fields")
    List<Field> getAllBlocking();
}
