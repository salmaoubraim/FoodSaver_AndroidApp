package com.foodsaver.android.data;

import android.content.Context;

import com.foodsaver.android.FoodSaverApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommerceRepository {
    private final CommerceDao dao;
    private final AppDatabase appDb;
    private final DatabaseReference db;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public CommerceRepository(Context context) {
        this.appDb = ((FoodSaverApp) context.getApplicationContext()).getDatabase();
        this.dao   = appDb.commerceDao();
        this.db    = FirebaseDatabase.getInstance().getReference();
    }

    public List<Commerce> getAllCommerces() { return dao.getAll(); }
    public List<Commerce> getCommercesWithLocation() { return dao.getAllWithLocation(); }
    public List<Commerce> getCommercesByMerchant(String merchantId) { return dao.getByMerchant(merchantId); }
    public Commerce getById(String id) { return dao.getById(id); }

    public void insertCommerce(Commerce commerce, OnCommerceCreatedListener listener) {
        String key = db.child("commerces").push().getKey();
        commerce.id = key;
        db.child("commerces").child(key).setValue(commerce.toFirestoreMap())
                .addOnSuccessListener(v -> {
                    commerce.isSynced = true;
                    executor.execute(() -> {
                        dao.insert(commerce);
                        if (listener != null) listener.onCreated(commerce);
                    });
                })
                .addOnFailureListener(e -> {
                    commerce.id = "local_" + System.currentTimeMillis();
                    commerce.isSynced = false;
                    executor.execute(() -> {
                        dao.insert(commerce);
                        if (listener != null) listener.onCreated(commerce);
                    });
                });
    }

    public void updateCommerce(Commerce commerce) {
        executor.execute(() -> dao.update(commerce));
        if (commerce.isSynced && !commerce.id.startsWith("local_"))
            db.child("commerces").child(commerce.id).setValue(commerce.toFirestoreMap());
    }

    public void deleteCommerce(String id) {
        executor.execute(() -> dao.delete(id));
        db.child("commerces").child(id).removeValue();
    }

    /** Suppression en cascade : commerce + paniers + réservations (local + Firebase). */
    public void deleteCommerceCascade(String commerceId) {
        executor.execute(() -> {
            // 1) Paniers du commerce
            List<Panier> paniers = appDb.panierDao().getByCommerce(commerceId);
            java.util.List<String> panierIds = new java.util.ArrayList<>();
            for (Panier p : paniers) panierIds.add(p.id);

            // 2) Supprimer réservations liées
            if (!panierIds.isEmpty()) {
                appDb.reservationDao().deleteByPanierIds(panierIds);
            }

            // 3) Supprimer paniers
            appDb.panierDao().deleteByCommerce(commerceId);

            // 4) Supprimer commerce local
            dao.delete(commerceId);

            // 5) Supprimer dans Firebase
            for (String pid : panierIds) {
                db.child("reservations").orderByChild("panierId").equalTo(pid)
                        .get().addOnSuccessListener(snap -> {
                            for (DataSnapshot child : snap.getChildren()) {
                                child.getRef().removeValue();
                            }
                        });
                db.child("paniers").child(pid).removeValue();
            }
            db.child("commerces").child(commerceId).removeValue();
        });
    }

    public void syncFromFirestore(String merchantId) {
        db.child("commerces").orderByChild("merchantId").equalTo(merchantId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override public void onDataChange(DataSnapshot snap) {
                        executor.execute(() -> {
                            for (DataSnapshot child : snap.getChildren()) {
                                Commerce c = snapshotToCommerce(child);
                                if (c != null) dao.insert(c);
                            }
                        });
                    }
                    @Override public void onCancelled(DatabaseError e) {}
                });
    }

    public ValueEventListener listenAllCommerces(OnCommercesSyncedListener callback) {
        ValueEventListener listener = new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot snap) {
                executor.execute(() -> {
                    for (DataSnapshot child : snap.getChildren()) {
                        Commerce c = snapshotToCommerce(child);
                        if (c != null) dao.insert(c);
                    }
                    List<Commerce> updated = dao.getAll();
                    if (callback != null) callback.onSynced(updated);
                });
            }
            @Override public void onCancelled(DatabaseError e) {}
        };
        db.child("commerces").addValueEventListener(listener);
        return listener;
    }

    public void removeCommercesListener(ValueEventListener listener) {
        if (listener != null)
            db.child("commerces").removeEventListener(listener);
    }

    public void syncPendingCommerces() {
        executor.execute(() -> {
            for (Commerce c : dao.getUnsynced()) {
                String key = db.child("commerces").push().getKey();
                c.id = key;
                db.child("commerces").child(key).setValue(c.toFirestoreMap())
                        .addOnSuccessListener(v -> {
                            c.isSynced = true;
                            executor.execute(() -> dao.update(c));
                        });
            }
        });
    }

    private Commerce snapshotToCommerce(DataSnapshot snap) {
        if (!snap.exists()) return null;
        Commerce c = new Commerce();
        c.id         = snap.getKey();
        c.name       = snap.child("name").getValue(String.class);
        c.merchantId = snap.child("merchantId").getValue(String.class);
        c.address    = snap.child("address").getValue(String.class);
        c.city       = snap.child("city").getValue(String.class);
        c.categorie  = snap.child("categorie").getValue(String.class);
        c.imageUrl   = snap.child("imageUrl").getValue(String.class);
        c.quartier   = snap.child("quartier").getValue(String.class);
        c.isSynced   = true;
        Double lat = snap.child("latitude").getValue(Double.class);
        Double lng = snap.child("longitude").getValue(Double.class);
        Double rat = snap.child("rating").getValue(Double.class);
        if (lat != null) c.latitude  = lat;
        if (lng != null) c.longitude = lng;
        if (rat != null) c.rating    = rat.floatValue();
        return c;
    }

    public interface OnCommerceCreatedListener  { void onCreated(Commerce commerce); }
    public interface OnCommercesSyncedListener  { void onSynced(List<Commerce> commerces); }
}