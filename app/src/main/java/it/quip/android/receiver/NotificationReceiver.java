package it.quip.android.receiver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import it.quip.android.actvitiy.QuipitHomeActivity;
import it.quip.android.model.Notification;

public class NotificationReceiver extends ParsePushBroadcastReceiver {

    private static final String TAG = "NotificationReceiver";
    public static final String intentAction = "SEND_PUSH";

    public static final String intentSend = "com.parse.push.intent.SEND";
    public static final String intentReceive = "com.parse.push.intent.RECEIVE";

    protected Class<? extends Activity> getActivity(Context context, Intent intent) {
        return QuipitHomeActivity.class;
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        // Navigate based on Notification type
        if (intent == null) {
            Log.d(TAG, "Receiver intent null");
        } else {
            // Parse push message and handle accordingly
            processPush(context, intent);
        }
        super.onPushReceive(context, intent);
    }

    private void processPush(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "got action " + action);
        if (action.equals(ACTION_PUSH_RECEIVE)) {
            String channel = intent.getExtras().getString("com.parse.Channel");
            try {
                JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                Log.d(TAG, "got action " + action + " on channel " + channel + " with:");
                // Iterate the parse keys if needed
                Iterator<String> itr = json.keys();
                while (itr.hasNext()) {
                    String key = (String) itr.next();
                    // Extract custom push data
                    if ((key.equals("alert")) || (key.equals("quip_event"))) {
                        Notification notification = Notification.fromJson(json);
                        triggerBroadcastToActivity(context, notification);
                    }
                    Log.d(TAG, "..." + key + " => " + json.getString(key));
                }
            } catch (JSONException ex) {
                Log.d(TAG, "JSON failed!");
            }
        }
    }

    // Handle push notification by sending a local broadcast
    // to which the activity subscribes to
    private void triggerBroadcastToActivity(Context context, Notification notification) {
        Intent pupInt = new Intent(context, QuipitHomeActivity.class);
        pupInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        pupInt.putExtra("new_notification", notification);
        LocalBroadcastManager.getInstance(context).sendBroadcast(pupInt);
    }


}
