package com.foodsaver.android.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.foodsaver.android.auth.SessionManager;
import com.foodsaver.android.data.AppNotification;
import com.foodsaver.android.data.NotificationRepository;

import java.util.List;

public class NotificationsViewModel extends AndroidViewModel {

    private final NotificationRepository repo;
    private final LiveData<List<AppNotification>> notifications;
    private final LiveData<Integer> unreadCount;

    public NotificationsViewModel(@NonNull Application app) {
        super(app);
        repo = new NotificationRepository(app);
        String userId = new SessionManager(app).getUserId();
        if (userId == null) userId = "";
        notifications = repo.observeForUser(userId);
        unreadCount   = repo.observeUnreadCount(userId);
    }

    public LiveData<List<AppNotification>> getNotifications() {
        return notifications;
    }

    public LiveData<Integer> getUnreadCount() {
        return unreadCount;
    }

    public void markRead(long id) {
        repo.markRead(id);
    }

    public void markAllRead() {
        String userId = new SessionManager(getApplication()).getUserId();
        if (userId != null) repo.markAllRead(userId);
    }
}

