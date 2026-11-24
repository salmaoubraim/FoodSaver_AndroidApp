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

public class PanierRepository {
    private final PanierDao dao;
    private final DatabaseReference db;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public PanierRepository(Context context) {
        AppDatabase appDb = ((FoodSaverApp) context.getApplicationContext()).getDatabase();
        this.dao = appDb.panierDao();
        this.db  = FirebaseDatabase.getInstance().getReference();
    }

    public List<Panier> getPaniersByCommerce(String commerceId) { return dao.getByCommerce(commerceId); }
    public Panier getById(String id) { return dao.getById(id); }
    public List<String> getCommerceIdsWithStock() { return dao.getCommerceIdsWithStock(); }

    public int getCountByCategorie(String categorie) {
        if ("autre".equals(categorie)) return dao.countAutre();
        return dao.countByCategorie(categorie);
    }

    public List<String> getCommerceIdsByPanierCategorie(String categorie) {
        if ("autre".equals(categorie)) return dao.getCommerceIdsByPanierCategorieAutre();
        return dao.getCommerceIdsByPanierCategorie(categorie);
    }

    public void insertPanier(Panier panier, OnPanierCreatedListener listener) {
        String key = db.child("paniers").push().getKey();
        panier.id = key;
        db.child("paniers").child(key).setValue(panier.toFirestoreMap())
                .addOnSuccessListener(v -> {
                    panier.isSynced = true;
                    executor.execute(() -> {
                        dao.insert(panier);
                        if (listener != null) listener.onCreated(panier);
                    });
                })
                .addOnFailureListener(e -> {
                    panier.id = "local_" + System.currentTimeMillis();
                    panier.isSynced = false;
                    executor.execute(() -> {
                        dao.insert(panier);
                        if (listener != null) listener.onCreated(panier);
                    });
                });
    }

    public void updatePanier(Panier panier) {
        executor.execute(() -> dao.update(panier));
        if (panier.isSynced && !panier.id.startsWith("local_"))
            db.child("paniers").child(panier.id).setValue(panier.toFirestoreMap());
    }

    public void deletePanier(String id) {
        executor.execute(() -> dao.delete(id));
        db.child("paniers").child(id).removeValue();
    }

    public void syncFromFirestore(String commerceId) {
        db.child("paniers").orderByChild("commerceId").equalTo(commerceId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override public void onDataChange(DataSnapshot snap) {
                        executor.execute(() -> {
                            for (DataSnapshot child : snap.getChildren()) {
                                Panier p = snapshotToPanier(child);
                                if (p != null) dao.insert(p);
                            }
                        });
                    }
                    @Override public void onCancelled(DatabaseError e) {}
                });
    }

    public ValueEventListener listenPaniersByCommerce(String commerceId, OnPaniersSyncedListener callback) {
        ValueEventListener listener = new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot snap) {
                executor.execute(() -> {
                    for (DataSnapshot child : snap.getChildren()) {
                        Panier p = snapshotToPanier(child);
                        if (p != null) dao.insert(p);
                    }
                    List<Panier> updated = dao.getByCommerce(commerceId);
                    if (callback != null) callback.onSynced(updated);
                });
            }
            @Override public void onCancelled(DatabaseError e) {}
        };
        db.child("paniers").orderByChild("commerceId").equalTo(commerceId)
                .addValueEventListener(listener);
        return listener;
    }

    public ValueEventListener listenPanierById(String panierId, OnPanierFetchedListener callback) {
        ValueEventListener listener = new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot snap) {
                executor.execute(() -> {
                    Panier p = snapshotToPanier(snap);
                    if (p != null) dao.insert(p);
                    if (callback != null) callback.onFetched(p);
                });
            }
            @Override public void onCancelled(DatabaseError e) {
                if (callback != null) callback.onFetched(null);
            }
        };
        db.child("paniers").child(panierId).addValueEventListener(listener);
        return listener;
    }

    // ✅ NOUVEAU : écouter tous les paniers pour CategoriesActivity
    public ValueEventListener listenAllPaniers(OnPaniersSyncedListener callback) {
        ValueEventListener listener = new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot snap) {
                executor.execute(() -> {
                    for (DataSnapshot child : snap.getChildren()) {
                        Panier p = snapshotToPanier(child);
                        if (p != null) dao.insert(p);
                    }
                    List<Panier> updated = dao.getAll();
                    if (callback != null) callback.onSynced(updated);
                });
            }
            @Override public void onCancelled(DatabaseError e) {}
        };
        db.child("paniers").addValueEventListener(listener);
        return listener;
    }

    public void removePaniersListener(ValueEventListener listener) {
        if (listener == null) return;
        db.child("paniers").removeEventListener(listener);
    }

    public void removePanierListener(ValueEventListener listener) {
        if (listener == null) return;
        db.child("paniers").removeEventListener(listener);
    }

    public void syncPendingPaniers() {
        executor.execute(() -> {
            for (Panier p : dao.getUnsynced()) {
                String key = db.child("paniers").push().getKey();
                p.id = key;
                db.child("paniers").child(key).setValue(p.toFirestoreMap())
                        .addOnSuccessListener(v -> {
                            p.isSynced = true;
                            executor.execute(() -> dao.update(p));
                        });
            }
        });
    }

    private Panier snapshotToPanier(DataSnapshot snap) {
        if (!snap.exists()) return null;
        Panier p = new Panier();
        p.id          = snap.getKey();
        p.commerceId  = snap.child("commerceId").getValue(String.class);
        p.titre       = snap.child("titre").getValue(String.class);
        p.description = snap.child("description").getValue(String.class);
        p.imageUrl    = snap.child("imageUrl").getValue(String.class);
        p.categorie   = snap.child("categorie").getValue(String.class);
        p.isSynced    = true;
        Double prix = snap.child("prix").getValue(Double.class);
        Long   qty  = snap.child("quantite").getValue(Long.class);
        if (prix != null) p.prix     = prix;
        if (qty  != null) p.quantite = qty.intValue();
        return p;
    }

    public interface OnPanierCreatedListener { void onCreated(Panier panier); }
    public interface OnPaniersSyncedListener { void onSynced(List<Panier> paniers); }
    public interface OnPanierFetchedListener { void onFetched(Panier panier); }
}