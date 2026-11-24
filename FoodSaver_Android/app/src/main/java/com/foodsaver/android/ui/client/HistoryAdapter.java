package com.foodsaver.android.ui.client;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foodsaver.android.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final List<HistoryActivity.ReservationWithDetails> items;
    private final OnCancelClickListener listener;

    public interface OnCancelClickListener {
        void onCancel(HistoryActivity.ReservationWithDetails item);
    }

    public HistoryAdapter(List<HistoryActivity.ReservationWithDetails> items, OnCancelClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryActivity.ReservationWithDetails item = items.get(position);
        holder.tvInfo.setText(item.panierTitre + " x " + item.reservation.quantiteReservee + " - " + item.commerceName);
        holder.tvDate.setText(item.dateStr);
        holder.btnCancel.setOnClickListener(v -> listener.onCancel(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvInfo, tvDate;
        Button btnCancel;

        ViewHolder(View itemView) {
            super(itemView);
            tvInfo = itemView.findViewById(R.id.tvReservationInfo);
            tvDate = itemView.findViewById(R.id.tvReservationDate);
            btnCancel = itemView.findViewById(R.id.btnCancel);
        }
    }
}
