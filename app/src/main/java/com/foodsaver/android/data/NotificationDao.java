package com.foodsaver.android.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotificationDao {

    @Insert
    long insert(AppNotification n);

    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY dateTime DESC")
    LiveData<List<AppNotification>> observeForUser(String userId);

    @Query("SELECT COUNT(*) FROM notifications WHERE userId = :userId AND isRead = 0")
    LiveData<Integer> observeUnreadCount(String userId);

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    void markRead(long id);

    @Query("UPDATE notifications SET isRead = 1 WHERE userId = :userId")
    void markAllRead(String userId);
}

