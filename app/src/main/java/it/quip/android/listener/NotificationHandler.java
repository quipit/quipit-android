package it.quip.android.listener;

import it.quip.android.model.Circle;
import it.quip.android.model.Notification;
import it.quip.android.model.Quip;
import it.quip.android.model.User;

public interface NotificationHandler {
    /**
     * Define the interface of any activity or fragment that interacts with a notification adapter
     */

    void onClickNotificationUser(User selectedUser);
    void onClickNotificationCircle(Circle selectedCircle);
    void onClickNotificationQuip(Quip selectedQuip);
    void onClickNotification(int position, Notification selectedNotification);
}