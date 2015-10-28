package it.quip.android.network;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import it.quip.android.QuipitApplication;
import it.quip.android.actvitiy.QuipitHomeActivity;
import it.quip.android.model.Notification;
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
                    if ((notification != null) && (!notification.getSenderUid().equals(QuipitApplication.getCurrentUser().getObjectId()))) {
                        triggerBroadcastToActivity(context, notification);
                    }
                } else {
                    // This is a global push, just use alert
                    Notification notification = new Notification();
                    notification.setTimestamp(TimeUtils.currentTimestampInS());
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
        Intent pupInt = new Intent(context, QuipitHomeActivity.class);
        pupInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        pupInt.putExtra("new_notification", notification);
        LocalBroadcastManager.getInstance(context).sendBroadcast(pupInt);
    }


}
