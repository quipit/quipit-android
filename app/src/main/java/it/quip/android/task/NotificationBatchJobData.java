package it.quip.android.task;

import java.util.ArrayList;
import java.util.List;

import it.quip.android.model.Notification;
import it.quip.android.model.User;

public class NotificationBatchJobData {

    private List<User> mRecepients;
    private Notification mNotification;
    private String mSenderUid;

    public NotificationBatchJobData(List<User> receipients, String senderUid, Notification notification) {
        this.mRecepients = receipients;
        this.mSenderUid = senderUid;
        this.mNotification = notification;
    }

    public List<User> getRecepients() {
        return mRecepients;
    }

    public String getSenderUid() {
        return mSenderUid;
    }

    public Notification getNotification() {
        return mNotification;
    }

}
