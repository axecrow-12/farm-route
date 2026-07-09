package com.farmroute.app.sync;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.farmroute.app.data.local.AppDatabase;
import com.farmroute.app.data.local.Diagnosis;
import com.farmroute.app.data.local.DiagnosisDao;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Deferred, opportunistic upload. WorkManager only runs this when the declared
 * constraints (connected, battery not low) are satisfied, so the app never
 * spends the farmer data or battery at a bad time.
 */
public class SyncWorker extends Worker {

    // Replace with your deployed backend base URL when the backend exists.
    private static final String BASE_URL = "https://your-backend.example.com/";
    private static final String WORK_NAME = "farmroute_sync";

    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        DiagnosisDao dao = AppDatabase.getInstance(getApplicationContext()).diagnosisDao();
        List<Diagnosis> unsynced = dao.getUnsynced();
        if (unsynced.isEmpty()) return Result.success();

        try {
            SyncApi api = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(SyncApi.class);

            Response<Void> response = api.uploadDiagnoses(unsynced).execute();
            if (response.isSuccessful()) {
                for (Diagnosis d : unsynced) dao.markSynced(d.id);
                return Result.success();
            }
            return Result.retry();
        } catch (Exception e) {
            // No connectivity or backend down: retry later, nothing is lost.
            return Result.retry();
        }
    }

    /** Call once at app start to schedule periodic sync. */
    public static void schedule(Context context) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build();

        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(
                SyncWorker.class, 6, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, request);
    }
}
