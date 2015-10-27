package it.quip.android.network;


import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

import it.quip.android.QuipitApplication;
import it.quip.android.model.User;


public class FacebookClient {

    public interface OnFacebookResponse {

        void onFacebookResponse(GraphResponse response);

    }

    public static class FacebookClientException extends Exception {

        public FacebookClientException(String detailedMessage) {
            super(detailedMessage);
        }

    }

    private static final String FACEBOOK_IMAGE_URL = "https://graph.facebook.com/%s/picture";

    private static FacebookClient client;

    private AccessToken accessToken;

    public static void createNewUser() throws FacebookClientException {
        FacebookClient client = FacebookClient.getInstance();
        if (null == client) {
            throw new FacebookClientException("Attempting to create new user with no FacebookClient.");
        }

        client.getCurrentUsersFacebookInfo(new OnFacebookResponse() {
            @Override
            public void onFacebookResponse(GraphResponse response) {
                JSONObject responseJSON = response.getJSONObject();
                User user = new User();
                try {
                    user.setName(responseJSON.getString("name"));
                    user.setEmail(responseJSON.optString("email"));
                    user.setFacebookId(responseJSON.getString("id"));
                    user.setImageUrl(String.format(FACEBOOK_IMAGE_URL, user.getFacebookId()));
                    user.saveInBackground();
                    QuipitApplication.setCurrentUser(user);
                } catch (JSONException jsonException) {
                    Log.e("FacebookClient", "Unable to parse JSON in Facebook response.");
                }
            }
        });
    }

    public static FacebookClient getInstance() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if ((null == accessToken) || accessToken.isExpired()) {
            return null;
        }

        if (client == null) {
            client = new FacebookClient(accessToken);
        }

        return client;
    }

    public FacebookClient(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public void getFacebookFriends(final OnFacebookResponse responseHandler) {
        new GraphRequest(
                accessToken,
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        responseHandler.onFacebookResponse(response);
                    }
                }
        ).executeAsync();
    }

    public void getCurrentUsersFacebookInfo(final OnFacebookResponse responseHandler) {
        GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                responseHandler.onFacebookResponse(graphResponse);
            }
        }).executeAsync();
    }

}
