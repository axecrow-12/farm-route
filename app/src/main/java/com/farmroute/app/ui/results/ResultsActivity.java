package com.farmroute.app.ui.results;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.farmroute.app.R;

import java.util.Locale;

/** Displays the diagnosis: image, predicted label, and confidence. */
public class ResultsActivity extends AppCompatActivity {

    public static final String EXTRA_LABEL = "label";
    public static final String EXTRA_CONFIDENCE = "confidence";
    public static final String EXTRA_IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        String label = getIntent().getStringExtra(EXTRA_LABEL);
        float confidence = getIntent().getFloatExtra(EXTRA_CONFIDENCE, 0f);
        String imagePath = getIntent().getStringExtra(EXTRA_IMAGE);

        TextView labelView = findViewById(R.id.text_label);
        TextView confidenceView = findViewById(R.id.text_confidence);
        ImageView imageView = findViewById(R.id.image_result);

        labelView.setText(prettify(label));
        confidenceView.setText(String.format(Locale.US, "Confidence: %.0f%%", confidence * 100));
        if (imagePath != null) {
            imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));
        }
    }

    /** Turn Tomato___Late_blight into Tomato, Late blight for display. */
    private String prettify(String raw) {
        if (raw == null) return "Unknown";
        return raw.replace("___", ", ").replace("_", " ");
    }
}
