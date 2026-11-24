package com.foodsaver.android.data;

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
  private volatile UserDao _userDao;

  private volatile CommerceDao _commerceDao;

  private volatile PanierDao _panierDao;

  private volatile ReservationDao _reservationDao;

  private volatile EvaluationDao _evaluationDao;

  private volatile NotificationDao _notificationDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(6) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`id` TEXT NOT NULL, `email` TEXT, `name` TEXT, `role` TEXT, `isSynced` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `commerces` (`id` TEXT NOT NULL, `name` TEXT, `merchantId` TEXT, `latitude` REAL, `longitude` REAL, `address` TEXT, `city` TEXT, `categorie` TEXT, `imageUrl` TEXT, `rating` REAL NOT NULL, `quartier` TEXT, `isSynced` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `paniers` (`id` TEXT NOT NULL, `commerceId` TEXT, `titre` TEXT, `prix` REAL NOT NULL, `quantite` INTEGER NOT NULL, `description` TEXT, `imageUrl` TEXT, `categorie` TEXT, `isSynced` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `reservations` (`id` TEXT NOT NULL, `userId` TEXT, `panierId` TEXT, `commerceId` TEXT, `quantiteReservee` INTEGER NOT NULL, `dateReservation` INTEGER NOT NULL, `statut` TEXT, `isSynced` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `evaluations` (`id` TEXT NOT NULL, `userId` TEXT, `commerceId` TEXT, `rating` REAL NOT NULL, `comment` TEXT, `dateEvaluation` INTEGER NOT NULL, `isSynced` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `notifications` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userId` TEXT NOT NULL, `title` TEXT, `message` TEXT, `dateTime` INTEGER NOT NULL, `isRead` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '4f4acfb7af43027d67f535173a347e93')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `users`");
        db.execSQL("DROP TABLE IF EXISTS `commerces`");
        db.execSQL("DROP TABLE IF EXISTS `paniers`");
        db.execSQL("DROP TABLE IF EXISTS `reservations`");
        db.execSQL("DROP TABLE IF EXISTS `evaluations`");
        db.execSQL("DROP TABLE IF EXISTS `notifications`");
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
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(5);
        _columnsUsers.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("email", new TableInfo.Column("email", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("role", new TableInfo.Column("role", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("isSynced", new TableInfo.Column("isSynced", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(db, "users");
        if (!_infoUsers.equals(_existingUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "users(com.foodsaver.android.data.User).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        final HashMap<String, TableInfo.Column> _columnsCommerces = new HashMap<String, TableInfo.Column>(12);
        _columnsCommerces.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommerces.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommerces.put("merchantId", new TableInfo.Column("merchantId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommerces.put("latitude", new TableInfo.Column("latitude", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommerces.put("longitude", new TableInfo.Column("longitude", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommerces.put("address", new TableInfo.Column("address", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommerces.put("city", new TableInfo.Column("city", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommerces.put("categorie", new TableInfo.Column("categorie", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommerces.put("imageUrl", new TableInfo.Column("imageUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommerces.put("rating", new TableInfo.Column("rating", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommerces.put("quartier", new TableInfo.Column("quartier", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommerces.put("isSynced", new TableInfo.Column("isSynced", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCommerces = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCommerces = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCommerces = new TableInfo("commerces", _columnsCommerces, _foreignKeysCommerces, _indicesCommerces);
        final TableInfo _existingCommerces = TableInfo.read(db, "commerces");
        if (!_infoCommerces.equals(_existingCommerces)) {
          return new RoomOpenHelper.ValidationResult(false, "commerces(com.foodsaver.android.data.Commerce).\n"
                  + " Expected:\n" + _infoCommerces + "\n"
                  + " Found:\n" + _existingCommerces);
        }
        final HashMap<String, TableInfo.Column> _columnsPaniers = new HashMap<String, TableInfo.Column>(9);
        _columnsPaniers.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPaniers.put("commerceId", new TableInfo.Column("commerceId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPaniers.put("titre", new TableInfo.Column("titre", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPaniers.put("prix", new TableInfo.Column("prix", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPaniers.put("quantite", new TableInfo.Column("quantite", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPaniers.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPaniers.put("imageUrl", new TableInfo.Column("imageUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPaniers.put("categorie", new TableInfo.Column("categorie", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPaniers.put("isSynced", new TableInfo.Column("isSynced", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPaniers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPaniers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPaniers = new TableInfo("paniers", _columnsPaniers, _foreignKeysPaniers, _indicesPaniers);
        final TableInfo _existingPaniers = TableInfo.read(db, "paniers");
        if (!_infoPaniers.equals(_existingPaniers)) {
          return new RoomOpenHelper.ValidationResult(false, "paniers(com.foodsaver.android.data.Panier).\n"
                  + " Expected:\n" + _infoPaniers + "\n"
                  + " Found:\n" + _existingPaniers);
        }
        final HashMap<String, TableInfo.Column> _columnsReservations = new HashMap<String, TableInfo.Column>(8);
        _columnsReservations.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReservations.put("userId", new TableInfo.Column("userId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReservations.put("panierId", new TableInfo.Column("panierId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReservations.put("commerceId", new TableInfo.Column("commerceId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReservations.put("quantiteReservee", new TableInfo.Column("quantiteReservee", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReservations.put("dateReservation", new TableInfo.Column("dateReservation", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReservations.put("statut", new TableInfo.Column("statut", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReservations.put("isSynced", new TableInfo.Column("isSynced", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysReservations = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesReservations = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoReservations = new TableInfo("reservations", _columnsReservations, _foreignKeysReservations, _indicesReservations);
        final TableInfo _existingReservations = TableInfo.read(db, "reservations");
        if (!_infoReservations.equals(_existingReservations)) {
          return new RoomOpenHelper.ValidationResult(false, "reservations(com.foodsaver.android.data.Reservation).\n"
                  + " Expected:\n" + _infoReservations + "\n"
                  + " Found:\n" + _existingReservations);
        }
        final HashMap<String, TableInfo.Column> _columnsEvaluations = new HashMap<String, TableInfo.Column>(7);
        _columnsEvaluations.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvaluations.put("userId", new TableInfo.Column("userId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvaluations.put("commerceId", new TableInfo.Column("commerceId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvaluations.put("rating", new TableInfo.Column("rating", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvaluations.put("comment", new TableInfo.Column("comment", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvaluations.put("dateEvaluation", new TableInfo.Column("dateEvaluation", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEvaluations.put("isSynced", new TableInfo.Column("isSynced", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEvaluations = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesEvaluations = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoEvaluations = new TableInfo("evaluations", _columnsEvaluations, _foreignKeysEvaluations, _indicesEvaluations);
        final TableInfo _existingEvaluations = TableInfo.read(db, "evaluations");
        if (!_infoEvaluations.equals(_existingEvaluations)) {
          return new RoomOpenHelper.ValidationResult(false, "evaluations(com.foodsaver.android.data.Evaluation).\n"
                  + " Expected:\n" + _infoEvaluations + "\n"
                  + " Found:\n" + _existingEvaluations);
        }
        final HashMap<String, TableInfo.Column> _columnsNotifications = new HashMap<String, TableInfo.Column>(6);
        _columnsNotifications.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotifications.put("userId", new TableInfo.Column("userId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotifications.put("title", new TableInfo.Column("title", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotifications.put("message", new TableInfo.Column("message", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotifications.put("dateTime", new TableInfo.Column("dateTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotifications.put("isRead", new TableInfo.Column("isRead", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysNotifications = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesNotifications = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoNotifications = new TableInfo("notifications", _columnsNotifications, _foreignKeysNotifications, _indicesNotifications);
        final TableInfo _existingNotifications = TableInfo.read(db, "notifications");
        if (!_infoNotifications.equals(_existingNotifications)) {
          return new RoomOpenHelper.ValidationResult(false, "notifications(com.foodsaver.android.data.AppNotification).\n"
                  + " Expected:\n" + _infoNotifications + "\n"
                  + " Found:\n" + _existingNotifications);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "4f4acfb7af43027d67f535173a347e93", "54c1fb596271d74d48e121aaa1a5af67");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "users","commerces","paniers","reservations","evaluations","notifications");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `users`");
      _db.execSQL("DELETE FROM `commerces`");
      _db.execSQL("DELETE FROM `paniers`");
      _db.execSQL("DELETE FROM `reservations`");
      _db.execSQL("DELETE FROM `evaluations`");
      _db.execSQL("DELETE FROM `notifications`");
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
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CommerceDao.class, CommerceDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PanierDao.class, PanierDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ReservationDao.class, ReservationDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(EvaluationDao.class, EvaluationDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(NotificationDao.class, NotificationDao_Impl.getRequiredConverters());
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
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public CommerceDao commerceDao() {
    if (_commerceDao != null) {
      return _commerceDao;
    } else {
      synchronized(this) {
        if(_commerceDao == null) {
          _commerceDao = new CommerceDao_Impl(this);
        }
        return _commerceDao;
      }
    }
  }

  @Override
  public PanierDao panierDao() {
    if (_panierDao != null) {
      return _panierDao;
    } else {
      synchronized(this) {
        if(_panierDao == null) {
          _panierDao = new PanierDao_Impl(this);
        }
        return _panierDao;
      }
    }
  }

  @Override
  public ReservationDao reservationDao() {
    if (_reservationDao != null) {
      return _reservationDao;
    } else {
      synchronized(this) {
        if(_reservationDao == null) {
          _reservationDao = new ReservationDao_Impl(this);
        }
        return _reservationDao;
      }
    }
  }

  @Override
  public EvaluationDao evaluationDao() {
    if (_evaluationDao != null) {
      return _evaluationDao;
    } else {
      synchronized(this) {
        if(_evaluationDao == null) {
          _evaluationDao = new EvaluationDao_Impl(this);
        }
        return _evaluationDao;
      }
    }
  }

  @Override
  public NotificationDao notificationDao() {
    if (_notificationDao != null) {
      return _notificationDao;
    } else {
      synchronized(this) {
        if(_notificationDao == null) {
          _notificationDao = new NotificationDao_Impl(this);
        }
        return _notificationDao;
      }
    }
  }
}
