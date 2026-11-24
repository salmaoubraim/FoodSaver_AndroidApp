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
import com.foodsaver.android.data.Panier;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class PanierClientAdapter extends RecyclerView.Adapter<PanierClientAdapter.ViewHolder> {

    private final List<Panier> items;
    private final OnPanierClickListener onPanierClickListener;
    private final OnReserveClickListener onReserveClickListener;

    public interface OnPanierClickListener {
        void onPanierClick(Panier panier);
    }

    public interface OnReserveClickListener {
        void onReserve(Panier panier);
    }

    /** Use both: onPanierClick for opening detail, onReserveClick for quick reserve (can be null). */
    public PanierClientAdapter(List<Panier> items, OnPanierClickListener onPanierClick, OnReserveClickListener onReserveClick) {
        this.items = items;
        this.onPanierClickListener = onPanierClick;
        this.onReserveClickListener = onReserveClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_panier_client, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Panier p = items.get(position);
        holder.tvTitre.setText(p.titre);
        holder.tvPrix.setText(holder.itemView.getContext().getString(R.string.price) + ": " + String.format("%.0f", p.prix) + " MAD");
        holder.tvQuantite.setText(holder.itemView.getContext().getString(R.string.quantity) + ": " + p.quantite);
        holder.btnReserve.setEnabled(p.quantite > 0);
        holder.itemView.setOnClickListener(v -> { if (onPanierClickListener != null) onPanierClickListener.onPanierClick(p); });
        holder.btnReserve.setOnClickListener(v -> { if (onReserveClickListener != null) onReserveClickListener.onReserve(p); });

        // ✅ Charger image depuis DB
        if (p.imageUrl != null && !p.imageUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(p.imageUrl)
                    .placeholder(R.drawable.placeholder_food)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(holder.imgPanier);
        } else {
            holder.imgPanier.setImageResource(R.drawable.placeholder_food);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPanier;
        TextView tvTitre, tvPrix, tvQuantite;
        MaterialButton btnReserve;

        ViewHolder(View itemView) {
            super(itemView);
            imgPanier = itemView.findViewById(R.id.imgPanier);
            tvTitre = itemView.findViewById(R.id.tvPanierTitre);
            tvPrix = itemView.findViewById(R.id.tvPanierPrix);
            tvQuantite = itemView.findViewById(R.id.tvPanierQuantite);
            btnReserve = itemView.findViewById(R.id.btnReserve);
        }
    }
}
