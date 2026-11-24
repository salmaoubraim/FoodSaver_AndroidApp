package com.foodsaver.android.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PanierDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Panier panier);

    @Update
    void update(Panier panier);

    @Query("DELETE FROM paniers WHERE id = :id")
    void delete(String id);

    @Query("SELECT * FROM paniers WHERE commerceId = :commerceId")
    List<Panier> getByCommerce(String commerceId);

    @Query("SELECT * FROM paniers WHERE id = :id LIMIT 1")
    Panier getById(String id);

    @Query("SELECT * FROM paniers WHERE isSynced = 0")
    List<Panier> getUnsynced();

    // ✅ NOUVEAU : requis par listenAllPaniers()
    @Query("SELECT * FROM paniers")
    List<Panier> getAll();

    @Query("SELECT DISTINCT commerceId FROM paniers WHERE quantite > 0")
    List<String> getCommerceIdsWithStock();

    @Query("SELECT COUNT(*) FROM paniers WHERE quantite > 0 AND categorie = :categorie")
    int countByCategorie(String categorie);

    @Query("SELECT COUNT(*) FROM paniers WHERE quantite > 0 AND (categorie IS NULL OR categorie = '' OR categorie = 'autre')")
    int countAutre();

    @Query("SELECT DISTINCT commerceId FROM paniers WHERE quantite > 0 AND categorie = :categorie")
    List<String> getCommerceIdsByPanierCategorie(String categorie);

    @Query("SELECT DISTINCT commerceId FROM paniers WHERE quantite > 0 AND (categorie IS NULL OR categorie = '' OR categorie = 'autre')")
    List<String> getCommerceIdsByPanierCategorieAutre();

    @Query("DELETE FROM paniers WHERE commerceId = :commerceId")
    void deleteByCommerce(String commerceId);
}