package com.farmroute.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.farmroute.app.sync.SyncWorker;
import com.farmroute.app.ui.camera.CameraActivity;
import com.farmroute.app.ui.fields.FieldRegistrationActivity;
import com.farmroute.app.ui.history.HistoryActivity;

/**
 * Home screen. Three actions: diagnose a crop, register a field, view history.
 * Also schedules the deferred sync worker on launch.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SyncWorker.schedule(this);

        Button diagnose = findViewById(R.id.btn_diagnose);
        Button fields = findViewById(R.id.btn_fields);
        Button history = findViewById(R.id.btn_history);

        diagnose.setOnClickListener(v ->
                startActivity(new Intent(this, CameraActivity.class)));
        fields.setOnClickListener(v ->
                startActivity(new Intent(this, FieldRegistrationActivity.class)));
        history.setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class)));
    }
}
