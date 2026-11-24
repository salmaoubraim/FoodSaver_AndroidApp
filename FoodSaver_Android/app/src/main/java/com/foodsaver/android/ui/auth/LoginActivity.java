package com.foodsaver.android.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.foodsaver.android.R;
import com.foodsaver.android.auth.SessionManager;
import com.foodsaver.android.ui.BaseActivity;
import com.foodsaver.android.ui.MainActivity;
import com.foodsaver.android.ui.admin.AdminHomeActivity;
import com.foodsaver.android.ui.client.ClientHomeActivity;
import com.foodsaver.android.ui.merchant.MerchantHomeActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends BaseActivity {

    private ProgressBar progressBar;
    private TextInputEditText etEmail, etPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        etEmail    = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.btnLogin).setOnClickListener(v -> doLogin());
        findViewById(R.id.btnSkipLogin).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
        findViewById(R.id.linkRegister).setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });
        findViewById(R.id.btnForgotPassword).setOnClickListener(v ->
                sendPasswordReset());
    }

    private void doLogin() {
        String email    = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString() : "";

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.error_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading(true);

        // ✅ Firebase Auth login
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    String uid = authResult.getUser().getUid();
                    fetchUserRoleAndNavigate(uid);
                })
                .addOnFailureListener(e -> {
                    showLoading(false);
                    Toast.makeText(this,
                            "Échec connexion : " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }

    // ✅ Récupérer le rôle depuis Firestore
    private void fetchUserRoleAndNavigate(String uid) {
        FirebaseDatabase.getInstance().getReference()
                .child("users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snap) {
                        showLoading(false);
                        if (snap.exists()) {
                            String role  = snap.child("role").getValue(String.class);
                            String name  = snap.child("name").getValue(String.class);
                            String email = snap.child("email").getValue(String.class);
                            new SessionManager(LoginActivity.this).login(uid, email, role, name);
                            navigateToHome(role);
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Profil introuvable.", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError e) {
                        showLoading(false);
                        Toast.makeText(LoginActivity.this,
                                "Erreur réseau : " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void sendPasswordReset() {
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        if (email.isEmpty()) {
            Toast.makeText(this, "Entre ton email d'abord", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(v ->
                        Toast.makeText(this, "Email de réinitialisation envoyé !", Toast.LENGTH_LONG).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Erreur : " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void navigateToHome(String role) {
        Intent intent;
        switch (role != null ? role : "client") {
            case "admin":    intent = new Intent(this, AdminHomeActivity.class); break;
            case "merchant": intent = new Intent(this, MerchantHomeActivity.class); break;
            default:         intent = new Intent(this, ClientHomeActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void showLoading(boolean show) {
        if (progressBar != null)
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        findViewById(R.id.btnLogin).setEnabled(!show);
    }
}