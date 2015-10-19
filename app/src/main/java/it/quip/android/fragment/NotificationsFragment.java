package it.quip.android.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import it.quip.android.R;
import it.quip.android.adapter.NotificationAdapter;
import it.quip.android.interfaces.NotificationHandler;
import it.quip.android.model.Circle;
import it.quip.android.model.Notification;
import it.quip.android.model.Quip;
import it.quip.android.model.User;

public class NotificationsFragment extends BaseFragment implements NotificationHandler {

    private View mView;
    private SwipeRefreshLayout mSwipeContainer;
    private NotificationAdapter mNotificationAdapter;
    private RecyclerView mRvContacts;

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
        mRvContacts = (RecyclerView) view.findViewById(R.id.rvNotifications);
        mNotificationAdapter = new NotificationAdapter(Notification.getNotifcations(0), this, this.getContext());
        mRvContacts.setAdapter(mNotificationAdapter);
        mRvContacts.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mRvContacts.setHasFixedSize(true);
    }

    private void attachSwipeContainer(View view) {
        mSwipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.scNotificationSwipeContainer);
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshNotifications();
            }
        });
        mSwipeContainer.setColorSchemeResources(
                // TODO: theme this
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void refreshNotifications() {
        // TODO: implement
        mSwipeContainer.setRefreshing(false);
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
