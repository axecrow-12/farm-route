package com.farmroute.app.data.local;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class DiagnosisDao_Impl implements DiagnosisDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Diagnosis> __insertionAdapterOfDiagnosis;

  private final SharedSQLiteStatement __preparedStmtOfMarkSynced;

  public DiagnosisDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDiagnosis = new EntityInsertionAdapter<Diagnosis>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `diagnoses` (`id`,`imagePath`,`predictedLabel`,`confidence`,`latitude`,`longitude`,`fieldId`,`timestamp`,`syncedToServer`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Diagnosis entity) {
        statement.bindLong(1, entity.id);
        if (entity.imagePath == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.imagePath);
        }
        if (entity.predictedLabel == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.predictedLabel);
        }
        statement.bindDouble(4, entity.confidence);
        statement.bindDouble(5, entity.latitude);
        statement.bindDouble(6, entity.longitude);
        statement.bindLong(7, entity.fieldId);
        statement.bindLong(8, entity.timestamp);
        final int _tmp = entity.syncedToServer ? 1 : 0;
        statement.bindLong(9, _tmp);
      }
    };
    this.__preparedStmtOfMarkSynced = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE diagnoses SET syncedToServer = 1 WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public long insert(final Diagnosis diagnosis) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfDiagnosis.insertAndReturnId(diagnosis);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void markSynced(final long id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfMarkSynced.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfMarkSynced.release(_stmt);
    }
  }

  @Override
  public LiveData<List<Diagnosis>> observeAll() {
    final String _sql = "SELECT * FROM diagnoses ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"diagnoses"}, false, new Callable<List<Diagnosis>>() {
      @Override
      @Nullable
      public List<Diagnosis> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
          final int _cursorIndexOfPredictedLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "predictedLabel");
          final int _cursorIndexOfConfidence = CursorUtil.getColumnIndexOrThrow(_cursor, "confidence");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfFieldId = CursorUtil.getColumnIndexOrThrow(_cursor, "fieldId");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfSyncedToServer = CursorUtil.getColumnIndexOrThrow(_cursor, "syncedToServer");
          final List<Diagnosis> _result = new ArrayList<Diagnosis>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Diagnosis _item;
            _item = new Diagnosis();
            _item.id = _cursor.getLong(_cursorIndexOfId);
            if (_cursor.isNull(_cursorIndexOfImagePath)) {
              _item.imagePath = null;
            } else {
              _item.imagePath = _cursor.getString(_cursorIndexOfImagePath);
            }
            if (_cursor.isNull(_cursorIndexOfPredictedLabel)) {
              _item.predictedLabel = null;
            } else {
              _item.predictedLabel = _cursor.getString(_cursorIndexOfPredictedLabel);
            }
            _item.confidence = _cursor.getFloat(_cursorIndexOfConfidence);
            _item.latitude = _cursor.getDouble(_cursorIndexOfLatitude);
            _item.longitude = _cursor.getDouble(_cursorIndexOfLongitude);
            _item.fieldId = _cursor.getLong(_cursorIndexOfFieldId);
            _item.timestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSyncedToServer);
            _item.syncedToServer = _tmp != 0;
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public List<Diagnosis> getUnsynced() {
    final String _sql = "SELECT * FROM diagnoses WHERE syncedToServer = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
      final int _cursorIndexOfPredictedLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "predictedLabel");
      final int _cursorIndexOfConfidence = CursorUtil.getColumnIndexOrThrow(_cursor, "confidence");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfFieldId = CursorUtil.getColumnIndexOrThrow(_cursor, "fieldId");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final int _cursorIndexOfSyncedToServer = CursorUtil.getColumnIndexOrThrow(_cursor, "syncedToServer");
      final List<Diagnosis> _result = new ArrayList<Diagnosis>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Diagnosis _item;
        _item = new Diagnosis();
        _item.id = _cursor.getLong(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfImagePath)) {
          _item.imagePath = null;
        } else {
          _item.imagePath = _cursor.getString(_cursorIndexOfImagePath);
        }
        if (_cursor.isNull(_cursorIndexOfPredictedLabel)) {
          _item.predictedLabel = null;
        } else {
          _item.predictedLabel = _cursor.getString(_cursorIndexOfPredictedLabel);
        }
        _item.confidence = _cursor.getFloat(_cursorIndexOfConfidence);
        _item.latitude = _cursor.getDouble(_cursorIndexOfLatitude);
        _item.longitude = _cursor.getDouble(_cursorIndexOfLongitude);
        _item.fieldId = _cursor.getLong(_cursorIndexOfFieldId);
        _item.timestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfSyncedToServer);
        _item.syncedToServer = _tmp != 0;
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<Diagnosis>> observeForField(final long fieldId) {
    final String _sql = "SELECT * FROM diagnoses WHERE fieldId = ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, fieldId);
    return __db.getInvalidationTracker().createLiveData(new String[] {"diagnoses"}, false, new Callable<List<Diagnosis>>() {
      @Override
      @Nullable
      public List<Diagnosis> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfImagePath = CursorUtil.getColumnIndexOrThrow(_cursor, "imagePath");
          final int _cursorIndexOfPredictedLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "predictedLabel");
          final int _cursorIndexOfConfidence = CursorUtil.getColumnIndexOrThrow(_cursor, "confidence");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfFieldId = CursorUtil.getColumnIndexOrThrow(_cursor, "fieldId");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfSyncedToServer = CursorUtil.getColumnIndexOrThrow(_cursor, "syncedToServer");
          final List<Diagnosis> _result = new ArrayList<Diagnosis>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Diagnosis _item;
            _item = new Diagnosis();
            _item.id = _cursor.getLong(_cursorIndexOfId);
            if (_cursor.isNull(_cursorIndexOfImagePath)) {
              _item.imagePath = null;
            } else {
              _item.imagePath = _cursor.getString(_cursorIndexOfImagePath);
            }
            if (_cursor.isNull(_cursorIndexOfPredictedLabel)) {
              _item.predictedLabel = null;
            } else {
              _item.predictedLabel = _cursor.getString(_cursorIndexOfPredictedLabel);
            }
            _item.confidence = _cursor.getFloat(_cursorIndexOfConfidence);
            _item.latitude = _cursor.getDouble(_cursorIndexOfLatitude);
            _item.longitude = _cursor.getDouble(_cursorIndexOfLongitude);
            _item.fieldId = _cursor.getLong(_cursorIndexOfFieldId);
            _item.timestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfSyncedToServer);
            _item.syncedToServer = _tmp != 0;
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public int countRecentForField(final long fieldId, final long since) {
    final String _sql = "SELECT COUNT(*) FROM diagnoses WHERE fieldId = ? AND timestamp > ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, fieldId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, since);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
