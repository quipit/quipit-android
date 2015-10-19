package it.quip.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import it.quip.android.R;
import it.quip.android.interfaces.NotificationHandler;
import it.quip.android.model.Notification;

public class NotificationBaseViewHolder extends RecyclerView.ViewHolder {

    public TextView headLineText;
    public TextView timestampText;
    public ImageView notificationImage;
    private final NotificationAdapter mAdapter;
    private final NotificationHandler mHandler;

    public NotificationBaseViewHolder(View v, NotificationHandler handler, NotificationAdapter adapter) {
        super(v);
        // "its the ... u know... viewholer pattern...anyone else wanna take a stab at this?"
        headLineText = (TextView) itemView.findViewById(R.id.tvNotificationHeadline);
        timestampText = (TextView) itemView.findViewById(R.id.tvNotificationTimestamp);
        notificationImage = (ImageView) itemView.findViewById(R.id.ivNotificationImage);
        mHandler = handler;
        mAdapter = adapter;

        notificationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Triggers click upwards to the adapter on click
                int position = getLayoutPosition();
                Notification notification = mAdapter.getItems().get(position);
                mHandler.onClickNotification(position, notification);
            }
        });
    }

    public TextView getHeadLineText() {
        return headLineText;
    }

    public void setHeadLineText(TextView headLineText) {
        this.headLineText = headLineText;
    }

    public TextView getTimestampText() {
        return timestampText;
    }

    public void setTimestampText(TextView timestampText) {
        this.timestampText = timestampText;
    }

    public ImageView getNotificationImage() {
        return notificationImage;
    }

    public void setNotificationImage(ImageView notificationImage) {
        this.notificationImage = notificationImage;
    }
}
