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
    private long sourceId;
    private String sourceName;
    private Circle circle;

    public long getUid() {
        return uid;
    }

    public Circle getCircle() {
        return circle;
    }

    public String getText() {
        return text;
    }

    public long getSourceId() {
        return sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public static Quip fromJSON(JSONObject quipJson) {
        Quip quip = new Quip();

        try {
            quip.uid = quipJson.getLong("id");
            quip.circle = Circle.fromJSON(quipJson.getJSONObject("circle"));
            quip.text = quipJson.getString("text");
            quip.sourceId = quipJson.getLong("source_id");
            quip.sourceName = quipJson.getString("source_name");
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
        dest.writeLong(this.sourceId);
        dest.writeString(this.sourceName);
        dest.writeParcelable(this.circle, flags);
    }

    public Quip() {
    }

    private Quip(Parcel in) {
        this.uid = in.readLong();
        this.text = in.readString();
        this.sourceId = in.readLong();
        this.sourceName = in.readString();
        this.circle = in.readParcelable(Circle.class.getClassLoader());
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
