package it.quip.android.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import it.quip.android.R;
import it.quip.android.adapter.NotificationAdapter;
import it.quip.android.listener.NotificationHandler;
import it.quip.android.model.Circle;
import it.quip.android.task.MarkAndRefreshJobData;
import it.quip.android.model.Notification;
import it.quip.android.model.Quip;
import it.quip.android.model.User;
import it.quip.android.task.MarkCurrentAsReadAndRefreshNotifications;

public class NotificationsFragment extends BaseFragment implements NotificationHandler {

    private View mView;
    private SwipeRefreshLayout mSwipeContainer;
    private NotificationAdapter mNotificationAdapter;
    private RecyclerView mRvContacts;
    private List<Notification> mNotifications;

    @Override
    public CharSequence getTitle() {
        return stringRes(R.string.title_notifications);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_notifications, container, false);
        attachNotificationAdapter(mView);
        attachSwipeContainer(mView);
        return mView;
    }

    @Override
    public void onResult(List<Notification> notifications) {
        setNotifications(notifications);
    }

    public void setNotifications(List<Notification> notifications) {
        mNotifications.clear();
        mNotifications.addAll(notifications);
        mNotificationAdapter.notifyDataSetChanged();
        mSwipeContainer.setRefreshing(false);
    }


    @Override
    public void onException(ParseException e) {
        Log.d("Parse Exception", e.toString());
        // None thangs
    }

    private void attachNotificationAdapter(View view) {
        mRvContacts = (RecyclerView) view.findViewById(R.id.rv_notifications);
        mNotifications = new ArrayList<>();
        mNotificationAdapter = new NotificationAdapter(mNotifications, this, this.getContext());
        Notification.queryNotifcations(this);
        mRvContacts.setAdapter(mNotificationAdapter);
        mRvContacts.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mRvContacts.setHasFixedSize(true);
    }

    private void attachSwipeContainer(View view) {
        mSwipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.srl_notifications);
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
        MarkCurrentAsReadAndRefreshNotifications job = new MarkCurrentAsReadAndRefreshNotifications();
        MarkAndRefreshJobData jobData = new MarkAndRefreshJobData(mNotifications, this);
        job.execute(jobData);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClickNotificationUser(User selectedUser) {
        // TODO: Sprint 2 hookups
        Toast.makeText(this.getContext(), selectedUser.getName(), Toast.LENGTH_LONG).show();
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
        // TODO: remove this is only for testing
        selectedNotification.send();
        //Toast.makeText(this.getContext(), selectedNotification.getText(), Toast.LENGTH_LONG).show();

    }



}
