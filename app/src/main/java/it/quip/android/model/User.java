package it.quip.android.model;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

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

}
