package it.quip.android;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

import it.quip.android.model.Notification;
import it.quip.android.model.User;
import it.quip.android.util.MockUtils;

public class QuipitApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        Parse.enableLocalDatastore(this);
        Parse.initialize(this,
                stringRes(R.string.parse_application_id),
                stringRes(R.string.parse_client_key));
        ParseObject.registerSubclass(Notification.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    public String stringRes(int resId) {
        return getResources().getString(resId);
    }

    public static User getCurrentUser() {
        return MockUtils.getUsers().get(0);
    }

}
