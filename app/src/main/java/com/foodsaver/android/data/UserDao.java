package com.foodsaver.android.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Update
    void update(User user);

    @Query("DELETE FROM users WHERE id = :id")
    void delete(String id);

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    User getById(String id);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getByEmail(String email);

    @Query("SELECT * FROM users ORDER BY email")
    List<User> getAll();

}