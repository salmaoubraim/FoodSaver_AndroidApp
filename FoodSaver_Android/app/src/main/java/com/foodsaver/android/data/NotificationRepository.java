package com.foodsaver.android.data;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.foodsaver.android.FoodSaverApp;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NotificationRepository {

    private final NotificationDao dao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public NotificationRepository(Context ctx) {
        AppDatabase db = ((FoodSaverApp) ctx.getApplicationContext()).getDatabase();
        this.dao = db.notificationDao();
    }

    public LiveData<List<AppNotification>> observeForUser(String userId) {
        return dao.observeForUser(userId);
    }

    public LiveData<Integer> observeUnreadCount(String userId) {
        return dao.observeUnreadCount(userId);
    }

    public void insertAsync(AppNotification n) {
        executor.execute(() -> dao.insert(n));
    }

    public void markRead(long id) {
        executor.execute(() -> dao.markRead(id));
    }

    public void markAllRead(String userId) {
        executor.execute(() -> dao.markAllRead(userId));
    }
}

