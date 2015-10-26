package it.quip.android.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.facebook.AccessToken;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import it.quip.android.QuipitApplication;
import it.quip.android.network.FacebookClient;


@ParseClassName("User")
public class User extends BaseParseObject implements Parcelable {

    private static final String FACEBOOK_ID = "facebook_id";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String IMAGE_URL = "image_url";
    private static final String CIRCLES = "circles";

    private String facebookId;
    private String name;
    private String email;
    private String imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public List<Circle> getCircles() {
        return circles;
    }

    public Circle getCircle(int circleIndex) {
        if (circleIndex < circles.size()) {
            return circles.get(circleIndex);
        }

        return null;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
        this.safePut(FACEBOOK_ID, facebookId);
    }

    public void setName(String name) {
        this.name = name;
        this.safePut(NAME, name);
    }

    public void setEmail(String email) {
        this.email = email;
        this.safePut(EMAIL, email);
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        this.safePut(IMAGE_URL, imageUrl);
    }

    public void setCircles(List<Circle> circles) {
        this.circles = circles;
        this.safePut(CIRCLES, circlesToIds());
    }

    public void addCircle(Circle circle) {
        circles.add(circle);
        this.setCircles(circles);
    }

    public static ParseQuery<User> getQuery() {
        return ParseQuery.getQuery(User.class);
    }

    public static void setUserForSession() {
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
            try {
                FacebookClient.createNewUser();
            } catch (FacebookClient.FacebookClientException facebookClientException) {
                Log.e("FacebookClient", "Could not create new user from Facebook.");
            }
        } else {
            QuipitApplication.setCurrentUser(currentUser);
        }
    }

    private List<String> circlesToIds() {
        List<String> circleIds = new ArrayList<String>();
        for (Circle circle : getCircles()) {
            circleIds.add(circle.getObjectId());
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
        dest.writeString(this.imageUrl);
        dest.writeList(this.circles);
    }

    public User() {

    }

    private User(Parcel in) {
        this.setFacebookId(in.readString());
        this.setName(in.readString());
        this.setEmail(in.readString());
        this.setImageUrl(in.readString());

        List<Circle> circles = new ArrayList<>();
        in.readList(circles, Circle.class.getClassLoader());
        this.setCircles(circles);
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
