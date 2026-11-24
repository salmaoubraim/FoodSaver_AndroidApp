package com.foodsaver.android.ui.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.foodsaver.android.R;
import com.foodsaver.android.ui.BaseActivity;
import com.google.android.material.button.MaterialButton;

public class OrderSuccessActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        // ✅ commerceId devient String
        String commerceId   = getIntent().getStringExtra("commerceId");
        String commerceName = getIntent().getStringExtra("commerceName");

        MaterialButton btnRate = findViewById(R.id.btnRate);
        if (commerceId != null) {
            btnRate.setVisibility(View.VISIBLE);
            btnRate.setOnClickListener(v -> {
                startActivity(new Intent(this, RateCommerceActivity.class)
                    .putExtra("commerceId",   commerceId)                             // ✅ String
                    .putExtra("commerceName", commerceName != null ? commerceName : ""));
                finish();
            });
        }

        findViewById(R.id.btnOk).setOnClickListener(v -> {
            startActivity(new Intent(this, ClientHomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        });
    }
}
