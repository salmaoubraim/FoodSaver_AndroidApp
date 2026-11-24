package com.foodsaver.android.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EvaluationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Evaluation evaluation);

    @Update
    void update(Evaluation evaluation);

    // ✅ commerceId devient String
    @Query("SELECT * FROM evaluations WHERE commerceId = :commerceId ORDER BY dateEvaluation DESC")
    List<Evaluation> getByCommerce(String commerceId);

    // ✅ userId devient String
    @Query("SELECT * FROM evaluations WHERE userId = :userId ORDER BY dateEvaluation DESC")
    List<Evaluation> getByUser(String userId);

    @Query("SELECT AVG(rating) FROM evaluations WHERE commerceId = :commerceId")
    Double getAverageRating(String commerceId);

    @Query("SELECT COUNT(*) FROM evaluations WHERE commerceId = :commerceId")
    int getCountByCommerce(String commerceId);

    @Query("SELECT * FROM evaluations WHERE userId = :userId AND commerceId = :commerceId ORDER BY dateEvaluation DESC LIMIT 1")
    Evaluation getLastByUserAndCommerce(String userId, String commerceId);

    @Query("SELECT * FROM evaluations WHERE isSynced = 0")
    List<Evaluation> getUnsynced();
}