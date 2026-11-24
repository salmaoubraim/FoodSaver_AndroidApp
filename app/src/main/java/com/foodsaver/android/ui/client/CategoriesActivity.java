package com.foodsaver.android.ui.client;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodsaver.android.R;
import com.foodsaver.android.data.PanierRepository;
import com.foodsaver.android.ui.BaseActivity;
import com.google.firebase.database.ValueEventListener;

public class CategoriesActivity extends BaseActivity {

    private static final String[] CATEGORY_KEYS   = { "tagine", "pizza", "sandwich", "petit-dejeuner", "salade", "autre" };
    private static final String[] CATEGORY_LABELS = { "🍔 Tagine", "🍕 Pizza", "🌯 Sandwichs", "☕ Café & Petit-déj", "🥗 Salades", "🍽 Autre" };

    private PanierRepository panierRepo;
    private CategoryAdapter  adapter;
    private int[]            counts;

    // ✅ garder référence du listener Firebase
    private ValueEventListener panierListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        setupBottomNav(R.id.nav_categories);

        panierRepo = new PanierRepository(this);
        counts     = new int[CATEGORY_KEYS.length];

        RecyclerView recycler = findViewById(R.id.recyclerCategories);
        recycler.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new CategoryAdapter(CATEGORY_LABELS, counts, position -> {
            Intent i = new Intent(this, ClientHomeActivity.class);
            i.putExtra("filterCategory", CATEGORY_KEYS[position]);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
            finish();
        });
        recycler.setAdapter(adapter);

        loadCounts();
    }

    // ✅ libérer le listener Firebase
    @Override
    protected void onDestroy() {
        super.onDestroy();
        panierRepo.removePaniersListener(panierListener);
    }

    private void loadCounts() {
        // 1. Afficher les compteurs locaux immédiatement
        refreshCounts();

        // 2. Sync Firebase puis rafraîchir les compteurs
        panierListener = panierRepo.listenAllPaniers(updatedList -> {
            runOnUiThread(this::refreshCounts);
        });
    }

    private void refreshCounts() {
        for (int i = 0; i < CATEGORY_KEYS.length; i++) {
            counts[i] = panierRepo.getCountByCategorie(CATEGORY_KEYS[i]);
        }
        adapter.updateCounts(counts);
    }
}