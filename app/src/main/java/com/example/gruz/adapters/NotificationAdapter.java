package com.example.gruz.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gruz.models.NotificationResponse;
import com.example.gruz.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<NotificationResponse> notifications;
    public NotificationAdapter(List<NotificationResponse> notifications) {
        this.notifications = notifications;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationResponse notif = notifications.get(position);

        holder.tvMessage.setText(notif.getMessage());
        String time = notif.getSentAt();
        if (time != null && time.length() > 16) {
            holder.tvTime.setText(time.substring(11, 16));
        } else {
            holder.tvTime.setText(time);
        }
        String status = notif.getStatus();
        if (status != null) {
            if (status.equalsIgnoreCase("Доставлен")) {
                holder.tvIcon.setText("✅");
                holder.tvIcon.setBackgroundResource(R.drawable.bg_circle_green);
            } else if (status.equalsIgnoreCase("В пути")) {
                holder.tvIcon.setText("🚚");
                holder.tvIcon.setBackgroundResource(R.drawable.bg_circle_blue);
            } else if (status.equalsIgnoreCase("Отменен") || status.equalsIgnoreCase("Задержан")) {
                holder.tvIcon.setText("⚠️");
                holder.tvIcon.setBackgroundResource(R.drawable.bg_circle_orange);
            } else {
                holder.tvIcon.setText("📦");
                holder.tvIcon.setBackgroundResource(R.drawable.bg_circle_blue);
            }
        }
        holder.viewUnread.setVisibility(notif.isRead() ? View.GONE : View.VISIBLE);
    }
    @Override
    public int getItemCount() {
        return notifications.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTime, tvIcon;
        View viewUnread;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvNotifMessage);
            tvTime = itemView.findViewById(R.id.tvNotifTime);
            tvIcon = itemView.findViewById(R.id.tvNotifIcon);
            viewUnread = itemView.findViewById(R.id.viewUnread);
        }
    }
}
