package com.farmroute.app.sync;

import com.farmroute.app.data.local.Diagnosis;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Backend contract for uploading diagnoses. The backend is optional; the app
 * is fully functional if this endpoint is never reachable.
 */
public interface SyncApi {

    @POST("api/diagnoses")
    Call<Void> uploadDiagnoses(@Body List<Diagnosis> diagnoses);
}
