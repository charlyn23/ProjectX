package charlyn23.c4q.nyc.projectx;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.*;

<<<<<<< HEAD
=======
import charlyn23.c4q.nyc.projectx.model.Shame;

>>>>>>> 89c073ced9a7d2328de094150011678bc0c8d514
/**
 * Created by July on 8/9/15.
 */
public class ProjectX extends Application {
<<<<<<< HEAD
=======

    public static final String YOUR_APPLICATION_ID = "wXm5LSYRqb26gArXbWoZDkLCqzO4dD1pa3y5J34O";
    public static final String YOUR_CLIENT_KEY = "kvV4Abba1l7DKhUBQxK3PWLvIsFjQwuPyrcuMhXq";
    public static final String TWITTER_ID = "ouo6bc7Pw4aScPNRnsKJlvm2K";
    public static final String TWITTER_KEY = "qRY7V1WbBMXogze0wC0m05cIgMpzl3bn8Kt9NpOpHkW7S5kKLk";

>>>>>>> 89c073ced9a7d2328de094150011678bc0c8d514
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialization of  Crash Reporting.
        ParseCrashReporting.enable(this);

        //Local DataStore.
        Parse.enableLocalDatastore(this);

        // initialization Parse, Twitter and Facebook keys
<<<<<<< HEAD
        Parse.initialize(this, "wXm5LSYRqb26gArXbWoZDkLCqzO4dD1pa3y5J34O", "kvV4Abba1l7DKhUBQxK3PWLvIsFjQwuPyrcuMhXq");
        ParseTwitterUtils.initialize("ouo6bc7Pw4aScPNRnsKJlvm2K", "qRY7V1WbBMXogze0wC0m05cIgMpzl3bn8Kt9NpOpHkW7S5kKLk");
=======
        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);
        ParseTwitterUtils.initialize(TWITTER_ID, TWITTER_KEY);
>>>>>>> 89c073ced9a7d2328de094150011678bc0c8d514
        FacebookSdk.sdkInitialize(getApplicationContext());
        ParseFacebookUtils.initialize(getApplicationContext());

        com.parse.ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // Optionally to enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
<<<<<<< HEAD
=======

        // Register Shame object
        ParseObject.registerSubclass(Shame.class);
>>>>>>> 89c073ced9a7d2328de094150011678bc0c8d514
    }
}