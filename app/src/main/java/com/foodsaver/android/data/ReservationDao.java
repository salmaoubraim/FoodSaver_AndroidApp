package com.foodsaver.android.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ReservationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Reservation reservation);

    @Update
    void update(Reservation reservation);

    // ✅ id devient String
    @Query("DELETE FROM reservations WHERE id = :id")
    void delete(String id);

    // ✅ userId devient String
    @Query("SELECT * FROM reservations WHERE userId = :userId ORDER BY dateReservation DESC")
    List<Reservation> getByUser(String userId);

    // ✅ merchantId devient String — JOIN simplifié via commerceId
    @Query("SELECT * FROM reservations WHERE commerceId IN " +
            "(SELECT id FROM commerces WHERE merchantId = :merchantId) " +
            "ORDER BY dateReservation DESC")
    List<Reservation> getByMerchant(String merchantId);

    @Query("SELECT * FROM reservations WHERE id = :id LIMIT 1")
    Reservation getById(String id);

    @Query("SELECT * FROM reservations WHERE isSynced = 0")
    List<Reservation> getUnsynced();

    @Query("SELECT COUNT(*) FROM reservations")
    int countAll();

    @Query("UPDATE reservations SET statut = :statut WHERE id = :id")
    void updateStatut(String id, String statut);

    @Query("SELECT COUNT(*) FROM reservations WHERE commerceId = :commerceId")
    int countByCommerce(String commerceId);

    @Query("SELECT COUNT(*) FROM reservations WHERE commerceId = :commerceId AND (statut IS NULL OR statut != 'annulee')")
    int countActiveByCommerce(String commerceId);

    @Query("DELETE FROM reservations WHERE panierId IN (:panierIds)")
    void deleteByPanierIds(List<String> panierIds);
}