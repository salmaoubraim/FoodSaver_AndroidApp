package com.foodsaver.android.ui.merchant;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.foodsaver.android.R;
import com.foodsaver.android.auth.SessionManager;
import com.foodsaver.android.data.Commerce;
import com.foodsaver.android.data.CommerceRepository;
import com.foodsaver.android.ui.BaseActivity;
import com.foodsaver.android.ui.MainActivity;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

public class MerchantProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_profile);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        // ✅ getUserId() retourne String
        String userId = new SessionManager(this).getUserId();

        CommerceRepository   repo      = new CommerceRepository(this);
        // ✅ getCommercesByMerchant prend String
        List<Commerce> commerces = repo.getCommercesByMerchant(userId);

        if (!commerces.isEmpty()) {
            Commerce c = commerces.get(0);
            TextView tvName = findViewById(R.id.tvCommerceName);
            TextView tvAddr = findViewById(R.id.tvCommerceAddress);
            if (tvName != null) tvName.setText(c.name);
            if (tvAddr != null) {
                String addr = c.address != null
                    ? c.address + ", " + (c.city != null ? c.city : "")
                    : (c.city != null ? c.city : "");
                tvAddr.setText(addr);
            }
        }

        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            new SessionManager(this).logout();
            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        });
    }
}
