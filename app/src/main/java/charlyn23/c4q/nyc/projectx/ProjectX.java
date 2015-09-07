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
        Parse.initialize(this, Constants.YOUR_APPLICATION_ID, Constants.YOUR_CLIENT_KEY);
        ParseTwitterUtils.initialize(Constants.TWITTER_ID, Constants.TWITTER_KEY);
        FacebookSdk.sdkInitialize(getApplicationContext());

        com.parse.ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // Optionally to enable public read access.
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

    }
}