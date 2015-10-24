package it.quip.android.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.parse.ParsePush;
import com.parse.SendCallback;

import org.json.JSONException;
import org.json.JSONObject;


@ParseClassName("Notification")
public class Notification extends ParseObject implements Parcelable {

    public static final int STANDARD_NOTIFICATION = 0;
    public static final String PUSH_TEXT_BODY_KEY = "alert";
    public static final String PUSH_SENDER_ID = "sender";
    public static final String PUSH_CIRCLE_KEY = "circle";
    public static final String PUSH_TYPE_KEY = "notification_type";

    private long uid;
    private String text;
    private String notificationImageUrl;
    private int timestamp;
    private int type;  // TODO: define enum
    private boolean viewed;

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }

    public long getUid() {
        return uid;
    }

    public String getText() {
        return text;
    }

    public String getNotificationImageUrl() {
        return notificationImageUrl;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public String getTimestampString() {
        // TODO: implement this based on unix timestamp
        return "2h";
    }

    public Notification() {

    }

    public Notification(long uid, String text, String notificationImageUrl, int timestamp, boolean viewed) {

        this.uid = uid;
        this.text = text;
        this.notificationImageUrl = notificationImageUrl;
        this.timestamp = timestamp;
        this.viewed = viewed;
        this.type = STANDARD_NOTIFICATION;
    }

    public static Notification fromJson(JSONObject json) {
        Notification notification = new Notification();
        try {
            notification.text = json.getString("alert");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return notification;
    }

    public JSONObject toJson() {
        JSONObject data = new JSONObject();
        try {
            data.put(PUSH_TYPE_KEY, this.type);
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

    public static List<Notification> getNotifcations(int page) {
        /**
         * Getting the notifications by page needs to be done in some helper or client
         * TODO: implement this in sprint 2
         */

        List<Notification> stubs = new ArrayList<>();

        stubs.add(new Notification(new Long(1),
                "Brothers Darknesshas added you to circle @unity",
                "http://image.iheart.com/images/1080/MI0001411019.jpg",
                1445205833,
                false

        ));

        stubs.add(new Notification(new Long(2),
                "Edgar Juarez just quipped in circle @darkness",
                "http://laaficion.milenio.com/beisbol/Toros_MILIMA20140326_0343_11.jpg",
                1445205839,
                true
        ));

        return stubs;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.uid);
        dest.writeString(this.text);
        dest.writeString(this.notificationImageUrl);
        dest.writeInt(this.timestamp);
        dest.writeInt(this.type);
        dest.writeByte(viewed ? (byte) 1 : (byte) 0);
    }

    protected Notification(Parcel in) {
        this.uid = in.readLong();
        this.text = in.readString();
        this.notificationImageUrl = in.readString();
        this.timestamp = in.readInt();
        this.type = in.readInt();
        this.viewed = in.readByte() != 0;
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
