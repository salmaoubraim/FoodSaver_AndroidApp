package com.foodsaver.android.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.foodsaver.android.R;
import com.foodsaver.android.auth.SessionManager;
import com.foodsaver.android.ui.admin.AdminHomeActivity;
import com.foodsaver.android.ui.auth.LoginActivity;
import com.foodsaver.android.ui.client.ClientHomeActivity;
import com.foodsaver.android.ui.merchant.MerchantHomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
// ✅ Remplacer FirebaseFirestore par FirebaseDatabase
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends BaseActivity {

    private static final String PREFS_APP           = "FoodSaverApp";
    private static final String KEY_ONBOARDING_DONE = "onboarding_done";
    private static final long   SPLASH_DELAY_MS     = 2000;
    private static final long   FADE_DURATION_MS    = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        View logoIcon  = findViewById(R.id.logoIcon);
        View tvAppName = findViewById(R.id.tvAppName);
        ObjectAnimator fadeLogo = ObjectAnimator.ofFloat(logoIcon,  View.ALPHA, 0f, 1f);
        fadeLogo.setDuration(FADE_DURATION_MS);
        ObjectAnimator fadeText = ObjectAnimator.ofFloat(tvAppName, View.ALPHA, 0f, 1f);
        fadeText.setDuration(FADE_DURATION_MS);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(fadeLogo, fadeText);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.start();

        new Handler(Looper.getMainLooper()).postDelayed(this::navigateNext, SPLASH_DELAY_MS);
    }

    private void navigateNext() {
        SharedPreferences prefs = getSharedPreferences(PREFS_APP, MODE_PRIVATE);
        boolean onboardingDone  = prefs.getBoolean(KEY_ONBOARDING_DONE, false);

        if (!onboardingDone) {
            startActivity(new Intent(this, OnboardingActivity.class));
            finish();
            return;
        }

        FirebaseUser   firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        SessionManager session      = new SessionManager(this);

        if (firebaseUser != null && session.isLoggedIn()) {
            navigateToHome(session.getRole());

        } else if (firebaseUser != null && !session.isLoggedIn()) {
            // ✅ Realtime Database au lieu de Firestore
            FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(firebaseUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snap) {
                            if (snap.exists()) {
                                String role  = snap.child("role").getValue(String.class);
                                String name  = snap.child("name").getValue(String.class);
                                String email = snap.child("email").getValue(String.class);
                                session.login(firebaseUser.getUid(), email, role, name);
                                navigateToHome(role);
                            } else {
                                goToLogin();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError e) { goToLogin(); }
                    });
        } else {
            goToLogin();
        }
    }

    private void navigateToHome(String role) {
        Intent intent;
        switch (role != null ? role : "client") {
            case "admin":    intent = new Intent(this, AdminHomeActivity.class);    break;
            case "merchant": intent = new Intent(this, MerchantHomeActivity.class); break;
            default:         intent = new Intent(this, ClientHomeActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void goToLogin() {
        startActivity(new Intent(this, LoginActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();
    }
}