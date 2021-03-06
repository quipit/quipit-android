package it.quip.android.model;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SendCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import it.quip.android.QuipitApplication;
import it.quip.android.listener.NotificationHandler;
import it.quip.android.task.NotificationBatchJob;
import it.quip.android.task.NotificationBatchJobData;
import it.quip.android.util.TimeUtils;


@ParseClassName("Notification")
public class Notification extends BaseParseObject implements Parcelable {

    public static final int FLAG_NOTIFICATION_RECEIVED = 45678;
    public static final String NOTIFICATION_RECEIVED_ACTION = "com.notifications.ReceivedQuipNotification";
    public static final String MARSHALL_INTENT_KEY = "new_notification";


    public static final int STANDARD_NOTIFICATION = 0;
    public static final String PUSH_TEXT_BODY_KEY = "text_body";
    public static final String PUSH_SENDER_ID = "sender_uid";
    public static final String PUSH_IMAGE_URL_KEY = "image_url";
    public static final String PUSH_RECEIVER_ID = "receiver_uid";
    public static final String PUSH_TYPE_KEY = "notification_type";
    public static final String PUSH_VIEWED_KEY = "viewed";
    public static final String PUSH_CHANNEL_KEY = "channel";

    private String mSenderUid;
    private String mReceiverUid;
    private String mText;
    private String mNotificationImageUrl;
    private String mChannel;
    private Circle mCircle;
    private List<User> mRecepients;

    private int mType;  // TODO: define enum
    private boolean mViewed;

    public String getChannel() {
        mChannel = this.getString(PUSH_CHANNEL_KEY);
        return mChannel;
    }

    public boolean getViewed() {
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

    public Circle getCircle() {
        return this.mCircle;
    }

    public List<User> getRecepients() {
        return this.mRecepients;
    }

    public void setViewed(boolean viewed) {
        this.mViewed = viewed;
        this.safePut(PUSH_VIEWED_KEY, viewed);
    }

    public void setChannel(String channel) {
        this.safePut(PUSH_CHANNEL_KEY, channel);
        this.mChannel = channel;
    }

    public void setSenderUid(String uid) {
        this.mSenderUid = uid;
        this.safePut(PUSH_SENDER_ID, uid);
    }

    public void setReceiverUid(String uid) {
        this.mReceiverUid = uid;
        this.safePut(PUSH_RECEIVER_ID, uid);
    }

    public void setText(String textString) {
        this.mText = textString;
        this.safePut(PUSH_TEXT_BODY_KEY, textString);
    }

    public void setNotificationImageUrl(String imageUrl) {
        this.mNotificationImageUrl = imageUrl;
        this.safePut(PUSH_IMAGE_URL_KEY, imageUrl);
    }

    public void setType(int type) {
        this.mType = type;
        this.safePut(PUSH_TYPE_KEY, type);
    }

    public void setCircle(Circle circle) {
        this.mCircle = circle;
        this.setRecepients(circle.getMembers());
    }

    public void setRecepients(List<User> users) {
        this.mRecepients = users;
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
        private Circle circle;
        private List<User> bulkRecepients;

        public with(Context c) {
            this.context = c;
            this.notificationSenderId = null;
            this.notificationReceiverId = null;
            this.notificationImageUrl = null;
            this.notificationType = Notification.STANDARD_NOTIFICATION;
            this.circle = null;
            this.bulkRecepients = null;
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

        public with circle(Circle c) {
            circle = c;
            bulkRecepients = c.getMembers();
            return this;
        }

        public with bulkRecepients(List<User> users) {
            // only use if not sending bulk to circles and you want to send to many.
            bulkRecepients = users;
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
        this.safePut(Notification.PUSH_TYPE_KEY, mType);
        mSenderUid = builder.notificationSenderId;
        this.safePut(Notification.PUSH_SENDER_ID, mSenderUid);
        mReceiverUid = builder.notificationReceiverId;
        this.safePut(Notification.PUSH_RECEIVER_ID, mReceiverUid);
        mText = builder.notificationText;
        this.safePut(Notification.PUSH_TEXT_BODY_KEY, mText);
        mNotificationImageUrl = builder.notificationImageUrl;
        this.safePut(Notification.PUSH_IMAGE_URL_KEY, mNotificationImageUrl);
        mCircle = builder.circle;
        mRecepients = builder.bulkRecepients;
    }

    public static Notification fromJson(JSONObject json) {
        Notification notification = new Notification();
        try {
            notification.setText(json.getString(Notification.PUSH_TEXT_BODY_KEY));
            notification.setViewed(json.getBoolean(Notification.PUSH_VIEWED_KEY));
            notification.setSenderUid(json.optString(Notification.PUSH_SENDER_ID, ""));
            notification.setType(json.getInt(Notification.PUSH_TYPE_KEY));
            notification.setNotificationImageUrl(json.optString(Notification.PUSH_IMAGE_URL_KEY, "https://scontent.xx.fbcdn.net/hphotos-xpa1/v/t1.0-9/1506408_10205437954245349_5292698869694708416_n.jpg?oh=fb5f9c98209217c65fd4ddeb7aaaafc1&oe=56C2F425"));

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
            data.put(PUSH_VIEWED_KEY, this.getViewed());
            data.put(PUSH_IMAGE_URL_KEY, this.getNotificationImageUrl());
            return data;
        } catch (JSONException e) {
            // TODO: implement some sort of better handling
            return null;
        }
    }

    public void send() {
        if (this.mRecepients == null) {
            deliver();
        } else {
            NotificationBatchJobData jobData = new NotificationBatchJobData(this.mRecepients, this.mSenderUid, this);
            AsyncTask task = new NotificationBatchJob().execute(jobData);
        }

    }

    public void deliver() {
        JSONObject data = this.toJson();
        ParsePush push = new ParsePush();
        // TODO: need to implement query handling with circles
        String channel = this.getChannel();
        if (channel != null) {
            push.setChannel(this.getChannel());
        }
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
        notifications.whereEqualTo(PUSH_RECEIVER_ID, QuipitApplication.getCurrentUser().getObjectId());
        notifications.orderByDescending(CREATED_AT);
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
        dest.writeInt(this.mType);
        dest.writeByte(mViewed ? (byte) 1 : (byte) 0);
        dest.writeString(this.mChannel);
    }

    protected Notification(Parcel in) {
        this.setSenderUid(in.readString());
        this.setReceiverUid(in.readString());
        this.setText(in.readString());
        this.setNotificationImageUrl(in.readString());
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
