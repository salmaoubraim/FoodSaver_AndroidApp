package com.foodsaver.android.ui.merchant;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.foodsaver.android.R;
import com.foodsaver.android.auth.SessionManager;
import com.foodsaver.android.data.Commerce;
import com.foodsaver.android.data.CommerceRepository;
import com.foodsaver.android.data.Panier;
import com.foodsaver.android.data.PanierRepository;
import com.foodsaver.android.data.Reservation;
import com.foodsaver.android.data.ReservationRepository;
import com.foodsaver.android.ui.BaseActivity;

import java.util.List;
import java.util.Locale;

public class StatsActivity extends BaseActivity {

    private TextView      tvTotalCommerces, tvTotalPaniers, tvTotalReservations, tvTotalRevenue;
    private ProgressBar   progressBar;
    private CommerceRepository    commerceRepo;
    private PanierRepository      panierRepo;
    private ReservationRepository reservationRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.statistics);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        tvTotalCommerces    = findViewById(R.id.tvTotalCommerces);
        tvTotalPaniers      = findViewById(R.id.tvTotalPaniers);
        tvTotalReservations = findViewById(R.id.tvTotalReservations);
        tvTotalRevenue      = findViewById(R.id.tvTotalRevenue);
        progressBar         = findViewById(R.id.progressBar);

        commerceRepo    = new CommerceRepository(this);
        panierRepo      = new PanierRepository(this);
        reservationRepo = new ReservationRepository(this);

        View btn = findViewById(R.id.btnRefresh);
        if (btn != null) btn.setOnClickListener(v -> loadStats());

        loadStats();
    }

    private void loadStats() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);

        // ✅ getUserId() retourne String
        String merchantId = new SessionManager(this).getUserId();

        // ✅ getCommercesByMerchant / getByMerchant prennent String
        List<Commerce>    commerces    = commerceRepo.getCommercesByMerchant(merchantId);
        List<Reservation> reservations = reservationRepo.getByMerchant(merchantId);

        int    totalPaniers = 0;
        double totalRevenu  = 0;

        for (Commerce c : commerces) {
            // ✅ getPaniersByCommerce prend String (c.id est déjà String)
            totalPaniers += panierRepo.getPaniersByCommerce(c.id).size();
        }
        for (Reservation r : reservations) {
            // ✅ getById prend String
            Panier p = panierRepo.getById(r.panierId);
            if (p != null) totalRevenu += r.quantiteReservee * p.prix;
        }

        if (progressBar         != null) progressBar.setVisibility(View.GONE);
        if (tvTotalCommerces    != null) tvTotalCommerces.setText(String.valueOf(commerces.size()));
        if (tvTotalPaniers      != null) tvTotalPaniers.setText(String.valueOf(totalPaniers));
        if (tvTotalReservations != null) tvTotalReservations.setText(String.valueOf(reservations.size()));
        if (tvTotalRevenue      != null) tvTotalRevenue.setText(
            String.format(Locale.FRANCE, "%.0f MAD", totalRevenu));
    }

    @Override public void onBackPressed()          { finish(); }
    @Override public boolean onSupportNavigateUp() { finish(); return true; }
}
