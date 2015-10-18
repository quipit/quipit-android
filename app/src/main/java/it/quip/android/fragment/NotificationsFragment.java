package it.quip.android.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import it.quip.android.QuipitApplication;
import it.quip.android.R;
import it.quip.android.adapter.NotificationAdapter;
import it.quip.android.model.Notification;

public class NotificationsFragment extends BaseFragment {

    private View mView;
    private SwipeRefreshLayout swipeContainer;
    private NotificationAdapter notificationAdapter;
    private RecyclerView rvContacts;

    @Override
    public CharSequence getTitle() {
        return stringRes(R.string.NOTIFICATION_FRAGMENT_TITLE);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_notifications, container, false);
        attachNotificationAdapter(mView);
        attachSwipeContainer(mView);
        return mView;
    }

    private void attachNotificationAdapter(View view) {
        rvContacts = (RecyclerView) view.findViewById(R.id.rvNotifications);
        // Create adapter passing in the sample user data
        notificationAdapter= new NotificationAdapter(Notification.getNotifcations(0));
        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(notificationAdapter);
        // Set layout manager to position the items
        rvContacts.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rvContacts.setHasFixedSize(true);
    }

    private void attachSwipeContainer(View view) {
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.scNotificationSwipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshNotifications();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
                // TODO: theme this
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void refreshNotifications() {
        // TODO: implement

        swipeContainer.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
