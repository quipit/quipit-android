package it.quip.android;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

import it.quip.android.model.Circle;
import it.quip.android.model.Notification;
import it.quip.android.model.Quip;
import it.quip.android.model.User;

public class QuipitApplication extends Application {

    private static User sUser;

    private void setupParse() {
        Parse.enableLocalDatastore(this);
        Parse.initialize(this,
                stringRes(R.string.parse_application_id),
                stringRes(R.string.parse_client_key));
        ParseObject.registerSubclass(Notification.class);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Quip.class);
        ParseObject.registerSubclass(Circle.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    private void setupFacebook() {
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setupParse();
        setupFacebook();
    }

    public String stringRes(int resId) {
        return getResources().getString(resId);
    }

    public static User getCurrentUser() {
        return sUser;
    }

    public static void setCurrentUser(User user) {
        sUser = user;
    }

}
