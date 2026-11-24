package com.foodsaver.android.ui.client;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import com.foodsaver.android.ui.BaseActivity;

import com.foodsaver.android.R;
import com.foodsaver.android.auth.SessionManager;
import com.foodsaver.android.ui.MainActivity;
import com.foodsaver.android.util.LanguageManager;
import com.google.android.material.button.MaterialButton;

public class ClientProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LanguageManager.applyLanguage(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile);

        SessionManager session = new SessionManager(this);

        TextView tvName  = findViewById(R.id.tvName);
        TextView tvEmail = findViewById(R.id.tvEmail);
        String name  = session.getUserName();
        String email = session.getEmail();
        tvName.setText((name  != null && !name.isEmpty())  ? name  : "Client");
        tvEmail.setText((email != null && !email.isEmpty()) ? email : "");

        MaterialButton btnLogout = findViewById(R.id.btnLogout);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                session.logout(); // ✅ déconnexion
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
            setupBottomNav(R.id.nav_profile);
        }

        android.view.View optLang = findViewById(R.id.optionLanguage);
        if (optLang != null) optLang.setOnClickListener(v -> showLanguageDialog());
    }

    private void showLanguageDialog() {
        String[] langs = {"Français", "العربية", "English"};
        String[] codes = {"fr", "ar", "en"};
        new AlertDialog.Builder(this)
            .setTitle("Langue / اللغة / Language")
            .setItems(langs, (d, i) -> {
                LanguageManager.setLanguage(this, codes[i]);
                recreate();
            })
            .setNegativeButton(android.R.string.cancel, null)
            .show();
    }

    @Override
    public void onBackPressed() { goHome(); }
}