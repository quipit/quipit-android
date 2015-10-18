package it.quip.android;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

public class QuipitApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this,
                stringRes(R.string.parse_application_id),
                stringRes(R.string.parse_client_key));
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    private String stringRes(int resId) {
        return getResources().getString(resId);
    }

}
