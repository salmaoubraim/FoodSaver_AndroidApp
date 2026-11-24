package com.foodsaver.android.data;

import android.content.Context;

import com.foodsaver.android.FoodSaverApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReservationRepository {
    private final ReservationDao dao;
    private final DatabaseReference db;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ReservationRepository(Context context) {
        AppDatabase appDb = ((FoodSaverApp) context.getApplicationContext()).getDatabase();
        this.dao = appDb.reservationDao();
        this.db  = FirebaseDatabase.getInstance().getReference();
    }

    public List<Reservation> getByUser(String userId)         { return dao.getByUser(userId); }
    public List<Reservation> getByMerchant(String merchantId) { return dao.getByMerchant(merchantId); }
    public Reservation getById(String id)                     { return dao.getById(id); }
    public int countAllReservations()                         { return dao.countAll(); }

    // ✅ AJOUT : compter les réservations par commerce
    public int countReservationsByCommerce(String commerceId) {
        return dao.countByCommerce(commerceId);
    }

    public int countActiveReservationsByCommerce(String commerceId) {
        return dao.countActiveByCommerce(commerceId);
    }

    public void insertReservation(Reservation reservation, OnReservationCreatedListener listener) {
        String key = db.child("reservations").push().getKey();
        reservation.id = key;
        db.child("reservations").child(key).setValue(reservation.toFirestoreMap())
                .addOnSuccessListener(v -> {
                    reservation.isSynced = true;
                    executor.execute(() -> {
                        dao.insert(reservation);
                        if (listener != null) listener.onCreated(reservation);
                    });
                })
                .addOnFailureListener(e -> {
                    reservation.id = "local_" + System.currentTimeMillis();
                    reservation.isSynced = false;
                    executor.execute(() -> {
                        dao.insert(reservation);
                        if (listener != null) listener.onCreated(reservation);
                    });
                });
    }

    public void cancelReservation(String id) {
        executor.execute(() -> dao.updateStatut(id, "annulee"));
        db.child("reservations").child(id).child("statut").setValue("annulee");
    }

    public void confirmReservation(String id) {
        executor.execute(() -> dao.updateStatut(id, "confirmee"));
        db.child("reservations").child(id).child("statut").setValue("confirmee");
    }

    public void syncPendingReservations() {
        executor.execute(() -> {
            for (Reservation r : dao.getUnsynced()) {
                String key = db.child("reservations").push().getKey();
                r.id = key;
                db.child("reservations").child(key).setValue(r.toFirestoreMap())
                        .addOnSuccessListener(v -> {
                            r.isSynced = true;
                            executor.execute(() -> dao.update(r));
                        });
            }
        });
    }
    public void getTopCommerceByReservations(List<String> commerceIds, OnTopCommerceListener listener) {
        db.child("reservations").get().addOnSuccessListener(snapshot -> {
            // Compter les réservations par commerceId
            java.util.Map<String, Integer> counts = new java.util.HashMap<>();
            for (DataSnapshot child : snapshot.getChildren()) {
                String cId = child.child("commerceId").getValue(String.class);
                if (cId != null && commerceIds.contains(cId))
                    counts.put(cId, counts.getOrDefault(cId, 0) + 1);
            }

            // Trouver le max
            String topId  = null;
            int    maxVal = 0;
            for (java.util.Map.Entry<String, Integer> e : counts.entrySet()) {
                if (e.getValue() > maxVal) { maxVal = e.getValue(); topId = e.getKey(); }
            }

            final String finalTopId  = topId;
            final int    finalMaxVal = maxVal;
            listener.onResult(finalTopId, finalMaxVal);
        }).addOnFailureListener(e -> listener.onResult(null, 0));
    }

    public interface OnTopCommerceListener {
        void onResult(String topCommerceId, int count);
    }

    public interface OnReservationCreatedListener { void onCreated(Reservation reservation); }
}