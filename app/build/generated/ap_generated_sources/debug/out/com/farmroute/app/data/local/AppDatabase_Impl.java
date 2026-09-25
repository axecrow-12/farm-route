package com.farmroute.app.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile DiagnosisDao _diagnosisDao;

  private volatile FieldDao _fieldDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `diagnoses` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `imagePath` TEXT, `predictedLabel` TEXT, `confidence` REAL NOT NULL, `latitude` REAL NOT NULL, `longitude` REAL NOT NULL, `fieldId` INTEGER NOT NULL, `timestamp` INTEGER NOT NULL, `syncedToServer` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `fields` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `latitude` REAL NOT NULL, `longitude` REAL NOT NULL, `radiusMeters` REAL NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '12d836f72661274b24de6afaef7ddcd4')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `diagnoses`");
        db.execSQL("DROP TABLE IF EXISTS `fields`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsDiagnoses = new HashMap<String, TableInfo.Column>(9);
        _columnsDiagnoses.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiagnoses.put("imagePath", new TableInfo.Column("imagePath", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiagnoses.put("predictedLabel", new TableInfo.Column("predictedLabel", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiagnoses.put("confidence", new TableInfo.Column("confidence", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiagnoses.put("latitude", new TableInfo.Column("latitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiagnoses.put("longitude", new TableInfo.Column("longitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiagnoses.put("fieldId", new TableInfo.Column("fieldId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiagnoses.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDiagnoses.put("syncedToServer", new TableInfo.Column("syncedToServer", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDiagnoses = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDiagnoses = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDiagnoses = new TableInfo("diagnoses", _columnsDiagnoses, _foreignKeysDiagnoses, _indicesDiagnoses);
        final TableInfo _existingDiagnoses = TableInfo.read(db, "diagnoses");
        if (!_infoDiagnoses.equals(_existingDiagnoses)) {
          return new RoomOpenHelper.ValidationResult(false, "diagnoses(com.farmroute.app.data.local.Diagnosis).\n"
                  + " Expected:\n" + _infoDiagnoses + "\n"
                  + " Found:\n" + _existingDiagnoses);
        }
        final HashMap<String, TableInfo.Column> _columnsFields = new HashMap<String, TableInfo.Column>(6);
        _columnsFields.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFields.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFields.put("latitude", new TableInfo.Column("latitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFields.put("longitude", new TableInfo.Column("longitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFields.put("radiusMeters", new TableInfo.Column("radiusMeters", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFields.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFields = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFields = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFields = new TableInfo("fields", _columnsFields, _foreignKeysFields, _indicesFields);
        final TableInfo _existingFields = TableInfo.read(db, "fields");
        if (!_infoFields.equals(_existingFields)) {
          return new RoomOpenHelper.ValidationResult(false, "fields(com.farmroute.app.data.local.Field).\n"
                  + " Expected:\n" + _infoFields + "\n"
                  + " Found:\n" + _existingFields);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "12d836f72661274b24de6afaef7ddcd4", "a31b23e4300787c4e825def1facb5b87");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "diagnoses","fields");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `diagnoses`");
      _db.execSQL("DELETE FROM `fields`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(DiagnosisDao.class, DiagnosisDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FieldDao.class, FieldDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public DiagnosisDao diagnosisDao() {
    if (_diagnosisDao != null) {
      return _diagnosisDao;
    } else {
      synchronized(this) {
        if(_diagnosisDao == null) {
          _diagnosisDao = new DiagnosisDao_Impl(this);
        }
        return _diagnosisDao;
      }
    }
  }

  @Override
  public FieldDao fieldDao() {
    if (_fieldDao != null) {
      return _fieldDao;
    } else {
      synchronized(this) {
        if(_fieldDao == null) {
          _fieldDao = new FieldDao_Impl(this);
        }
        return _fieldDao;
      }
    }
  }
}
