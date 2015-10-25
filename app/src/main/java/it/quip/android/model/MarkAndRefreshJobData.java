package it.quip.android.model;

import java.util.List;

import it.quip.android.listener.NotificationHandler;

public class MarkAndRefreshJobData {
    private NotificationHandler mHandler;
    private List<Notification> mNotifications;

    public MarkAndRefreshJobData(List<Notification> notifications, NotificationHandler handler) {
        this.mHandler = handler;
        this.mNotifications = notifications;
    }

    public NotificationHandler getHandler() {
        return mHandler;
    }

    public void setHandler(NotificationHandler mHandler) {
        this.mHandler = mHandler;
    }

    public List<Notification> getNotifications() {
        return mNotifications;
    }

    public void setNotifications(List<Notification> mNotifications) {
        this.mNotifications = mNotifications;
    }
}

