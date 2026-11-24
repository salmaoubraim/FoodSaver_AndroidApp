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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends BaseActivity {

    private final List<ReservationWithDetails> items = new ArrayList<>();
    private HistoryAdapter adapter;
    private ReservationRepository reservationRepo;
    private PanierRepository      panierRepo;
    private CommerceRepository    commerceRepo;
    private NotificationRepository notifRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupBottomNav(R.id.nav_cart);

        // ✅ getUserId() retourne String
        String userId = new SessionManager(this).getUserId();

        reservationRepo = new ReservationRepository(this);
        panierRepo      = new PanierRepository(this);
        commerceRepo    = new CommerceRepository(this);
        notifRepo       = new NotificationRepository(this);

        RecyclerView recycler = findViewById(R.id.recyclerHistory);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryAdapter(items, this::onCancelClick);
        recycler.setAdapter(adapter);

        loadHistory(userId);
    }

    private void loadHistory(String userId) {
        items.clear();
        // ✅ getByUser prend String
        List<Reservation> list = reservationRepo.getByUser(userId);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        for (Reservation r : list) {
            // Ne pas afficher les réservations annulées dans la liste
            if ("annulee".equalsIgnoreCase(r.statut)) continue;

            // ✅ getById prend String
            Panier p = panierRepo.getById(r.panierId);
            String commerceName = "";
            if (p != null) {
                // ✅ getById prend String
                Commerce c = commerceRepo.getById(p.commerceId);
                if (c != null) commerceName = c.name;
            }
            String panierTitre = (p != null && p.titre != null) ? p.titre : "";
            items.add(new ReservationWithDetails(r, panierTitre, commerceName,
                sdf.format(new Date(r.dateReservation))));
        }
        adapter.notifyDataSetChanged();
    }

    private void onCancelClick(ReservationWithDetails item) {
        new AlertDialog.Builder(this)
            .setTitle(R.string.cancel)
            .setMessage("Annuler cette réservation ?")
            .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                // ✅ cancelReservation prend String
                reservationRepo.cancelReservation(item.reservation.id);
                Panier p = panierRepo.getById(item.reservation.panierId);
                if (p != null) {
                    p.quantite += item.reservation.quantiteReservee;
                    panierRepo.updatePanier(p);
                }
                Toast.makeText(this, R.string.reservation_cancelled, Toast.LENGTH_SHORT).show();
                // Notification locale pour le client : annulation
                NotificationHelper.showClientNotification(
                        this,
                        getString(R.string.app_name),
                        getString(R.string.reservation_cancelled),
                        item.reservation.id.hashCode()
                );

                // Notifications in-app (cloche) pour client + commerçant
                long now = System.currentTimeMillis();
                String clientId = new SessionManager(this).getUserId();
                if (clientId != null) {
                    notifRepo.insertAsync(new AppNotification(
                            clientId,
                            getString(R.string.app_name),
                            getString(R.string.notification_reservation_annulee_client),
                            now
                    ));
                }
                // Commerçant
                if (p != null) {
                    Commerce c = commerceRepo.getById(p.commerceId);
                    if (c != null && c.merchantId != null) {
                        notifRepo.insertAsync(new AppNotification(
                                c.merchantId,
                                getString(R.string.app_name),
                                getString(R.string.notification_reservation_annulee_merchant,
                                        c.name != null ? c.name : ""),
                                now
                        ));
                    }
                }
                loadHistory(new SessionManager(this).getUserId());
            })
            .setNegativeButton(android.R.string.no, null)
            .show();
    }

    public static class ReservationWithDetails {
        public final Reservation reservation;
        public final String panierTitre;
        public final String commerceName;
        public final String dateStr;

        public ReservationWithDetails(Reservation reservation, String panierTitre,
                                      String commerceName, String dateStr) {
            this.reservation  = reservation;
            this.panierTitre  = panierTitre;
            this.commerceName = commerceName;
            this.dateStr      = dateStr;
        }
    }
}
