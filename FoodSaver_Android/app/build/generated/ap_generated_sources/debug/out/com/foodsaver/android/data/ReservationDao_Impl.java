package com.foodsaver.android.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ReservationDao_Impl implements ReservationDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Reservation> __insertionAdapterOfReservation;

  private final EntityDeletionOrUpdateAdapter<Reservation> __updateAdapterOfReservation;

  private final SharedSQLiteStatement __preparedStmtOfDelete;

  private final SharedSQLiteStatement __preparedStmtOfUpdateStatut;

  public ReservationDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfReservation = new EntityInsertionAdapter<Reservation>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `reservations` (`id`,`userId`,`panierId`,`commerceId`,`quantiteReservee`,`dateReservation`,`statut`,`isSynced`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final Reservation entity) {
        if (entity.id == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.id);
        }
        if (entity.userId == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.userId);
        }
        if (entity.panierId == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.panierId);
        }
        if (entity.commerceId == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.commerceId);
        }
        statement.bindLong(5, entity.quantiteReservee);
        statement.bindLong(6, entity.dateReservation);
        if (entity.statut == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.statut);
        }
        final int _tmp = entity.isSynced ? 1 : 0;
        statement.bindLong(8, _tmp);
      }
    };
    this.__updateAdapterOfReservation = new EntityDeletionOrUpdateAdapter<Reservation>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `reservations` SET `id` = ?,`userId` = ?,`panierId` = ?,`commerceId` = ?,`quantiteReservee` = ?,`dateReservation` = ?,`statut` = ?,`isSynced` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final Reservation entity) {
        if (entity.id == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.id);
        }
        if (entity.userId == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.userId);
        }
        if (entity.panierId == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.panierId);
        }
        if (entity.commerceId == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.commerceId);
        }
        statement.bindLong(5, entity.quantiteReservee);
        statement.bindLong(6, entity.dateReservation);
        if (entity.statut == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.statut);
        }
        final int _tmp = entity.isSynced ? 1 : 0;
        statement.bindLong(8, _tmp);
        if (entity.id == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.id);
        }
      }
    };
    this.__preparedStmtOfDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM reservations WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateStatut = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE reservations SET statut = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public void insert(final Reservation reservation) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfReservation.insert(reservation);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Reservation reservation) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfReservation.handle(reservation);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final String id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDelete.acquire();
    int _argIndex = 1;
    if (id == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, id);
    }
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDelete.release(_stmt);
    }
  }

  @Override
  public void updateStatut(final String id, final String statut) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateStatut.acquire();
    int _argIndex = 1;
    if (statut == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, statut);
    }
    _argIndex = 2;
    if (id == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, id);
    }
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfUpdateStatut.release(_stmt);
    }
  }

  @Override
  public List<Reservation> getByUser(final String userId) {
    final String _sql = "SELECT * FROM reservations WHERE userId = ? ORDER BY dateReservation DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (userId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfPanierId = CursorUtil.getColumnIndexOrThrow(_cursor, "panierId");
      final int _cursorIndexOfCommerceId = CursorUtil.getColumnIndexOrThrow(_cursor, "commerceId");
      final int _cursorIndexOfQuantiteReservee = CursorUtil.getColumnIndexOrThrow(_cursor, "quantiteReservee");
      final int _cursorIndexOfDateReservation = CursorUtil.getColumnIndexOrThrow(_cursor, "dateReservation");
      final int _cursorIndexOfStatut = CursorUtil.getColumnIndexOrThrow(_cursor, "statut");
      final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
      final List<Reservation> _result = new ArrayList<Reservation>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Reservation _item;
        _item = new Reservation();
        if (_cursor.isNull(_cursorIndexOfId)) {
          _item.id = null;
        } else {
          _item.id = _cursor.getString(_cursorIndexOfId);
        }
        if (_cursor.isNull(_cursorIndexOfUserId)) {
          _item.userId = null;
        } else {
          _item.userId = _cursor.getString(_cursorIndexOfUserId);
        }
        if (_cursor.isNull(_cursorIndexOfPanierId)) {
          _item.panierId = null;
        } else {
          _item.panierId = _cursor.getString(_cursorIndexOfPanierId);
        }
        if (_cursor.isNull(_cursorIndexOfCommerceId)) {
          _item.commerceId = null;
        } else {
          _item.commerceId = _cursor.getString(_cursorIndexOfCommerceId);
        }
        _item.quantiteReservee = _cursor.getInt(_cursorIndexOfQuantiteReservee);
        _item.dateReservation = _cursor.getLong(_cursorIndexOfDateReservation);
        if (_cursor.isNull(_cursorIndexOfStatut)) {
          _item.statut = null;
        } else {
          _item.statut = _cursor.getString(_cursorIndexOfStatut);
        }
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
        _item.isSynced = _tmp != 0;
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Reservation> getByMerchant(final String merchantId) {
    final String _sql = "SELECT * FROM reservations WHERE commerceId IN (SELECT id FROM commerces WHERE merchantId = ?) ORDER BY dateReservation DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (merchantId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, merchantId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfPanierId = CursorUtil.getColumnIndexOrThrow(_cursor, "panierId");
      final int _cursorIndexOfCommerceId = CursorUtil.getColumnIndexOrThrow(_cursor, "commerceId");
      final int _cursorIndexOfQuantiteReservee = CursorUtil.getColumnIndexOrThrow(_cursor, "quantiteReservee");
      final int _cursorIndexOfDateReservation = CursorUtil.getColumnIndexOrThrow(_cursor, "dateReservation");
      final int _cursorIndexOfStatut = CursorUtil.getColumnIndexOrThrow(_cursor, "statut");
      final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
      final List<Reservation> _result = new ArrayList<Reservation>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Reservation _item;
        _item = new Reservation();
        if (_cursor.isNull(_cursorIndexOfId)) {
          _item.id = null;
        } else {
          _item.id = _cursor.getString(_cursorIndexOfId);
        }
        if (_cursor.isNull(_cursorIndexOfUserId)) {
          _item.userId = null;
        } else {
          _item.userId = _cursor.getString(_cursorIndexOfUserId);
        }
        if (_cursor.isNull(_cursorIndexOfPanierId)) {
          _item.panierId = null;
        } else {
          _item.panierId = _cursor.getString(_cursorIndexOfPanierId);
        }
        if (_cursor.isNull(_cursorIndexOfCommerceId)) {
          _item.commerceId = null;
        } else {
          _item.commerceId = _cursor.getString(_cursorIndexOfCommerceId);
        }
        _item.quantiteReservee = _cursor.getInt(_cursorIndexOfQuantiteReservee);
        _item.dateReservation = _cursor.getLong(_cursorIndexOfDateReservation);
        if (_cursor.isNull(_cursorIndexOfStatut)) {
          _item.statut = null;
        } else {
          _item.statut = _cursor.getString(_cursorIndexOfStatut);
        }
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
        _item.isSynced = _tmp != 0;
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Reservation getById(final String id) {
    final String _sql = "SELECT * FROM reservations WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfPanierId = CursorUtil.getColumnIndexOrThrow(_cursor, "panierId");
      final int _cursorIndexOfCommerceId = CursorUtil.getColumnIndexOrThrow(_cursor, "commerceId");
      final int _cursorIndexOfQuantiteReservee = CursorUtil.getColumnIndexOrThrow(_cursor, "quantiteReservee");
      final int _cursorIndexOfDateReservation = CursorUtil.getColumnIndexOrThrow(_cursor, "dateReservation");
      final int _cursorIndexOfStatut = CursorUtil.getColumnIndexOrThrow(_cursor, "statut");
      final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
      final Reservation _result;
      if (_cursor.moveToFirst()) {
        _result = new Reservation();
        if (_cursor.isNull(_cursorIndexOfId)) {
          _result.id = null;
        } else {
          _result.id = _cursor.getString(_cursorIndexOfId);
        }
        if (_cursor.isNull(_cursorIndexOfUserId)) {
          _result.userId = null;
        } else {
          _result.userId = _cursor.getString(_cursorIndexOfUserId);
        }
        if (_cursor.isNull(_cursorIndexOfPanierId)) {
          _result.panierId = null;
        } else {
          _result.panierId = _cursor.getString(_cursorIndexOfPanierId);
        }
        if (_cursor.isNull(_cursorIndexOfCommerceId)) {
          _result.commerceId = null;
        } else {
          _result.commerceId = _cursor.getString(_cursorIndexOfCommerceId);
        }
        _result.quantiteReservee = _cursor.getInt(_cursorIndexOfQuantiteReservee);
        _result.dateReservation = _cursor.getLong(_cursorIndexOfDateReservation);
        if (_cursor.isNull(_cursorIndexOfStatut)) {
          _result.statut = null;
        } else {
          _result.statut = _cursor.getString(_cursorIndexOfStatut);
        }
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
        _result.isSynced = _tmp != 0;
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Reservation> getUnsynced() {
    final String _sql = "SELECT * FROM reservations WHERE isSynced = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfPanierId = CursorUtil.getColumnIndexOrThrow(_cursor, "panierId");
      final int _cursorIndexOfCommerceId = CursorUtil.getColumnIndexOrThrow(_cursor, "commerceId");
      final int _cursorIndexOfQuantiteReservee = CursorUtil.getColumnIndexOrThrow(_cursor, "quantiteReservee");
      final int _cursorIndexOfDateReservation = CursorUtil.getColumnIndexOrThrow(_cursor, "dateReservation");
      final int _cursorIndexOfStatut = CursorUtil.getColumnIndexOrThrow(_cursor, "statut");
      final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
      final List<Reservation> _result = new ArrayList<Reservation>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Reservation _item;
        _item = new Reservation();
        if (_cursor.isNull(_cursorIndexOfId)) {
          _item.id = null;
        } else {
          _item.id = _cursor.getString(_cursorIndexOfId);
        }
        if (_cursor.isNull(_cursorIndexOfUserId)) {
          _item.userId = null;
        } else {
          _item.userId = _cursor.getString(_cursorIndexOfUserId);
        }
        if (_cursor.isNull(_cursorIndexOfPanierId)) {
          _item.panierId = null;
        } else {
          _item.panierId = _cursor.getString(_cursorIndexOfPanierId);
        }
        if (_cursor.isNull(_cursorIndexOfCommerceId)) {
          _item.commerceId = null;
        } else {
          _item.commerceId = _cursor.getString(_cursorIndexOfCommerceId);
        }
        _item.quantiteReservee = _cursor.getInt(_cursorIndexOfQuantiteReservee);
        _item.dateReservation = _cursor.getLong(_cursorIndexOfDateReservation);
        if (_cursor.isNull(_cursorIndexOfStatut)) {
          _item.statut = null;
        } else {
          _item.statut = _cursor.getString(_cursorIndexOfStatut);
        }
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
        _item.isSynced = _tmp != 0;
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int countAll() {
    final String _sql = "SELECT COUNT(*) FROM reservations";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
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

  @Override
  public int countByCommerce(final String commerceId) {
    final String _sql = "SELECT COUNT(*) FROM reservations WHERE commerceId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (commerceId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, commerceId);
    }
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

  @Override
  public int countActiveByCommerce(final String commerceId) {
    final String _sql = "SELECT COUNT(*) FROM reservations WHERE commerceId = ? AND (statut IS NULL OR statut != 'annulee')";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (commerceId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, commerceId);
    }
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

  @Override
  public void deleteByPanierIds(final List<String> panierIds) {
    __db.assertNotSuspendingTransaction();
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("DELETE FROM reservations WHERE panierId IN (");
    final int _inputSize = panierIds == null ? 1 : panierIds.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final SupportSQLiteStatement _stmt = __db.compileStatement(_sql);
    int _argIndex = 1;
    if (panierIds == null) {
      _stmt.bindNull(_argIndex);
    } else {
      for (String _item : panierIds) {
        if (_item == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, _item);
        }
        _argIndex++;
      }
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
