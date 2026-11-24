package com.foodsaver.android.data;

import android.content.Context;

import com.foodsaver.android.FoodSaverApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private final UserDao dao;
    private final DatabaseReference db;
    private final FirebaseAuth mAuth;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public UserRepository(Context context) {
        AppDatabase appDb = ((FoodSaverApp) context.getApplicationContext()).getDatabase();
        this.dao   = appDb.userDao();
        this.db    = FirebaseDatabase.getInstance().getReference();
        this.mAuth = FirebaseAuth.getInstance();
    }

    public void getById(String id, OnUserFetchedListener listener) {
        executor.execute(() -> {
            User local = dao.getById(id);
            if (local != null) { if (listener != null) listener.onFetched(local); return; }
            db.child("users").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override public void onDataChange(DataSnapshot snap) {
                    if (snap.exists()) {
                        User u = new User(id,
                                snap.child("email").getValue(String.class),
                                snap.child("name").getValue(String.class),
                                snap.child("role").getValue(String.class));
                        u.isSynced = true;
                        executor.execute(() -> dao.insert(u));
                        if (listener != null) listener.onFetched(u);
                    } else { if (listener != null) listener.onFetched(null); }
                }
                @Override public void onCancelled(DatabaseError e) {
                    if (listener != null) listener.onFetched(null);
                }
            });
        });
    }

    public void getByEmail(String email, OnUserFetchedListener listener) {
        executor.execute(() -> {
            User local = dao.getByEmail(email);
            if (local != null) { if (listener != null) listener.onFetched(local); return; }
            db.child("users").orderByChild("email").equalTo(email)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override public void onDataChange(DataSnapshot snap) {
                            if (snap.exists()) {
                                DataSnapshot first = snap.getChildren().iterator().next();
                                User u = new User(first.getKey(),
                                        first.child("email").getValue(String.class),
                                        first.child("name").getValue(String.class),
                                        first.child("role").getValue(String.class));
                                u.isSynced = true;
                                executor.execute(() -> dao.insert(u));
                                if (listener != null) listener.onFetched(u);
                            } else { if (listener != null) listener.onFetched(null); }
                        }
                        @Override public void onCancelled(DatabaseError e) {
                            if (listener != null) listener.onFetched(null);
                        }
                    });
        });
    }

    public List<User> getAllUsers() { return dao.getAll(); }

    public void updateUser(User user) {
        executor.execute(() -> dao.update(user));
        db.child("users").child(user.id).setValue(user.toFirestoreMap());
    }

    public void deleteUser(String id) {
        executor.execute(() -> dao.delete(id));
        db.child("users").child(id).removeValue();
        if (mAuth.getCurrentUser() != null) mAuth.getCurrentUser().delete();
    }

    public void syncCurrentUser() {
        if (mAuth.getCurrentUser() == null) return;
        String uid = mAuth.getCurrentUser().getUid();
        db.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot snap) {
                if (snap.exists()) {
                    User u = new User(uid,
                            snap.child("email").getValue(String.class),
                            snap.child("name").getValue(String.class),
                            snap.child("role").getValue(String.class));
                    u.isSynced = true;
                    executor.execute(() -> dao.insert(u));
                }
            }
            @Override public void onCancelled(DatabaseError e) {}
        });
    }

    public interface OnUserFetchedListener { void onFetched(User user); }
}