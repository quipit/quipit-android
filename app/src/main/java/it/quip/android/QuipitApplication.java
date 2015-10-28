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
import it.quip.android.repository.circle.CircleRepository;
import it.quip.android.repository.circle.ParseCircleRepository;
import it.quip.android.repository.user.ParseUserRepository;
import it.quip.android.repository.user.UserRepository;

public class QuipitApplication extends Application {

    private static User sUser;
    private static CircleRepository sCircleRepo;
    private static UserRepository sUserRepo;

    private void setupParse() {
        Parse.enableLocalDatastore(this);
        Parse.initialize(this,
                stringRes(R.string.parse_application_id),
                stringRes(R.string.parse_client_key));

        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Quip.class);
        ParseObject.registerSubclass(Circle.class);
        ParseObject.registerSubclass(Notification.class);

        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    private void setupFacebook() {
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    private void setupRepos() {
        sCircleRepo = new ParseCircleRepository();
        sUserRepo = new ParseUserRepository();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setupParse();
        setupFacebook();
        setupRepos();
    }

    public String stringRes(int resId) {
        return getResources().getString(resId);
    }

    public static User getCurrentUser() {
        return sUser;
    }

    public static CircleRepository getCircleRepo() {
        return sCircleRepo;
    }

    public static UserRepository getUserRepo() {
        return sUserRepo;
    }

    public static void setCurrentUser(User user) {
        sUser = user;
    }

}
