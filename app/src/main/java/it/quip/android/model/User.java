package it.quip.android.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.facebook.AccessToken;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


@ParseClassName("User")
public class User extends ParseObject implements Parcelable {

    private static final String FACEBOOK_ID = "facebook_id";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String CIRCLES = "circles";

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
        this.put(FACEBOOK_ID, facebookId);
    }

    public void setName(String name) {
        this.name = name;
        this.put(NAME, name);
    }

    public void setEmail(String email) {
        this.email = email;
        this.put(EMAIL, email);
    }

    public void setCircles(List<Circle> circles) {
        this.circles = circles;
        this.put(CIRCLES, circlesToIds());
    }

    private String facebookId;
    private String name;
    private String email;
    private List<Circle> circles = new ArrayList<>();

    public String getFacebookId() {
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

    public static ParseQuery<User> getQuery() {
        return ParseQuery.getQuery(User.class);
    }

    public static User fromJSON(JSONObject userJson) {
        User user = new User();
        
        try {
            user.facebookId = userJson.getString(FACEBOOK_ID);
            user.name = userJson.getString(NAME);
            user.email = userJson.getString(EMAIL);

            JSONArray circlesJson = userJson.optJSONArray(CIRCLES);
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

    public static User getUserForSession() {
        String facebookId = AccessToken.getCurrentAccessToken().getUserId();
        User currentUser = null;
        try {
            List<User> currentUsers = getQuery()
                    .whereEqualTo(FACEBOOK_ID, facebookId)
                    .find();
            if (currentUsers.size() > 0) {
                currentUser = currentUsers.get(0);
            }
        } catch (ParseException parseException) {
            Log.d("User", "Error loading User object from Parse.");
        }

        if (null == currentUser) {
            currentUser = new User();
            currentUser.setFacebookId(facebookId);
            currentUser.saveInBackground();
        }

        return currentUser;
    }

    private List<Long> circlesToIds() {
        List<Long> circleIds = new ArrayList<Long>();
        for (Circle circle : getCircles()) {
            circleIds.add(circle.getUid());
        }

        return circleIds;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.facebookId);
        dest.writeString(this.name);
        dest.writeString(this.email);
    }

    public User() {

    }

    private User(Parcel in) {
        this.facebookId = in.readString();
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
