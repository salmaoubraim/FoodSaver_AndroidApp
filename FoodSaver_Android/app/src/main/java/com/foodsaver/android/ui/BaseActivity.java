package com.foodsaver.android.ui;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.foodsaver.android.R;
import com.foodsaver.android.ui.client.CategoriesActivity;
import com.foodsaver.android.ui.client.ClientHomeActivity;
import com.foodsaver.android.ui.client.ClientProfileActivity;
import com.foodsaver.android.ui.client.CommercesMapActivity;
import com.foodsaver.android.ui.client.HistoryActivity;
import com.foodsaver.android.util.LanguageManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageManager.wrapContext(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupMapButton();
    }

    // ✅ Gère le bouton carte (btnMap) dans le toolbar include ou directement dans le layout
    private void setupMapButton() {
        View btnMap = null;

        // Cas 1 : toolbar est un <include> avec id="toolbar"
        View toolbarInclude = findViewById(R.id.toolbar);
        if (toolbarInclude != null)
            btnMap = toolbarInclude.findViewById(R.id.btnMap);

        // Cas 2 : btnMap directement dans le layout (fallback)
        if (btnMap == null)
            btnMap = findViewById(R.id.btnMap);

        if (btnMap != null) {
            btnMap.setOnClickListener(v ->
                    startActivity(new Intent(this, CommercesMapActivity.class)));
        }
    }

    protected void setupBottomNav(int selectedItemId) {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        if (bottomNav == null) return;
        bottomNav.setSelectedItemId(selectedItemId);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == selectedItemId) return false;
            Intent intent = null;
            if      (id == R.id.nav_home)       intent = new Intent(this, ClientHomeActivity.class);
            else if (id == R.id.nav_categories) intent = new Intent(this, CategoriesActivity.class);
            else if (id == R.id.nav_cart)       intent = new Intent(this, HistoryActivity.class);
            else if (id == R.id.nav_profile)    intent = new Intent(this, ClientProfileActivity.class);
            if (intent != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            }
            return true;
        });
    }

    protected void goHome() {
        startActivity(new Intent(this, ClientHomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
        finish();
    }
}