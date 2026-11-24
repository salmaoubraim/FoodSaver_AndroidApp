package com.foodsaver.android.ui.client;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PaniersActivity extends BaseActivity {

    private String commerceId;
    private String commerceName;

    private final List<Panier> paniers = new ArrayList<>();
    private PanierClientAdapter   adapter;
    private PanierRepository      panierRepo;
    private ReservationRepository reservationRepo;
    private NotificationRepository notifRepo;
    private CommerceRepository     commerceRepo;

    // ✅ NOUVEAU : garder référence du listener Firebase
    private ValueEventListener panierListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paniers);

        commerceId   = getIntent().getStringExtra("commerceId");
        commerceName = getIntent().getStringExtra("commerceName");
        if (commerceId == null) { finish(); return; }

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(commerceName != null ? commerceName : getString(R.string.paniers));
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            toolbar.setNavigationOnClickListener(v -> goHome());
        }

        panierRepo      = new PanierRepository(this);
        reservationRepo = new ReservationRepository(this);
        notifRepo       = new NotificationRepository(this);
        commerceRepo    = new CommerceRepository(this);

        RecyclerView recycler = findViewById(R.id.recyclerPaniers);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PanierClientAdapter(paniers, this::onPanierClick, this::onReserveClick);
        recycler.setAdapter(adapter);

        loadPaniers();
    }

    // ✅ NOUVEAU : libérer le listener Firebase
    @Override
    protected void onDestroy() {
        super.onDestroy();
        panierRepo.removePaniersListener(panierListener);
    }

    @Override public void onBackPressed() { goHome(); }

    private void loadPaniers() {
        // 1. Afficher ce qu'on a déjà en local
        paniers.clear();
        paniers.addAll(panierRepo.getPaniersByCommerce(commerceId));
        adapter.notifyDataSetChanged();

        // 2. ✅ Écouter Firebase et rafraîchir quand les données arrivent
        panierListener = panierRepo.listenPaniersByCommerce(commerceId, updatedList -> {
            paniers.clear();
            paniers.addAll(updatedList);
            runOnUiThread(() -> adapter.notifyDataSetChanged());
        });
    }

    private void onPanierClick(Panier panier) {
        startActivity(new android.content.Intent(this, PanierDetailActivity.class)
                .putExtra("panierId",     panier.id)
                .putExtra("commerceId",   commerceId)
                .putExtra("commerceName", commerceName));
    }

    private void onReserveClick(Panier panier) {
        if (panier.quantite <= 0) {
            Toast.makeText(this, "Plus de stock.", Toast.LENGTH_SHORT).show();
            return;
        }
        int maxQty = panier.quantite;
        final int[] selected = {1};
        String[] options = new String[maxQty];
        for (int i = 0; i < maxQty; i++) options[i] = String.valueOf(i + 1);

        new AlertDialog.Builder(this)
                .setTitle(R.string.reserve)
                .setMessage(getString(R.string.quantity) + " (max " + maxQty + ")")
                .setSingleChoiceItems(options, 0, (dialog, which) -> selected[0] = which + 1)
                .setPositiveButton(R.string.reserve, (dialog, which) -> {
                    String userId = new SessionManager(this).getUserId();
                    Reservation r = new Reservation(userId, panier.id, commerceId,
                            selected[0], System.currentTimeMillis());
                    reservationRepo.insertReservation(r, reservation -> {
                        panier.quantite -= selected[0];
                        panierRepo.updatePanier(panier);
                        runOnUiThread(() -> {
                            Toast.makeText(this, R.string.reservation_confirmed, Toast.LENGTH_SHORT).show();
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
                            Commerce commerce = commerceRepo.getById(commerceId);
                            if (commerce != null && commerce.merchantId != null) {
                                String cName = commerce.name != null ? commerce.name : "";
                                notifRepo.insertAsync(new AppNotification(
                                        commerce.merchantId,
                                        getString(R.string.app_name),
                                        getString(R.string.notification_nouvelle_reservation, cName),
                                        now
                                ));
                            }

                            loadPaniers();
                        });
                    });
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }
}