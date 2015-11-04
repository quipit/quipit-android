package it.quip.android;

import android.app.ActivityManager;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;

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
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class QuipitApplication extends Application {

    private static User sUser;
    private static CircleRepository sCircleRepo;
    private static UserRepository sUserRepo;
    public static NotificationManager sNotificationManager;
    public static ActivityManager sActivityManager;

    @Override
    public void onCreate() {
        super.onCreate();
        setupParse();
        setupFacebook();
        setupRepos();
        setupFonts();
    }

    private void setupParse() {
        Parse.enableLocalDatastore(this);
        Parse.initialize(this,
                stringRes(R.string.parse_application_id),
                stringRes(R.string.parse_client_key));

        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Quip.class);
        ParseObject.registerSubclass(Circle.class);
        ParseObject.registerSubclass(Notification.class);
        sNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        sActivityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    private void setupFacebook() {
        FacebookSdk.sdkInitialize(getApplicationContext());
    }

    private void setupRepos() {
        sCircleRepo = new ParseCircleRepository();
        sUserRepo = new ParseUserRepository();
    }

    private void setupFonts() {
        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Lato-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
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

    public static NotificationManager notificationManager() {
        return sNotificationManager;
    }

    public static ActivityManager activityManager() { return sActivityManager; }

}
