package com.foodsaver.android.ui.merchant;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodsaver.android.R;
import com.foodsaver.android.auth.SessionManager;
import com.foodsaver.android.data.Panier;
import com.foodsaver.android.data.PanierRepository;
import com.foodsaver.android.data.Reservation;
import com.foodsaver.android.data.ReservationRepository;
import com.foodsaver.android.data.UserRepository;
import com.foodsaver.android.ui.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReservationsActivity extends BaseActivity {

    // ✅ String au lieu de long
    private String commerceId;
    private String commerceName;

    private final List<ReservationWithDetails> items = new ArrayList<>();
    private ReservationMerchantAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations);

        // ✅ getStringExtra au lieu de getLongExtra
        commerceId   = getIntent().getStringExtra("commerceId");
        commerceName = getIntent().getStringExtra("commerceName");
        if (commerceId == null) { finish(); return; }

        // ✅ getUserId() retourne String
        String merchantId = new SessionManager(this).getUserId();

        ReservationRepository reservationRepo = new ReservationRepository(this);
        PanierRepository      panierRepo      = new PanierRepository(this);
        UserRepository        userRepo        = new UserRepository(this);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(commerceName != null
                ? commerceName + " - Réservations"
                : getString(R.string.reservations_received));
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // ✅ getByMerchant prend String
        List<Reservation> list = reservationRepo.getByMerchant(merchantId);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        for (Reservation r : list) {
            // ✅ getById prend String
            Panier p = panierRepo.getById(r.panierId);
            // ✅ comparaison String
            if (p == null || !commerceId.equals(p.commerceId)) continue;

            // ✅ UserRepository.getById async → on utilise le nom depuis Firestore
            String[] clientName = {"?"};
            userRepo.getById(r.userId, user -> {
                if (user != null) clientName[0] = user.name != null ? user.name : "?";
            });

            items.add(new ReservationWithDetails(r, p.titre, clientName[0],
                sdf.format(new Date(r.dateReservation))));
        }

        RecyclerView recycler = findViewById(R.id.recyclerReservations);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReservationMerchantAdapter(items);
        recycler.setAdapter(adapter);

        TextView tvEmpty = findViewById(R.id.tvEmpty);
        if (tvEmpty != null) {
            tvEmpty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
            recycler.setVisibility(items.isEmpty() ? View.GONE : View.VISIBLE);
        }
    }

    public static class ReservationWithDetails {
        public final Reservation reservation;
        public final String panierTitre;
        public final String clientName;
        public final String dateStr;

        public ReservationWithDetails(Reservation r, String panierTitre,
                                      String clientName, String dateStr) {
            this.reservation  = r;
            this.panierTitre  = panierTitre;
            this.clientName   = clientName;
            this.dateStr      = dateStr;
        }
    }
}
