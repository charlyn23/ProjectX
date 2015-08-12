package charlyn23.c4q.nyc.projectx;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.*;

/**
 * Created by July on 8/9/15.
 */
public class ProjectX extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialization of  Crash Reporting.
        ParseCrashReporting.enable(this);

        //Local DataStore.
        Parse.enableLocalDatastore(this);

        // initialization Parse, Twitter and Facebook keys
        Parse.initialize(this, "wXm5LSYRqb26gArXbWoZDkLCqzO4dD1pa3y5J34O", "kvV4Abba1l7DKhUBQxK3PWLvIsFjQwuPyrcuMhXq");
        ParseTwitterUtils.initialize("ouo6bc7Pw4aScPNRnsKJlvm2K", "qRY7V1WbBMXogze0wC0m05cIgMpzl3bn8Kt9NpOpHkW7S5kKLk");
        FacebookSdk.sdkInitialize(getApplicationContext());
        ParseFacebookUtils.initialize(getApplicationContext());

        com.parse.ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // Optionally to enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}