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
    public static final String INTENT_ACTION = "SEND_PUSH";

    public static final String INTENT_SEND = "com.parse.push.intent.SEND";
    public static final String INTENT_RECEIVE = "com.parse.push.intent.RECEIVE";

    public static final String PARSE_CHANNEL_INTENT_KEY = "com.parse.Channel";
    public static final String PARSE_DATA_INTENT_KEY = "com.parse.Data";

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
        Log.d(TAG, "got action " + action);
        if (action.equals(ACTION_PUSH_RECEIVE)) {
            String channel = intent.getExtras().getString(PARSE_CHANNEL_INTENT_KEY);
            try {
                JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                Log.d(TAG, "got action " + action + " on channel " + channel + " with:");
                Iterator<String> itr = json.keys();
                while (itr.hasNext()) {
                    String key = itr.next();
                    if ((key.equals("alert")) || (key.equals("quip_event"))) {
                        Notification notification = Notification.fromJson(json);
                        triggerBroadcastToActivity(context, notification);
                    }
                    Log.d(TAG, "..." + key + " => " + json.getString(key));
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void triggerBroadcastToActivity(Context context, Notification notification) {
        Intent pupInt = new Intent(context, QuipitHomeActivity.class);
        pupInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        pupInt.putExtra("new_notification", notification);
        LocalBroadcastManager.getInstance(context).sendBroadcast(pupInt);
    }


}
