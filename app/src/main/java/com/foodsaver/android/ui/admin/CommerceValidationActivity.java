package com.foodsaver.android.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodsaver.android.R;
import com.foodsaver.android.data.Commerce;
import com.foodsaver.android.data.CommerceRepository;
import com.foodsaver.android.data.UserRepository;
import com.foodsaver.android.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class CommerceValidationActivity extends BaseActivity {

    private ProgressBar progressBar;
    private TextView    tvEmpty;
    private CommerceAdapter adapter;
    private final List<Commerce> commerces = new ArrayList<>();
    private CommerceRepository commerceRepo;
    private UserRepository     userRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commerce_validation);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.commerces_validation);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        RecyclerView recyclerView = findViewById(R.id.recyclerCommerces);
        progressBar  = findViewById(R.id.progressBar);
        tvEmpty      = findViewById(R.id.tvEmpty);
        commerceRepo = new CommerceRepository(this);
        userRepo     = new UserRepository(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommerceAdapter();
        recyclerView.setAdapter(adapter);

        loadCommerces();
    }

    private void loadCommerces() {
        progressBar.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);
        commerces.clear();
        commerces.addAll(commerceRepo.getAllCommerces());
        adapter.notifyDataSetChanged();
        tvEmpty.setVisibility(commerces.isEmpty() ? View.VISIBLE : View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    class CommerceAdapter extends RecyclerView.Adapter<CommerceAdapter.ViewHolder> {

        @NonNull @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_commerce_admin, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Commerce commerce = commerces.get(position);
            holder.tvName.setText(commerce.name);
            holder.tvMerchantId.setText("Commerçant ID: " + commerce.merchantId);

            // Nom du commerçant (async)
            userRepo.getById(commerce.merchantId, user -> runOnUiThread(() ->
                    holder.tvStatus.setText(user != null && user.name != null ? user.name : "?")));

            holder.tvStatus.setText("...");
            holder.tvStatus.setTextColor(
                    getResources().getColor(R.color.primary, null));

            if (holder.btnDelete != null) {
                holder.btnDelete.setOnClickListener(v -> {
                    new androidx.appcompat.app.AlertDialog.Builder(CommerceValidationActivity.this)
                            .setTitle(R.string.delete)
                            .setMessage(getString(R.string.confirm_delete_commerce, commerce.name))
                            .setPositiveButton(R.string.delete, (d, w) -> {
                                commerceRepo.deleteCommerceCascade(commerce.id);
                                commerces.remove(commerce);
                                notifyDataSetChanged();
                                tvEmpty.setVisibility(commerces.isEmpty() ? View.VISIBLE : View.GONE);
                            })
                            .setNegativeButton(android.R.string.cancel, null)
                            .show();
                });
            }
        }

        @Override public int getItemCount() { return commerces.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvMerchantId, tvStatus, btnDelete;
            ViewHolder(View v) {
                super(v);
                tvName       = v.findViewById(R.id.tvCommerceName);
                tvMerchantId = v.findViewById(R.id.tvMerchantId);
                tvStatus     = v.findViewById(R.id.tvStatus);
                btnDelete    = v.findViewById(R.id.btnDelete);
            }
        }
    }
}