# Model assets

Two files from the FarmRoute Lite Training notebook go in this folder:

1. `farmroute_disease_model.tflite` — the quantised classifier
2. `labels.txt` — the class labels, one per line, in the exact model output order

Do not rename them. The app looks for these exact filenames.

If you change the input size in the notebook (for example to 128 to save
memory), update `INPUT_SIZE` in `TFLiteClassifier.java` to match, or inference
will be wrong.
