package com.foodsaver.android.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notifications")
public class AppNotification {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String userId;   // UID Firebase (client ou commerçant)

    public String title;
    public String message;
    public long   dateTime;
    public boolean isRead;

    public AppNotification(@NonNull String userId, String title, String message, long dateTime) {
        this.userId  = userId;
        this.title   = title;
        this.message = message;
        this.dateTime = dateTime;
        this.isRead  = false;
    }
}

