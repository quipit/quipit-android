package it.quip.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danbuscaglia on 10/18/15.
 */
public class Notification implements Parcelable {

    private long uid;
    private String text;
    private String notificationImageUrl;
    private int timestamp;
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

    public Notification(long uid, String text, String notificationImageUrl, int timestamp, boolean viewed) {

        this.uid = uid;
        this.text = text;
        this.notificationImageUrl = notificationImageUrl;
        this.timestamp = timestamp;
        this.viewed = viewed;
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
        dest.writeByte(viewed ? (byte) 1 : (byte) 0);
    }

    protected Notification(Parcel in) {
        this.uid = in.readLong();
        this.text = in.readString();
        this.notificationImageUrl = in.readString();
        this.timestamp = in.readInt();
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

    public static List<Notification> getNotifcations(int page) {
        // perform query, for now use stub data

        List<Notification> stubs = new ArrayList<>();

        stubs.add(new Notification(new Long(1),
                "<b>Brothers Darkness</b> has added you to circle <b><i>@unity</i></b>",
                "http://image.iheart.com/images/1080/MI0001411019.jpg",
                1445205833,
                false

        ));

        stubs.add(new Notification(new Long(2),
                "<b>Edgard Juarez</b> just quipped in circle <b><i>@darkness</i></b>",
                "http://laaficion.milenio.com/beisbol/Toros_MILIMA20140326_0343_11.jpg",
                1445205839,
                false
        ));

        return stubs;

    }
}
