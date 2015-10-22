package it.quip.android.network;


import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;


public class FacebookClient {

    public interface OnFacebookResponse {

        void onFacebookResponse(GraphResponse response);

    }

    public static class FacebookClientException extends Exception {

        public FacebookClientException(String detailedMessage) {
            super(detailedMessage);
        }

    }

    private static FacebookClient client;

    private AccessToken accessToken;

    public static FacebookClient getInstance(LoginResult loginResult) throws FacebookClientException {
        if (client == null) {
            if (loginResult == null) {
                throw new FacebookClientException("No loginResult found.");
            }

            client = new FacebookClient(loginResult.getAccessToken());
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

}
