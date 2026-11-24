package com.foodsaver.android.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "users")
public class User {

    @PrimaryKey @NonNull
    public String id = "";
    public String  email;
    public String  name;
    public String  role;
    public boolean isSynced;

    public User() {}

    @Ignore
    public User(@NonNull String id, String email, String name, String role) {
        this.id       = id;
        this.email    = email;
        this.name     = name;
        this.role     = role;
        this.isSynced = false;
    }

    public Map<String, Object> toFirestoreMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("name",  name);
        map.put("role",  role);
        return map;
    }

    // ✅ fromFirestore → fromSnapshot
    public static User fromSnapshot(DataSnapshot snap) {
        User u    = new User();
        u.id      = snap.getKey();
        u.email   = snap.child("email").getValue(String.class);
        u.name    = snap.child("name").getValue(String.class);
        u.role    = snap.child("role").getValue(String.class);
        u.isSynced = true;
        return u;
    }
}