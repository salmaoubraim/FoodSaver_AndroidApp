package com.foodsaver.android.ui.merchant;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.foodsaver.android.R;
import com.foodsaver.android.data.Panier;
import com.foodsaver.android.data.PanierRepository;
import com.foodsaver.android.ui.BaseActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class EditPanierActivity extends BaseActivity {

    // ✅ String au lieu de long
    private String commerceId;
    private String panierId = null;

    private PanierRepository panierRepo;
    private String currentImagePath;

    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() != RESULT_OK || result.getData() == null) return;
            Uri uri = result.getData().getData();
            if (uri != null) copyImageToAppStorage(uri);
        }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_panier);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        // ✅ getStringExtra au lieu de getLongExtra
        commerceId = getIntent().getStringExtra("commerceId");
        panierId   = getIntent().getStringExtra("panierId");   // null si nouveau panier
        panierRepo = new PanierRepository(this);

        ImageView         imgPanier   = findViewById(R.id.imgPanier);
        TextInputEditText etTitre     = findViewById(R.id.etTitre);
        TextInputEditText etPrix      = findViewById(R.id.etPrix);
        TextInputEditText etQuantite  = findViewById(R.id.etQuantite);

        // ✅ panierId est String (null = nouveau)
        if (panierId != null) {
            Panier p = panierRepo.getById(panierId);
            if (p != null) {
                etTitre.setText(p.titre);
                etPrix.setText(String.valueOf(p.prix));
                etQuantite.setText(String.valueOf(p.quantite));
                if (p.imageUrl != null && !p.imageUrl.isEmpty()) {
                    currentImagePath = p.imageUrl;
                    loadImageInto(imgPanier, currentImagePath);
                }
            } else {
                etTitre.setText(getIntent().getStringExtra("titre"));
                etPrix.setText(String.valueOf(getIntent().getDoubleExtra("prix", 0)));
                etQuantite.setText(String.valueOf(getIntent().getIntExtra("quantite", 0)));
            }
        }

        findViewById(R.id.btnPickImage).setOnClickListener(v -> openImagePicker());

        findViewById(R.id.btnSave).setOnClickListener(v -> {
            String titre    = etTitre.getText()    != null ? etTitre.getText().toString().trim()    : "";
            String prixStr  = etPrix.getText()     != null ? etPrix.getText().toString().trim()     : "";
            String qtyStr   = etQuantite.getText() != null ? etQuantite.getText().toString().trim() : "";

            if (titre.isEmpty() || prixStr.isEmpty() || qtyStr.isEmpty()) {
                Toast.makeText(this, R.string.error_fields, Toast.LENGTH_SHORT).show();
                return;
            }
            double prix;
            int quantite;
            try {
                prix     = Double.parseDouble(prixStr);
                quantite = Integer.parseInt(qtyStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Prix et quantité doivent être des nombres.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (panierId != null) {
                // ✅ Modifier panier existant
                Panier p = panierRepo.getById(panierId);
                if (p != null) {
                    p.titre    = titre;
                    p.prix     = prix;
                    p.quantite = quantite;
                    p.imageUrl = currentImagePath;
                    panierRepo.updatePanier(p);
                }
                setResult(RESULT_OK);
                finish();
            } else {
                // ✅ Nouveau panier — insertPanier async
                Panier p = new Panier(commerceId, titre, prix, quantite);
                p.imageUrl = currentImagePath;
                panierRepo.insertPanier(p, panier -> runOnUiThread(() -> {
                    setResult(RESULT_OK);
                    finish();
                }));
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        pickImageLauncher.launch(Intent.createChooser(intent, getString(R.string.choose_image)));
    }

    private void copyImageToAppStorage(Uri uri) {
        File dir = new File(getFilesDir(), "panier_images");
        if (!dir.exists()) dir.mkdirs();
        String name = "panier_" + System.currentTimeMillis() + ".jpg";
        File dest = new File(dir, name);
        try (InputStream in  = getContentResolver().openInputStream(uri);
             OutputStream out = new FileOutputStream(dest)) {
            if (in == null) return;
            byte[] buf = new byte[8192];
            int len;
            while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
            currentImagePath = dest.getAbsolutePath();
            loadImageInto(findViewById(R.id.imgPanier), currentImagePath);
        } catch (Exception e) {
            Toast.makeText(this, "Impossible de copier l'image.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadImageInto(ImageView iv, String path) {
        if (path == null) return;
        Glide.with(this)
            .load(path.startsWith("/") ? new File(path) : path)
            .placeholder(R.drawable.placeholder_food)
            .transition(DrawableTransitionOptions.withCrossFade())
            .centerCrop()
            .into(iv);
    }
}
