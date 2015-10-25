package it.quip.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Circle implements Parcelable {

    private long uid;
    private String name = "";
    private List<User> members = new ArrayList<>();

    public long getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getMembers() {
        return members;
    }

    public void addMember(User member) {
        members.add(member);
    }

    public static Circle fromJSON(JSONObject circleJson) {
        Circle circle = new Circle();

        try {
            circle.uid = circleJson.getLong("id");
            circle.name = circleJson.getString("name");
            circle.members = User.fromJSONArray(circleJson.getJSONArray("members"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return circle;
    }

    public static List<Circle> fromJSONArray(JSONArray circlesJson) {
        List<Circle> circles = new ArrayList<>();

        for (int i = 0; i < circlesJson.length(); i++) {

            Circle circle;
            try {
                JSONObject quipJson = circlesJson.getJSONObject(i);
                circle = Circle.fromJSON(quipJson);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

            if (circle != null) {
                circles.add(circle);
            }
        }

        return circles;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.uid);
        dest.writeString(this.name);
        dest.writeTypedList(members);
    }

    public Circle() {
    }

    private Circle(Parcel in) {
        this.uid = in.readLong();
        this.name = in.readString();
        in.readTypedList(members, User.CREATOR);
    }

    public static final Parcelable.Creator<Circle> CREATOR = new Parcelable.Creator<Circle>() {
        public Circle createFromParcel(Parcel source) {
            return new Circle(source);
        }

        public Circle[] newArray(int size) {
            return new Circle[size];
        }
    };

}
