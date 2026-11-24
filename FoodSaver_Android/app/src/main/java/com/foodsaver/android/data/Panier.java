package com.foodsaver.android.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "paniers")
public class Panier {

    @PrimaryKey @NonNull
    public String id = "";
    public String  commerceId;
    public String  titre;
    public double  prix;
    public int     quantite;
    public String  description;
    public String  imageUrl;
    public String  categorie;
    public boolean isSynced;

    public Panier() {}

    @Ignore
    public Panier(String commerceId, String titre, double prix, int quantite) {
        this.commerceId  = commerceId;
        this.titre       = titre;
        this.prix        = prix;
        this.quantite    = quantite;
        this.description = null;
        this.imageUrl    = null;
        this.categorie   = "autre";
        this.isSynced    = false;
    }

    @Ignore
    public Panier(String commerceId, String titre, double prix,
                  int quantite, String description, String categorie) {
        this.commerceId  = commerceId;
        this.titre       = titre;
        this.prix        = prix;
        this.quantite    = quantite;
        this.description = description;
        this.imageUrl    = null;
        this.categorie   = categorie != null ? categorie : "autre";
        this.isSynced    = false;
    }

    public Map<String, Object> toFirestoreMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("commerceId",  commerceId);
        map.put("titre",       titre);
        map.put("prix",        prix);
        map.put("quantite",    quantite);
        map.put("description", description);
        map.put("imageUrl",    imageUrl);
        map.put("categorie",   categorie);
        return map;
    }

    // ✅ fromFirestore → fromSnapshot
    public static Panier fromSnapshot(DataSnapshot snap) {
        Panier p      = new Panier();
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
}