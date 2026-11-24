package com.foodsaver.android.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "reservations")
public class Reservation {

    @PrimaryKey @NonNull
    public String id = "";
    public String  userId;
    public String  panierId;
    public String  commerceId;
    public int     quantiteReservee;
    public long    dateReservation;
    public String  statut;
    public boolean isSynced;

    public Reservation() {}

    @Ignore
    public Reservation(String userId, String panierId, String commerceId,
                       int quantiteReservee, long dateReservation) {
        this.userId           = userId;
        this.panierId         = panierId;
        this.commerceId       = commerceId;
        this.quantiteReservee = quantiteReservee;
        this.dateReservation  = dateReservation;
        this.statut           = "en_attente";
        this.isSynced         = false;
    }

    public Map<String, Object> toFirestoreMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId",           userId);
        map.put("panierId",         panierId);
        map.put("commerceId",       commerceId);
        map.put("quantiteReservee", quantiteReservee);
        map.put("dateReservation",  dateReservation);
        map.put("statut",           statut);
        return map;
    }

    // ✅ fromFirestore → fromSnapshot
    public static Reservation fromSnapshot(DataSnapshot snap) {
        Reservation r = new Reservation();
        r.id          = snap.getKey();
        r.userId      = snap.child("userId").getValue(String.class);
        r.panierId    = snap.child("panierId").getValue(String.class);
        r.commerceId  = snap.child("commerceId").getValue(String.class);
        r.statut      = snap.child("statut").getValue(String.class);
        Long qty  = snap.child("quantiteReservee").getValue(Long.class);
        Long date = snap.child("dateReservation").getValue(Long.class);
        r.quantiteReservee = qty  != null ? qty.intValue() : 0;
        r.dateReservation  = date != null ? date           : 0;
        r.isSynced         = true;
        return r;
    }
}