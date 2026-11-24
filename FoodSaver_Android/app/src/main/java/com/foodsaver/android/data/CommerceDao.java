package com.foodsaver.android.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CommerceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Commerce commerce);      // ✅ void (plus de long auto-généré)

    @Update
    void update(Commerce commerce);

    @Query("DELETE FROM commerces WHERE id = :id")
    void delete(String id);

    @Query("SELECT * FROM commerces")
    List<Commerce> getAll();

    // ✅ merchantId devient String (Firebase UID)
    @Query("SELECT * FROM commerces WHERE merchantId = :merchantId")
    List<Commerce> getByMerchant(String merchantId);

    // ✅ id devient String (Firestore doc ID)
    @Query("SELECT * FROM commerces WHERE id = :id LIMIT 1")
    Commerce getById(String id);

    @Query("SELECT * FROM commerces WHERE isSynced = 0")
    List<Commerce> getUnsynced();

    @Query("SELECT * FROM commerces WHERE latitude IS NOT NULL AND longitude IS NOT NULL")
    List<Commerce> getAllWithLocation();


}