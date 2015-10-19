package it.quip.android.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import java.util.List;

import it.quip.android.R;
import it.quip.android.model.Notification;

/**
 * Created by danbuscaglia on 10/18/15.
 */
public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int UNREAD = 0, READ = 1;  // State

    // Store a member variable for the notifications
    private List<Notification> mNotifications;
    // Handler to call back when events happen
    private NotificationHandler mHandler;
    // Context at which to act upon
    private Context mContext;

    // Pass in the contact array into the constructor
    public NotificationAdapter(List<Notification> notifications, NotificationHandler handler, Context context) {
        mNotifications = notifications;
        mHandler = handler;
        mContext = context;

    }

    public List<Notification> getItems() {
        return mNotifications;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.mNotifications.size();
    }

    //Returns the view type of the item at position for the purposes of view recycling.
    @Override
    public int getItemViewType(int position) {
        if (mNotifications.get(position).isViewed()) {
            return READ;
        } else {
            return UNREAD;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case READ:
                View v1 = inflater.inflate(R.layout.layout_navigation_card_read, viewGroup, false);
                viewHolder = new NotificationBaseViewHolder(v1, mHandler, this);
                break;
            case UNREAD:
                View v2 = inflater.inflate(R.layout.layout_navigation_card_unread, viewGroup, false);
                viewHolder = new NotificationBaseViewHolder(v2, mHandler, this);
                break;
            default:
                View v3 = inflater.inflate(R.layout.layout_navigation_card_unread, viewGroup, false);
                viewHolder = new NotificationBaseViewHolder(v3, mHandler, this);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case READ:
                NotificationBaseViewHolder vh1 = (NotificationBaseViewHolder) viewHolder;
                configureViewHolder(vh1, position);
                break;
            case UNREAD:
                NotificationBaseViewHolder vh2 = (NotificationBaseViewHolder) viewHolder;
                configureViewHolder(vh2, position);
                break;
            default:
                NotificationBaseViewHolder vh3 = (NotificationBaseViewHolder) viewHolder;
                configureViewHolder(vh3, position);
                break;
        }
    }

    public void configureViewHolder(NotificationBaseViewHolder vh, int position) {
        /**
         * Right now, both notifications style mostly the same.  This is left in case
         * during polishing we want to do anything unique.
         */
        Notification notification = mNotifications.get(position);
        vh.getHeadLineText().setText(Html.fromHtml(notification.getText()));
        vh.getTimestampText().setText(notification.getTimestampString());

        vh.getNotificationImage().setImageResource(0);
        // Lookup view for data population
        Picasso.with(mContext)
                .load(notification.getNotificationImageUrl())
                .fit()
                .into(vh.getNotificationImage());

    }


}
