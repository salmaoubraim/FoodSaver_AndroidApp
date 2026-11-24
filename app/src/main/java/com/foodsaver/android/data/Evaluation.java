package com.foodsaver.android.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

// ✅ Supprimé : import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "evaluations")
public class Evaluation {

    @PrimaryKey
    @NonNull
    public String id = "";

    public String userId;
    public String commerceId;
    public float  rating;
    public String comment;
    public long   dateEvaluation;
    public boolean isSynced;

    public Evaluation() {}

    @Ignore
    public Evaluation(String userId, String commerceId,
                      float rating, String comment, long dateEvaluation) {
        this.userId         = userId;
        this.commerceId     = commerceId;
        this.rating         = rating;
        this.comment        = comment;
        this.dateEvaluation = dateEvaluation;
        this.isSynced       = false;
    }

    // ✅ toFirestoreMap() → toMap() mais le nom ne change pas, ça marche pareil
    public Map<String, Object> toFirestoreMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId",         userId);
        map.put("commerceId",     commerceId);
        map.put("rating",         rating);
        map.put("comment",        comment);
        map.put("dateEvaluation", dateEvaluation);
        return map;
    }

    // ✅ fromFirestore(DocumentSnapshot) → fromSnapshot(DataSnapshot)
    public static Evaluation fromSnapshot(DataSnapshot snap) {
        Evaluation e      = new Evaluation();
        e.id              = snap.getKey();
        e.userId          = snap.child("userId").getValue(String.class);
        e.commerceId      = snap.child("commerceId").getValue(String.class);
        e.comment         = snap.child("comment").getValue(String.class);
        Double rating     = snap.child("rating").getValue(Double.class);
        Long date         = snap.child("dateEvaluation").getValue(Long.class);
        e.rating          = rating != null ? rating.floatValue() : 0f;
        e.dateEvaluation  = date   != null ? date               : 0;
        e.isSynced        = true;
        return e;
    }
}