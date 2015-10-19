package it.quip.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable {

    private long uid;
    private long facebookId;
    private String name;
    private String email;
    private List<Circle> circles = new ArrayList<>();

    public long getUid() {
        return uid;
    }

    public long getFacebookId() {
        return facebookId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<Circle> getCircles() {
        return circles;
    }

    public Circle getCircle(long circleId) {
        for (Circle circle : circles) {
            if (circle.getUid() == circleId) {
                return circle;
            }
        }

        return null;
    }

    public void addCircle(Circle circle) {
        circles.add(circle);
    }

    public static User fromJSON(JSONObject userJson) {
        User user = new User();
        
        try {
            user.uid = userJson.getLong("id");
            user.facebookId = userJson.optLong("facebook_id", -1);
            user.name = userJson.getString("name");
            user.email = userJson.getString("email");

            JSONArray circlesJson = userJson.optJSONArray("circles");
            if (circlesJson != null) {
                user.circles = Circle.fromJSONArray(circlesJson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return user;
    }

    public static List<User> fromJSONArray(JSONArray circlesJson) {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < circlesJson.length(); i++) {

            User user;
            try {
                JSONObject quipJson = circlesJson.getJSONObject(i);
                user = User.fromJSON(quipJson);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

            if (user != null) {
                users.add(user);
            }
        }

        return users;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.uid);
        dest.writeLong(this.facebookId);
        dest.writeString(this.name);
        dest.writeString(this.email);
    }

    public User() {
    }

    private User(Parcel in) {
        this.uid = in.readLong();
        this.facebookId = in.readLong();
        this.name = in.readString();
        this.email = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
