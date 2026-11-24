package com.foodsaver.android.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.foodsaver.android.R;
import com.foodsaver.android.data.AppNotification;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class NotificationsAdapter extends ListAdapter<AppNotification, NotificationsAdapter.Holder> {

    interface OnNotificationClick {
        void onClick(AppNotification n);
    }

    private final OnNotificationClick listener;
    private final SimpleDateFormat sdf =
            new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    NotificationsAdapter(OnNotificationClick listener) {
        super(DIFF);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<AppNotification> DIFF =
            new DiffUtil.ItemCallback<AppNotification>() {
                @Override
                public boolean areItemsTheSame(@NonNull AppNotification oldItem,
                                               @NonNull AppNotification newItem) {
                    return oldItem.id == newItem.id;
                }

                @Override
                public boolean areContentsTheSame(@NonNull AppNotification oldItem,
                                                  @NonNull AppNotification newItem) {
                    return oldItem.isRead == newItem.isRead
                            && equals(oldItem.title, newItem.title)
                            && equals(oldItem.message, newItem.message)
                            && oldItem.dateTime == newItem.dateTime;
                }

                private boolean equals(String a, String b) {
                    return a == null ? b == null : a.equals(b);
                }
            };

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        AppNotification n = getItem(position);
        holder.tvTitle.setText(n.title);
        holder.tvMessage.setText(n.message);
        holder.tvDate.setText(sdf.format(new Date(n.dateTime)));

        holder.itemView.setAlpha(n.isRead ? 0.6f : 1f);
        holder.itemView.setOnClickListener(v -> listener.onClick(n));
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvMessage, tvDate;

        Holder(View itemView) {
            super(itemView);
            tvTitle   = itemView.findViewById(R.id.tvTitle);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvDate    = itemView.findViewById(R.id.tvDate);
        }
    }
}

