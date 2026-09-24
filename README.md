# FarmRoute

An offline first, geofenced crop disease monitoring platform for smallholder farmers in Zimbabwe.

A farmer photographs an affected leaf, and FarmRoute classifies the likely disease within seconds, entirely on the device, with no internet connection. Each diagnosis is tagged to the field where it was taken. When connectivity appears, records sync opportunistically to a central service, building a district level picture of disease pressure.

## Why offline first

Cloud based crop diagnosis tools assume reliable, affordable connectivity, an assumption that fails across much of rural Zimbabwe. FarmRoute inverts that model: all inference runs on the phone using a compact quantised neural network, location tagging uses GPS and geofencing with no live map download, and synchronisation is deferred rather than continuous. The app is fully usable even if the backend is never reached.

## Features

- On device crop disease classification using a quantised MobileNetV2 model (TensorFlow Lite)
- GPS location tagging with per field geofencing
- Fully offline capture, inference, and storage
- Deferred, constraint aware background sync (WorkManager)
- Local diagnosis history with sync status

## Architecture

| Layer | Responsibility | Key components |
|-------|----------------|----------------|
| Presentation | Capture, results, history | Activities, CameraX, RecyclerView |
| Domain | Inference, geofencing, sync | TFLiteClassifier, GeofenceManager, SyncWorker |
| Data | Local persistence | Room database, DAOs, entities, repository |
| Platform | Device services | FusedLocationProvider, WorkManager |
| Backend (optional) | Aggregation | REST sync endpoint |

## Tech stack

Java, Room (SQLite), CameraX, TensorFlow Lite, FusedLocationProvider, WorkManager, Retrofit.

## Project structure

```
app/src/main/java/com/farmroute/app/
  MainActivity.java
  data/local/        Room entities, DAOs, database
  data/repository/   FarmRepository
  ml/                TFLiteClassifier
  location/          GeofenceManager
  sync/              SyncWorker, SyncApi
  ui/camera/         CameraActivity
  ui/results/        ResultsActivity
  ui/fields/         FieldRegistrationActivity
  ui/history/        HistoryActivity, HistoryAdapter
app/src/main/assets/  model + labels go here
training/             FarmRoute_Lite_Training.ipynb (produces the model)
```

## Getting started

1. Clone the repo and open it in Android Studio.
2. Train the model with [`training/FarmRoute_Lite_Training.ipynb`](training/FarmRoute_Lite_Training.ipynb) in Google Colab (see the ML section below), or use your own.
3. Drop `farmroute_disease_model.tflite` and `labels.txt` into `app/src/main/assets/`.
4. Build and run on a device with a camera (an emulator will not have a real camera or GPS).

The project ships with a placeholder `labels.txt` so it compiles before you add a real model. Replace it with the one your training run produces.

## The machine learning model

The classifier is a MobileNetV2 with the narrowest width multiplier (alpha 0.35), trained with transfer learning on the PlantVillage dataset, filtered to the crops most relevant to Zimbabwean smallholders: maize, tomato, potato, and pepper. Training streams images from disk and exports a quantised TFLite model so it runs on entry level phones and trains within free tier cloud limits.

To train it, open `training/FarmRoute_Lite_Training.ipynb` in Google Colab, choose a T4 GPU runtime, and run all cells (roughly 30 to 45 minutes on the free tier). The notebook downloads only the 19 needed PlantVillage classes, trains in two stages (new head, then fine tuning the top of the backbone), reports per class accuracy on a held out test set, and exports an int8 quantised model with float input and output. Before downloading, it checks the exported model against the app's expectations: input shape, output size, data types, and accuracy after quantisation.

Important: the training input size (160 x 160) must match `INPUT_SIZE` in `TFLiteClassifier.java`. If you change one, change the other.

## The optional backend

Set your base URL in `SyncWorker.java` and implement a `POST api/diagnoses` endpoint that accepts a JSON array of diagnoses. Until then, records simply accumulate on the device and are marked pending, losing nothing.

## Roadmap

- Phase 1: model and core offline app
- Phase 2: location intelligence and geofencing
- Phase 3: synchronisation and backend
- Phase 4: district aggregation dashboard
- Phase 5: field validation and model retraining on real captures

## Author

Wilmar Takudzwa Macheke, BTech Software Engineering, Harare Institute of Technology.

## License

MIT. See LICENSE.
