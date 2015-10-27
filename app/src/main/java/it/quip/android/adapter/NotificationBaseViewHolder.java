package it.quip.android.adapter;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import it.quip.android.QuipitApplication;
import it.quip.android.R;
import it.quip.android.listener.NotificationHandler;
import it.quip.android.model.Notification;
import it.quip.android.view.TagTextSpanner;

public class NotificationBaseViewHolder extends RecyclerView.ViewHolder {

    public TextView headLineText;
    public TextView timestampText;
    public ImageView notificationImage;
    private final NotificationAdapter mAdapter;
    private final NotificationHandler mHandler;
    private final TagTextSpanner mCircleParser;

    public TagTextSpanner getCircleParser() {
        return mCircleParser;
    }

    public NotificationBaseViewHolder(View v, NotificationHandler handler, final NotificationAdapter adapter) {
        super(v);
        // "its the ... u know... view holder pattern...anyone else wanna take a stab at this?"
        headLineText = (TextView) itemView.findViewById(R.id.tv_notification_headline);
        timestampText = (TextView) itemView.findViewById(R.id.tv_notification_timestamp);
        notificationImage = (ImageView) itemView.findViewById(R.id.iv_notification_image);
        mHandler = handler;
        mAdapter = adapter;
        mCircleParser = new TagTextSpanner();
        notificationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Triggers click upwards to the adapter on click
                int position = getLayoutPosition();
                Notification notification = mAdapter.getItems().get(position);
                mHandler.onClickNotification(position, notification);
            }
        });

        timestampText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = mAdapter.getContext();
                Notification notification = new Notification.with(context)
                        .body("Jean Claude Van Como deposited some darkness in @sfsewers thangs")
                        .sender(QuipitApplication.getCurrentUser())
                        .type(Notification.STANDARD_NOTIFICATION)
                        .deliver();

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
