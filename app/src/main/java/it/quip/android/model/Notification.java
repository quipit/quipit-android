package it.quip.android.model;

import android.content.Context;
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

import it.quip.android.listener.ParseModelQueryHandler;
import it.quip.android.util.TimeUtil;


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

    private String mSenderUid;
    private String mReceiverUid;
    private String mText;
    private String mNotificationImageUrl;
    private long mTimestamp;
    private int mType;  // TODO: define enum
    private boolean mViewed;

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
        this.put(PUSH_VIEWED_KEY, viewed);
    }

    public void setSenderUid(String uid) {
        this.mSenderUid = uid;
        this.put(PUSH_SENDER_ID, uid);
    }

    public void setReceiverUid(String uid) {
        this.mReceiverUid = uid;
        this.put(PUSH_RECEIVER_ID, uid);
    }

    public void setText(String textString) {
        this.mText = textString;
        this.put(PUSH_TEXT_BODY_KEY, textString);
    }

    public void setNotificationImageUrl(String imageUrl) {
        this.mNotificationImageUrl = imageUrl;
        this.put(PUSH_IMAGE_URL_KEY, imageUrl);
    }

    public void setTimestamp(long timestamp) {
        this.mTimestamp = timestamp;
        this.put(PUSH_TIMESTAMP_KEY, timestamp);
    }

    public void setType(int type) {
        this.mType = type;
        this.put(PUSH_TYPE_KEY, type);
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
    }

    private Notification(with builder) {
        mType = builder.notificationType;
        this.put(Notification.PUSH_TYPE_KEY, mType);
        mSenderUid = builder.notificationSenderId;
        this.put(Notification.PUSH_SENDER_ID, mSenderUid);
        mReceiverUid = builder.notificationReceiverId;
        this.put(Notification.PUSH_RECEIVER_ID, mReceiverUid);
        mText = builder.notificationText;
        this.put(Notification.PUSH_TEXT_BODY_KEY, mText);
        mTimestamp = TimeUtil.currentTimestampInS();
        this.put(Notification.PUSH_TIMESTAMP_KEY, mTimestamp);
        mNotificationImageUrl = builder.notificationImageUrl;
        this.put(Notification.PUSH_IMAGE_URL_KEY, mNotificationImageUrl);
    }

    public static Notification fromJson(JSONObject json) {
        Notification notification = new Notification();
        try {
            notification.mText = json.getString("alert");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return notification;
    }

    public JSONObject toJson() {
        JSONObject data = new JSONObject();
        try {
            data.put(PUSH_TYPE_KEY, this.mType);
            data.put(PUSH_TEXT_BODY_KEY, this.getText());
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
        // push.setChannel("notification");
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

    public static void queryNotifcations(final ParseModelQueryHandler handler) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Notification");
        //query.whereEqualTo("receiver", );

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    handler.onResult(objects);
                } else {
                    handler.onException(e);
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
    }

    protected Notification(Parcel in) {
        this.setSenderUid(in.readString());
        this.setReceiverUid(in.readString());
        this.setText(in.readString());
        this.setNotificationImageUrl(in.readString());
        this.setTimestamp(in.readLong());
        this.setType(in.readInt());
        this.setViewed(in.readByte() != 0);
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
