package com.farmroute.app.ml;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads the quantised MobileNetV2 model and runs on device inference.
 *
 * IMPORTANT: INPUT_SIZE must match the IMG_SIZE used during training in the
 * Colab notebook. The lite training pipeline uses 160, so keep this at 160.
 */
public class TFLiteClassifier {

    private static final String MODEL_FILE = "farmroute_disease_model.tflite";
    private static final String LABELS_FILE = "labels.txt";
    private static final int INPUT_SIZE = 160;
    private static final int PIXEL_COUNT = INPUT_SIZE * INPUT_SIZE;

    private final Interpreter interpreter;
    private final List<String> labels;

    public static class Result {
        public final String label;
        public final float confidence;
        public Result(String label, float confidence) {
            this.label = label;
            this.confidence = confidence;
        }
    }

    public TFLiteClassifier(Context context) throws IOException {
        Interpreter.Options options = new Interpreter.Options();
        options.setNumThreads(2);
        interpreter = new Interpreter(loadModelFile(context), options);
        labels = loadLabels(context);
    }

    private MappedByteBuffer loadModelFile(Context context) throws IOException {
        AssetFileDescriptor fd = context.getAssets().openFd(MODEL_FILE);
        try (FileInputStream is = new FileInputStream(fd.getFileDescriptor())) {
            FileChannel channel = is.getChannel();
            return channel.map(FileChannel.MapMode.READ_ONLY,
                    fd.getStartOffset(), fd.getDeclaredLength());
        }
    }

    private List<String> loadLabels(Context context) throws IOException {
        List<String> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(context.getAssets().open(LABELS_FILE)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) result.add(line.trim());
            }
        }
        return result;
    }

    /** Runs inference on a bitmap and returns the top predicted class. */
    public Result classify(Bitmap bitmap) {
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, true);
        ByteBuffer input = convertBitmapToByteBuffer(resized);

        float[][] output = new float[1][labels.size()];
        interpreter.run(input, output);

        int bestIndex = 0;
        float bestScore = output[0][0];
        for (int i = 1; i < output[0].length; i++) {
            if (output[0][i] > bestScore) {
                bestScore = output[0][i];
                bestIndex = i;
            }
        }
        return new Result(labels.get(bestIndex), bestScore);
    }

    /**
     * The model expects float RGB. MobileNetV2 preprocessing scales pixels to
     * the range minus one to one, matching preprocess_input in the notebook.
     */
    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * PIXEL_COUNT * 3);
        buffer.order(ByteOrder.nativeOrder());
        int[] pixels = new int[PIXEL_COUNT];
        bitmap.getPixels(pixels, 0, INPUT_SIZE, 0, 0, INPUT_SIZE, INPUT_SIZE);
        for (int pixel : pixels) {
            float r = ((pixel >> 16) & 0xFF);
            float g = ((pixel >> 8) & 0xFF);
            float b = (pixel & 0xFF);
            buffer.putFloat(r / 127.5f - 1.0f);
            buffer.putFloat(g / 127.5f - 1.0f);
            buffer.putFloat(b / 127.5f - 1.0f);
        }
        return buffer;
    }

    public void close() {
        interpreter.close();
    }
}
