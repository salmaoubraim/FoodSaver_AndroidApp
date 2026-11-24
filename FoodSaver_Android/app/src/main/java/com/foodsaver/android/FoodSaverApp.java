package com.foodsaver.android;

import android.app.Application;
import android.content.SharedPreferences;

import com.foodsaver.android.data.AppDatabase;
import com.foodsaver.android.util.LanguageManager;
import com.foodsaver.android.util.NotificationHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FoodSaverApp extends Application {

    private static final String PREFS_APP            = "FoodSaverApp";
    private static final String KEY_DATA_INITIALIZED = "data_initialized_v2";

    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        LanguageManager.applyLanguage(this);
        NotificationHelper.createChannels(this);
        database = AppDatabase.getInstance(this);
        seedDataIfNeeded();
    }

    private void seedDataIfNeeded() {
        SharedPreferences prefs = getSharedPreferences(PREFS_APP, MODE_PRIVATE);
        if (prefs.getBoolean(KEY_DATA_INITIALIZED, false)) return;

        FirebaseAuth auth = FirebaseAuth.getInstance();
        // ✅ Realtime Database uniquement
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        // ✅ Merchant de test
        auth.createUserWithEmailAndPassword("merchant@test.ma", "test123")
                .addOnSuccessListener(result -> {
                    String uid = result.getUser().getUid();
                    Map<String, Object> m = new HashMap<>();
                    m.put("name",  "Commerçant Test");
                    m.put("email", "merchant@test.ma");
                    m.put("role",  "merchant");
                    // ✅ Realtime Database : child() pas collection()
                    db.child("users").child(uid).setValue(m);
                    insertAgadirCommercesAndPaniers(db, uid);
                    auth.signOut();
                })
                .addOnFailureListener(e -> {
                    // Compte existe déjà → récupérer l'UID
                    auth.signInWithEmailAndPassword("merchant@test.ma", "test123")
                            .addOnSuccessListener(result -> {
                                String uid = result.getUser().getUid();
                                // ✅ Vérifier si les commerces existent déjà
                                db.child("commerces").orderByChild("merchantId").equalTo(uid)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot snap) {
                                                if (!snap.exists()) insertAgadirCommercesAndPaniers(db, uid);
                                            }
                                            @Override public void onCancelled(DatabaseError e2) {}
                                        });
                                auth.signOut();
                            });
                });

        // ✅ Client de test
        auth.createUserWithEmailAndPassword("client@test.ma", "test123")
                .addOnSuccessListener(result -> {
                    String uid = result.getUser().getUid();
                    Map<String, Object> m = new HashMap<>();
                    m.put("name",  "Client Test");
                    m.put("email", "client@test.ma");
                    m.put("role",  "client");
                    db.child("users").child(uid).setValue(m);
                    auth.signOut();
                });

        // ✅ Admin de test
        auth.createUserWithEmailAndPassword("admin@foodsaver.ma", "admin123")
                .addOnSuccessListener(result -> {
                    String uid = result.getUser().getUid();
                    Map<String, Object> m = new HashMap<>();
                    m.put("name",  "Admin FoodSaver");
                    m.put("email", "admin@foodsaver.ma");
                    m.put("role",  "admin");
                    db.child("users").child(uid).setValue(m);
                    auth.signOut();
                });

        prefs.edit().putBoolean(KEY_DATA_INITIALIZED, true).apply();
    }

    private void insertAgadirCommercesAndPaniers(DatabaseReference db, String merchantUid) {

        String[][] commercesData = {
                {"Snack Atlas",        "30.4150", "-9.5890", "Hay Essalam",    "Agadir", "snack",       "Hay Essalam",    "4.5"},
                {"Café Tafarnout",     "30.4278", "-9.5981", "Talborjt",       "Agadir", "cafe",        "Talborjt",       "4.8"},
                {"Restaurant Al Bahr", "30.4200", "-9.6050", "Secteur 5",      "Agadir", "restaurant",  "Secteur 5",      "4.2"},
                {"Boulangerie Agadir", "30.4320", "-9.6010", "Lhay Lmohamadi", "Agadir", "boulangerie", "Lhay Lmohamadi", "4.6"},
                {"Snack Souss",        "30.4100", "-9.5750", "Dakhla",         "Agadir", "snack",       "Dakhla",         "4.0"},
                {"Café du Souk",       "30.4230", "-9.6100", "Souk El Had",    "Agadir", "cafe",        "Souk El Had",    "4.4"},
        };

        String[][][] paniersData = {
                {{"Tagine Poulet","35","tagine","https://images.unsplash.com/photo-1547592180-85f173990554?w=600"},
                        {"Sandwich Club","25","sandwich","https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=600"},
                        {"Salade César","28","salade","https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=600"}},

                {{"Petit-déjeuner complet","30","petit-dejeuner","https://images.unsplash.com/photo-1533089860892-a7c6f0a88666?w=600"},
                        {"Croissant + café","15","autre","https://images.unsplash.com/photo-1555507036-ab1f4038808a?w=600"},
                        {"Panini","22","sandwich","https://images.unsplash.com/photo-1509722747041-616f39b57569?w=600"}},

                {{"Tagine Poulet","35","tagine","https://images.unsplash.com/photo-1547592180-85f173990554?w=600"},
                        {"Pizza Margherita","45","pizza","https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=600"},
                        {"Salade mixte","25","salade","https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=600"}},

                {{"Viennoiseries du jour","12","autre","https://images.unsplash.com/photo-1555507036-ab1f4038808a?w=600"},
                        {"Pain invendu","5","autre","https://images.unsplash.com/photo-1549931319-a545dcf3bc7c?w=600"},
                        {"Pâtisseries assorties","20","autre","https://images.unsplash.com/photo-1587314168485-3236d6710814?w=600"}},

                {{"Sandwich merguez","18","sandwich","https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=600"},
                        {"Tagine (restes)","30","tagine","https://images.unsplash.com/photo-1547592180-85f173990554?w=600"},
                        {"Frites + boisson","15","autre","https://images.unsplash.com/photo-1573080496219-bb080dd4f877?w=600"}},

                {{"Thé à la menthe + gâteau","18","autre","https://images.unsplash.com/photo-1576092768241-dec231879fc3?w=600"},
                        {"Petit-déjeuner marocain","35","petit-dejeuner","https://images.unsplash.com/photo-1533089860892-a7c6f0a88666?w=600"},
                        {"Sandwich","20","sandwich","https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=600"}},
        };

        for (int i = 0; i < commercesData.length; i++) {
            String[] cd = commercesData[i];
            final String[][] paniers = paniersData[i];

            // ✅ push() génère une clé unique comme Firestore
            String commerceKey = db.child("commerces").push().getKey();

            Map<String, Object> commerce = new HashMap<>();
            commerce.put("name",       cd[0]);
            commerce.put("merchantId", merchantUid);
            commerce.put("latitude",   Double.parseDouble(cd[1]));
            commerce.put("longitude",  Double.parseDouble(cd[2]));
            commerce.put("address",    cd[3]);
            commerce.put("city",       cd[4]);
            commerce.put("categorie",  cd[5]);
            commerce.put("quartier",   cd[6]);
            commerce.put("rating",     Double.parseDouble(cd[7]));
            commerce.put("imageUrl",   "");

            // ✅ setValue() au lieu de add()
            db.child("commerces").child(commerceKey).setValue(commerce)
                    .addOnSuccessListener(v -> {
                        for (String[] pd : paniers) {
                            int qty = 5 + (int)(Math.random() * 10);
                            String panierKey = db.child("paniers").push().getKey();
                            Map<String, Object> panier = new HashMap<>();
                            panier.put("commerceId",  commerceKey);
                            panier.put("titre",       pd[0]);
                            panier.put("prix",        Double.parseDouble(pd[1]));
                            panier.put("categorie",   pd[2]);
                            panier.put("imageUrl",    pd[3]);
                            panier.put("quantite",    qty);
                            panier.put("description", "");
                            // ✅ Realtime Database
                            db.child("paniers").child(panierKey).setValue(panier);
                        }
                    });
        }
    }

    public AppDatabase getDatabase() { return database; }
}