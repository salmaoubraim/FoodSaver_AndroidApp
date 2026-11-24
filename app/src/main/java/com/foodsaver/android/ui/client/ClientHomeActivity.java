package com.foodsaver.android.ui.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodsaver.android.R;
import com.foodsaver.android.auth.SessionManager;
import com.foodsaver.android.data.Commerce;
import com.foodsaver.android.data.CommerceRepository;
import com.foodsaver.android.data.PanierRepository;
import com.foodsaver.android.data.ReservationRepository;
import com.foodsaver.android.data.Reservation;
import com.foodsaver.android.util.NotificationHelper;
import androidx.lifecycle.ViewModelProvider;

import com.foodsaver.android.ui.BaseActivity;
import com.foodsaver.android.ui.NotificationsActivity;
import com.foodsaver.android.ui.NotificationsViewModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ClientHomeActivity extends BaseActivity {

    private static final String CATEGORY_ALL = "all";

    private final List<Commerce> allCommerces      = new ArrayList<>();
    private final List<Commerce> filteredCommerces = new ArrayList<>();

    private Set<String> commerceIdsWithStock = new HashSet<>();

    private CommerceRepository    commerceRepo;
    private PanierRepository      panierRepo;
    private ReservationRepository reservationRepo; // ✅ AJOUT
    private CommerceAdapter       adapter;

    private String selectedCategory       = CATEGORY_ALL;
    private String searchQuery            = "";
    private String filterByPanierCategory = null;

    private ValueEventListener  commerceListener;
    private Query               clientReservationQuery;
    private ChildEventListener  clientReservationListener;
    private final java.util.Map<String, String> reservationStatusCache = new java.util.HashMap<>();

    private final String[] categoryKeys   = { CATEGORY_ALL, "snack", "cafe", "restaurant", "boulangerie", "epicerie", "autre" };
    private final String[] categoryLabels = { "Tous", "Snack", "Café", "Restaurant", "Boulangerie", "Épicerie", "Autre" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);

        commerceRepo    = new CommerceRepository(this);
        panierRepo      = new PanierRepository(this);
        reservationRepo = new ReservationRepository(this); // ✅ AJOUT

        if (getIntent() != null && getIntent().hasExtra("filterCategory")) {
            filterByPanierCategory = getIntent().getStringExtra("filterCategory");
            selectedCategory = CATEGORY_ALL;
        }

        TextView tvGreeting = findViewById(R.id.tvGreeting);
        String name = new SessionManager(this).getUserName();
        tvGreeting.setText(getString(R.string.greeting_format, name.isEmpty() ? "Client" : name));

        // Icône cloche : ouvre le centre de notifications dédié + badge dynamique
        NotificationsViewModel notifVm =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View toolbarInclude = findViewById(R.id.toolbar);
        if (toolbarInclude != null) {
            View bell = toolbarInclude.findViewById(R.id.btnNotification);
            TextView badge = toolbarInclude.findViewById(R.id.tvNotificationBadge);
            if (bell != null) {
                bell.setOnClickListener(v ->
                        startActivity(new Intent(this, NotificationsActivity.class)));
            }
            if (badge != null) {
                notifVm.getUnreadCount().observe(this, count -> {
                    int c = count != null ? count : 0;
                    if (c <= 0) {
                        badge.setVisibility(View.GONE);
                    } else {
                        badge.setVisibility(View.VISIBLE);
                        badge.setText(String.valueOf(Math.min(c, 99)));
                    }
                });
            }
        }

        setupSearchView();
        setupBanner();
        setupCategoryChips();
        setupRecyclerCommerces();
        setupBottomNav(R.id.nav_home);
        setupFilterBar();
        loadData();

        startClientNotifications();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (intent != null && intent.hasExtra("filterCategory")) {
            filterByPanierCategory = intent.getStringExtra("filterCategory");
            selectedCategory = CATEGORY_ALL;
            loadData();
            updateFilterBar();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        commerceRepo.removeCommercesListener(commerceListener);
        stopClientNotifications();
    }

    private void setupFilterBar() {
        TextView tvShowAll = findViewById(R.id.tvShowAll);
        if (tvShowAll != null) tvShowAll.setOnClickListener(v -> {
            filterByPanierCategory = null;
            applyFilters();
            updateFilterBar();
        });
    }

    private void updateFilterBar() {
        View bar          = findViewById(R.id.filterCategoryBar);
        TextView tvFilter = findViewById(R.id.tvFilterCategory);
        if (bar == null || tvFilter == null) return;
        if (filterByPanierCategory != null && !filterByPanierCategory.isEmpty()) {
            bar.setVisibility(View.VISIBLE);
            tvFilter.setText(getString(R.string.results_for_category,
                    getPanierCategoryLabel(filterByPanierCategory)));
        } else {
            bar.setVisibility(View.GONE);
        }
    }

    private static String getPanierCategoryLabel(String key) {
        if (key == null) return "";
        switch (key) {
            case "tagine":         return "Tagine";
            case "pizza":          return "Pizza";
            case "sandwich":       return "Sandwich";
            case "petit_dejeuner":
            case "petit-dejeuner": return "Petit-déjeuner";
            case "salade":         return "Salade";
            case "autre":          return "Autre";
            default:               return key;
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.exit_app_confirm)
                .setPositiveButton(R.string.yes, (d, w) -> finishAffinity())
                .setNegativeButton(R.string.no,  (d, w) -> super.onBackPressed())
                .show();
    }

    private void setupSearchView() {
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String q) { return false; }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchQuery = newText != null ? newText.trim().toLowerCase(Locale.getDefault()) : "";
                applyFilters();
                return true;
            }
        });
    }

    private void setupBanner() {
        findViewById(R.id.btnBannerOrder).setOnClickListener(v -> {
            if (!filteredCommerces.isEmpty()) {
                List<String> ids = new ArrayList<>();
                for (Commerce c : allCommerces) ids.add(c.id);

                reservationRepo.getTopCommerceByReservations(ids, (topId, count) -> {
                    runOnUiThread(() -> {
                        Commerce target = null;
                        for (Commerce c : allCommerces)
                            if (c.id != null && c.id.equals(topId)) { target = c; break; }
                        if (target == null) target = filteredCommerces.get(0);

                        startActivity(new Intent(this, PaniersActivity.class)
                                .putExtra("commerceId",   target.id)
                                .putExtra("commerceName", target.name));
                    });
                });
            } else {
                Toast.makeText(this, R.string.no_commerces, Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.tvSeeMore).setOnClickListener(v ->
                startActivity(new Intent(this, CategoriesActivity.class)));
    }

    // ✅ Affiche le nom du top commerce dans le banner
    private void updateBannerName() {
        if (allCommerces.isEmpty()) return;

        List<String> ids = new ArrayList<>();
        for (Commerce c : allCommerces) ids.add(c.id);

        reservationRepo.getTopCommerceByReservations(ids, (topId, count) -> {
            runOnUiThread(() -> {
                TextView tvName = findViewById(R.id.tvBannerCommerceName);
                if (tvName == null) return;

                Commerce top = null;
                for (Commerce c : allCommerces)
                    if (c.id != null && c.id.equals(topId)) { top = c; break; }

                if (top != null) {
                    tvName.setText("⭐ " + top.name);
                    tvName.setVisibility(View.VISIBLE);
                } else if (!allCommerces.isEmpty()) {
                    tvName.setText(allCommerces.get(0).name);
                    tvName.setVisibility(View.VISIBLE);
                }
            });
        });
    }

    private void setupCategoryChips() {
        RecyclerView recyclerCategories = findViewById(R.id.recyclerCategories);
        recyclerCategories.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        CategoryChipAdapter chipAdapter = new CategoryChipAdapter(categoryLabels, selectedCategory, pos -> {
            selectedCategory = categoryKeys[pos];
            applyFilters();
        });
        recyclerCategories.setAdapter(chipAdapter);
    }

    private void setupRecyclerCommerces() {
        RecyclerView recycler = findViewById(R.id.recyclerCommerces);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommerceAdapter(filteredCommerces, commerceIdsWithStock, commerce ->
                startActivity(new Intent(this, PaniersActivity.class)
                        .putExtra("commerceId",   commerce.id)
                        .putExtra("commerceName", commerce.name)));
        recycler.setAdapter(adapter);
    }

    private void loadData() {
        new Thread(() -> {
            List<Commerce> local = commerceRepo.getAllCommerces();
            Set<String>    stock = new HashSet<>(panierRepo.getCommerceIdsWithStock());

            runOnUiThread(() -> {
                allCommerces.clear();
                allCommerces.addAll(local);
                commerceIdsWithStock = stock;
                applyFilters();
                updateBannerName(); // ✅ après chargement local
            });

            commerceListener = commerceRepo.listenAllCommerces(updatedList -> {
                Set<String> stockUpdate = new HashSet<>(panierRepo.getCommerceIdsWithStock());
                runOnUiThread(() -> {
                    allCommerces.clear();
                    allCommerces.addAll(updatedList);
                    commerceIdsWithStock = stockUpdate;
                    applyFilters();
                    updateBannerName(); // ✅ après sync Firebase
                });
            });
        }).start();
    }

    private void applyFilters() {
        filteredCommerces.clear();

        Set<String> panierCategoryCommerceIds = null;
        if (filterByPanierCategory != null) {
            panierCategoryCommerceIds = new HashSet<>(
                    panierRepo.getCommerceIdsByPanierCategorie(filterByPanierCategory));
        }

        for (Commerce c : allCommerces) {
            boolean matchPanierCategory = panierCategoryCommerceIds == null
                    || panierCategoryCommerceIds.contains(c.id);
            boolean matchCategory = CATEGORY_ALL.equals(selectedCategory)
                    || (c.categorie != null && c.categorie.equalsIgnoreCase(selectedCategory));
            boolean matchSearch = searchQuery.isEmpty()
                    || (c.name != null && c.name.toLowerCase(Locale.getDefault()).contains(searchQuery));
            if (matchPanierCategory && matchCategory && matchSearch)
                filteredCommerces.add(c);
        }
        adapter.notifyDataSetChanged();
        updateFilterBar();
    }

    // ── Notifications client : réservation confirmée ───────────────────────────
    private void startClientNotifications() {
        String userId = new SessionManager(this).getUserId();
        if (userId == null || userId.isEmpty()) return;

        clientReservationQuery = FirebaseDatabase.getInstance()
                .getReference("reservations")
                .orderByChild("userId")
                .equalTo(userId);

        clientReservationListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                handleReservationSnapshot(snapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
                handleReservationSnapshot(snapshot);
            }

            @Override public void onChildRemoved(DataSnapshot snapshot) {}
            @Override public void onChildMoved(DataSnapshot snapshot, String previousChildName) {}
            @Override public void onCancelled(DatabaseError error) {}
        };

        clientReservationQuery.addChildEventListener(clientReservationListener);
    }

    private void stopClientNotifications() {
        if (clientReservationQuery != null && clientReservationListener != null) {
            clientReservationQuery.removeEventListener(clientReservationListener);
        }
        reservationStatusCache.clear();
    }

    private void handleReservationSnapshot(DataSnapshot snapshot) {
        Reservation r = Reservation.fromSnapshot(snapshot);
        if (r == null || r.id == null) return;

        String previousStatus = reservationStatusCache.get(r.id);
        reservationStatusCache.put(r.id, r.statut);

        // Notifier seulement lors du passage à "confirmee"
        if (!"confirmee".equals(previousStatus) && "confirmee".equals(r.statut)) {
            String title = getString(R.string.app_name);
            String message = getString(R.string.notification_reservation_confirmee);
            NotificationHelper.showClientNotification(this, title, message, r.id.hashCode());
        }
    }

    // ── Inner adapter for category chips ──────────────────────────────────────
    private static class CategoryChipAdapter
            extends RecyclerView.Adapter<CategoryChipAdapter.Holder> {

        private final String[] labels;
        private String selectedKey;
        private final OnCategorySelectedListener listener;
        private static final String[] KEYS =
                { "all", "snack", "cafe", "restaurant", "boulangerie", "epicerie", "autre" };

        interface OnCategorySelectedListener { void onSelected(int position); }

        CategoryChipAdapter(String[] labels, String selectedKey,
                            OnCategorySelectedListener listener) {
            this.labels      = labels;
            this.selectedKey = selectedKey;
            this.listener    = listener;
        }

        @NonNull @Override
        public Holder onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
            return new Holder(android.view.LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chip_category, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            holder.chip.setText(labels[position]);
            holder.chip.setSelected(selectedKey.equals(KEYS[position]));
            holder.chip.setOnClickListener(v -> {
                selectedKey = KEYS[position];
                listener.onSelected(position);
                notifyDataSetChanged();
            });
        }

        @Override public int getItemCount() { return labels.length; }

        static class Holder extends RecyclerView.ViewHolder {
            com.google.android.material.chip.Chip chip;
            Holder(View itemView) {
                super(itemView);
                chip = itemView.findViewById(R.id.chipCategory);
            }
        }
    }
}