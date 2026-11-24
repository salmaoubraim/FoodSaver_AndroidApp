package com.foodsaver.android.ui.merchant;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.foodsaver.android.R;
import com.foodsaver.android.auth.SessionManager;
import com.foodsaver.android.data.Commerce;
import com.foodsaver.android.data.CommerceRepository;
import com.foodsaver.android.data.PanierRepository;
import com.foodsaver.android.data.Reservation;
import com.foodsaver.android.data.ReservationRepository;
import com.foodsaver.android.ui.BaseActivity;
import com.foodsaver.android.ui.MainActivity;
import com.foodsaver.android.ui.NotificationsActivity;
import com.foodsaver.android.ui.NotificationsViewModel;
import com.foodsaver.android.util.CloudinaryManager;
import com.foodsaver.android.util.NotificationHelper;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MerchantHomeActivity extends BaseActivity {

    private final List<Commerce> commerces = new ArrayList<>();
    private MerchantCommerceAdapter adapter;
    private CommerceRepository    commerceRepo;
    private PanierRepository      panierRepo;
    private ReservationRepository reservationRepo;
    private String merchantId;

    private TextView tvStatCommerces, tvStatPaniers, tvStatReservations;
    private View     layoutEmpty;
    private ValueEventListener commerceListener;
    private ChildEventListener reservationListener;

    // ✅ Upload state
    private String   pendingImageUrl  = null;
    private ImageView activeImgPreview;
    private View      activeLayoutHint;
    private ProgressBar activeProgress;
    private MaterialButton activeBtnSave;

    // ✅ Galerie launcher
    private ActivityResultLauncher<String> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_home);

        merchantId      = new SessionManager(this).getUserId();
        commerceRepo    = new CommerceRepository(this);
        panierRepo      = new PanierRepository(this);
        reservationRepo = new ReservationRepository(this);

        // ✅ Init Cloudinary
        CloudinaryManager.init(this);

        // ✅ Register galerie picker
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> { if (uri != null) uploadToCloudinary(uri); }
        );

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            String name = new SessionManager(this).getUserName();
            toolbar.setTitle("Bonjour, " + (name.isEmpty() ? "Commerçant" : name));
            toolbar.setSubtitle("Gérez vos commerces");
            toolbar.setNavigationIcon(null);

            NotificationsViewModel notifVm =
                    new ViewModelProvider(this).get(NotificationsViewModel.class);
            View bell = toolbar.findViewById(R.id.btnNotification);
            TextView badge = toolbar.findViewById(R.id.tvNotificationBadge);
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

        tvStatCommerces    = findViewById(R.id.tvStatCommerces);
        tvStatPaniers      = findViewById(R.id.tvStatPaniers);
        tvStatReservations = findViewById(R.id.tvStatReservations);
        layoutEmpty        = findViewById(R.id.layoutEmpty);

        RecyclerView recycler = findViewById(R.id.recyclerCommerces);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MerchantCommerceAdapter(commerces,
                c -> startActivity(new Intent(this, MerchantPaniersActivity.class)
                        .putExtra("commerceId",   c.id)
                        .putExtra("commerceName", c.name)),
                c -> startActivity(new Intent(this, ReservationsActivity.class)
                        .putExtra("commerceId",   c.id)
                        .putExtra("commerceName", c.name)),
                this::showEditDialog,
                this::showDeleteDialog
        );
        recycler.setAdapter(adapter);

        ExtendedFloatingActionButton fab = findViewById(R.id.fabAddCommerce);
        if (fab != null) fab.setOnClickListener(v -> showCommerceDialog(null));

        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrolled(RecyclerView rv, int dx, int dy) {
                if (fab == null) return;
                if (dy > 10)  fab.shrink();
                if (dy < -10) fab.extend();
            }
        });

        loadCommerces();
        startMerchantNotifications();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        commerceRepo.removeCommercesListener(commerceListener);
        stopMerchantNotifications();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStats();
    }

    private void loadCommerces() {
        commerces.clear();
        commerces.addAll(commerceRepo.getCommercesByMerchant(merchantId));
        updateUI();

        commerceListener = commerceRepo.listenAllCommerces(updatedList -> {
            List<Commerce> mine = new ArrayList<>();
            for (Commerce c : updatedList)
                if (merchantId.equals(c.merchantId)) mine.add(c);
            commerces.clear();
            commerces.addAll(mine);
            runOnUiThread(this::updateUI);
        });
    }

    private void updateUI() {
        adapter.notifyDataSetChanged();
        updateStats();
        if (layoutEmpty != null)
            layoutEmpty.setVisibility(commerces.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void updateStats() {
        if (tvStatCommerces != null)
            tvStatCommerces.setText(String.valueOf(commerces.size()));
        if (tvStatPaniers != null) {
            int total = 0;
            for (Commerce c : commerces)
                total += panierRepo.getPaniersByCommerce(c.id).size();
            tvStatPaniers.setText(String.valueOf(total));
        }
        if (tvStatReservations != null) {
            int totalRes = 0;
            for (Commerce c : commerces) {
                totalRes += reservationRepo.countActiveReservationsByCommerce(c.id);
            }
            tvStatReservations.setText(String.valueOf(totalRes));
        }
    }

    // ── Notifications commerçant : nouvelles réservations ─────────────────────
    private void startMerchantNotifications() {
        if (merchantId == null || merchantId.isEmpty()) return;

        reservationListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                handleReservationForMerchant(snapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
                handleReservationForMerchant(snapshot);
            }

            @Override public void onChildRemoved(DataSnapshot snapshot) {}
            @Override public void onChildMoved(DataSnapshot snapshot, String previousChildName) {}
            @Override public void onCancelled(DatabaseError error) {}
        };

        FirebaseDatabase.getInstance()
                .getReference("reservations")
                .addChildEventListener(reservationListener);
    }

    private void stopMerchantNotifications() {
        if (reservationListener != null) {
            FirebaseDatabase.getInstance()
                    .getReference("reservations")
                    .removeEventListener(reservationListener);
        }
    }

    private void handleReservationForMerchant(DataSnapshot snapshot) {
        Reservation r = Reservation.fromSnapshot(snapshot);
        if (r == null || r.commerceId == null) return;

        // On essaie de retrouver le commerce pour personnaliser le message,
        // mais même si ce n'est pas encore chargé on envoie une notification générique.
        Commerce target = null;
        for (Commerce c : commerces) {
            if (r.commerceId.equals(c.id)) {
                target = c;
                break;
            }
        }

        String title = getString(R.string.app_name);
        String message;

        if ("annulee".equalsIgnoreCase(r.statut)) {
            // Réservation annulée par le client
            String commerceName = (target != null) ? target.name : getString(R.string.commerces);
            message = getString(R.string.notification_reservation_annulee_merchant, commerceName);
        } else {
            // Nouvelle réservation ou mise à jour autre
            String commerceName = (target != null) ? target.name : getString(R.string.commerces);
            message = getString(R.string.notification_nouvelle_reservation, commerceName);
        }
        NotificationHelper.showMerchantNotification(this, title, message, r.id.hashCode());
    }

    private void showEditDialog(Commerce commerce) {
        showCommerceDialog(commerce);
    }

    private void showCommerceDialog(Commerce existing) {
        boolean isEdit   = existing != null;
        pendingImageUrl  = isEdit ? existing.imageUrl : null;

        android.app.Dialog dialog = new android.app.Dialog(this);
        dialog.setContentView(R.layout.dialog_commerce_form);
        if (dialog.getWindow() != null)
            dialog.getWindow().setLayout(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

        EditText      etName      = dialog.findViewById(R.id.etName);
        EditText      etAddress   = dialog.findViewById(R.id.etAddress);
        EditText      etCity      = dialog.findViewById(R.id.etCity);
        Spinner       spCategorie = dialog.findViewById(R.id.spCategorie);
        TextView      tvTitle     = dialog.findViewById(R.id.tvDialogTitle);
        ImageView     imgPreview  = dialog.findViewById(R.id.imgPreview);
        View          layoutHint  = dialog.findViewById(R.id.layoutUploadHint);
        ProgressBar   progress    = dialog.findViewById(R.id.progressUpload);
        MaterialButton btnPick    = dialog.findViewById(R.id.btnPickImage);
        MaterialButton btnSave    = dialog.findViewById(R.id.btnSave);
        MaterialButton btnCancel  = dialog.findViewById(R.id.btnCancel);

        // ✅ Garder références pour l'upload async
        activeImgPreview = imgPreview;
        activeLayoutHint = layoutHint;
        activeProgress   = progress;
        activeBtnSave    = btnSave;

        tvTitle.setText(isEdit ? "Modifier le commerce" : "Nouveau commerce");

        if (isEdit) {
            if (existing.name     != null) etName.setText(existing.name);
            if (existing.address  != null) etAddress.setText(existing.address);
            if (existing.city     != null) etCity.setText(existing.city);
            if (existing.imageUrl != null && !existing.imageUrl.isEmpty())
                showPreview(imgPreview, layoutHint, existing.imageUrl);
        }

        // ✅ Bouton galerie
        if (btnPick != null)
            btnPick.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));

        // Spinner
        String[] categories = {"autre", "restaurant", "boulangerie",
                "epicerie", "patisserie", "supermarche",
                "snack", "cafe"};
        android.widget.ArrayAdapter<String> sa = new android.widget.ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categories);
        sa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategorie.setAdapter(sa);
        if (isEdit && existing.categorie != null)
            for (int i = 0; i < categories.length; i++)
                if (categories[i].equals(existing.categorie)) {
                    spCategorie.setSelection(i); break;
                }

        btnCancel.setOnClickListener(v -> {
            pendingImageUrl = null;
            dialog.dismiss();
        });

        btnSave.setOnClickListener(v -> {
            String name      = etName.getText().toString().trim();
            String address   = etAddress.getText().toString().trim();
            String city      = etCity.getText().toString().trim();
            String categorie = categories[spCategorie.getSelectedItemPosition()];

            if (name.isEmpty()) { etName.setError("Nom requis"); return; }

            // Image obligatoire pour que le commerce apparaisse côté client
            if (!isEdit && (pendingImageUrl == null || pendingImageUrl.isEmpty())) {
                Toast.makeText(this, "Veuillez ajouter une image du commerce.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isEdit) {
                existing.name      = name;
                existing.address   = address.isEmpty()  ? existing.address  : address;
                existing.city      = city.isEmpty()     ? existing.city     : city;
                existing.imageUrl  = pendingImageUrl != null
                        ? pendingImageUrl : existing.imageUrl;
                existing.categorie = categorie;
                commerceRepo.updateCommerce(existing);
                runOnUiThread(this::loadCommerces);
            } else {
                Commerce c  = new Commerce(name, merchantId);
                c.address   = address;
                c.city      = city;
                c.imageUrl  = pendingImageUrl;
                c.categorie = categorie;
                commerceRepo.insertCommerce(c,
                        commerce -> runOnUiThread(this::loadCommerces));
            }
            pendingImageUrl = null;
            dialog.dismiss();
        });

        dialog.show();
    }

    // ✅ Upload Cloudinary
    private void uploadToCloudinary(Uri uri) {
        if (activeProgress   != null) activeProgress.setVisibility(View.VISIBLE);
        if (activeLayoutHint != null) activeLayoutHint.setVisibility(View.GONE);
        if (activeBtnSave    != null) activeBtnSave.setEnabled(false);

        MediaManager.get().upload(uri)
                .option("folder", "foodsaver/commerces")
                .option("upload_preset", "foodsaver_preset")
                .option("resource_type", "image")
                .unsigned("foodsaver_preset")
                .callback(new UploadCallback() {
                    @Override public void onStart(String id) {}
                    @Override public void onProgress(String id, long bytes, long total) {}

                    @Override
                    public void onSuccess(String id, Map resultData) {
                        pendingImageUrl = (String) resultData.get("secure_url");
                        runOnUiThread(() -> {
                            if (activeProgress != null)
                                activeProgress.setVisibility(View.GONE);
                            if (activeBtnSave != null)
                                activeBtnSave.setEnabled(true);
                            if (pendingImageUrl != null)
                                showPreview(activeImgPreview, activeLayoutHint,
                                        pendingImageUrl);
                            Toast.makeText(MerchantHomeActivity.this,
                                    "✓ Image uploadée", Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onError(String id, ErrorInfo error) {
                        runOnUiThread(() -> {
                            Log.e("CLOUDINARY", "Code: " + error.getCode()
                                    + " | " + error.getDescription());
                            if (activeProgress != null)
                                activeProgress.setVisibility(View.GONE);
                            if (activeBtnSave != null)
                                activeBtnSave.setEnabled(true);
                            Toast.makeText(MerchantHomeActivity.this,
                                    "Erreur : " + error.getDescription(),
                                    Toast.LENGTH_LONG).show();
                        });
                    }

                    @Override public void onReschedule(String id, ErrorInfo e) {}
                })
                .dispatch();
    }

    private void showPreview(ImageView img, View hint, String url) {
        if (hint != null) hint.setVisibility(View.GONE);
        if (img  != null) Glide.with(this).load(url).centerCrop().into(img);
    }

    private void showDeleteDialog(Commerce commerce) {
        new AlertDialog.Builder(this)
                .setTitle("Supprimer")
                .setMessage("Supprimer \"" + commerce.name + "\" ?")
                .setPositiveButton("Supprimer", (d, w) -> {
                    commerceRepo.deleteCommerce(commerce.id);
                    commerces.remove(commerce);
                    adapter.notifyDataSetChanged();
                    updateStats();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void confirmExit() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.exit_app_confirm)
                .setPositiveButton(R.string.yes, (d, w) -> finishAffinity())
                .setNegativeButton(R.string.no, null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.merchant_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_stats) {
            startActivity(new Intent(this, StatsActivity.class)); return true;
        }
        if (id == R.id.action_profile) {
            startActivity(new Intent(this, MerchantProfileActivity.class)); return true;
        }
        if (id == R.id.action_logout) {
            new SessionManager(this).logout();
            startActivity(new Intent(this, MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public void onBackPressed() { confirmExit(); }
}