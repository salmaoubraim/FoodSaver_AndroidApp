package com.foodsaver.android.ui.client;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.foodsaver.android.R;
import com.foodsaver.android.auth.SessionManager;
import com.foodsaver.android.data.Evaluation;
import com.foodsaver.android.data.EvaluationRepository;
import com.foodsaver.android.ui.BaseActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RateCommerceActivity extends BaseActivity {

    // ✅ String au lieu de long
    private String commerceId;
    private EvaluationRepository evaluationRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_commerce);

        // ✅ getStringExtra au lieu de getLongExtra
        commerceId = getIntent().getStringExtra("commerceId");
        String commerceName = getIntent().getStringExtra("commerceName");
        if (commerceId == null) { finish(); return; }

        evaluationRepo = new EvaluationRepository(this);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        TextView tvCommerceName = findViewById(R.id.tvCommerceName);
        if (tvCommerceName != null) {
            tvCommerceName.setText(getString(R.string.rating_for,
                (commerceName != null && !commerceName.isEmpty()) ? commerceName : "ce commerce"));
        }

        RatingBar          ratingBar = findViewById(R.id.ratingBar);
        TextInputEditText  etComment = findViewById(R.id.etComment);
        MaterialButton     btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            if (rating < 0.5f) {
                Toast.makeText(this, "Veuillez choisir une note entre 1 et 5", Toast.LENGTH_SHORT).show();
                return;
            }
            // ✅ getUserId() retourne String
            String userId = new SessionManager(this).getUserId();
            if (userId == null) {
                Toast.makeText(this, R.string.error_login, Toast.LENGTH_SHORT).show();
                return;
            }
            String comment = etComment.getText() != null
                ? etComment.getText().toString().trim() : null;

            // ✅ Evaluation prend Strings
            Evaluation eval = new Evaluation(userId, commerceId, rating, comment,
                System.currentTimeMillis());

            // ✅ nouvelle signature async
            evaluationRepo.addEvaluation(eval, evaluation -> runOnUiThread(() -> {
                Toast.makeText(this, R.string.rating_saved, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, ClientHomeActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }));
        });
    }
}
