package com.foodsaver.android.ui.client;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foodsaver.android.R;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final String[] labels;
    private final int[]    counts;
    private final OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(int position);
    }

    public CategoryAdapter(String[] labels, int[] counts, OnCategoryClickListener listener) {
        this.labels   = labels;
        this.counts   = counts;
        this.listener = listener;
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(labels[position]);
        holder.tvCount.setText(holder.itemView.getContext()
                .getString(R.string.category_articles_format, counts[position]));
        holder.itemView.setOnClickListener(v -> listener.onCategoryClick(position));
    }

    @Override
    public int getItemCount() { return labels.length; }

    // ✅ NOUVEAU : mettre à jour les compteurs après sync Firebase
    public void updateCounts(int[] newCounts) {
        System.arraycopy(newCounts, 0, this.counts, 0, newCounts.length);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCount;
        ViewHolder(View itemView) {
            super(itemView);
            tvName  = itemView.findViewById(R.id.tvCategoryName);
            tvCount = itemView.findViewById(R.id.tvCount);
        }
    }
}