package com.aphealthcare.assp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aphealthcare.assp.R;
import com.aphealthcare.assp.helpers.Notification;

import java.util.List;

public class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.NotifViewHolder> {

    private List<Notification> notificationList;

    public class NotifViewHolder extends RecyclerView.ViewHolder {
        public TextView author, title, text, publishedDate;

        public NotifViewHolder(View view){
            super(view);
            author = view.findViewById(R.id.author);
            title = view.findViewById(R.id.title);
            text = view.findViewById(R.id.text);
            publishedDate = view.findViewById(R.id.publishdate);
        }
    }

    public NotifAdapter(List<Notification> notificationList){
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotifViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notif_list_row, parent, false);

        return new NotifViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifViewHolder holder, int position) {
        Notification notification = notificationList.get(position);
        holder.author.setText(notification.getAuthor());
        holder.title.setText(notification.getTitle());
        holder.text.setText(notification.snippet());
        holder.publishedDate.setText(notification.getPublished_date());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}
