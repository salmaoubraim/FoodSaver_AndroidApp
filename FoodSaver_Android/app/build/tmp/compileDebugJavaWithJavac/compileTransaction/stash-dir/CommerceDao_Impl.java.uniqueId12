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
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CommerceDao_Impl implements CommerceDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Commerce> __insertionAdapterOfCommerce;

  private final EntityDeletionOrUpdateAdapter<Commerce> __updateAdapterOfCommerce;

  private final SharedSQLiteStatement __preparedStmtOfDelete;

  public CommerceDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCommerce = new EntityInsertionAdapter<Commerce>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `commerces` (`id`,`name`,`merchantId`,`latitude`,`longitude`,`address`,`city`,`categorie`,`imageUrl`,`rating`,`quartier`,`isSynced`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Commerce entity) {
        if (entity.id == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.id);
        }
        if (entity.name == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.name);
        }
        if (entity.merchantId == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.merchantId);
        }
        if (entity.latitude == null) {
          statement.bindNull(4);
        } else {
          statement.bindDouble(4, entity.latitude);
        }
        if (entity.longitude == null) {
          statement.bindNull(5);
        } else {
          statement.bindDouble(5, entity.longitude);
        }
        if (entity.address == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.address);
        }
        if (entity.city == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.city);
        }
        if (entity.categorie == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.categorie);
        }
        if (entity.imageUrl == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.imageUrl);
        }
        statement.bindDouble(10, entity.rating);
        if (entity.quartier == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.quartier);
        }
        final int _tmp = entity.isSynced ? 1 : 0;
        statement.bindLong(12, _tmp);
      }
    };
    this.__updateAdapterOfCommerce = new EntityDeletionOrUpdateAdapter<Commerce>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `commerces` SET `id` = ?,`name` = ?,`merchantId` = ?,`latitude` = ?,`longitude` = ?,`address` = ?,`city` = ?,`categorie` = ?,`imageUrl` = ?,`rating` = ?,`quartier` = ?,`isSynced` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Commerce entity) {
        if (entity.id == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.id);
        }
        if (entity.name == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.name);
        }
        if (entity.merchantId == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.merchantId);
        }
        if (entity.latitude == null) {
          statement.bindNull(4);
        } else {
          statement.bindDouble(4, entity.latitude);
        }
        if (entity.longitude == null) {
          statement.bindNull(5);
        } else {
          statement.bindDouble(5, entity.longitude);
        }
        if (entity.address == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.address);
        }
        if (entity.city == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.city);
        }
        if (entity.categorie == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.categorie);
        }
        if (entity.imageUrl == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.imageUrl);
        }
        statement.bindDouble(10, entity.rating);
        if (entity.quartier == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.quartier);
        }
        final int _tmp = entity.isSynced ? 1 : 0;
        statement.bindLong(12, _tmp);
        if (entity.id == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.id);
        }
      }
    };
    this.__preparedStmtOfDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM commerces WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public void insert(final Commerce commerce) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfCommerce.insert(commerce);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Commerce commerce) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfCommerce.handle(commerce);
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
  public List<Commerce> getAll() {
    final String _sql = "SELECT * FROM commerces";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfMerchantId = CursorUtil.getColumnIndexOrThrow(_cursor, "merchantId");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
      final int _cursorIndexOfCategorie = CursorUtil.getColumnIndexOrThrow(_cursor, "categorie");
      final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfQuartier = CursorUtil.getColumnIndexOrThrow(_cursor, "quartier");
      final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
      final List<Commerce> _result = new ArrayList<Commerce>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Commerce _item;
        _item = new Commerce();
        if (_cursor.isNull(_cursorIndexOfId)) {
          _item.id = null;
        } else {
          _item.id = _cursor.getString(_cursorIndexOfId);
        }
        if (_cursor.isNull(_cursorIndexOfName)) {
          _item.name = null;
        } else {
          _item.name = _cursor.getString(_cursorIndexOfName);
        }
        if (_cursor.isNull(_cursorIndexOfMerchantId)) {
          _item.merchantId = null;
        } else {
          _item.merchantId = _cursor.getString(_cursorIndexOfMerchantId);
        }
        if (_cursor.isNull(_cursorIndexOfLatitude)) {
          _item.latitude = null;
        } else {
          _item.latitude = _cursor.getDouble(_cursorIndexOfLatitude);
        }
        if (_cursor.isNull(_cursorIndexOfLongitude)) {
          _item.longitude = null;
        } else {
          _item.longitude = _cursor.getDouble(_cursorIndexOfLongitude);
        }
        if (_cursor.isNull(_cursorIndexOfAddress)) {
          _item.address = null;
        } else {
          _item.address = _cursor.getString(_cursorIndexOfAddress);
        }
        if (_cursor.isNull(_cursorIndexOfCity)) {
          _item.city = null;
        } else {
          _item.city = _cursor.getString(_cursorIndexOfCity);
        }
        if (_cursor.isNull(_cursorIndexOfCategorie)) {
          _item.categorie = null;
        } else {
          _item.categorie = _cursor.getString(_cursorIndexOfCategorie);
        }
        if (_cursor.isNull(_cursorIndexOfImageUrl)) {
          _item.imageUrl = null;
        } else {
          _item.imageUrl = _cursor.getString(_cursorIndexOfImageUrl);
        }
        _item.rating = _cursor.getFloat(_cursorIndexOfRating);
        if (_cursor.isNull(_cursorIndexOfQuartier)) {
          _item.quartier = null;
        } else {
          _item.quartier = _cursor.getString(_cursorIndexOfQuartier);
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
  public List<Commerce> getByMerchant(final String merchantId) {
    final String _sql = "SELECT * FROM commerces WHERE merchantId = ?";
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
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfMerchantId = CursorUtil.getColumnIndexOrThrow(_cursor, "merchantId");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
      final int _cursorIndexOfCategorie = CursorUtil.getColumnIndexOrThrow(_cursor, "categorie");
      final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfQuartier = CursorUtil.getColumnIndexOrThrow(_cursor, "quartier");
      final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
      final List<Commerce> _result = new ArrayList<Commerce>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Commerce _item;
        _item = new Commerce();
        if (_cursor.isNull(_cursorIndexOfId)) {
          _item.id = null;
        } else {
          _item.id = _cursor.getString(_cursorIndexOfId);
        }
        if (_cursor.isNull(_cursorIndexOfName)) {
          _item.name = null;
        } else {
          _item.name = _cursor.getString(_cursorIndexOfName);
        }
        if (_cursor.isNull(_cursorIndexOfMerchantId)) {
          _item.merchantId = null;
        } else {
          _item.merchantId = _cursor.getString(_cursorIndexOfMerchantId);
        }
        if (_cursor.isNull(_cursorIndexOfLatitude)) {
          _item.latitude = null;
        } else {
          _item.latitude = _cursor.getDouble(_cursorIndexOfLatitude);
        }
        if (_cursor.isNull(_cursorIndexOfLongitude)) {
          _item.longitude = null;
        } else {
          _item.longitude = _cursor.getDouble(_cursorIndexOfLongitude);
        }
        if (_cursor.isNull(_cursorIndexOfAddress)) {
          _item.address = null;
        } else {
          _item.address = _cursor.getString(_cursorIndexOfAddress);
        }
        if (_cursor.isNull(_cursorIndexOfCity)) {
          _item.city = null;
        } else {
          _item.city = _cursor.getString(_cursorIndexOfCity);
        }
        if (_cursor.isNull(_cursorIndexOfCategorie)) {
          _item.categorie = null;
        } else {
          _item.categorie = _cursor.getString(_cursorIndexOfCategorie);
        }
        if (_cursor.isNull(_cursorIndexOfImageUrl)) {
          _item.imageUrl = null;
        } else {
          _item.imageUrl = _cursor.getString(_cursorIndexOfImageUrl);
        }
        _item.rating = _cursor.getFloat(_cursorIndexOfRating);
        if (_cursor.isNull(_cursorIndexOfQuartier)) {
          _item.quartier = null;
        } else {
          _item.quartier = _cursor.getString(_cursorIndexOfQuartier);
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
  public Commerce getById(final String id) {
    final String _sql = "SELECT * FROM commerces WHERE id = ? LIMIT 1";
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
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfMerchantId = CursorUtil.getColumnIndexOrThrow(_cursor, "merchantId");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
      final int _cursorIndexOfCategorie = CursorUtil.getColumnIndexOrThrow(_cursor, "categorie");
      final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfQuartier = CursorUtil.getColumnIndexOrThrow(_cursor, "quartier");
      final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
      final Commerce _result;
      if (_cursor.moveToFirst()) {
        _result = new Commerce();
        if (_cursor.isNull(_cursorIndexOfId)) {
          _result.id = null;
        } else {
          _result.id = _cursor.getString(_cursorIndexOfId);
        }
        if (_cursor.isNull(_cursorIndexOfName)) {
          _result.name = null;
        } else {
          _result.name = _cursor.getString(_cursorIndexOfName);
        }
        if (_cursor.isNull(_cursorIndexOfMerchantId)) {
          _result.merchantId = null;
        } else {
          _result.merchantId = _cursor.getString(_cursorIndexOfMerchantId);
        }
        if (_cursor.isNull(_cursorIndexOfLatitude)) {
          _result.latitude = null;
        } else {
          _result.latitude = _cursor.getDouble(_cursorIndexOfLatitude);
        }
        if (_cursor.isNull(_cursorIndexOfLongitude)) {
          _result.longitude = null;
        } else {
          _result.longitude = _cursor.getDouble(_cursorIndexOfLongitude);
        }
        if (_cursor.isNull(_cursorIndexOfAddress)) {
          _result.address = null;
        } else {
          _result.address = _cursor.getString(_cursorIndexOfAddress);
        }
        if (_cursor.isNull(_cursorIndexOfCity)) {
          _result.city = null;
        } else {
          _result.city = _cursor.getString(_cursorIndexOfCity);
        }
        if (_cursor.isNull(_cursorIndexOfCategorie)) {
          _result.categorie = null;
        } else {
          _result.categorie = _cursor.getString(_cursorIndexOfCategorie);
        }
        if (_cursor.isNull(_cursorIndexOfImageUrl)) {
          _result.imageUrl = null;
        } else {
          _result.imageUrl = _cursor.getString(_cursorIndexOfImageUrl);
        }
        _result.rating = _cursor.getFloat(_cursorIndexOfRating);
        if (_cursor.isNull(_cursorIndexOfQuartier)) {
          _result.quartier = null;
        } else {
          _result.quartier = _cursor.getString(_cursorIndexOfQuartier);
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
  public List<Commerce> getUnsynced() {
    final String _sql = "SELECT * FROM commerces WHERE isSynced = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfMerchantId = CursorUtil.getColumnIndexOrThrow(_cursor, "merchantId");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
      final int _cursorIndexOfCategorie = CursorUtil.getColumnIndexOrThrow(_cursor, "categorie");
      final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfQuartier = CursorUtil.getColumnIndexOrThrow(_cursor, "quartier");
      final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
      final List<Commerce> _result = new ArrayList<Commerce>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Commerce _item;
        _item = new Commerce();
        if (_cursor.isNull(_cursorIndexOfId)) {
          _item.id = null;
        } else {
          _item.id = _cursor.getString(_cursorIndexOfId);
        }
        if (_cursor.isNull(_cursorIndexOfName)) {
          _item.name = null;
        } else {
          _item.name = _cursor.getString(_cursorIndexOfName);
        }
        if (_cursor.isNull(_cursorIndexOfMerchantId)) {
          _item.merchantId = null;
        } else {
          _item.merchantId = _cursor.getString(_cursorIndexOfMerchantId);
        }
        if (_cursor.isNull(_cursorIndexOfLatitude)) {
          _item.latitude = null;
        } else {
          _item.latitude = _cursor.getDouble(_cursorIndexOfLatitude);
        }
        if (_cursor.isNull(_cursorIndexOfLongitude)) {
          _item.longitude = null;
        } else {
          _item.longitude = _cursor.getDouble(_cursorIndexOfLongitude);
        }
        if (_cursor.isNull(_cursorIndexOfAddress)) {
          _item.address = null;
        } else {
          _item.address = _cursor.getString(_cursorIndexOfAddress);
        }
        if (_cursor.isNull(_cursorIndexOfCity)) {
          _item.city = null;
        } else {
          _item.city = _cursor.getString(_cursorIndexOfCity);
        }
        if (_cursor.isNull(_cursorIndexOfCategorie)) {
          _item.categorie = null;
        } else {
          _item.categorie = _cursor.getString(_cursorIndexOfCategorie);
        }
        if (_cursor.isNull(_cursorIndexOfImageUrl)) {
          _item.imageUrl = null;
        } else {
          _item.imageUrl = _cursor.getString(_cursorIndexOfImageUrl);
        }
        _item.rating = _cursor.getFloat(_cursorIndexOfRating);
        if (_cursor.isNull(_cursorIndexOfQuartier)) {
          _item.quartier = null;
        } else {
          _item.quartier = _cursor.getString(_cursorIndexOfQuartier);
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
  public List<Commerce> getAllWithLocation() {
    final String _sql = "SELECT * FROM commerces WHERE latitude IS NOT NULL AND longitude IS NOT NULL";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfMerchantId = CursorUtil.getColumnIndexOrThrow(_cursor, "merchantId");
      final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
      final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
      final int _cursorIndexOfCategorie = CursorUtil.getColumnIndexOrThrow(_cursor, "categorie");
      final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
      final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
      final int _cursorIndexOfQuartier = CursorUtil.getColumnIndexOrThrow(_cursor, "quartier");
      final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
      final List<Commerce> _result = new ArrayList<Commerce>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Commerce _item;
        _item = new Commerce();
        if (_cursor.isNull(_cursorIndexOfId)) {
          _item.id = null;
        } else {
          _item.id = _cursor.getString(_cursorIndexOfId);
        }
        if (_cursor.isNull(_cursorIndexOfName)) {
          _item.name = null;
        } else {
          _item.name = _cursor.getString(_cursorIndexOfName);
        }
        if (_cursor.isNull(_cursorIndexOfMerchantId)) {
          _item.merchantId = null;
        } else {
          _item.merchantId = _cursor.getString(_cursorIndexOfMerchantId);
        }
        if (_cursor.isNull(_cursorIndexOfLatitude)) {
          _item.latitude = null;
        } else {
          _item.latitude = _cursor.getDouble(_cursorIndexOfLatitude);
        }
        if (_cursor.isNull(_cursorIndexOfLongitude)) {
          _item.longitude = null;
        } else {
          _item.longitude = _cursor.getDouble(_cursorIndexOfLongitude);
        }
        if (_cursor.isNull(_cursorIndexOfAddress)) {
          _item.address = null;
        } else {
          _item.address = _cursor.getString(_cursorIndexOfAddress);
        }
        if (_cursor.isNull(_cursorIndexOfCity)) {
          _item.city = null;
        } else {
          _item.city = _cursor.getString(_cursorIndexOfCity);
        }
        if (_cursor.isNull(_cursorIndexOfCategorie)) {
          _item.categorie = null;
        } else {
          _item.categorie = _cursor.getString(_cursorIndexOfCategorie);
        }
        if (_cursor.isNull(_cursorIndexOfImageUrl)) {
          _item.imageUrl = null;
        } else {
          _item.imageUrl = _cursor.getString(_cursorIndexOfImageUrl);
        }
        _item.rating = _cursor.getFloat(_cursorIndexOfRating);
        if (_cursor.isNull(_cursorIndexOfQuartier)) {
          _item.quartier = null;
        } else {
          _item.quartier = _cursor.getString(_cursorIndexOfQuartier);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
