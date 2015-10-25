package it.quip.android.task;

import android.os.AsyncTask;

import com.parse.ParseException;

import java.util.List;

import it.quip.android.listener.NotificationHandler;
import it.quip.android.model.MarkAndRefreshJobData;
import it.quip.android.model.Notification;

public class MarkCurrentAsReadAndRefreshNotifications extends AsyncTask<MarkAndRefreshJobData, Void, Void> {


    @Override
    protected Void doInBackground(MarkAndRefreshJobData... params) {
        updateData(params[0].getNotifications(), params[0].getHandler());
        return null;
    }

    private void updateData(List<Notification> notifications, NotificationHandler handler) {
        for (Notification n : notifications) {
            n.setViewed(false);
            try {
                n.save();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Notification.queryNotifcations(handler);
    }

}