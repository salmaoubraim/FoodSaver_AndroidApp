package com.foodsaver.android.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

// ✅ Remplacé FirebaseFirestore par FirebaseDatabase
import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "commerces")
public class Commerce {

    @PrimaryKey
    @NonNull
    public String id = "";

    public String  name;
    public String  merchantId;
    public Double  latitude;
    public Double  longitude;
    public String  address;
    public String  city;
    public String  categorie;
    public String  imageUrl;
    public float   rating;
    public String  quartier;
    public boolean isSynced;

    public Commerce() {}

    @Ignore
    public Commerce(String name, String merchantId) {
        this.name       = name;
        this.merchantId = merchantId;
        this.categorie  = "autre";
        this.rating     = 0f;
        this.isSynced   = false;
    }

    @Ignore
    public Commerce(String name, String merchantId,
                    Double latitude, Double longitude,
                    String address, String city) {
        this.name       = name;
        this.merchantId = merchantId;
        this.latitude   = latitude;
        this.longitude  = longitude;
        this.address    = address;
        this.city       = city;
        this.categorie  = "autre";
        this.rating     = 0f;
        this.isSynced   = false;
    }

    @Ignore
    public Commerce(String name, String merchantId,
                    Double latitude, Double longitude,
                    String address, String city,
                    String categorie, String quartier, float rating) {
        this.name       = name;
        this.merchantId = merchantId;
        this.latitude   = latitude;
        this.longitude  = longitude;
        this.address    = address;
        this.city       = city;
        this.categorie  = categorie != null ? categorie : "autre";
        this.quartier   = quartier;
        this.rating     = rating;
        this.isSynced   = false;
    }

    public Map<String, Object> toFirestoreMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name",       name);
        map.put("merchantId", merchantId);
        map.put("latitude",   latitude);
        map.put("longitude",  longitude);
        map.put("address",    address);
        map.put("city",       city);
        map.put("categorie",  categorie);
        map.put("imageUrl",   imageUrl);
        map.put("rating",     rating);
        map.put("quartier",   quartier);
        return map;
    }

    // ✅ fromFirestore(DocumentSnapshot) → fromSnapshot(DataSnapshot)
    public static Commerce fromSnapshot(DataSnapshot snap) {
        Commerce c   = new Commerce();
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
}