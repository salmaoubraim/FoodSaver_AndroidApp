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
public final class PanierDao_Impl implements PanierDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Panier> __insertionAdapterOfPanier;

  private final EntityDeletionOrUpdateAdapter<Panier> __updateAdapterOfPanier;

  private final SharedSQLiteStatement __preparedStmtOfDelete;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByCommerce;

  public PanierDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPanier = new EntityInsertionAdapter<Panier>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `paniers` (`id`,`commerceId`,`titre`,`prix`,`quantite`,`description`,`imageUrl`,`categorie`,`isSynced`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Panier entity) {
        if (entity.id == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.id);
        }
        if (entity.commerceId == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.commerceId);
        }
        if (entity.titre == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.titre);
        }
        statement.bindDouble(4, entity.prix);
        statement.bindLong(5, entity.quantite);
        if (entity.description == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.description);
        }
        if (entity.imageUrl == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.imageUrl);
        }
        if (entity.categorie == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.categorie);
        }
        final int _tmp = entity.isSynced ? 1 : 0;
        statement.bindLong(9, _tmp);
      }
    };
    this.__updateAdapterOfPanier = new EntityDeletionOrUpdateAdapter<Panier>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `paniers` SET `id` = ?,`commerceId` = ?,`titre` = ?,`prix` = ?,`quantite` = ?,`description` = ?,`imageUrl` = ?,`categorie` = ?,`isSynced` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Panier entity) {
        if (entity.id == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.id);
        }
        if (entity.commerceId == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.commerceId);
        }
        if (entity.titre == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.titre);
        }
        statement.bindDouble(4, entity.prix);
        statement.bindLong(5, entity.quantite);
        if (entity.description == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.description);
        }
        if (entity.imageUrl == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.imageUrl);
        }
        if (entity.categorie == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.categorie);
        }
        final int _tmp = entity.isSynced ? 1 : 0;
        statement.bindLong(9, _tmp);
        if (entity.id == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.id);
        }
      }
    };
    this.__preparedStmtOfDelete = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM paniers WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteByCommerce = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM paniers WHERE commerceId = ?";
        return _query;
      }
    };
  }

  @Override
  public void insert(final Panier panier) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfPanier.insert(panier);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Panier panier) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfPanier.handle(panier);
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
  public void deleteByCommerce(final String commerceId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByCommerce.acquire();
    int _argIndex = 1;
    if (commerceId == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, commerceId);
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
      __preparedStmtOfDeleteByCommerce.release(_stmt);
    }
  }

  @Override
  public List<Panier> getByCommerce(final String commerceId) {
    final String _sql = "SELECT * FROM paniers WHERE commerceId = ?";
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
      final int _cursorIndexOfCommerceId = CursorUtil.getColumnIndexOrThrow(_cursor, "commerceId");
      final int _cursorIndexOfTitre = CursorUtil.getColumnIndexOrThrow(_cursor, "titre");
      final int _cursorIndexOfPrix = CursorUtil.getColumnIndexOrThrow(_cursor, "prix");
      final int _cursorIndexOfQuantite = CursorUtil.getColumnIndexOrThrow(_cursor, "quantite");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
      final int _cursorIndexOfCategorie = CursorUtil.getColumnIndexOrThrow(_cursor, "categorie");
      final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
      final List<Panier> _result = new ArrayList<Panier>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Panier _item;
        _item = new Panier();
        if (_cursor.isNull(_cursorIndexOfId)) {
          _item.id = null;
        } else {
          _item.id = _cursor.getString(_cursorIndexOfId);
        }
        if (_cursor.isNull(_cursorIndexOfCommerceId)) {
          _item.commerceId = null;
        } else {
          _item.commerceId = _cursor.getString(_cursorIndexOfCommerceId);
        }
        if (_cursor.isNull(_cursorIndexOfTitre)) {
          _item.titre = null;
        } else {
          _item.titre = _cursor.getString(_cursorIndexOfTitre);
        }
        _item.prix = _cursor.getDouble(_cursorIndexOfPrix);
        _item.quantite = _cursor.getInt(_cursorIndexOfQuantite);
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _item.description = null;
        } else {
          _item.description = _cursor.getString(_cursorIndexOfDescription);
        }
        if (_cursor.isNull(_cursorIndexOfImageUrl)) {
          _item.imageUrl = null;
        } else {
          _item.imageUrl = _cursor.getString(_cursorIndexOfImageUrl);
        }
        if (_cursor.isNull(_cursorIndexOfCategorie)) {
          _item.categorie = null;
        } else {
          _item.categorie = _cursor.getString(_cursorIndexOfCategorie);
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
  public Panier getById(final String id) {
    final String _sql = "SELECT * FROM paniers WHERE id = ? LIMIT 1";
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
      final int _cursorIndexOfCommerceId = CursorUtil.getColumnIndexOrThrow(_cursor, "commerceId");
      final int _cursorIndexOfTitre = CursorUtil.getColumnIndexOrThrow(_cursor, "titre");
      final int _cursorIndexOfPrix = CursorUtil.getColumnIndexOrThrow(_cursor, "prix");
      final int _cursorIndexOfQuantite = CursorUtil.getColumnIndexOrThrow(_cursor, "quantite");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
      final int _cursorIndexOfCategorie = CursorUtil.getColumnIndexOrThrow(_cursor, "categorie");
      final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
      final Panier _result;
      if (_cursor.moveToFirst()) {
        _result = new Panier();
        if (_cursor.isNull(_cursorIndexOfId)) {
          _result.id = null;
        } else {
          _result.id = _cursor.getString(_cursorIndexOfId);
        }
        if (_cursor.isNull(_cursorIndexOfCommerceId)) {
          _result.commerceId = null;
        } else {
          _result.commerceId = _cursor.getString(_cursorIndexOfCommerceId);
        }
        if (_cursor.isNull(_cursorIndexOfTitre)) {
          _result.titre = null;
        } else {
          _result.titre = _cursor.getString(_cursorIndexOfTitre);
        }
        _result.prix = _cursor.getDouble(_cursorIndexOfPrix);
        _result.quantite = _cursor.getInt(_cursorIndexOfQuantite);
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _result.description = null;
        } else {
          _result.description = _cursor.getString(_cursorIndexOfDescription);
        }
        if (_cursor.isNull(_cursorIndexOfImageUrl)) {
          _result.imageUrl = null;
        } else {
          _result.imageUrl = _cursor.getString(_cursorIndexOfImageUrl);
        }
        if (_cursor.isNull(_cursorIndexOfCategorie)) {
          _result.categorie = null;
        } else {
          _result.categorie = _cursor.getString(_cursorIndexOfCategorie);
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
  public List<Panier> getUnsynced() {
    final String _sql = "SELECT * FROM paniers WHERE isSynced = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfCommerceId = CursorUtil.getColumnIndexOrThrow(_cursor, "commerceId");
      final int _cursorIndexOfTitre = CursorUtil.getColumnIndexOrThrow(_cursor, "titre");
      final int _cursorIndexOfPrix = CursorUtil.getColumnIndexOrThrow(_cursor, "prix");
      final int _cursorIndexOfQuantite = CursorUtil.getColumnIndexOrThrow(_cursor, "quantite");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
      final int _cursorIndexOfCategorie = CursorUtil.getColumnIndexOrThrow(_cursor, "categorie");
      final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
      final List<Panier> _result = new ArrayList<Panier>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Panier _item;
        _item = new Panier();
        if (_cursor.isNull(_cursorIndexOfId)) {
          _item.id = null;
        } else {
          _item.id = _cursor.getString(_cursorIndexOfId);
        }
        if (_cursor.isNull(_cursorIndexOfCommerceId)) {
          _item.commerceId = null;
        } else {
          _item.commerceId = _cursor.getString(_cursorIndexOfCommerceId);
        }
        if (_cursor.isNull(_cursorIndexOfTitre)) {
          _item.titre = null;
        } else {
          _item.titre = _cursor.getString(_cursorIndexOfTitre);
        }
        _item.prix = _cursor.getDouble(_cursorIndexOfPrix);
        _item.quantite = _cursor.getInt(_cursorIndexOfQuantite);
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _item.description = null;
        } else {
          _item.description = _cursor.getString(_cursorIndexOfDescription);
        }
        if (_cursor.isNull(_cursorIndexOfImageUrl)) {
          _item.imageUrl = null;
        } else {
          _item.imageUrl = _cursor.getString(_cursorIndexOfImageUrl);
        }
        if (_cursor.isNull(_cursorIndexOfCategorie)) {
          _item.categorie = null;
        } else {
          _item.categorie = _cursor.getString(_cursorIndexOfCategorie);
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
  public List<Panier> getAll() {
    final String _sql = "SELECT * FROM paniers";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfCommerceId = CursorUtil.getColumnIndexOrThrow(_cursor, "commerceId");
      final int _cursorIndexOfTitre = CursorUtil.getColumnIndexOrThrow(_cursor, "titre");
      final int _cursorIndexOfPrix = CursorUtil.getColumnIndexOrThrow(_cursor, "prix");
      final int _cursorIndexOfQuantite = CursorUtil.getColumnIndexOrThrow(_cursor, "quantite");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
      final int _cursorIndexOfCategorie = CursorUtil.getColumnIndexOrThrow(_cursor, "categorie");
      final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
      final List<Panier> _result = new ArrayList<Panier>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Panier _item;
        _item = new Panier();
        if (_cursor.isNull(_cursorIndexOfId)) {
          _item.id = null;
        } else {
          _item.id = _cursor.getString(_cursorIndexOfId);
        }
        if (_cursor.isNull(_cursorIndexOfCommerceId)) {
          _item.commerceId = null;
        } else {
          _item.commerceId = _cursor.getString(_cursorIndexOfCommerceId);
        }
        if (_cursor.isNull(_cursorIndexOfTitre)) {
          _item.titre = null;
        } else {
          _item.titre = _cursor.getString(_cursorIndexOfTitre);
        }
        _item.prix = _cursor.getDouble(_cursorIndexOfPrix);
        _item.quantite = _cursor.getInt(_cursorIndexOfQuantite);
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _item.description = null;
        } else {
          _item.description = _cursor.getString(_cursorIndexOfDescription);
        }
        if (_cursor.isNull(_cursorIndexOfImageUrl)) {
          _item.imageUrl = null;
        } else {
          _item.imageUrl = _cursor.getString(_cursorIndexOfImageUrl);
        }
        if (_cursor.isNull(_cursorIndexOfCategorie)) {
          _item.categorie = null;
        } else {
          _item.categorie = _cursor.getString(_cursorIndexOfCategorie);
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
  public List<String> getCommerceIdsWithStock() {
    final String _sql = "SELECT DISTINCT commerceId FROM paniers WHERE quantite > 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final List<String> _result = new ArrayList<String>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final String _item;
        if (_cursor.isNull(0)) {
          _item = null;
        } else {
          _item = _cursor.getString(0);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int countByCategorie(final String categorie) {
    final String _sql = "SELECT COUNT(*) FROM paniers WHERE quantite > 0 AND categorie = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (categorie == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, categorie);
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
  public int countAutre() {
    final String _sql = "SELECT COUNT(*) FROM paniers WHERE quantite > 0 AND (categorie IS NULL OR categorie = '' OR categorie = 'autre')";
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
  public List<String> getCommerceIdsByPanierCategorie(final String categorie) {
    final String _sql = "SELECT DISTINCT commerceId FROM paniers WHERE quantite > 0 AND categorie = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (categorie == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, categorie);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final List<String> _result = new ArrayList<String>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final String _item;
        if (_cursor.isNull(0)) {
          _item = null;
        } else {
          _item = _cursor.getString(0);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<String> getCommerceIdsByPanierCategorieAutre() {
    final String _sql = "SELECT DISTINCT commerceId FROM paniers WHERE quantite > 0 AND (categorie IS NULL OR categorie = '' OR categorie = 'autre')";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final List<String> _result = new ArrayList<String>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final String _item;
        if (_cursor.isNull(0)) {
          _item = null;
        } else {
          _item = _cursor.getString(0);
        }
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
