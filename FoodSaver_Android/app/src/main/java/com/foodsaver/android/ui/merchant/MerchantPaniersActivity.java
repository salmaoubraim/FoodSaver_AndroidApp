package com.foodsaver.android.ui.merchant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodsaver.android.R;
import com.foodsaver.android.data.Panier;
import com.foodsaver.android.data.PanierRepository;
import com.foodsaver.android.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class MerchantPaniersActivity extends BaseActivity {

    // ✅ String au lieu de long
    private String commerceId;
    private String commerceName;

    private final List<Panier> paniers = new ArrayList<>();
    private PanierMerchantAdapter adapter;
    private PanierRepository panierRepo;

    private final ActivityResultLauncher<Intent> editLauncher =
        registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> { if (result.getResultCode() == RESULT_OK) loadPaniers(); });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_paniers);

        // ✅ getStringExtra au lieu de getLongExtra
        commerceId   = getIntent().getStringExtra("commerceId");
        commerceName = getIntent().getStringExtra("commerceName");
        if (commerceId == null) { finish(); return; }

        panierRepo = new PanierRepository(this);

        // ✅ Sync depuis Firestore au démarrage
        panierRepo.syncFromFirestore(commerceId);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(
                commerceName != null ? commerceName : getString(R.string.paniers));
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        RecyclerView recycler = findViewById(R.id.recyclerPaniers);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PanierMerchantAdapter(paniers, this::onEdit, this::onDelete);
        recycler.setAdapter(adapter);

        View fab = findViewById(R.id.fabAddPanier);
        if (fab != null) fab.setOnClickListener(v -> openAdd());

        loadPaniers();
    }

    @Override
    protected void onResume() { super.onResume(); loadPaniers(); }

    private void openAdd() {
        editLauncher.launch(new Intent(this, EditPanierActivity.class)
            .putExtra("commerceId", commerceId));  // ✅ String
    }

    private void loadPaniers() {
        paniers.clear();
        // ✅ getPaniersByCommerce prend String
        paniers.addAll(panierRepo.getPaniersByCommerce(commerceId));
        adapter.notifyDataSetChanged();
        TextView tvEmpty = findViewById(R.id.tvEmpty);
        if (tvEmpty != null)
            tvEmpty.setVisibility(paniers.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void onEdit(Panier p) {
        editLauncher.launch(new Intent(this, EditPanierActivity.class)
            .putExtra("commerceId", commerceId)   // ✅ String
            .putExtra("panierId",   p.id));        // ✅ String
    }

    private void onDelete(Panier p) {
        new AlertDialog.Builder(this)
            .setTitle(R.string.delete)
            .setMessage("Supprimer « " + p.titre + " » ?")
            .setPositiveButton(R.string.delete, (d, w) -> {
                // ✅ deletePanier prend String
                panierRepo.deletePanier(p.id);
                loadPaniers();
            })
            .setNegativeButton(android.R.string.cancel, null)
            .show();
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.merchant_paniers_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == R.id.action_add_panier) { openAdd(); return true; }
        return super.onOptionsItemSelected(item);
    }

    @Override public void onBackPressed()          { finish(); }
    @Override public boolean onSupportNavigateUp() { finish(); return true; }
}
