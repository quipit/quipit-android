package it.quip.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import it.quip.android.R;
import it.quip.android.model.Notification;

/**
 * Created by danbuscaglia on 10/18/15.
 */
public class NotificationAdapter extends
        RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView headLineText;
        public TextView timestampText;
        public ImageView notificationImage;

        public ViewHolder(View itemView) {
            super(itemView);
            // "its the ... u know... viewholer pattern...anyone else wanna take a stab at this?"
            headLineText = (TextView) itemView.findViewById(R.id.tvNotificationHeadline);
            timestampText = (TextView) itemView.findViewById(R.id.tvNotificationTimestamp);
            notificationImage = (ImageView) itemView.findViewById(R.id.ivNotificationImage);
        }

    }

    // Store a member variable for the notifications
    private List<Notification> mNotifications;

    // Pass in the contact array into the constructor
    public NotificationAdapter(List<Notification> notifications) {
        mNotifications = notifications;

    }
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the notification layout
        View notificationView = inflater.inflate(R.layout.layout_navigation_card_unread,
                                                 parent,
                                                 false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(notificationView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(NotificationAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Notification notification = mNotifications.get(position);

        TextView headline = viewHolder.headLineText;
        TextView timestamp = viewHolder.timestampText;
        headline.setText(Html.fromHtml(notification.getText()));
        //timestamp.setText(notification.getTimestampString());

        if (notification.isViewed()) {
            // TODO: multi view
        }
        else {
            // TODO: multi view
        }



    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return mNotifications.size();
    }
}
