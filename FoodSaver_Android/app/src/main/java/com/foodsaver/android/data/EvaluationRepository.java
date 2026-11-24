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

public class EvaluationRepository {
    private final EvaluationDao  dao;
    private final CommerceDao    commerceDao;
    private final DatabaseReference db;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public EvaluationRepository(Context context) {
        AppDatabase appDb = ((FoodSaverApp) context.getApplicationContext()).getDatabase();
        this.dao         = appDb.evaluationDao();
        this.commerceDao = appDb.commerceDao();
        this.db          = FirebaseDatabase.getInstance().getReference();
    }

    public List<Evaluation> getByCommerce(String commerceId) { return dao.getByCommerce(commerceId); }
    public List<Evaluation> getByUser(String userId) { return dao.getByUser(userId); }
    public Evaluation getLastByUserAndCommerce(String userId, String commerceId) {
        return dao.getLastByUserAndCommerce(userId, commerceId);
    }

    public void addEvaluation(Evaluation evaluation, OnEvaluationAddedListener listener) {
        String key = db.child("evaluations").push().getKey();
        evaluation.id = key;
        db.child("evaluations").child(key).setValue(evaluation.toFirestoreMap())
                .addOnSuccessListener(v -> {
                    evaluation.isSynced = true;
                    executor.execute(() -> {
                        dao.insert(evaluation);
                        updateCommerceRating(evaluation.commerceId);
                        if (listener != null) listener.onAdded(evaluation);
                    });
                })
                .addOnFailureListener(e -> {
                    evaluation.id = "local_" + System.currentTimeMillis();
                    evaluation.isSynced = false;
                    executor.execute(() -> {
                        dao.insert(evaluation);
                        updateCommerceRating(evaluation.commerceId);
                        if (listener != null) listener.onAdded(evaluation);
                    });
                });
    }

    private void updateCommerceRating(String commerceId) {
        Double avg = dao.getAverageRating(commerceId);
        Commerce c = commerceDao.getById(commerceId);
        if (c != null && avg != null) {
            c.rating = avg.floatValue();
            commerceDao.update(c);
            db.child("commerces").child(commerceId).child("rating").setValue(c.rating);
        }
    }

    public interface OnEvaluationAddedListener { void onAdded(Evaluation evaluation); }
}