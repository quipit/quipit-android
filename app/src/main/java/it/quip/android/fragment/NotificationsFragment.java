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
import android.widget.Toast;

import it.quip.android.QuipitApplication;
import it.quip.android.R;
import it.quip.android.adapter.NotificationAdapter;
import it.quip.android.adapter.NotificationHandler;
import it.quip.android.model.Circle;
import it.quip.android.model.Notification;
import it.quip.android.model.Quip;
import it.quip.android.model.User;

public class NotificationsFragment extends BaseFragment implements NotificationHandler {

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
        notificationAdapter= new NotificationAdapter(Notification.getNotifcations(0), this, this.getContext());
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

    @Override
    public void onClickNotificationUser(User selectedUser) {
        // TODO: Sprint 2 hookups
        Toast.makeText(this.getContext(), selectedUser.getFirstName(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClickNotificationCircle(Circle selectedCircle) {
        // TODO: sprint 2 hookups
        Toast.makeText(this.getContext(), selectedCircle.getName(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClickNotificationQuip(Quip selectedQuip) {
        // TODO: sprint 2 hookups
        Toast.makeText(this.getContext(), selectedQuip.getText(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClickNotification(int position, Notification selectedNotification) {
        // TODO: sprint 2 hookups
        Toast.makeText(this.getContext(), selectedNotification.getText(), Toast.LENGTH_LONG).show();

    }
}
