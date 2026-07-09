package com.farmroute.app.ui.history;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.farmroute.app.R;
import com.farmroute.app.data.repository.FarmRepository;

/** Lists all past diagnoses, most recent first, streamed live from Room. */
public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        RecyclerView recycler = findViewById(R.id.recycler_history);
        TextView empty = findViewById(R.id.text_empty);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        HistoryAdapter adapter = new HistoryAdapter();
        recycler.setAdapter(adapter);

        FarmRepository repository = new FarmRepository(this);
        repository.observeDiagnoses().observe(this, diagnoses -> {
            adapter.submit(diagnoses);
            empty.setVisibility(diagnoses.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }
}
