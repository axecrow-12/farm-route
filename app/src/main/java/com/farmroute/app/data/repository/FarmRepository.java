package com.farmroute.app.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.farmroute.app.data.local.AppDatabase;
import com.farmroute.app.data.local.Diagnosis;
import com.farmroute.app.data.local.DiagnosisDao;
import com.farmroute.app.data.local.Field;
import com.farmroute.app.data.local.FieldDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Single access point for local data. Write operations run on a background
 * executor so the UI thread is never blocked by database work.
 */
public class FarmRepository {

    private final DiagnosisDao diagnosisDao;
    private final FieldDao fieldDao;
    private final ExecutorService io = Executors.newSingleThreadExecutor();

    public FarmRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        diagnosisDao = db.diagnosisDao();
        fieldDao = db.fieldDao();
    }

    public interface Callback<T> { void onResult(T value); }

    public void saveDiagnosis(Diagnosis diagnosis, Callback<Long> callback) {
        io.execute(() -> {
            long id = diagnosisDao.insert(diagnosis);
            if (callback != null) callback.onResult(id);
        });
    }

    public void saveField(Field field, Callback<Long> callback) {
        io.execute(() -> {
            long id = fieldDao.insert(field);
            if (callback != null) callback.onResult(id);
        });
    }

    public void getFieldsBlocking(Callback<List<Field>> callback) {
        io.execute(() -> callback.onResult(fieldDao.getAllBlocking()));
    }

    public LiveData<List<Diagnosis>> observeDiagnoses() {
        return diagnosisDao.observeAll();
    }

    public LiveData<List<Field>> observeFields() {
        return fieldDao.observeAll();
    }

    public LiveData<List<Diagnosis>> observeForField(long fieldId) {
        return diagnosisDao.observeForField(fieldId);
    }
}
