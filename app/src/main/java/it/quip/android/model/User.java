package it.quip.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Parcelable {

    private long uid;
    private long facebookId;
    private String firstName;
    private String lastName;
    private String email;

    public long getUid() {
        return uid;
    }

    public long getFacebookId() {
        return facebookId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public static User fromJSON(JSONObject userJson) {
        User user = new User();
        
        try {
            user.uid = userJson.getLong("id");
            user.facebookId = userJson.getLong("facebook_id");
            user.firstName = userJson.getString("first_name");
            user.lastName = userJson.getString("last_name");
            user.email = userJson.getString("email");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.uid);
        dest.writeLong(this.facebookId);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.email);
    }

    public User() {
    }

    private User(Parcel in) {
        this.uid = in.readLong();
        this.facebookId = in.readLong();
        this.firstName = in.readString();
        this.lastName = in.readString();
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
