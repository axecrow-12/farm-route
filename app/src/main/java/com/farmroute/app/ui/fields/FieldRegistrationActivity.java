package com.farmroute.app.ui.fields;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.farmroute.app.R;
import com.farmroute.app.data.local.Field;
import com.farmroute.app.data.repository.FarmRepository;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

/**
 * Registers a field at the farmer current location with a chosen name and
 * radius. The field is stored as a circular geofence used later to tag
 * diagnoses.
 */
public class FieldRegistrationActivity extends AppCompatActivity {

    private FarmRepository repository;
    private FusedLocationProviderClient locationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_registration);

        repository = new FarmRepository(this);
        locationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }

        EditText nameInput = findViewById(R.id.input_name);
        EditText radiusInput = findViewById(R.id.input_radius);
        Button saveButton = findViewById(R.id.btn_save_field);

        saveButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Enter a field name", Toast.LENGTH_SHORT).show();
                return;
            }
            float radius;
            try {
                radius = Float.parseFloat(radiusInput.getText().toString().trim());
            } catch (NumberFormatException e) {
                radius = 100f;
            }
            saveAtCurrentLocation(name, radius);
        });
    }

    private void saveAtCurrentLocation(String name, float radius) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Location permission required", Toast.LENGTH_SHORT).show();
            return;
        }
        locationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location == null) {
                Toast.makeText(this, "Could not read location, try again", Toast.LENGTH_SHORT).show();
                return;
            }
            Field field = new Field();
            field.name = name;
            field.latitude = location.getLatitude();
            field.longitude = location.getLongitude();
            field.radiusMeters = radius;
            field.createdAt = System.currentTimeMillis();

            repository.saveField(field, id -> runOnUiThread(() -> {
                Toast.makeText(this, "Field saved", Toast.LENGTH_SHORT).show();
                finish();
            }));
        });
    }
}
