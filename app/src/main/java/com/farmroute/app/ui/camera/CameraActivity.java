package com.farmroute.app.ui.camera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.farmroute.app.R;
import com.farmroute.app.data.local.Diagnosis;
import com.farmroute.app.data.local.Field;
import com.farmroute.app.data.repository.FarmRepository;
import com.farmroute.app.location.GeofenceManager;
import com.farmroute.app.ml.TFLiteClassifier;
import com.farmroute.app.ui.results.ResultsActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Captures a leaf image with CameraX, runs on device inference, reads the GPS
 * location, matches it to a registered field, saves the diagnosis, and shows
 * the result. The entire flow works with no network connection.
 */
public class CameraActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST = 100;
    private ImageCapture imageCapture;
    private ExecutorService cameraExecutor;
    private TFLiteClassifier classifier;
    private FarmRepository repository;
    private FusedLocationProviderClient locationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        repository = new FarmRepository(this);
        locationClient = LocationServices.getFusedLocationProviderClient(this);
        cameraExecutor = Executors.newSingleThreadExecutor();

        try {
            classifier = new TFLiteClassifier(this);
        } catch (Exception e) {
            Toast.makeText(this, "Model failed to load. Check assets.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        findViewById(R.id.btn_capture).setOnClickListener(v -> captureAndClassify());

        if (hasPermissions()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, requiredPermissions(), PERMISSION_REQUEST);
        }
    }

    private String[] requiredPermissions() {
        return new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION
        };
    }

    private boolean hasPermissions() {
        for (String p : requiredPermissions()) {
            if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void startCamera() {
        PreviewView previewView = findViewById(R.id.preview_view);
        ListenableFuture<ProcessCameraProvider> future = ProcessCameraProvider.getInstance(this);
        future.addListener(() -> {
            try {
                ProcessCameraProvider provider = future.get();
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());
                imageCapture = new ImageCapture.Builder().build();
                provider.unbindAll();
                provider.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageCapture);
            } catch (Exception e) {
                Toast.makeText(this, "Camera init failed", Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void captureAndClassify() {
        if (imageCapture == null) return;
        File photoFile = new File(getFilesDir(), "capture_" + System.currentTimeMillis() + ".jpg");
        ImageCapture.OutputFileOptions options =
                new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(options, cameraExecutor, new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults results) {
                Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                TFLiteClassifier.Result result = classifier.classify(bitmap);
                resolveLocationAndSave(photoFile.getAbsolutePath(), result);
            }

            @Override
            public void onError(@NonNull ImageCaptureException e) {
                runOnUiThread(() ->
                        Toast.makeText(CameraActivity.this, "Capture failed", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void resolveLocationAndSave(String imagePath, TFLiteClassifier.Result result) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            save(imagePath, result, 0, 0, 0);
            return;
        }
        locationClient.getLastLocation().addOnSuccessListener(location -> {
            double lat = location != null ? location.getLatitude() : 0;
            double lng = location != null ? location.getLongitude() : 0;
            repository.getFieldsBlocking(fields -> {
                Field matched = GeofenceManager.matchField(lat, lng, fields);
                long fieldId = matched != null ? matched.id : 0;
                save(imagePath, result, lat, lng, fieldId);
            });
        });
    }

    private void save(String imagePath, TFLiteClassifier.Result result,
                      double lat, double lng, long fieldId) {
        Diagnosis d = new Diagnosis();
        d.imagePath = imagePath;
        d.predictedLabel = result.label;
        d.confidence = result.confidence;
        d.latitude = lat;
        d.longitude = lng;
        d.fieldId = fieldId;
        d.timestamp = System.currentTimeMillis();
        d.syncedToServer = false;

        repository.saveDiagnosis(d, id -> runOnUiThread(() -> {
            Intent intent = new Intent(this, ResultsActivity.class);
            intent.putExtra(ResultsActivity.EXTRA_LABEL, result.label);
            intent.putExtra(ResultsActivity.EXTRA_CONFIDENCE, result.confidence);
            intent.putExtra(ResultsActivity.EXTRA_IMAGE, imagePath);
            startActivity(intent);
            finish();
        }));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST && hasPermissions()) {
            startCamera();
        } else {
            Toast.makeText(this, "Camera and location are required", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraExecutor != null) cameraExecutor.shutdown();
        if (classifier != null) classifier.close();
    }
}
