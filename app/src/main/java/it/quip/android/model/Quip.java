package it.quip.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Quip implements Parcelable {

    private long uid;
    private String text;
    private User author;
    private User source;
    private Circle circle;
    private long timestamp;

    public long getUid() {
        return uid;
    }

    public Circle getCircle() {
        return circle;
    }

    public String getText() {
        return text;
    }

    public User getAuthor() {
        return author;
    }

    public User getSource() {
        return source;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public static Quip fromJSON(JSONObject quipJson) {
        Quip quip = new Quip();

        try {
            quip.uid = quipJson.getLong("id");
            quip.circle = Circle.fromJSON(quipJson.getJSONObject("circle"));
            quip.text = quipJson.getString("text");
            quip.author = User.fromJSON(quipJson.getJSONObject("author"));
            quip.source = User.fromJSON(quipJson.getJSONObject("source"));
            quip.timestamp = Long.parseLong(quipJson.getString("timestamp"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return quip;
    }

    public static List<Quip> fromJSONArray(JSONArray quipsJson) {
        List<Quip> quips = new ArrayList<>();

        for (int i = 0; i < quipsJson.length(); i++) {

            Quip quip;
            try {
                JSONObject quipJson = quipsJson.getJSONObject(i);
                quip = Quip.fromJSON(quipJson);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

            if (quip != null) {
                quips.add(quip);
            }
        }

        return quips;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.uid);
        dest.writeString(this.text);
        dest.writeParcelable(this.author, flags);
        dest.writeParcelable(this.source, flags);
        dest.writeParcelable(this.circle, flags);
        dest.writeLong(this.timestamp);
    }

    public Quip() {
    }

    public Quip(long uid, String text, User author, User source, Circle circle, long timestamp) {
        this.uid = uid;
        this.text = text;
        this.author = author;
        this.source = source;
        this.circle = circle;
        this.timestamp = timestamp;
    }

    private Quip(Parcel in) {
        this.uid = in.readLong();
        this.text = in.readString();
        this.author = in.readParcelable(User.class.getClassLoader());
        this.source = in.readParcelable(User.class.getClassLoader());
        this.circle = in.readParcelable(Circle.class.getClassLoader());
        this.timestamp = in.readLong();
    }

    public static final Parcelable.Creator<Quip> CREATOR = new Parcelable.Creator<Quip>() {
        public Quip createFromParcel(Parcel source) {
            return new Quip(source);
        }

        public Quip[] newArray(int size) {
            return new Quip[size];
        }
    };
}
