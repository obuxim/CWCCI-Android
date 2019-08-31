package com.octoriz.cwcci.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.octoriz.cwcci.R;
import com.octoriz.cwcci.model.Notification;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Context context;
    private List<Notification> notificationList;

    public NotificationAdapter(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.n_s_i_c, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notificationList.get(position);
        holder.mNotificationTextView.setText(notification.getData());
        holder.mNameTextView.setText(notification.getName());

        String receivedAt = notification.getReceivedAt();
        String receivedParts[] = receivedAt.split(" at ");

        holder.mReceivedDateTextView.setText(receivedParts[0]);
        holder.mReceivedTimeTextView.setText(receivedParts[1]);

        String seenAt = notification.getSeenAt();
        String seenParts[] = seenAt.split(" at ");

        holder.mSeenDateTextView.setText(seenParts[0]);
        holder.mSeenTimeTextView.setText(seenParts[1]);

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {

        public TextView mNotificationTextView;
        public TextView mReceivedTimeTextView;
        public TextView mReceivedDateTextView;
        public TextView mSeenTimeTextView;
        public TextView mSeenDateTextView;
        public TextView mNameTextView;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            mNotificationTextView = itemView.findViewById(R.id.notification_text);
            mReceivedTimeTextView = itemView.findViewById(R.id.received_time);
            mReceivedDateTextView = itemView.findViewById(R.id.received_date);
            mSeenTimeTextView = itemView.findViewById(R.id.seen_time);
            mSeenDateTextView = itemView.findViewById(R.id.seen_date);
            mNameTextView = itemView.findViewById(R.id.member_name);
        }
    }
}
