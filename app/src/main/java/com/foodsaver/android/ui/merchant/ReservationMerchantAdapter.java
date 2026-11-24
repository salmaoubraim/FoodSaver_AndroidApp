package com.foodsaver.android.ui.merchant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foodsaver.android.R;

import java.util.List;

public class ReservationMerchantAdapter extends RecyclerView.Adapter<ReservationMerchantAdapter.ViewHolder> {

    private final List<ReservationsActivity.ReservationWithDetails> items;

    public ReservationMerchantAdapter(List<ReservationsActivity.ReservationWithDetails> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation_merchant, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReservationsActivity.ReservationWithDetails item = items.get(position);
        holder.tvPanierTitre.setText(item.panierTitre);
        holder.tvClientInfo.setText("Client: " + item.clientName);
        holder.tvQuantiteDate.setText("Qté: " + item.reservation.quantiteReservee + " - " + item.dateStr);
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPanierTitre, tvClientInfo, tvQuantiteDate;
        ViewHolder(View itemView) {
            super(itemView);
            tvPanierTitre = itemView.findViewById(R.id.tvPanierTitre);
            tvClientInfo = itemView.findViewById(R.id.tvClientInfo);
            tvQuantiteDate = itemView.findViewById(R.id.tvQuantiteDate);
        }
    }
}
