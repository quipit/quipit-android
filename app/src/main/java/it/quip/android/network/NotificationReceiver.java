package it.quip.android.network;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import it.quip.android.QuipitApplication;
import it.quip.android.R;
import it.quip.android.activity.LoginActivity;
import it.quip.android.activity.QuipitHomeActivity;
import it.quip.android.model.Notification;
import it.quip.android.model.Quip;
import it.quip.android.model.User;
import it.quip.android.util.TimeUtils;

public class NotificationReceiver extends ParsePushBroadcastReceiver {

    private static final String TAG = "NotificationReceiver";
    public static final String INTENT_ACTION = "SEND_PUSH";

    public static final String INTENT_SEND = "com.parse.push.intent.SEND";
    public static final String INTENT_RECEIVE = "com.parse.push.intent.RECEIVE";

    public static final String PARSE_CHANNEL_INTENT_KEY = "com.parse.Channel";
    public static final String PARSE_DATA_INTENT_KEY = "com.parse.Data";

    public static final String PARSE_ALERT_KEY = "alert";

    protected Class<? extends Activity> getActivity(Context context, Intent intent) {
        return QuipitHomeActivity.class;
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        if (intent == null) {
            Log.d(TAG, "Receiver intent null");
        } else {

            processPush(context, intent);
        }
        super.onPushReceive(context, intent);
    }

    private void processPush(Context context, Intent intent) {
        String action = intent.getAction();
        // Note: we do not save notifications here anymore unless they are global notifications.
        // Their representation should already be persisted in the parse database, as the actual
        // GCM call is ensured to happen after the models are persisted. This is done in a serial
        // blocking fashion off the UI thread of the client generating the notification.
        if (action.equals(ACTION_PUSH_RECEIVE)) {
            String channel = intent.getExtras().getString(PARSE_CHANNEL_INTENT_KEY);
            try {
                JSONObject json = new JSONObject(intent.getExtras().getString(PARSE_DATA_INTENT_KEY));
                if (json.has(Notification.PUSH_TEXT_BODY_KEY)) {

                    Notification notification = Notification.fromJson(json);
                    triggerBroadcastToActivity(context, notification);
                } else {
                    // This is a global push, just use alert
                    Notification notification = new Notification();
                    notification.setReceiverUid(QuipitApplication.getCurrentUser().getObjectId());
                    notification.setText(json.getString(PARSE_ALERT_KEY));
                    notification.saveInternal();
                    // TODO: use our image app for this?
                    triggerBroadcastToActivity(context, notification);
                }

            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void triggerBroadcastToActivity(Context context, Notification notification) {
        ActivityManager am = QuipitApplication.activityManager();
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        Intent intent = new Intent(context, LoginActivity.class);
        int requestID = (int) System.currentTimeMillis(); //unique requestID to differentiate between various notification with same NotifId
        int flags = PendingIntent.FLAG_CANCEL_CURRENT; // cancel old intent and create new one
        PendingIntent pIntent = PendingIntent.getActivity(context, requestID, intent, flags);
        if(componentInfo.getPackageName().equalsIgnoreCase("it.quip.android")) {

            Intent pupInt = new Intent(Notification.NOTIFICATION_RECEIVED_ACTION);
            pupInt.setFlags(Notification.FLAG_NOTIFICATION_RECEIVED);
            pupInt.setAction(Notification.NOTIFICATION_RECEIVED_ACTION);
            pupInt.putExtra(Notification.MARSHALL_INTENT_KEY, notification);
            LocalBroadcastManager.getInstance(context).sendBroadcast(pupInt);
        }

    }

}
