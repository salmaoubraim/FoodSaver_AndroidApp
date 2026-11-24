package com.foodsaver.android.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.foodsaver.android.R;
import com.foodsaver.android.auth.SessionManager;
import com.foodsaver.android.data.Commerce;
import com.foodsaver.android.data.Panier;
import com.foodsaver.android.ui.BaseActivity;
import com.foodsaver.android.ui.client.ClientHomeActivity;
import com.foodsaver.android.ui.merchant.MerchantHomeActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends BaseActivity {

    private ProgressBar progressBar;
    private String selectedRole = "client";
    private MaterialCardView cardRoleClient, cardRoleMerchant;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        TextInputEditText etName            = findViewById(R.id.etName);
        TextInputEditText etEmail           = findViewById(R.id.etEmail);
        TextInputEditText etPassword        = findViewById(R.id.etPassword);
        TextInputEditText etConfirmPassword = findViewById(R.id.etConfirmPassword);
        cardRoleClient   = findViewById(R.id.cardRoleClient);
        cardRoleMerchant = findViewById(R.id.cardRoleMerchant);
        progressBar      = findViewById(R.id.progressBar);

        cardRoleClient.setOnClickListener(v -> selectRole("client"));
        cardRoleMerchant.setOnClickListener(v -> selectRole("merchant"));

        findViewById(R.id.linkLogin).setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        findViewById(R.id.btnRegister).setOnClickListener(v -> {
            String name     = etName.getText() != null ? etName.getText().toString().trim() : "";
            String email    = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
            String password = etPassword.getText() != null ? etPassword.getText().toString() : "";
            String confirm  = etConfirmPassword.getText() != null ? etConfirmPassword.getText().toString() : "";

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, R.string.error_fields, Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 6) {
                Toast.makeText(this, "Le mot de passe doit contenir au moins 6 caractères", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.equals(confirm)) {
                Toast.makeText(this, R.string.error_password_match, Toast.LENGTH_SHORT).show();
                return;
            }

            showLoading(true);
            registerWithFirebase(name, email, password, selectedRole);
        });
    }

    private void registerWithFirebase(String name, String email, String password, String role) {
        // ✅ Étape 1 — Créer le compte Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String uid = authResult.getUser().getUid();
                    // ✅ Étape 2 — Sauvegarder le profil dans Firestore
                    saveUserToFirestore(uid, name, email, role);
                })
                .addOnFailureListener(e -> {
                    showLoading(false);
                    Toast.makeText(this,
                            "Erreur inscription : " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }

    private void saveUserToFirestore(String uid, String name, String email, String role) {
        // ✅ Realtime Database
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        java.util.Map<String, Object> userMap = new java.util.HashMap<>();
        userMap.put("name",  name);
        userMap.put("email", email);
        userMap.put("role",  role);

        db.child("users").child(uid).setValue(userMap)
                .addOnSuccessListener(v -> {
                    if ("merchant".equals(role)) createMerchantData(uid, name, email, role);
                    else onRegisterSuccess(uid, name, email, role);
                })
                .addOnFailureListener(e -> {
                    showLoading(false);
                    Toast.makeText(this, "Erreur : " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void createMerchantData(String uid, String name, String email, String role) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        String commerceKey = db.child("commerces").push().getKey();

        java.util.Map<String, Object> commerceMap = new java.util.HashMap<>();
        commerceMap.put("name",       name + " - Commerce");
        commerceMap.put("merchantId", uid);
        commerceMap.put("rating",     0.0);
        commerceMap.put("categorie",  "");
        commerceMap.put("imageUrl",   "");
        commerceMap.put("address",    "");
        commerceMap.put("city",       "");

        db.child("commerces").child(commerceKey).setValue(commerceMap)
                .addOnSuccessListener(v -> {
                    String panierKey = db.child("paniers").push().getKey();
                    java.util.Map<String, Object> panierMap = new java.util.HashMap<>();
                    panierMap.put("commerceId",  commerceKey);
                    panierMap.put("titre",       "Exemple panier");
                    panierMap.put("prix",        5.0);
                    panierMap.put("quantite",    5);
                    panierMap.put("categorie",   "autre");
                    panierMap.put("imageUrl",    "");
                    panierMap.put("description", "");
                    db.child("paniers").child(panierKey).setValue(panierMap)
                            .addOnSuccessListener(v2 -> onRegisterSuccess(uid, name, email, role))
                            .addOnFailureListener(e  -> onRegisterSuccess(uid, name, email, role));
                })
                .addOnFailureListener(e -> onRegisterSuccess(uid, name, email, role));
    }

    private void onRegisterSuccess(String uid, String name, String email, String role) {
        showLoading(false);
        // ✅ SessionManager avec Firebase UID (String)
        new SessionManager(this).login(uid, email, role, name);
        Toast.makeText(this, "Inscription réussie !", Toast.LENGTH_SHORT).show();
        navigateToHome(role);
    }

    private void navigateToHome(String role) {
        Intent intent = "merchant".equals(role)
                ? new Intent(this, MerchantHomeActivity.class)
                : new Intent(this, ClientHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void selectRole(String role) {
        selectedRole = role;
        int strokeWidth = getResources().getDimensionPixelSize(R.dimen.role_card_stroke);
        cardRoleClient.setStrokeWidth(role.equals("client") ? strokeWidth : 0);
        cardRoleClient.setStrokeColor(role.equals("client") ? getColor(R.color.primary) : 0);
        cardRoleMerchant.setStrokeWidth(role.equals("merchant") ? strokeWidth : 0);
        cardRoleMerchant.setStrokeColor(role.equals("merchant") ? getColor(R.color.primary) : 0);
    }

    private void showLoading(boolean show) {
        if (progressBar != null)
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        findViewById(R.id.btnRegister).setEnabled(!show);
    }
}