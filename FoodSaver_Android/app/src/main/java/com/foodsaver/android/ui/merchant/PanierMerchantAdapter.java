package com.foodsaver.android.ui.merchant;

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

public class PanierMerchantAdapter extends RecyclerView.Adapter<PanierMerchantAdapter.ViewHolder> {

    private final List<Panier> items;
    private final OnEditClickListener onEdit;
    private final OnDeleteClickListener onDelete;

    public interface OnEditClickListener { void onEdit(Panier p); }
    public interface OnDeleteClickListener { void onDelete(Panier p); }

    public PanierMerchantAdapter(List<Panier> items, OnEditClickListener onEdit, OnDeleteClickListener onDelete) {
        this.items = items;
        this.onEdit = onEdit;
        this.onDelete = onDelete;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_panier_merchant, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Panier p = items.get(position);
        holder.tvTitre.setText(p.titre);
        holder.tvPrix.setText(holder.itemView.getContext().getString(R.string.price) + ": " + p.prix + " €");
        holder.tvQuantite.setText(holder.itemView.getContext().getString(R.string.quantity) + ": " + p.quantite);
        holder.btnEdit.setOnClickListener(v -> onEdit.onEdit(p));
        holder.btnDelete.setOnClickListener(v -> onDelete.onDelete(p));

        // Image du panier : imageUrl (fichier local ou URL) ou drawable par catégorie
        if (p.imageUrl != null && !p.imageUrl.isEmpty()) {
            Object loadTarget = p.imageUrl.startsWith("/") ? new java.io.File(p.imageUrl) : p.imageUrl;
            Glide.with(holder.itemView.getContext())
                .load(loadTarget)
                .placeholder(R.drawable.placeholder_food)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(holder.imgPanier);
        } else {
            String cat = (p.categorie != null && !p.categorie.isEmpty()) ? p.categorie.toLowerCase().replace("-", "_") : "autre";
            int resId = holder.itemView.getContext().getResources().getIdentifier("panier_" + cat, "drawable", holder.itemView.getContext().getPackageName());
            if (resId != 0) {
                holder.imgPanier.setImageResource(resId);
            } else {
                holder.imgPanier.setImageResource(R.drawable.placeholder_food);
            }
        }
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPanier;
        TextView tvTitre, tvPrix, tvQuantite;
        MaterialButton btnEdit, btnDelete;
        ViewHolder(View itemView) {
            super(itemView);
            imgPanier = itemView.findViewById(R.id.imgPanier);
            tvTitre = itemView.findViewById(R.id.tvTitre);
            tvPrix = itemView.findViewById(R.id.tvPrix);
            tvQuantite = itemView.findViewById(R.id.tvQuantite);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
