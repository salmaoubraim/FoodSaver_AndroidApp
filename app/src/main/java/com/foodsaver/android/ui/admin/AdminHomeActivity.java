package com.foodsaver.android.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import com.foodsaver.android.ui.BaseActivity;

import com.foodsaver.android.R;
import com.foodsaver.android.auth.SessionManager;
import com.foodsaver.android.data.CommerceRepository;
import com.foodsaver.android.data.ReservationRepository;
import com.foodsaver.android.data.UserRepository;
import com.foodsaver.android.ui.MainActivity;

public class AdminHomeActivity extends BaseActivity {

    private TextView tvTotalUsers, tvTotalCommerces, tvTotalReservations;
    private UserRepository userRepo;
    private CommerceRepository commerceRepo;
    private ReservationRepository reservationRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.admin_panel);
        }

        tvTotalUsers = findViewById(R.id.tvTotalUsers);
        tvTotalCommerces = findViewById(R.id.tvTotalCommerces);
        tvTotalReservations = findViewById(R.id.tvTotalReservations);

        userRepo = new UserRepository(this);
        commerceRepo = new CommerceRepository(this);
        reservationRepo = new ReservationRepository(this);

        findViewById(R.id.cardUsers).setOnClickListener(v ->
            startActivity(new Intent(this, UserManagementActivity.class)));

        findViewById(R.id.cardCommerces).setOnClickListener(v ->
            startActivity(new Intent(this, CommerceValidationActivity.class)));

        loadStats();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
            .setMessage(R.string.exit_app_confirm)
            .setPositiveButton(R.string.yes, (d, w) -> finishAffinity())
            .setNegativeButton(R.string.no, null)
            .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStats();
    }

    private void loadStats() {
        tvTotalUsers.setText(String.valueOf(userRepo.getAllUsers().size()));
        tvTotalCommerces.setText(String.valueOf(commerceRepo.getAllCommerces().size()));
        tvTotalReservations.setText(String.valueOf(reservationRepo.countAllReservations()));
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            new SessionManager(this).logout();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
