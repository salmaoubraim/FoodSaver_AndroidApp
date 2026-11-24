package com.foodsaver.android.ui.client;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.foodsaver.android.R;
import com.foodsaver.android.auth.SessionManager;
import com.foodsaver.android.data.Commerce;
import com.foodsaver.android.data.CommerceRepository;
import com.foodsaver.android.data.Panier;
import com.foodsaver.android.data.PanierRepository;
import com.foodsaver.android.data.Reservation;
import com.foodsaver.android.data.ReservationRepository;
import com.foodsaver.android.data.AppNotification;
import com.foodsaver.android.data.NotificationRepository;
import com.foodsaver.android.ui.BaseActivity;
import com.foodsaver.android.util.NotificationHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.database.ValueEventListener;

public class PanierDetailActivity extends BaseActivity {

    private Panier   panier;
    private Commerce commerce;
    private double   totalPrice;

    private PanierRepository panierRepo;

    // ✅ NOUVEAU : garder référence du listener Firebase
    private ValueEventListener panierListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panier_detail);

        String panierId   = getIntent().getStringExtra("panierId");
        String commerceId = getIntent().getStringExtra("commerceId");
        if (panierId == null || commerceId == null) { finish(); return; }

        panierRepo         = new PanierRepository(this);
        CommerceRepository cr = new CommerceRepository(this);

        commerce = cr.getById(commerceId);

        // ✅ Charger local d'abord
        panier = panierRepo.getById(panierId);

        // ✅ Si panier null en local, attendre sync Firebase
        if (panier == null) {
            panierListener = panierRepo.listenPanierById(panierId, syncedPanier -> {
                if (syncedPanier == null) { runOnUiThread(this::finish); return; }
                panier = syncedPanier;
                runOnUiThread(() -> setupUI());
            });
        } else if (commerce == null) {
            finish();
        } else {
            setupUI();
        }
    }

    // ✅ NOUVEAU : libérer le listener Firebase
    @Override
    protected void onDestroy() {
        super.onDestroy();
        panierRepo.removePanierListener(panierListener);
    }

    private void setupUI() {
        if (commerce == null) { finish(); return; }

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnFavorite).setOnClickListener(v -> { /* TODO */ });

        ImageView imgPanier = findViewById(R.id.imgPanier);
        if (panier.imageUrl != null && !panier.imageUrl.isEmpty()) {
            Glide.with(this).load(panier.imageUrl)
                    .placeholder(R.drawable.placeholder_food)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imgPanier);
        } else {
            imgPanier.setImageResource(R.drawable.placeholder_food);
        }

        ((TextView) findViewById(R.id.tvPanierTitle)).setText(panier.titre);
        String meta = (commerce.categorie != null ? commerce.categorie : "•")
                + " | ⭐ " + String.format("%.1f", commerce.rating) + " | ⏰ 20-30 min";
        ((TextView) findViewById(R.id.tvMeta)).setText(meta);
        ((TextView) findViewById(R.id.tvDescription)).setText(
                panier.description != null ? panier.description : getString(R.string.no_paniers));
        String loc = (commerce.quartier != null ? commerce.quartier
                : commerce.address != null ? commerce.address : "")
                + ", " + (commerce.city != null ? commerce.city : "Agadir");
        ((TextView) findViewById(R.id.tvLocation)).setText(loc);

        MaterialCheckBox optSauce = findViewById(R.id.optSauce);
        MaterialCheckBox optBread = findViewById(R.id.optBread);
        TextView tvTotalPrice     = findViewById(R.id.tvTotalPrice);
        updateTotal(tvTotalPrice, optSauce.isChecked(), optBread.isChecked());
        optSauce.setOnCheckedChangeListener((v, c) -> updateTotal(tvTotalPrice, c, optBread.isChecked()));
        optBread.setOnCheckedChangeListener((v, c) -> updateTotal(tvTotalPrice, optSauce.isChecked(), c));

        MaterialButton btnReserve = findViewById(R.id.btnReserve);
        btnReserve.setEnabled(panier.quantite > 0);
        btnReserve.setOnClickListener(v ->
                showCheckoutDialog(optSauce.isChecked(), optBread.isChecked()));
    }

    private void updateTotal(TextView tv, boolean sauce, boolean bread) {
        totalPrice = panier.prix + (sauce ? 2 : 0) + (bread ? 1 : 0);
        tv.setText(getString(R.string.total_price, String.format("%.0f", totalPrice)));
    }

    private void showCheckoutDialog(boolean sauce, boolean bread) {
        double delivery = 10, subtotal = totalPrice, total = subtotal + delivery;
        String msg = "Sous-total : " + String.format("%.0f", subtotal)
                + " MAD\nLivraison : " + String.format("%.0f", delivery)
                + " MAD\nTotal : "     + String.format("%.0f", total) + " MAD";
        new AlertDialog.Builder(this)
                .setTitle(R.string.checkout)
                .setMessage(msg + "\n\nMéthode de paiement : Cash à la livraison (par défaut)")
                .setPositiveButton(R.string.reserve_button, (d, w) -> doReserve())
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void doReserve() {
        String userId = new SessionManager(this).getUserId();
        if (userId == null) {
            Toast.makeText(this, R.string.error_login, Toast.LENGTH_SHORT).show();
            return;
        }
        int qty = 1;
        ReservationRepository reservationRepo = new ReservationRepository(this);
        PanierRepository      panierRepo      = new PanierRepository(this);
        NotificationRepository notifRepo      = new NotificationRepository(this);

        Reservation r = new Reservation(userId, panier.id, commerce.id,
                qty, System.currentTimeMillis());

        reservationRepo.insertReservation(r, reservation -> {
            panier.quantite -= qty;
            panierRepo.updatePanier(panier);
            runOnUiThread(() -> {
                // Notification locale immédiate pour le client
                NotificationHelper.showClientNotification(
                        this,
                        getString(R.string.app_name),
                        getString(R.string.reservation_confirmed),
                        reservation.id.hashCode()
                );

                // Notifications in-app (cloche) pour client + commerçant
                long now = System.currentTimeMillis();
                // Client
                notifRepo.insertAsync(new AppNotification(
                        userId,
                        getString(R.string.app_name),
                        getString(R.string.notification_reservation_confirmee),
                        now
                ));
                // Commerçant
                if (commerce.merchantId != null) {
                    notifRepo.insertAsync(new AppNotification(
                            commerce.merchantId,
                            getString(R.string.app_name),
                            getString(R.string.notification_nouvelle_reservation, commerce.name),
                            now
                    ));
                }

                startActivity(new Intent(this, OrderSuccessActivity.class)
                        .putExtra("commerceId",   commerce.id)
                        .putExtra("commerceName", commerce.name));
                finish();
            });
        });
    }
}