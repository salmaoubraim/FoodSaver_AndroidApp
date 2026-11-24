package com.foodsaver.android.ui.client;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.foodsaver.android.R;
import com.foodsaver.android.data.Commerce;
import com.google.android.material.chip.Chip;

import java.util.List;
import java.util.Set;

public class CommerceAdapter extends RecyclerView.Adapter<CommerceAdapter.ViewHolder> {

    private final List<Commerce> items;
    // ✅ Set<String> au lieu de Set<Long>
    private final Set<String> commerceIdsWithStock;
    private final OnCommerceClickListener listener;

    public interface OnCommerceClickListener {
        void onCommerceClick(Commerce commerce);
    }

    public CommerceAdapter(List<Commerce> items, Set<String> commerceIdsWithStock,
                           OnCommerceClickListener listener) {
        this.items                = items;
        this.commerceIdsWithStock = commerceIdsWithStock != null
            ? commerceIdsWithStock : new java.util.HashSet<>();
        this.listener = listener;
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_commerce, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Commerce c = items.get(position);
        holder.tvName.setText(c.name);
        holder.chipCategory.setText(formatCategoryLabel(c.categorie));
        holder.tvAddress.setText(formatAddress(c));
        holder.tvRating.setText(String.format("%.1f", c.rating));

        // ✅ c.id est String — contains() fonctionne directement
        boolean available = commerceIdsWithStock.contains(c.id);
        holder.tvBadge.setText(available
            ? holder.itemView.getContext().getString(R.string.available)
            : holder.itemView.getContext().getString(R.string.sold_out));
        holder.tvBadge.setBackgroundResource(available
            ? R.drawable.bg_badge_available
            : R.drawable.bg_badge_soldout);

        holder.itemView.setOnClickListener(v -> listener.onCommerceClick(c));
        holder.btnFavorite.setOnClickListener(v -> { /* TODO favoris */ });

        if (c.imageUrl != null && !c.imageUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                .load(c.imageUrl)
                .placeholder(R.drawable.placeholder_commerce)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imgCommerce);
        } else {
            holder.imgCommerce.setImageDrawable(
                holder.itemView.getContext().getDrawable(R.drawable.placeholder_commerce));
        }
    }

    private static String formatCategoryLabel(String categorie) {
        if (categorie == null) return "Autre";
        switch (categorie.toLowerCase()) {
            case "snack":       return "🍕 Snack";
            case "cafe":        return "☕ Café";
            case "restaurant":  return "🍽 Restaurant";
            case "boulangerie": return "🥐 Boulangerie";
            case "epicerie":    return "🛒 Épicerie";
            default:            return "• " + categorie;
        }
    }

    private static String formatAddress(Commerce c) {
        if (c.quartier != null && c.city != null) return c.quartier + ", " + c.city;
        if (c.address  != null) return c.address;
        if (c.city     != null) return c.city;
        return "";
    }

    @Override public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCommerce, btnFavorite;
        TextView  tvName, tvAddress, tvRating, tvBadge;
        Chip      chipCategory;

        ViewHolder(View itemView) {
            super(itemView);
            imgCommerce  = itemView.findViewById(R.id.imgCommerce);
            tvName       = itemView.findViewById(R.id.tvCommerceName);
            chipCategory = itemView.findViewById(R.id.chipCategory);
            tvAddress    = itemView.findViewById(R.id.tvAddress);
            tvRating     = itemView.findViewById(R.id.tvRating);
            tvBadge      = itemView.findViewById(R.id.tvBadge);
            btnFavorite  = itemView.findViewById(R.id.btnFavorite);
        }
    }
}
