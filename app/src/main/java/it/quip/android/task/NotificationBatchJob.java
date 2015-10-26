package it.quip.android.task;

import android.os.AsyncTask;
import android.util.Log;

import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import it.quip.android.model.Circle;
import it.quip.android.model.NotificationBatchJobData;
import it.quip.android.model.Notification;
import it.quip.android.model.User;

public class NotificationBatchJob extends AsyncTask<NotificationBatchJobData, Void, Void> {


    @Override
    protected Void doInBackground(NotificationBatchJobData... params) {
        updateData(params[0].getRecepients(), params[0].getNotification(), params[0].getSenderUid());
        return null;
    }

    private void updateData(List<User> users, Notification notification, String senderUid) {
        for (User user : users) {
            if (user.getObjectId().equals(senderUid) != true) {
                Notification n = new Notification.with(null)
                                    .body(notification.getText())
                                    .type(notification.getType())
                                    .receiver(user)
                                    .circle(notification.getCircle())
                                    .imageUrl(notification.getNotificationImageUrl())
                                    .build();
                try {
                    n.save();
                } catch (ParseException e) {
                    Log.d("BATCHJOB", e.toString());
                }
            }
        }

        notification.send();
    }
}
