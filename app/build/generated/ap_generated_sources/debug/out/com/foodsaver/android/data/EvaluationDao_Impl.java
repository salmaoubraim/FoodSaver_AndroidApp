package com.foodsaver.android.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Double;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class EvaluationDao_Impl implements EvaluationDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Evaluation> __insertionAdapterOfEvaluation;

  private final EntityDeletionOrUpdateAdapter<Evaluation> __updateAdapterOfEvaluation;

  public EvaluationDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfEvaluation = new EntityInsertionAdapter<Evaluation>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `evaluations` (`id`,`userId`,`commerceId`,`rating`,`comment`,`dateEvaluation`,`isSynced`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final Evaluation entity) {
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
        if (entity.commerceId == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.commerceId);
        }
        statement.bindDouble(4, entity.rating);
        if (entity.comment == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.comment);
        }
        statement.bindLong(6, entity.dateEvaluation);
        final int _tmp = entity.isSynced ? 1 : 0;
        statement.bindLong(7, _tmp);
      }
    };
    this.__updateAdapterOfEvaluation = new EntityDeletionOrUpdateAdapter<Evaluation>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `evaluations` SET `id` = ?,`userId` = ?,`commerceId` = ?,`rating` = ?,`comment` = ?,`dateEvaluation` = ?,`isSynced` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final Evaluation entity) {
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
        if (entity.commerceId == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.commerceId);
        }
        statement.bindDouble(4, entity.rating);
        if (entity.comment == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.comment);
        }
        statement.bindLong(6, entity.dateEvaluation);
        final int _tmp = entity.isSynced ? 1 : 0;
        statement.bindLong(7, _tmp);
        if (entity.id == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.id);
        }
      }
    };
  }

  @Override
  public void insert(final Evaluation evaluation) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfEvaluation.insert(evaluation);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Evaluation evaluation) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfEvaluation.handle(evaluation);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Evaluation> getByCommerce(final String commerceId) {
    final String _sql = "SELECT * FROM evaluations WHERE commerceId = ? ORDER BY dateEvaluation DESC";
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
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfCommerceId = CursorUtil.getColumnIndexOrThrow(_cursor, "commerceId");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfComment = CursorUtil.getColumnIndexOrThrow(_cursor, "comment");
      final int _cursorIndexOfDateEvaluation = CursorUtil.getColumnIndexOrThrow(_cursor, "dateEvaluation");
      final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
      final List<Evaluation> _result = new ArrayList<Evaluation>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Evaluation _item;
        _item = new Evaluation();
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
        if (_cursor.isNull(_cursorIndexOfCommerceId)) {
          _item.commerceId = null;
        } else {
          _item.commerceId = _cursor.getString(_cursorIndexOfCommerceId);
        }
        _item.rating = _cursor.getFloat(_cursorIndexOfRating);
        if (_cursor.isNull(_cursorIndexOfComment)) {
          _item.comment = null;
        } else {
          _item.comment = _cursor.getString(_cursorIndexOfComment);
        }
        _item.dateEvaluation = _cursor.getLong(_cursorIndexOfDateEvaluation);
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
  public List<Evaluation> getByUser(final String userId) {
    final String _sql = "SELECT * FROM evaluations WHERE userId = ? ORDER BY dateEvaluation DESC";
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
      final int _cursorIndexOfCommerceId = CursorUtil.getColumnIndexOrThrow(_cursor, "commerceId");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfComment = CursorUtil.getColumnIndexOrThrow(_cursor, "comment");
      final int _cursorIndexOfDateEvaluation = CursorUtil.getColumnIndexOrThrow(_cursor, "dateEvaluation");
      final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
      final List<Evaluation> _result = new ArrayList<Evaluation>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Evaluation _item;
        _item = new Evaluation();
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
        if (_cursor.isNull(_cursorIndexOfCommerceId)) {
          _item.commerceId = null;
        } else {
          _item.commerceId = _cursor.getString(_cursorIndexOfCommerceId);
        }
        _item.rating = _cursor.getFloat(_cursorIndexOfRating);
        if (_cursor.isNull(_cursorIndexOfComment)) {
          _item.comment = null;
        } else {
          _item.comment = _cursor.getString(_cursorIndexOfComment);
        }
        _item.dateEvaluation = _cursor.getLong(_cursorIndexOfDateEvaluation);
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
  public Double getAverageRating(final String commerceId) {
    final String _sql = "SELECT AVG(rating) FROM evaluations WHERE commerceId = ?";
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
      final Double _result;
      if (_cursor.moveToFirst()) {
        final Double _tmp;
        if (_cursor.isNull(0)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getDouble(0);
        }
        _result = _tmp;
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
  public int getCountByCommerce(final String commerceId) {
    final String _sql = "SELECT COUNT(*) FROM evaluations WHERE commerceId = ?";
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
  public Evaluation getLastByUserAndCommerce(final String userId, final String commerceId) {
    final String _sql = "SELECT * FROM evaluations WHERE userId = ? AND commerceId = ? ORDER BY dateEvaluation DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (userId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userId);
    }
    _argIndex = 2;
    if (commerceId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, commerceId);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfCommerceId = CursorUtil.getColumnIndexOrThrow(_cursor, "commerceId");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfComment = CursorUtil.getColumnIndexOrThrow(_cursor, "comment");
      final int _cursorIndexOfDateEvaluation = CursorUtil.getColumnIndexOrThrow(_cursor, "dateEvaluation");
      final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
      final Evaluation _result;
      if (_cursor.moveToFirst()) {
        _result = new Evaluation();
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
        if (_cursor.isNull(_cursorIndexOfCommerceId)) {
          _result.commerceId = null;
        } else {
          _result.commerceId = _cursor.getString(_cursorIndexOfCommerceId);
        }
        _result.rating = _cursor.getFloat(_cursorIndexOfRating);
        if (_cursor.isNull(_cursorIndexOfComment)) {
          _result.comment = null;
        } else {
          _result.comment = _cursor.getString(_cursorIndexOfComment);
        }
        _result.dateEvaluation = _cursor.getLong(_cursorIndexOfDateEvaluation);
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
  public List<Evaluation> getUnsynced() {
    final String _sql = "SELECT * FROM evaluations WHERE isSynced = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfCommerceId = CursorUtil.getColumnIndexOrThrow(_cursor, "commerceId");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfComment = CursorUtil.getColumnIndexOrThrow(_cursor, "comment");
      final int _cursorIndexOfDateEvaluation = CursorUtil.getColumnIndexOrThrow(_cursor, "dateEvaluation");
      final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
      final List<Evaluation> _result = new ArrayList<Evaluation>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Evaluation _item;
        _item = new Evaluation();
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
        if (_cursor.isNull(_cursorIndexOfCommerceId)) {
          _item.commerceId = null;
        } else {
          _item.commerceId = _cursor.getString(_cursorIndexOfCommerceId);
        }
        _item.rating = _cursor.getFloat(_cursorIndexOfRating);
        if (_cursor.isNull(_cursorIndexOfComment)) {
          _item.comment = null;
        } else {
          _item.comment = _cursor.getString(_cursorIndexOfComment);
        }
        _item.dateEvaluation = _cursor.getLong(_cursorIndexOfDateEvaluation);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
