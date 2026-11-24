package com.foodsaver.android.ui;

import android.content.Intent;
import android.os.Bundle;
import com.foodsaver.android.ui.BaseActivity;

import com.foodsaver.android.R;
import com.foodsaver.android.auth.SessionManager;
import com.foodsaver.android.ui.admin.AdminHomeActivity;
import com.foodsaver.android.ui.auth.LoginActivity;
import com.foodsaver.android.ui.auth.RegisterActivity;
import com.foodsaver.android.ui.client.ClientHomeActivity;
import com.foodsaver.android.ui.merchant.MerchantHomeActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SessionManager session = new SessionManager(this);
        if (session.isLoggedIn()) {
            String role = session.getRole();
            Intent intent;
            switch (role != null ? role : "client") {
                case "admin":
                    intent = new Intent(this, AdminHomeActivity.class);
                    break;
                case "merchant":
                    intent = new Intent(this, MerchantHomeActivity.class);
                    break;
                default:
                    intent = new Intent(this, ClientHomeActivity.class);
            }
            startActivity(intent);
            finish();
            return;
        }

        findViewById(R.id.btnLogin).setOnClickListener(v ->
            startActivity(new Intent(this, LoginActivity.class)));
        findViewById(R.id.btnRegister).setOnClickListener(v ->
            startActivity(new Intent(this, RegisterActivity.class)));
    }
}
