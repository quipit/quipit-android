package it.quip.android.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.facebook.AccessToken;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.List;

import it.quip.android.QuipitApplication;
import it.quip.android.network.FacebookClient;
import it.quip.android.repository.circle.CirclesResponseHandler;


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
        return getString(FACEBOOK_ID);
    }

    public String getName() {
        return getString(NAME);
    }

    public String getEmail() {
        return getString(EMAIL);
    }

    public String getImageUrl() {
        return getString(IMAGE_URL);
    }

    public List<Circle> getCircles() {
        return getCircles(null);
    }

    public List<Circle> getCircles(final CirclesResponseHandler responseHandler) {
        if ((null == circles) || (circles.isEmpty())) {
            QuipitApplication.getCircleRepo().getAllForUser(this, new CirclesResponseHandler() {
                @Override
                public void onSuccess(List<Circle> fetchedCircles) {
                    User.this.setCircles(fetchedCircles);

                    if (responseHandler != null) {
                        responseHandler.onSuccess(fetchedCircles);
                    }
                }
            });
        }

        return circles;
    }

    public Circle getCircle(int circleIndex) {
        if (circleIndex < getCircles().size()) {
            return getCircles().get(circleIndex);
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
        ParseRelation<Circle> relation = getRelation(CIRCLES);
        for (Circle previousCircle : this.circles) {
            relation.remove(previousCircle);
        }

        this.circles = circles;

        for (Circle newCircle : circles) {
            relation.add(newCircle);
        }
    }

    public void addCircle(Circle circle) {
        getRelation(CIRCLES).add(circle);
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
                currentUser = new User();
            } catch (FacebookClient.FacebookClientException facebookClientException) {
                Log.e("FacebookClient", "Could not create new user from Facebook.");
            }
        }

        QuipitApplication.setCurrentUser(currentUser);
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
        dest.writeString(getFacebookId());
        dest.writeString(getName());
        dest.writeString(getEmail());
        dest.writeString(getImageUrl());
    }

    public User() {

    }

    private User(Parcel in) {
        this.setFacebookId(in.readString());
        this.setName(in.readString());
        this.setEmail(in.readString());
        this.setImageUrl(in.readString());
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
