package it.quip.android.model;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SendCallback;

import org.json.JSONException;
import org.json.JSONObject;

import it.quip.android.listener.NotificationHandler;
import it.quip.android.listener.ParseModelQueryHandler;
import it.quip.android.util.TimeUtils;


@ParseClassName("Notification")
public class Notification extends ParseObject implements Parcelable {

    public static final int STANDARD_NOTIFICATION = 0;
    public static final String PUSH_TEXT_BODY_KEY = "text_body";
    public static final String PUSH_SENDER_ID = "sender_uid";
    public static final String PUSH_IMAGE_URL_KEY = "image_url";
    public static final String PUSH_RECEIVER_ID = "receiver_uid";
    public static final String PUSH_TYPE_KEY = "notification_type";
    public static final String PUSH_TIMESTAMP_KEY = "timestamp";
    public static final String PUSH_VIEWED_KEY = "viewed";
    public static final String PUSH_CHANNEL_KEY = "channel";

    private String mSenderUid;
    private String mReceiverUid;
    private String mText;
    private String mNotificationImageUrl;
    private String mChannel;

    private long mTimestamp;
    private int mType;  // TODO: define enum
    private boolean mViewed;

    public String getChannel() {
        mChannel = this.getString(PUSH_CHANNEL_KEY);
        return mChannel;
    }

    public void setChannel(String channel) {
        this.safePutKey(PUSH_CHANNEL_KEY, channel);
        this.mChannel = channel;
    }

    public boolean viewed() {
        mViewed = this.getBoolean(PUSH_VIEWED_KEY);
        return mViewed;

    }

    public String getText() {
        mText = this.getString(PUSH_TEXT_BODY_KEY);
        return mText;
    }

    public String getNotificationImageUrl() {
        mNotificationImageUrl = this.getString(PUSH_IMAGE_URL_KEY);
        return mNotificationImageUrl;
    }

    public int getType() {
        mType = this.getInt(PUSH_TYPE_KEY);
        return mType;
    }

    public String getReceiverUid() {
        mReceiverUid = this.getString(PUSH_RECEIVER_ID);
        return mReceiverUid;
    }

    public String getSenderUid() {
        mSenderUid = this.getString(PUSH_SENDER_ID);
        return mSenderUid;
    }

    public String getTimestampString() {
        // TODO: implement this based on unix mTimestamp
        return "2h";
    }

    // SETTERS

    public void setViewed(boolean viewed) {
        this.mViewed = viewed;
        this.safePutKey(PUSH_VIEWED_KEY, viewed);
    }

    public void setSenderUid(String uid) {
        this.mSenderUid = uid;
        this.safePutKey(PUSH_SENDER_ID, uid);
    }

    public void setReceiverUid(String uid) {
        this.mReceiverUid = uid;
        this.safePutKey(PUSH_RECEIVER_ID, uid);
    }

    public void setText(String textString) {
        this.mText = textString;
        this.safePutKey(PUSH_TEXT_BODY_KEY, textString);
    }

    public void setNotificationImageUrl(String imageUrl) {
        this.mNotificationImageUrl = imageUrl;
        this.safePutKey(PUSH_IMAGE_URL_KEY, imageUrl);
    }

    public void setTimestamp(long timestamp) {
        this.mTimestamp = timestamp;
        this.safePutKey(PUSH_TIMESTAMP_KEY, timestamp);
    }

    public void setType(int type) {
        this.mType = type;
        this.safePutKey(PUSH_TYPE_KEY, type);
    }

    public Notification() {

    }

    public static class with {
        private final Context context;
        private int notificationType;
        private String notificationText;
        private String notificationSenderId;
        private String notificationReceiverId;
        private String notificationImageUrl;

        public with(Context c) {
            this.context = c;
            // defaults
            this.notificationSenderId = null;
            this.notificationReceiverId = null;
            this.notificationImageUrl = null;
            this.notificationType = Notification.STANDARD_NOTIFICATION;
        }

        public with type(int type) {
            notificationType = type;
            return this;
        }

        public with body(String bodyText) {
            notificationText = bodyText;
            return this;
        }

        public with sender(User user) {
            notificationSenderId = user.getObjectId();
            return this;
        }

        public with receiver(User user) {
            notificationReceiverId = user.getObjectId();
            return this;
        }

        public with imageUrl(String url) {
            notificationImageUrl = url;
            return this;
        }

        public Notification build() {
            Notification n = new Notification(this);
            n.mViewed = false;
            return n;
        }
        public Notification deliver() {
            Notification n = new Notification(this);
            n.mViewed = false;
            n.send();
            return n;
        }

    }

    private Notification(with builder) {
        mType = builder.notificationType;
        this.safePutKey(Notification.PUSH_TYPE_KEY, mType);
        mSenderUid = builder.notificationSenderId;
        this.safePutKey(Notification.PUSH_SENDER_ID, mSenderUid);
        mReceiverUid = builder.notificationReceiverId;
        this.safePutKey(Notification.PUSH_RECEIVER_ID, mReceiverUid);
        mText = builder.notificationText;
        this.safePutKey(Notification.PUSH_TEXT_BODY_KEY, mText);
        mTimestamp = TimeUtils.currentTimestampInS();
        this.safePutKey(Notification.PUSH_TIMESTAMP_KEY, mTimestamp);
        mNotificationImageUrl = builder.notificationImageUrl;
        this.safePutKey(Notification.PUSH_IMAGE_URL_KEY, mNotificationImageUrl);
    }

    private void safePutKey(String key, Object value) {
        if (value != null) {
            this.put(key, value);
        }
    }

    public static Notification fromJson(JSONObject json) {
        Notification notification = new Notification();
        try {
            notification.setText(json.getString(Notification.PUSH_TEXT_BODY_KEY));
            notification.setChannel(json.getString(Notification.PUSH_CHANNEL_KEY));
            notification.setNotificationImageUrl(json.getString(Notification.PUSH_IMAGE_URL_KEY));
            notification.setViewed(json.getBoolean(Notification.PUSH_VIEWED_KEY));
            notification.setReceiverUid(json.getString(Notification.PUSH_RECEIVER_ID));
            notification.setSenderUid(json.getString(Notification.PUSH_SENDER_ID));
            notification.setTimestamp(json.getLong(Notification.PUSH_TIMESTAMP_KEY));
            notification.setType(json.getInt(Notification.PUSH_TYPE_KEY));

        } catch (JSONException e) {
            // This is a global alert, just return default
            Notification alert = new Notification();
            return alert;
        }
        return notification;
    }

    public JSONObject toJson() {
        JSONObject data = new JSONObject();
        try {
            data.put(PUSH_TYPE_KEY, this.mType);
            data.put(PUSH_TEXT_BODY_KEY, this.getText());
            data.put(PUSH_CHANNEL_KEY, this.getChannel());
            data.put(PUSH_RECEIVER_ID, this.getReceiverUid());
            data.put(PUSH_SENDER_ID, this.getSenderUid());
            data.put(PUSH_TIMESTAMP_KEY, this.getTimestampString());
            data.put(PUSH_VIEWED_KEY, this.viewed());
            data.put(PUSH_IMAGE_URL_KEY, this.getNotificationImageUrl());
            return data;
        } catch (JSONException e) {
            // TODO: implement some sort of better handling
            return null;
        }
    }

    public void send() {
        JSONObject data = this.toJson();
        ParsePush push = new ParsePush();
        // TODO: need to implement query handling with circles
        push.setChannel(this.getChannel());
        push.setData(data);
        push.sendInBackground(new SendCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("push", "The push campaign has been created.");
                } else {
                    Log.d("push", "Error sending push:" + e.getMessage());
                }
            }

        });

    }

    public static void queryNotifcations(final NotificationHandler handler) {
        ParseQuery<Notification> notifications = ParseQuery.getQuery(Notification.class);
        notifications.orderByDescending(Notification.PUSH_TIMESTAMP_KEY);
        notifications.findInBackground(new FindCallback<Notification>() {
            public void done(List<Notification> notifs, ParseException exception) {
                if (exception == null) {
                    handler.onResult(notifs);
                } else {
                    handler.onException(exception);
                }
            }
        });

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mSenderUid);
        dest.writeString(this.mReceiverUid);
        dest.writeString(this.mText);
        dest.writeString(this.mNotificationImageUrl);
        dest.writeLong(this.mTimestamp);
        dest.writeInt(this.mType);
        dest.writeByte(mViewed ? (byte) 1 : (byte) 0);
        dest.writeString(this.mChannel);
    }

    protected Notification(Parcel in) {
        this.setSenderUid(in.readString());
        this.setReceiverUid(in.readString());
        this.setText(in.readString());
        this.setNotificationImageUrl(in.readString());
        this.setTimestamp(in.readLong());
        this.setType(in.readInt());
        this.setViewed(in.readByte() != 0);
        this.setChannel(in.readString());
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        public Notification createFromParcel(Parcel source) {
            return new Notification(source);
        }
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };


}
