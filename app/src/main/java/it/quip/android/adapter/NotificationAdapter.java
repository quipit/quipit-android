package it.quip.android.adapter;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import it.quip.android.R;
import it.quip.android.graphics.CircleTransformation;
import it.quip.android.listener.NotificationHandler;
import it.quip.android.model.Notification;
import it.quip.android.util.FormatUtils;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int UNREAD = 0, READ = 1;
    private List<Notification> mNotifications;
    private NotificationHandler mHandler;
    private Context mContext;

    public NotificationAdapter(List<Notification> notifications, NotificationHandler handler, Context context) {
        mNotifications = notifications;
        mHandler = handler;
        mContext = context;
    }

    public List<Notification> getItems() {
        return mNotifications;
    }

    @Override
    public int getItemCount() {
        return this.mNotifications.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mNotifications.get(position).getViewed()) {
            return READ;
        } else {
            return UNREAD;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        int layout;
        switch (viewType) {
            case UNREAD:
                layout = R.layout.item_notification_unread;
                break;
            default:
                layout = R.layout.item_notification_read;
                break;
        }

        View v1 = inflater.inflate(layout, viewGroup, false);
        viewHolder = new NotificationBaseViewHolder(v1, mHandler, this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        NotificationBaseViewHolder vh;
        switch (viewHolder.getItemViewType()) {
            case READ:
                vh = (NotificationBaseViewHolder) viewHolder;
                break;
            case UNREAD:
                vh = (NotificationBaseViewHolder) viewHolder;
                break;
            default:
                vh = (NotificationBaseViewHolder) viewHolder;
                break;
        }

        configureViewHolder(vh, position);
    }

    public void configureViewHolder(NotificationBaseViewHolder vh, int position) {
        /**
         * Right now, both notifications style mostly the same.  This is left in case
         * during polishing we want to do anything unique.
         *
         * TODO: style the different views
         */
        Notification notification = mNotifications.get(position);
        vh.getHeadLineText().setMovementMethod(LinkMovementMethod.getInstance());
        vh.getHeadLineText().setText(notification.getText().toString());
        if (!notification.getViewed()) {
            vh.getHeadLineText().setTypeface(null, Typeface.BOLD);
        } else {
            vh.getHeadLineText().setTypeface(null, Typeface.NORMAL);
        }
        vh.getTimestampText().setText(FormatUtils.getRelativeTimeAgo(notification.getTimestamp()));
        vh.getNotificationImage().setImageResource(0);

        String imageUrl = notification.getNotificationImageUrl();
        if (null != imageUrl) {
            Picasso.with(mContext)
                    .load(imageUrl)
                    .centerCrop()
                    .transform(new CircleTransformation(4, Color.WHITE))
                    .fit()
                    .into(vh.getNotificationImage());
        } else {
            vh.getNotificationImage().setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.quipit));
        }

    }

    public Context getContext() {
        return mContext;
    }


}
