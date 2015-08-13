package charlyn23.c4q.nyc.projectx;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseTwitterUtils;

import charlyn23.c4q.nyc.projectx.shames.Shame;

/**
 * Created by July on 8/9/15.
 */
public class ProjectX extends Application {

    public static final String YOUR_APPLICATION_ID = "wXm5LSYRqb26gArXbWoZDkLCqzO4dD1pa3y5J34O";
    public static final String YOUR_CLIENT_KEY = "kvV4Abba1l7DKhUBQxK3PWLvIsFjQwuPyrcuMhXq";
    public static final String TWITTER_ID = "ouo6bc7Pw4aScPNRnsKJlvm2K";
    public static final String TWITTER_KEY = "qRY7V1WbBMXogze0wC0m05cIgMpzl3bn8Kt9NpOpHkW7S5kKLk";

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialization of  Crash Reporting.
        ParseCrashReporting.enable(this);

        //Local DataStore.
        Parse.enableLocalDatastore(this);

        // Register Shame object
        ParseObject.registerSubclass(Shame.class);

        // initialization Parse, Twitter and Facebook keys
        Parse.initialize(this, "wXm5LSYRqb26gArXbWoZDkLCqzO4dD1pa3y5J34O", "kvV4Abba1l7DKhUBQxK3PWLvIsFjQwuPyrcuMhXq");
        ParseTwitterUtils.initialize("ouo6bc7Pw4aScPNRnsKJlvm2K", "qRY7V1WbBMXogze0wC0m05cIgMpzl3bn8Kt9NpOpHkW7S5kKLk");
        ParseTwitterUtils.initialize(TWITTER_ID, TWITTER_KEY);
        FacebookSdk.sdkInitialize(getApplicationContext());
        ParseFacebookUtils.initialize(getApplicationContext());

        com.parse.ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // Optionally to enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

    }
}