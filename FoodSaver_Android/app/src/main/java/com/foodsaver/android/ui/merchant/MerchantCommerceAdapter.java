package com.foodsaver.android.ui.merchant;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.foodsaver.android.R;
import com.foodsaver.android.data.Commerce;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class MerchantCommerceAdapter
        extends RecyclerView.Adapter<MerchantCommerceAdapter.ViewHolder> {

    private final List<Commerce> items;
    private final OnPaniersClickListener       onPaniersClick;
    private final OnReservationsClickListener  onReservationsClick;
    private final OnEditClickListener          onEditClick;
    private final OnDeleteClickListener        onDeleteClick;

    public interface OnPaniersClickListener      { void onClick(Commerce c); }
    public interface OnReservationsClickListener { void onClick(Commerce c); }
    public interface OnEditClickListener         { void onClick(Commerce c); }
    public interface OnDeleteClickListener       { void onClick(Commerce c); }

    public MerchantCommerceAdapter(
            List<Commerce> items,
            OnPaniersClickListener onPaniersClick,
            OnReservationsClickListener onReservationsClick,
            OnEditClickListener onEditClick,
            OnDeleteClickListener onDeleteClick) {
        this.items               = items;
        this.onPaniersClick      = onPaniersClick;
        this.onReservationsClick = onReservationsClick;
        this.onEditClick         = onEditClick;
        this.onDeleteClick       = onDeleteClick;
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_merchant_commerce, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        Commerce c = items.get(position);

        // Name
        h.tvName.setText(c.name != null ? c.name : "");

        // Address
        String addr = "";
        if (c.quartier != null && !c.quartier.isEmpty()) addr = "📍 " + c.quartier;
        else if (c.address != null && !c.address.isEmpty()) addr = "📍 " + c.address;
        if (c.city != null && !c.city.isEmpty())
            addr += (addr.isEmpty() ? "📍 " : ", ") + c.city;
        h.tvAddress.setText(addr.isEmpty() ? "📍 Adresse non renseignée" : addr);

        // Categorie badge
        h.tvCategorie.setText(c.categorie != null ? capitalize(c.categorie) : "Autre");

        // Rating
        h.tvRating.setText(String.format("⭐ %.1f", c.rating));

        // Image
        if (c.imageUrl != null && !c.imageUrl.isEmpty()) {
            Glide.with(h.imgCommerce.getContext())
                    .load(c.imageUrl)
                    .placeholder(R.drawable.placeholder_food)
                    .centerCrop()
                    .into(h.imgCommerce);
        } else {
            h.imgCommerce.setImageResource(R.drawable.placeholder_food);
        }

        // Buttons
        h.btnPaniers.setOnClickListener(v -> onPaniersClick.onClick(c));
        h.btnReservations.setOnClickListener(v -> onReservationsClick.onClick(c));
        h.btnEdit.setOnClickListener(v -> onEditClick.onClick(c));
        h.btnDelete.setOnClickListener(v -> onDeleteClick.onClick(c));
    }

    @Override public int getItemCount() { return items.size(); }

    private static String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView   imgCommerce;
        TextView    tvName, tvAddress, tvCategorie, tvRating;

        com.google.android.material.button.MaterialButton btnEdit, btnDelete;
        com.google.android.material.button.MaterialButton btnPaniers, btnReservations;

        ViewHolder(View v) {
            super(v);
            imgCommerce     = v.findViewById(R.id.imgCommerce);
            tvName          = v.findViewById(R.id.tvCommerceName);
            tvAddress       = v.findViewById(R.id.tvAddress);
            tvCategorie     = v.findViewById(R.id.tvCategorie);
            tvRating        = v.findViewById(R.id.tvRating);
            btnEdit         = v.findViewById(R.id.btnEdit);
            btnDelete       = v.findViewById(R.id.btnDelete);
            btnPaniers      = v.findViewById(R.id.btnPaniers);
            btnReservations = v.findViewById(R.id.btnReservations);
        }
    }
}